package com.example.sasuke.dailysuvichar.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.CustomVideo;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.models.Video;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.example.sasuke.dailysuvichar.view.adapter.CustomVideoAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.PhotoItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.StatusItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.VideoItemAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class GuruDetailActivity extends BaseActivity{

    private static final int RESULT_LOAD_IMAGE = 8008, RESULT_LOAD_COVER = 8009;
    private static final String TAG = "GURUDETAIL";
    private Context context;

    @BindView(R.id.recyclerview)
    RecyclerView mRvHome;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.dp)
    ImageView dp;
    @BindView(R.id.cover)
    ImageView cover;
    @BindView(R.id.bio)
    TextView bio;
    @BindView(R.id.specialization)
    TextView spec;
    @BindView(R.id.follow)
    Button follow;
    private boolean isFollowing = false;
    private String uid;
    private DatabaseReference mDatabaseReference, mDatabaseReferencePosts;
    private StorageReference mStorageReference;
    private FirebaseUser mFirebaseUser;
    private LinearLayoutManager mLayoutManager;
    Items items;
    private MultiTypeAdapter mAdapter;
    CustomVideoAdapter customVideoAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_detail);
        ButterKnife.bind(this);

        context = getApplicationContext();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("profile").child("user");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("users");

        Intent i = getIntent();
        isFollowing = i.getBooleanExtra("isfollowing", false);
        uid = i.getStringExtra("uid");

        setFollowing(isFollowing);
        if(dp!=null) {
//            ViewTarget.setTagId(R.id.glide_tag);

            Glide.with(GuruDetailActivity.this).
                                using(new FirebaseImageLoader())
                                .load(mStorageReference.child("dp").child(uid))
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(dp);
                    }
        if(cover!=null) {
//            ViewTarget.setTagId(R.id.glide_tag);

                        Glide.with(GuruDetailActivity.this).
                                using(new FirebaseImageLoader())
                                .load(mStorageReference.child("cover").child(uid))
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(cover);
        }

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Status.class, new StatusItemAdapter());
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mAdapter.register(Video.class, new VideoItemAdapter(this));
        mAdapter.register(CustomVideo.class, new CustomVideoAdapter());
        mRvHome.setAdapter(mAdapter);

        customVideoAdapter = new CustomVideoAdapter();
        mRvHome.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                customVideoAdapter.releaseVideo();
            }
        });

        items = new Items();

        fetchStatusFromFirebase();
        fetchPhotosFromFirebase();
        fetchVideosFromFirebase();

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        }

        if(uid!=null) {
            mDatabaseReference.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
                    if (dataSnapshot.child("name").getValue() != null) {
                        name.setText(dataSnapshot.child("name").getValue().toString());
                    }
                    if (dataSnapshot.child("bio").getValue() != null) {
                        bio.setText(dataSnapshot.child("bio").getValue().toString());
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

//        cover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(SharedPrefs.getUserType().equals("GURU")){
//                    Intent i = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RESULT_LOAD_COVER);
//                }
//            }
//        });

//        dp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(SharedPrefs.getUserType().equals("GURU")){
//                    Intent i = new Intent(
//                            Intent.ACTION_PICK,
//                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, RESULT_LOAD_IMAGE);
//                }
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
            dp.setImageBitmap(bmImg);
        }
        else if(requestCode == RESULT_LOAD_COVER && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
            cover.setImageBitmap(bmImg);
        }
    }

    @OnClick(R.id.name)
    public void setName(){
//        if(SharedPrefs.getUserType().equals("GURU")){
//            new MaterialDialog.Builder(this)
//                    .title("Set Your Username")
//                    .content("First Name + Last Name")
//                    .inputType(InputType.TYPE_CLASS_TEXT)
//                    .input("Enter the name...", "", new MaterialDialog.InputCallback() {
//                        @Override
//                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//                            name.setText(input);
//                        }
//                    }).show();
//        }
    }

    @OnClick(R.id.bio)
    public void setBio(){
//        if(SharedPrefs.getUserType().equals("GURU")){
//            new MaterialDialog.Builder(this)
//                    .title("Set Your Bio")
//                    .content("A Short Descripton About Yourself.")
//                    .inputType(InputType.TYPE_CLASS_TEXT)
//                    .input("Enter bio here...", "", new MaterialDialog.InputCallback() {
//                        @Override
//                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
//                            bio.setText("Bio:" + input);
//                        }
//                    }).show();
//        }
    }

    @OnClick(R.id.specialization)
    public void setSpec(){
//        if(SharedPrefs.getUserType().equals("GURU")){
//            new MaterialDialog.Builder(this)
//                    .title("Set Your Specialization:")
//                    .content("Spiritual Skill You Majorly Specialize In.")
//                    .items(new String[]{"Astrology Guru", "Yoga Guru", "Pandit", "Motivation Guru", "Ayurveda Guru"})
//                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
//                        @Override
//                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
//                            /**
//                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
//                             * returning false here won't allow the newly selected radio button to actually be selected.
//                             **/
//                            switch (which){
//                                case 0:{
//                                    spec.setText("Astrology Guru");
//                                    break;
//                                }
//                                case 1:{
//                                    spec.setText("Yoga Guru");
//                                    break;
//                                }
//                                case 2:{
//                                    spec.setText("Pandit");
//                                    break;
//                                }
//                                case 3:{
//                                    spec.setText("Motivation Guru");
//                                    break;
//                                }
//                                case 4:{
//                                    spec.setText("Ayurveda Guru");
//                                    break;
//                                }
//                            }
//                            return true;
//                        }
//                    })
//                    .positiveText("Choose This")
//                    .show();
//        }
    }

    public void setFollowing(boolean isFollowing){
        if(isFollowing){
            follow.setText("FOLLOWING");
            follow.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else {
            follow.setText("FOLLOW");
            follow.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    @OnClick(R.id.follow)
    public void onClickFOllow(){
        if(follow.getText().equals("FOLLOW")){
            follow.setText("FOLLOWING");
            follow.setBackgroundColor(getResources().getColor(R.color.green));
        }
        else {
            follow.setText("FOLLOW");
            follow.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    private void fetchPhotosFromFirebase() {

        if (uid != null) {
            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(uid).child("posts");
            mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");

            mDatabaseReferencePosts.child("photo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Photo photoSnap = postSnapshot.getValue(Photo.class);
                        photoSnap.setStorageReference(mStorageReference.child(postSnapshot.getKey()));

                        items.add(photoSnap);
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
    }

    private void fetchStatusFromFirebase() {

        if (uid != null) {

            mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(uid).child("posts");
            mDatabaseReferencePosts.child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Status status = postSnapshot.getValue(Status.class);
                        items.add(status);
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
    }

    private void fetchVideosFromFirebase() {


        mDatabaseReferencePosts = FirebaseDatabase.getInstance().getReference("users").child(uid).child("posts");
            final StorageReference mStorageReferenceVideo = FirebaseStorage.getInstance().getReference("posts").child("videos");

                mDatabaseReferencePosts.child("video").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

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
                            items.add(videoSnap);
                            }
                            mAdapter.notifyDataSetChanged();
                        }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: " + databaseError.getMessage());
                    }
                });
        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {

        if (customVideoAdapter != null) {
            customVideoAdapter.releaseVideo();
        }

        super.onPause();
    }


}
