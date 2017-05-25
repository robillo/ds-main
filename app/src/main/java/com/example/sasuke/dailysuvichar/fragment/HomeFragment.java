package com.example.sasuke.dailysuvichar.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.FullScreenActivity;
import com.example.sasuke.dailysuvichar.activity.HomeActivity;
import com.example.sasuke.dailysuvichar.activity.SelectActivity;
import com.example.sasuke.dailysuvichar.activity.SelectPhotoActivity;
import com.example.sasuke.dailysuvichar.activity.SelectVideoActivity;
import com.example.sasuke.dailysuvichar.event.DoubleTabEvent;
import com.example.sasuke.dailysuvichar.models.CustomVideo;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.models.Video;
import com.example.sasuke.dailysuvichar.view.adapter.CustomVideoAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.PhotoItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.StatusItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.VideoItemAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment {

    private MultiTypeAdapter mAdapter;

    @BindView(R.id.rv_home)
    RecyclerView mRvHome;
    @BindView(R.id.rl_menu)
    RelativeLayout mRlMenu;
    @BindView(R.id.tv_status)
    TextView status;
    @BindView(R.id.pullToRefresh)
    FireworkyPullToRefreshLayout mPullToRefresh;
    @BindView(R.id.iv_profile)
    CircularImageView homeDP;

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

    private static final String TAG = "ROBILLO", STATUS = "status";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    Items items;
    CustomVideoAdapter customVideoAdapter;
    private Animation slide_down;
    private Animation slide_up;
    private int CHECK = 1;
    private String intentDBReference = null;
    private HashMap<String, Long> isDone;
    private HashMap<String, Long> allPostsHashMap, statusHashMap, photoHashMap, customVidHashMap;
    private HashMap<String, Status> statusHashMapStore;
    private HashMap<String, Photo>  photoHashMapStore;
    private HashMap<String, CustomVideo> customVidHashMapStore;
    private HashMap<String, Boolean> isPhotoDone, isVideoDone;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");

        fetchDP();

        avi = (AVLoadingIndicatorView) view.findViewById(R.id.avi);

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
        mAdapter.register(Status.class, new StatusItemAdapter());
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mAdapter.register(CustomVideo.class, new CustomVideoAdapter());
        mAdapter.register(Video.class, new VideoItemAdapter(getActivity()));
        mRvHome.setAdapter(mAdapter);

        items = new Items();

        mRvHome.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                customVideoAdapter.releaseVideo();
            }
        });

        mPullToRefresh.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fetchVideosFromFirebase();
                        fetchStatusFromFirebase();
                        fetchPhotosFromFirebase();

                        mPullToRefresh.setRefreshing(false);
                        Toast.makeText(getActivity(), "Feeds Updated Successfully.", Toast.LENGTH_SHORT).show();
                    }
                }, 5000);
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        Log.e(TAG, uid);
        Log.e(TAG, mDatabaseReference.toString());

        mSelectedSubInterests = new ArrayList<>();


        mDatabaseReference.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onDataChange: SUBINTS " + dataSnapshot.child("selectedSubInterests"));
