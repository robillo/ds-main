package com.example.sasuke.dailysuvichar.newfragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.CustomVideo;
import com.example.sasuke.dailysuvichar.view.adapter.CustomVideoAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllVideosFragment extends Fragment {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mPullToRefresh;
    @BindView(R.id.alternate_layout)
    LinearLayout alternateLayout;

    private MultiTypeAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;
    private static FirebaseUser mFirebaseUser;
    private String uid;
    private AVLoadingIndicatorView avi;
    private HashMap<String, String> userStatus;
    private static DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePosts;
    private StorageReference mStorageReference;
    private ArrayList<String> mSelectedSubInterests;

    private StorageReference mStorageReferenceDP;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersDatabase;

    private static final String TAG = "ALLSTATUS", STATUS = "status";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    Items items;
    private Animation slide_down;
    private Animation slide_up;
    private int CHECK = 1;
    private String intentDBReference = null;
    private CustomVideoAdapter customVideoAdapter;
    private HashMap<String, Long> isDone;
    private HashMap<String, CustomVideo> videoHashMapStore;
    private HashMap<String, Long> videoHashMap;

    @BindView(R.id.recyclerview)
    RecyclerView mRvHome;

    public AllVideosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_all_videos, container, false);
        ButterKnife.bind(getActivity());

        mRvHome= (RecyclerView) v.findViewById(R.id.recyclerview);
        mPullToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        alternateLayout = (LinearLayout) v.findViewById(R.id.alternate_layout);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");

//        fetchDP();

//        avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        mRvHome.setVisibility(View.VISIBLE);

        uid = mFirebaseUser.getUid();

        Log.e(TAG, uid);

        slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        customVideoAdapter = new CustomVideoAdapter();

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(CustomVideo.class, new CustomVideoAdapter());
        mRvHome.setAdapter(mAdapter);

        items = new Items();


        mRvHome.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                customVideoAdapter.releaseVideo();
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.e(TAG, uid);
        Log.e(TAG, mDatabaseReference.toString());

        mSelectedSubInterests = new ArrayList<>();

        isDone= new HashMap<>();
        videoHashMap = new HashMap<>();
        videoHashMapStore = new HashMap<>();
        if(isOnline()) {
            refresh();


            mDatabaseReference.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("mSelectedSubInterests").getValue() != null) {
                        mSelectedSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                    }
                    fetchVideosFromFirebase();
                    alternateLayout.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            fetchVideosFromFirebase();
        }else{
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

//        Handler handler= new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Log.d(TAG, "onViewCreated: VAL "+allPostsHashMapFinal.size());
//                Log.d(TAG, "onViewCreated: VAL "+videoHashMap.size());
//                Log.d(TAG, "onViewCreated: VAL "+videoHashMap.size());
//                Log.d(TAG, "onViewCreated: VAL "+customVidHashMap.size());
//            }
//        },5000);

        return v;
    }

    private void refresh(){
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                Handler handler1 = new Handler();
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        //CALL DATA HERE
                        if(isOnline()) {
                            fetchVideosFromFirebase();
//
                            mPullToRefresh.setRefreshing(false);
                            Toast.makeText(getActivity(), "Feeds Updated Successfully.", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(false);
                        Toast.makeText(getActivity(), "Feeds Updated Successfully.", Toast.LENGTH_SHORT).show();
                    }
                },1500);
            }
        });
    }

    private void fetchVideosFromFirebase() {


        if (mSelectedSubInterests.size() > 0) {

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
            final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

            for (int i = 0; i < mSelectedSubInterests.size(); i++) {

                String subInt = mSelectedSubInterests.get(i);
//                mDatabaseReferencePosts.child(subInt).child("status");

                final int finalI = i;
                mDatabaseReferencePosts.child(subInt).child("video").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getValue());
//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getChildrenCount());

//                        Log.i(TAG, "onDataChange: "+mUserList.size()+" ");

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange: HOMEFRAGVIDEO 2 "+mStorageReferenceVideo);
                            final CustomVideo videoSnap = postSnapshot.getValue(CustomVideo.class);
                            videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                            mStorageReferenceVideo.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    videoSnap.setVideoURI(uri.toString());
                                }
                            });
                            if (!isDone.containsKey(postSnapshot.getKey())) {
//                                items.add(photoSnap);
                                isDone.put(postSnapshot.getKey(), videoSnap.getTimestamp());
                                videoHashMapStore.put(postSnapshot.getKey(), videoSnap);
                            }
//                            Log.d(TAG, "fetchVideosFromFirebase: ISDONE "+isDone.size());

                            mAdapter.notifyDataSetChanged();
//                            if (mAdapter.getItemCount() > 0) {
//                                avi.hide();
//                            }
                        }
                        Log.d(TAG, "fetchVideosFromFirebase: ISDONE " + isDone.size());
                        if (finalI == mSelectedSubInterests.size() - 1 && isDone.size() > 0) {
                            videoHashMap = sortByComparator(isDone, false);
                            Log.d(TAG, "fetchVideosFromFirebase: photo" + videoHashMap);
                            Log.d(TAG, "fetchVideosFromFirebase: photo" + videoHashMapStore);
                            for (int i = 0; i < videoHashMap.size(); i++) {
                                if (!items.contains(videoHashMapStore.get(videoHashMap.keySet().toArray()[i]))) {
                                    items.add(videoHashMapStore.get(videoHashMap.keySet().toArray()[i]));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
//                Log.d(TAG, "fetchVideosFromFirebase: ISDONE "+isDone.size());

            }
//            Log.d(TAG, "fetchVideosFromFirebase: ISDONE "+isDone.size());

        }
        Log.d(TAG, "fetchVideosFromFirebase: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
//        if(mAdapter.getItemCount()>0){
//            avi.hide();
//        }
    }

    private static HashMap<String, Long> sortByComparator(HashMap<String, Long> unsortMap, final boolean order)
    {

        List<HashMap.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<HashMap.Entry<String, Long>>()
        {
            public int compare(HashMap.Entry<String, Long> o1,
                               HashMap.Entry<String, Long> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        HashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (HashMap.Entry<String, Long> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onPause() {

        if (customVideoAdapter != null) {
            customVideoAdapter.releaseVideo();
        }

        super.onPause();
    }
}
