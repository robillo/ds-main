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
    private HashMap<String, Boolean>isVideoDoneYour;
    private static DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePosts, mDatabaseReferenceGuru;
    private StorageReference mStorageReference;
    private ArrayList<String> mSelectedSubInterests;
    private ArrayList<String> mSelectedGurus;
    private HashMap<String, Boolean> isVideoDoneGuru;

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
    private String from = "HOME";

    @BindView(R.id.recyclerview)
    RecyclerView mRvHome;

    public AllVideosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_videos, container, false);
        ButterKnife.bind(getActivity());

        Bundle args = getArguments();
        from = args.getString("from");

        mRvHome = (RecyclerView) v.findViewById(R.id.recyclerview);
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

        if (isOnline()) {
            //WONT CAUSE NPE DONT WORRY
            if (from.equals("YOUR")) {
                //SHOW YOUR FEEDS, COPY CODE FROM YOUR FEEDS FRAGMENT
                Log.e("FROM", "YOUR TO VIDEOS");


                isVideoDoneYour = new HashMap<>();

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
                Log.e(TAG, uid);
                Log.e(TAG, mDatabaseReference.toString());

                refresh();

                fetchVideosFromFirebaseYour();

            } else if (from.equals("EXPLORE")) {
                //SHOW FEEDS ON YOUR INTERESTS
                Log.e("FROM", "EXPLORE TO VIDEOS");

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
                Log.e(TAG, uid);
                Log.e(TAG, mDatabaseReference.toString());

                mSelectedSubInterests = new ArrayList<>();

                isDone = new HashMap<>();
                videoHashMap = new HashMap<>();
                videoHashMapStore = new HashMap<>();

                mDatabaseReference.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("mSelectedSubInterests").getValue() != null) {
                            mSelectedSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                        }
                        fetchVideosFromFirebaseExplore();
                        alternateLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                fetchVideosFromFirebaseExplore();
                Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();

            } else if (from.equals("HOME")) {
                //SHOW FEEDS FROM WHO YOU FOLLOW + DS PEOPLE
                Log.e("FROM", "HOME TO VIDEOS");

                mSelectedGurus = new ArrayList<>();
                isVideoDoneGuru = new HashMap<>();

                mDatabaseReferenceGuru = FirebaseDatabase.getInstance().getReference("users");

                refresh();

                mDatabaseReferenceGuru.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("following").getValue() != null) {
                            mSelectedGurus.addAll((Collection<? extends String>) dataSnapshot.child("following").getValue());
                        }
                        fetchVideosFromFirebaseGuru();
                        alternateLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                fetchVideosFromFirebaseGuru();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void refresh() {
        mPullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                Handler handler1 = new Handler();
                handler1.post(new Runnable() {
                    @Override
                    public void run() {
                        //CALL DATA HERE
                        if (isOnline()) {
                            fetchVideosFromFirebaseExplore();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    private void fetchVideosFromFirebaseGuru() {

        mLayoutManager.setStackFromEnd(true);

        if (mSelectedGurus != null && mSelectedGurus.size() > 0) {

            for (String guru : mSelectedGurus) {

                mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(guru).child("posts");
                mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("videos");

                mDatabaseReferencePosts.child("video").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            final CustomVideo videoSnap = postSnapshot.getValue(CustomVideo.class);
                            videoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));
                            mStorageReference.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    videoSnap.setVideoURI(uri.toString());
                                }
                            });

                            if (!isVideoDoneGuru.containsKey(postSnapshot.getKey())) {

                                items.add(videoSnap);
                                isVideoDoneGuru.put(postSnapshot.getKey(), true);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
            }

            Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void fetchVideosFromFirebaseYour() {

        mLayoutManager.setStackFromEnd(true);

        mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid()).child("posts");
        mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("videos");

        mDatabaseReferencePosts.child("video").orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final CustomVideo videoSnap = postSnapshot.getValue(CustomVideo.class);
                    videoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));
                    mStorageReference.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            videoSnap.setVideoURI(uri.toString());
                        }
                    });

                    if (!isVideoDoneYour.containsKey(postSnapshot.getKey())) {

                        items.add(videoSnap);
                        isVideoDoneYour.put(postSnapshot.getKey(), true);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });


        Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

    private void fetchVideosFromFirebaseExplore() {

//        final ExecutorService executor = new ThreadPoolExecutor(3, 3, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(15));

        if (mSelectedSubInterests.size() > 0) {

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
            final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

            for (int i = 0; i < mSelectedSubInterests.size(); i++) {

                String subInt = mSelectedSubInterests.get(i);
//                mDatabaseReferencePosts.child(subInt).child("status");

                final int finalI = i;
                mDatabaseReferencePosts.child(subInt).child("video").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getValue());
//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getChildrenCount());

//                        Log.i(TAG, "onDataChange: "+mUserList.size()+" ");

//                        executor.execute(new Runnable() {
//                            @Override
//                            public void run() {
                        for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "onDataChange: HOMEFRAGVIDEO 2 " + mStorageReferenceVideo);
                            final CustomVideo videoSnap = postSnapshot.getValue(CustomVideo.class);
                            if (mStorageReferenceVideo.child(postSnapshot.getKey()) != null) {

                                if (!isDone.containsKey(postSnapshot.getKey())) {
//                                items.add(photoSnap);
                                    videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                                    isDone.put(postSnapshot.getKey(), videoSnap.getTimestamp());
                                    videoHashMapStore.put(postSnapshot.getKey(), videoSnap);
                                    mStorageReferenceVideo.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            videoSnap.setVideoURI(uri.toString());
                                        }
                                    });
                                }
                            }

                        }
//                            Log.d(TAG, "fetchVideosFromFirebaseExplore: ISDONE "+isDone.size());

                        mAdapter.notifyDataSetChanged();
//                            if (mAdapter.getItemCount() > 0) {
//                                avi.hide();
//                            }

//                        });
                        Log.d(TAG, "fetchVideosFromFirebaseExplore: ISDONE " + isDone.size());
                        if (finalI == mSelectedSubInterests.size() - 1 && isDone.size() > 0) {
                            videoHashMap = sortByComparator(isDone, false);
                            Log.d(TAG, "fetchVideosFromFirebaseExplore: photo" + videoHashMap);
                            Log.d(TAG, "fetchVideosFromFirebaseExplore: photo" + videoHashMapStore);
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
//                Log.d(TAG, "fetchVideosFromFirebaseExplore: ISDONE "+isDone.size());

            }
//            Log.d(TAG, "fetchVideosFromFirebaseExplore: ISDONE "+isDone.size());

        }
        Log.d(TAG, "fetchVideosFromFirebaseExplore: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
//        executor.shutdown();
//        if(mAdapter.getItemCount()>0){
//            avi.hide();
//        }
    }

    private static HashMap<String, Long> sortByComparator(HashMap<String, Long> unsortMap, final boolean order) {

        List<HashMap.Entry<String, Long>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<HashMap.Entry<String, Long>>() {
            public int compare(HashMap.Entry<String, Long> o1,
                               HashMap.Entry<String, Long> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        HashMap<String, Long> sortedMap = new LinkedHashMap<>();
        for (HashMap.Entry<String, Long> entry : list) {
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