//                Log.d(TAG, "onDataChange: SUBINTS " + dataSnapshot.getChildrenCount());
//                User user = dataSnapshot.getValue(User.class);
//                Log.d(TAG, "onDataChange: INTTTT "+dataSnapshot.child("selectedSubInterests").getValue());
                if(dataSnapshot.child("mSelectedSubInterests").getValue()!=null) {
                    mSelectedSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                }
                fetchVideosFromFirebase();
                fetchStatusFromFirebase();
                fetchPhotosFromFirebase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        fetchVideosFromFirebase();

        fetchStatusFromFirebase();

        fetchPhotosFromFirebase();
    }

    @OnClick(R.id.iv_profile)
    public void viewHomeDP(){
        if(intentDBReference!=null){
            Intent i = new Intent(getActivity(), FullScreenActivity.class);
            i.putExtra("path", intentDBReference);
            Log.e("Storage Reference", intentDBReference);
            getActivity().startActivity(i);
        }
    }

    private void fetchDP() {

        mUsersDatabase.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                intentDBReference = mStorageReferenceDP.child(dataSnapshot.getKey()).toString();
                Log.e("REFERENCE", intentDBReference);
                if(dataSnapshot.child("photoUrl").getValue()!=null) {
                    Glide.with(getActivity()).
                            using(new FirebaseImageLoader())
                            .load(mStorageReferenceDP.child(dataSnapshot.getKey()))
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(homeDP);
                    Log.e("GILDED?", "YES");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void fetchVideosFromFirebase() {
        isVideoDone= new HashMap<>();
        customVidHashMap = new HashMap<>();
        customVidHashMapStore = new HashMap<>();
        allPostsHashMap = new HashMap<>();

        if (mSelectedSubInterests.size() > 0) {

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
            final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

            for (String subInt : mSelectedSubInterests) {

                mDatabaseReferencePosts.child(subInt).child("video").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        Log.d(TAG, "onDataChange: HOMEFRAG SUBINT "+dataSnapshot.getRef());

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
                            if(!isVideoDone.containsKey(postSnapshot.getKey())) {
                                Log.d(TAG, "onDataChange: KEY "+postSnapshot.getKey());

                                items.add(videoSnap);
                                isVideoDone.put(postSnapshot.getKey(),true);
                                customVidHashMap.put(postSnapshot.getKey(),videoSnap.getTimestamp());
                                customVidHashMapStore.put(postSnapshot.getKey(),videoSnap);
                                allPostsHashMap.put(postSnapshot.getKey(), videoSnap.getTimestamp());

                            }
                            mAdapter.notifyDataSetChanged();
                            if(mAdapter.getItemCount()>0){
                                avi.hide();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });

            }
        }
        Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
        if(mAdapter.getItemCount()>0){
            avi.hide();
        }
    }

    @Override
    public void onPause() {

        if (customVideoAdapter != null) {
            customVideoAdapter.releaseVideo();
        }

        super.onPause();
    }

    private void fetchPhotosFromFirebase() {

        isPhotoDone= new HashMap<>();
        photoHashMap = new HashMap<>();
        photoHashMapStore = new HashMap<>();
        allPostsHashMap = new HashMap<>();

//        items = new Items();
        if (mSelectedSubInterests.size() > 0) {

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
            mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");

            for (String subInt : mSelectedSubInterests) {

                mDatabaseReferencePosts.child(subInt).child("photo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Photo photoSnap = postSnapshot.getValue(Photo.class);
                            photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));

                            if(!isPhotoDone.containsKey(postSnapshot.getKey())) {
                                Log.d(TAG, "onDataChange: KEY "+postSnapshot.getKey());

                                items.add(photoSnap);
                                isPhotoDone.put(postSnapshot.getKey(),true);
                                photoHashMap.put(postSnapshot.getKey(),photoSnap.getTimestamp());
                                photoHashMapStore.put(postSnapshot.getKey(),photoSnap);
                                allPostsHashMap.put(postSnapshot.getKey(), photoSnap.getTimestamp());
                            }
                            mAdapter.notifyDataSetChanged();
                            if(mAdapter.getItemCount()>0){
                                avi.hide();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });

            }
        }
        Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
        if(mAdapter.getItemCount()>0){
            avi.hide();
        }
    }

    private void fetchStatusFromFirebase() {

        isDone= new HashMap<>();
        statusHashMap = new HashMap<>();
        statusHashMapStore = new HashMap<>();
        allPostsHashMap = new HashMap<>();

        if (mSelectedSubInterests.size() > 0) {
            Log.d(TAG, "fetchStatusFromFirebase: SIZE " + mSelectedSubInterests.size());
            Log.d(TAG, "fetchStatusFromFirebase: " + mSelectedSubInterests);

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("tags");
            for (int i = 0; i < mSelectedSubInterests.size(); i++) {

                String subInt = mSelectedSubInterests.get(i);
//                mDatabaseReferencePosts.child(subInt).child("status");

                final int finalI = i;
                mDatabaseReferencePosts.child(subInt).child("status").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getValue());
//                        Log.i(TAG, "onDataChange2222: " + dataSnapshot.getChildrenCount());

//                        Log.i(TAG, "onDataChange: "+mUserList.size()+" ");

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Status status = postSnapshot.getValue(Status.class);

                            if (!isDone.containsKey(postSnapshot.getKey())) {
//                                items.add(status);
                                isDone.put(postSnapshot.getKey(), status.getTimestamp());
                                statusHashMapStore.put(postSnapshot.getKey(), status);
                                allPostsHashMap.put(postSnapshot.getKey(), status.getTimestamp());
                            }
//                            Log.d(TAG, "fetchStatusFromFirebase: ISDONE "+isDone.size());

                            mAdapter.notifyDataSetChanged();
                            if (mAdapter.getItemCount() > 0) {
                                avi.hide();
                            }
                        }
                        Log.d(TAG, "fetchStatusFromFirebase: ISDONE " + isDone.size());
                        if (finalI == mSelectedSubInterests.size() - 1 && isDone.size() > 0) {
                            statusHashMap = sortByComparator(isDone, false);
                            Log.d(TAG, "fetchStatusFromFirebase: STATUS" + statusHashMap);
                            Log.d(TAG, "fetchStatusFromFirebase: STATUS" + statusHashMapStore);
                            for (int i = 0; i < statusHashMap.size(); i++) {
                                if (!items.contains(statusHashMapStore.get(statusHashMap.keySet().toArray()[i]))) {
                                    items.add(statusHashMapStore.get(statusHashMap.keySet().toArray()[i]));
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
//                Log.d(TAG, "fetchStatusFromFirebase: ISDONE "+isDone.size());

            }
//            Log.d(TAG, "fetchStatusFromFirebase: ISDONE "+isDone.size());

        }
        Log.d(TAG, "fetchStatusFromFirebase: " + items.size());
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
        if(mAdapter.getItemCount()>0){
            avi.hide();
        }
    }

    @OnClick(R.id.tv_status)
    public void intent() {
        startActivity(new Intent(getActivity(), SelectActivity.class));
    }

    @OnClick(R.id.tv_video)
    public void openVid() {
        startActivity(new Intent(getActivity(), SelectVideoActivity.class));
        getActivity().finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DoubleTabEvent event) {
        mRvHome.getLayoutManager().scrollToPosition(1);
    }

    @OnClick(R.id.tv_photo)
    public void uploadImage() {
        startActivity(new Intent(getActivity(), SelectPhotoActivity.class));
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
//                some_imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadToFirebase() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = mStorageReference.child("images/pic.jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(getActivity(), "File Uploaded ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();

                            Log.d(TAG, "onFailure: " + exception.getMessage());

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Log.d(TAG, "uploadToFirebase: No file chosen!");
        }
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
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

    public static void onLikeClicked(String item, final boolean isLiked){

        Log.d(TAG, "onLikeClicked: ");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(mFirebaseUser.getUid()).child("posts").child(item).child("Kk0gD76Af7InSLtYl84");
        mDatabaseReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Photo ph = mutableData.getValue(Photo.class);
                if (ph == null) {
                    return Transaction.success(mutableData);
                }

                if (!isLiked) {
                    // Unstar the post and remove self from stars
                    ph.setLikes(ph.getLikes()-1);
//                    ph.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    ph.setLikes(ph.getLikes()+1);
//                    ph.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(ph);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:{

                break;
            }
            case R.id.action_sort:{

                break;
            }
            case R.id.about:{

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
