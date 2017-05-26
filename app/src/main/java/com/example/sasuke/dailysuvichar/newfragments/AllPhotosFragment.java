package com.example.sasuke.dailysuvichar.newfragments;


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
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.view.adapter.PhotoItemAdapter;
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
public class AllPhotosFragment extends Fragment {

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
    private HashMap<String, Long> isDone;
    private HashMap<String, Photo> photoHashMapStore;
    private HashMap<String, Long> photoHashMap;

    @BindView(R.id.recyclerview)
    RecyclerView mRvHome;

    public AllPhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_all_photos, container, false);
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
//        customVideoAdapter = new CustomVideoAdapter();

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mRvHome.setAdapter(mAdapter);

        items = new Items();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.e(TAG, uid);
        Log.e(TAG, mDatabaseReference.toString());

        mSelectedSubInterests = new ArrayList<>();

        isDone= new HashMap<>();
        photoHashMap = new HashMap<>();
        photoHashMapStore = new HashMap<>();

        refresh();

        mDatabaseReference.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("mSelectedSubInterests").getValue()!=null) {
                    mSelectedSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                }
                fetchPhotosFromFirebase();
                alternateLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        fetchPhotosFromFirebase();



//        Handler handler= new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Log.d(TAG, "onViewCreated: VAL "+allPostsHashMapFinal.size());
//                Log.d(TAG, "onViewCreated: VAL "+photoHashMap.size());
//                Log.d(TAG, "onViewCreated: VAL "+photoHashMap.size());
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
                        fetchPhotosFromFirebase();
//
                        mPullToRefresh.setRefreshing(false);
                        Toast.makeText(getActivity(), "Feeds Updated Successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(false);
                    }
                },1500);
            }
        });
    }

    private void fetchPhotosFromFirebase() {


        if (mSelectedSubInterests.size() > 0) {
            Log.d(TAG, "fetchPhotosFromFirebase: SIZE " + mSelectedSubInterests.size());
            Log.d(TAG, "fetchPhotosFromFirebase: " + mSelectedSubInterests);

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
            for (int i = 0; i < mSelectedSubInterests.size(); i++) {

                String subInt = mSelectedSubInterests.get(i);
//                mDatabaseReferencePosts.child(subInt).child("status");

                final int finalI = i;
                mDatabaseReferencePosts.child(subInt).child("photo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getValue());
//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getChildrenCount());

//                        Log.i(TAG, "onDataChange: "+mUserList.size()+" ");

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Photo photoSnap = postSnapshot.getValue(Photo.class);
                            photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));

                            if (!isDone.containsKey(postSnapshot.getKey())) {
//                                items.add(status);
                                isDone.put(postSnapshot.getKey(), photoSnap.getTimestamp());
                                photoHashMapStore.put(postSnapshot.getKey(), photoSnap);
                            }
//                            Log.d(TAG, "fetchPhotosFromFirebase: ISDONE "+isDone.size());

                            mAdapter.notifyDataSetChanged();
//                            if (mAdapter.getItemCount() > 0) {
//                                avi.hide();
//                            }
                        }
                        Log.d(TAG, "fetchPhotosFromFirebase: ISDONE " + isDone.size());
                        if (finalI == mSelectedSubInterests.size() - 1 && isDone.size() > 0) {
                            photoHashMap = sortByComparator(isDone, false);
                            Log.d(TAG, "fetchPhotosFromFirebase: photo" + photoHashMap);
                            Log.d(TAG, "fetchPhotosFromFirebase: photo" + photoHashMapStore);
                            for (int i = 0; i < photoHashMap.size(); i++) {
                                if (!items.contains(photoHashMapStore.get(photoHashMap.keySet().toArray()[i]))) {
                                    items.add(photoHashMapStore.get(photoHashMap.keySet().toArray()[i]));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
//                Log.d(TAG, "fetchPhotosFromFirebase: ISDONE "+isDone.size());

            }
//            Log.d(TAG, "fetchPhotosFromFirebase: ISDONE "+isDone.size());

        }
        Log.d(TAG, "fetchPhotosFromFirebase: " + items.size());
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

}
