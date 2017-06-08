package com.example.sasuke.dailysuvichar.newnewfragments;


import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.AboutActivity;
import com.example.sasuke.dailysuvichar.models.CustomVideo;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.view.adapter.CustomVideoAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.PhotoItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.StatusItemAdapter;
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
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommonFragment extends Fragment {


    @BindView(R.id.swiperefresh_common)
    SwipeRefreshLayout mPullToRefresh;
    @BindView(R.id.alternate_layout_common)
    LinearLayout alternateLayout;

    private MultiTypeAdapter mAdapter;

    private LinearLayoutManager mLayoutManager;
    private static FirebaseUser mFirebaseUser;
    private String uid;
    private AVLoadingIndicatorView avi;
    private HashMap<String, String> userStatus;
    private static DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseReferencePosts, mDatabaseReferenceGuru;
    private StorageReference mStorageReference;
    private ArrayList<String> mSelectedSubInterests;
    private ArrayList<Status> statusYouraList, statusHomeaList;
    private StorageReference mStorageReferenceDP;
    private HashMap mAllInterests;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mUsersDatabase;
    private ArrayList<String> mSelectedGurus;

    private static final String TAG = "ALLSTATUS", STATUS = "status";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    Items items;
    private Animation slide_down;
    private Animation slide_up;
    private int CHECK = 1;
    private String intentDBReference = null;
    private HashMap<String, Long> isDone;
    private HashMap<String, Status> statusHashMapStore;
    private HashMap<String, Long> statusHashMap;
    private String from = "HOME";
    private HashMap<String, Boolean> isStatusDone, isStatusDoneGuru;

    @BindView(R.id.recyclerview_common)
    RecyclerView mRvHome;


    public CommonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ButterKnife.bind(getActivity());
        View v = inflater.inflate(R.layout.fragment_common, container, false);

        setHasOptionsMenu(true);

        Toast.makeText(getActivity(), "COMMON FRAGMENT", Toast.LENGTH_SHORT).show();
        String from = getArguments().getString("from");

        mRvHome = (RecyclerView) v.findViewById(R.id.recyclerview_common);
        mPullToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh_common);
        alternateLayout = (LinearLayout) v.findViewById(R.id.alternate_layout_common);
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

        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
