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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.sasuke.dailysuvichar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_detail);
        ButterKnife.bind(this);

        context = getApplicationContext();

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        }

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_COVER);
            }
        });

        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Insert Requested", "YES!.");
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
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

            Log.e("REQUEST BEFORE", picturePath);
//            Glide.with(this)
//                    .load(picturePath)
//                    .crossFade()
//                    .centerCrop()
//                    .into(userProfilePic);

            Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
            dp.setImageBitmap(bmImg);

            Log.e("REQUEST AFTER", picturePath);
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

//            Glide.with(this)
//                    .load(picturePath)
//                    .crossFade()
//                    .centerCrop()
//                    .into(userCoverPic);

            Bitmap bmImg = BitmapFactory.decodeFile(picturePath);
            cover.setImageBitmap(bmImg);
        }
    }

    @OnClick(R.id.name)
    public void setName(){
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

    @OnClick(R.id.bio)
    public void setBio(){
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

    @OnClick(R.id.specialization)
    public void setSpec(){
        new MaterialDialog.Builder(this)
                .title("Set Your Specialization")
                .content("Spiritual Skills You Specialize In.")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("Enter specialization here...", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        spec.setText("Specialization:" + input);
                    }
                }).show();
    }

    @OnClick(R.id.follow)
    public void setFollowing(){
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
