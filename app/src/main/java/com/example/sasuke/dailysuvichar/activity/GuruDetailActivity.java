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
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.firebase.ui.storage.images.FirebaseImageLoader;
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

public class GuruDetailActivity extends BaseActivity{

    private static final int RESULT_LOAD_IMAGE = 8008, RESULT_LOAD_COVER = 8009;
    private Context context;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
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
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;
    private FirebaseUser mFirebaseUser;


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
        isFollowing = i.getBooleanExtra("isfollowing",false);
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

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefs.getUserType().equals("GURU")){
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_COVER);
                }
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefs.getUserType().equals("GURU")){
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });
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
        if(SharedPrefs.getUserType().equals("GURU")){
            new MaterialDialog.Builder(this)
                    .title("Set Your Username")
                    .content("First Name + Last Name")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("Enter the name...", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            name.setText(input);
                        }
                    }).show();
        }
    }

    @OnClick(R.id.bio)
    public void setBio(){
        if(SharedPrefs.getUserType().equals("GURU")){
            new MaterialDialog.Builder(this)
                    .title("Set Your Bio")
                    .content("A Short Descripton About Yourself.")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("Enter bio here...", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            bio.setText("Bio:" + input);
                        }
                    }).show();
        }
    }

    @OnClick(R.id.specialization)
    public void setSpec(){
        if(SharedPrefs.getUserType().equals("GURU")){
            new MaterialDialog.Builder(this)
                    .title("Set Your Specialization:")
                    .content("Spiritual Skill You Majorly Specialize In.")
                    .items(new String[]{"Astrology Guru", "Yoga Guru", "Pandit", "Motivation Guru", "Ayurveda Guru"})
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/
                            switch (which){
                                case 0:{
                                    spec.setText("Astrology Guru");
                                    break;
                                }
                                case 1:{
                                    spec.setText("Yoga Guru");
                                    break;
                                }
                                case 2:{
                                    spec.setText("Pandit");
                                    break;
                                }
                                case 3:{
                                    spec.setText("Motivation Guru");
                                    break;
                                }
                                case 4:{
                                    spec.setText("Ayurveda Guru");
                                    break;
                                }
                            }
                            return true;
                        }
                    })
                    .positiveText("Choose This")
                    .show();
        }
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
}