//        customVideoAdapter = new CustomVideoAdapter();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Status.class, new StatusItemAdapter());
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mAdapter.register(CustomVideo.class, new CustomVideoAdapter());
        mRvHome.setAdapter(mAdapter);

        items = new Items();

        if(isOnline()) {
            if (from.equals(getString(R.string.title_home))) {
                Log.e("FROM", from);

                isDone = new HashMap<>();
                mSelectedGurus = new ArrayList<>();

                mDatabaseReferenceGuru = FirebaseDatabase.getInstance().getReference("users");

                    refresh();

                    mDatabaseReferenceGuru.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("following").getValue() != null) {
                                mSelectedGurus.addAll((Collection<? extends String>) dataSnapshot.child("following").getValue());
                            }
                            fetchGuruPostsFromFirebase();
                            alternateLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                fetchGuruPostsFromFirebase();

                refresh();

            } else if (from.equals(getString(R.string.title_explore))) {

                isDone = new HashMap<>();
                mAllInterests = new HashMap();

                Log.e("FROM", from);

                mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
                Log.e(TAG, uid);
                Log.e(TAG, mDatabaseReference.toString());

                mDatabaseReference.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("mAllInterests").getValue() != null) {
//                            mSelectedSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                            mAllInterests.putAll((Map<? extends String, ? extends ArrayList<String>>) dataSnapshot.child("mAllInterests").getValue());
                        }
                        fetchExplorePostsFromFirebase();
                        alternateLayout.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                fetchExplorePostsFromFirebase();

                refresh();

            } else if (from.equals(getString(R.string.title_your_feeds))) {
                Log.e("FROM", from);
                isDone = new HashMap<>();
                alternateLayout.setVisibility(View.INVISIBLE);

                fetchYourPostsFromFirebase();

                refresh();
            }
        }else{
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    private void fetchGuruPostsFromFirebase() {

        final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

        if (mSelectedGurus != null && mSelectedGurus.size() > 0) {

            for (String guru : mSelectedGurus) {

                mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(guru).child("userPosts");

                mDatabaseReferencePosts.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            if (!isDone.containsKey(postSnapshot.getKey())) {

                                if (postSnapshot.child("type").getValue().equals("status")) {
                                    Log.d(TAG, "onDataChange: DATA troo");
                                    Status statusSnap = postSnapshot.getValue(Status.class);
                                    isDone.put(postSnapshot.getKey(), (long) 1);
                                    items.add(statusSnap);
                                } else if (postSnapshot.child("type").getValue().equals("photo")) {
                                    Photo photoSnap = postSnapshot.getValue(Photo.class);
                                    photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));
                                    isDone.put(postSnapshot.getKey(), (long) 1);

                                    items.add(photoSnap);
                                } else if (postSnapshot.child("type").getValue().equals("video")) {
                                    final CustomVideo videoSnap = postSnapshot.getValue(CustomVideo.class);
                                    if (mStorageReferenceVideo.child(postSnapshot.getKey()) != null) {
                                        isDone.put(postSnapshot.getKey(), (long) 1);
                                        items.add(videoSnap);
                                        videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                                        mStorageReferenceVideo.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                videoSnap.setVideoURI(uri.toString());
                                            }
                                        });
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
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

    private void fetchYourPostsFromFirebase() {

        final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

        mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid()).child("userPosts");

        Log.d(TAG, "fetchStatusFromFirebase: URLL " + mDatabaseReferencePosts);
        mDatabaseReferencePosts.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");

                    if (!isDone.containsKey(postSnapshot.getKey())) {

                        if (postSnapshot.child("type").getValue().equals("status")) {
                            Log.d(TAG, "onDataChange: DATA troo");
                            Status statusSnap = postSnapshot.getValue(Status.class);
                            isDone.put(postSnapshot.getKey(), (long) 1);
                            items.add(statusSnap);
                        } else if (postSnapshot.child("type").getValue().equals("photo")) {
                            Photo photoSnap = postSnapshot.getValue(Photo.class);
                            photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));
                            isDone.put(postSnapshot.getKey(), (long) 1);

                            items.add(photoSnap);
                        } else if (postSnapshot.child("type").getValue().equals("video")) {
                            final CustomVideo videoSnap = postSnapshot.getValue(CustomVideo.class);
                            if (mStorageReferenceVideo.child(postSnapshot.getKey()) != null) {
                                isDone.put(postSnapshot.getKey(), (long) 1);
                                items.add(videoSnap);
                                videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                                mStorageReferenceVideo.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        videoSnap.setVideoURI(uri.toString());
                                    }
                                });
                            }
                        }
                        mAdapter.notifyDataSetChanged();
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


    private void fetchExplorePostsFromFirebase() {
        if (mAllInterests != null && mAllInterests.keySet().size() > 0) {
            ArrayList<String> interests = new ArrayList<>();
            interests.addAll(mAllInterests.keySet());

            Log.d(TAG, "fetchExplorePostsFromFirebase: "+interests);
            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("allPosts");

            mDatabaseReferencePosts.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");

                    final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        Log.d(TAG, "onDataChange: DATA "+postSnapshot);
                        Log.d(TAG, "onDataChange: DATA "+postSnapshot.child("type"));

                        if(!isDone.containsKey(postSnapshot.getKey())) {

                            if (postSnapshot.child("type").getValue().equals("status")) {
                                Log.d(TAG, "onDataChange: DATA troo");
                                Status statusSnap = postSnapshot.getValue(Status.class);
                                isDone.put(postSnapshot.getKey(), (long) 1);
                                items.add(statusSnap);
                            } else if (postSnapshot.child("type").getValue().equals("photo")) {
                                Photo photoSnap = postSnapshot.getValue(Photo.class);
                                photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));
                                isDone.put(postSnapshot.getKey(), (long) 1);

                                items.add(photoSnap);
                            } else if (postSnapshot.child("type").getValue().equals("video")) {
                                final CustomVideo videoSnap = postSnapshot.getValue(CustomVideo.class);
                                if (mStorageReferenceVideo.child(postSnapshot.getKey()) != null) {
                                    isDone.put(postSnapshot.getKey(), (long) 1);
                                    items.add(videoSnap);
                                    videoSnap.setStorageReference(mStorageReferenceVideo.child(postSnapshot.getKey()));
                                    mStorageReferenceVideo.child(postSnapshot.getKey()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            videoSnap.setVideoURI(uri.toString());
                                        }
                                    });
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }else{

                        }

                    }
                    mAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                }
            });

            Log.d(TAG, "fetchExplorePostsFromFirebase: " + items.size());
            mAdapter.setItems(items);
            mAdapter.notifyDataSetChanged();
        }else{
            Log.d(TAG, "fetchExplorePostsFromFirebase: NULL");
        }

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

                            if (from.equals(getString(R.string.title_home))) {
                                Log.e("FROM", from);

                                fetchGuruPostsFromFirebase();
                            } else if (from.equals(getString(R.string.title_explore))) {

                                Log.e("FROM", from);

                                fetchExplorePostsFromFirebase();

                            } else if (from.equals(getString(R.string.title_your_feeds))) {
                                Log.e("FROM", from);

                                fetchYourPostsFromFirebase();
                            }
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_common, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_language: {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.choose_lang)
                        .items(new String[]{getString(R.string.english_lang), getString(R.string.hindi_lang)})
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which){
                                    case 0:{
                                        //FETCH ENGLISH FEEDS
                                        if(from.equals(getString(R.string.title_home))){
                                            //FETCH ENGLISH FEEDS FOR HOME
                                        }
                                        else if(from.equals(getString(R.string.title_explore))){
                                            //FETCH ENGLISH FEEDS FOR EXPLORE
                                        }
                                        else if(from.equals(getString(R.string.title_your_feeds))){
                                            //FETCH ENGLISH FEEDS FOR YOUR FEEDS
                                        }
                                        break;
                                    }
                                    case 1:{
                                        //FETCH HINDI FEEDS
                                        if(from.equals(getString(R.string.title_home))){
                                            //FETCH HINDI FEEDS FOR HOME
                                        }
                                        else if(from.equals(getString(R.string.title_explore))){
                                            //FETCH HINDI FEEDS FOR EXPLORE
                                        }
                                        else if(from.equals(getString(R.string.title_your_feeds))){
                                            //FETCH HINDI FEEDS FOR YOUR FEEDS
                                        }
                                        break;
                                    }
                                }
                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .show();
                break;
            }
            case R.id.about: {
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
