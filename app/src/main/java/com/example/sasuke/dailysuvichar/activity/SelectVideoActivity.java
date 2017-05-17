package com.example.sasuke.dailysuvichar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.view.RVTags;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by rishabhshukla on 14/05/17.
 */

public class SelectVideoActivity extends BaseActivity{


    @BindView(R.id.vidView)
    VideoView mVideoView;
    @BindView(R.id.diet)
    TextView diet;
    @BindView(R.id.yoga)
    TextView yoga;
    @BindView(R.id.health)
    TextView health;
    @BindView(R.id.religion)
    TextView religion;
    @BindView(R.id.motivation)
    TextView motivation;
    @BindView(R.id.ayurveda)
    TextView ayurveda;
    @BindView(R.id.astrology)
    TextView astrology;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.grid)
    GridLayout grid;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.etCaption)
    EditText etCaption;


    private ArrayList<String> interests, subInterests, data, mSelectedItems;
    private Context context;

    private static final String TAG = "PHOTO_POST";
    private static final int PICK_VIDEO_REQUEST = 250;
    private Uri filePath;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReferenceTag,mDatabaseReferenceUser;
    private StorageReference mStorageReference;
    ProgressDialog progressDialog;
    Long size;
    String bucket, encoding, lang;
    Uri downloadUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_video);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        showFileChooser();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("videos");
//        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();

        context = getApplicationContext();
        interests = new ArrayList<>();
        subInterests = new ArrayList<>();
        mSelectedItems = new ArrayList<>();

    }

    @OnClick(R.id.diet)
    public void diet() {
        setAptBG("diet", diet);
    }

    @OnClick(R.id.yoga)
    public void yoga() {
        setAptBG("yoga", yoga);
    }

    @OnClick(R.id.health)
    public void health() {
        setAptBG("health", health);
    }

    @OnClick(R.id.religion)
    public void religion() {
        setAptBG("religion", religion);
    }

    @OnClick(R.id.motivation)
    public void motivation() {
        setAptBG("motivation", motivation);
    }

    @OnClick(R.id.ayurveda)
    public void ayurveda() {
        setAptBG("ayurveda", ayurveda);
    }

    @OnClick(R.id.astrology)
    public void astrology() {
        setAptBG("astrology", astrology);
    }

    @OnClick(R.id.next)
    public void nextIsSubinterests() {
        if (interests.size() < 1) {
            Toast.makeText(context, "Please Choose The Interest Category", Toast.LENGTH_SHORT).show();
        } else {
            if (interests.contains("diet")) {
                String[] temp = getResources().getStringArray(R.array.diet_array);
                addToSubinterests(temp);
            }
            if (interests.contains("yoga")) {
                String[] temp = getResources().getStringArray(R.array.yoga_array);
                addToSubinterests(temp);
            }
            if (interests.contains("health")) {
                String[] temp = getResources().getStringArray(R.array.health_array);
                addToSubinterests(temp);
            }
            if (interests.contains("religion")) {
                String[] temp = getResources().getStringArray(R.array.religion_array);
                addToSubinterests(temp);
            }
            if (interests.contains("motivation")) {
                String[] temp = getResources().getStringArray(R.array.motivation_array);
                addToSubinterests(temp);
            }
            if (interests.contains("ayurveda")) {
                String[] temp = getResources().getStringArray(R.array.ayurveda_array);
                addToSubinterests(temp);
            }
            if (interests.contains("astrology")) {
                String[] temp = getResources().getStringArray(R.array.astrology_array);
                addToSubinterests(temp);
            }
            grid.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
//            data = fillWithData();
            recyclerView.setAdapter(new RVTags(context, subInterests, mSelectedItems));
//            submit.setVisibility(View.VISIBLE);
        }
    }

    private void addToSubinterests(String[] temp) {
        for (String s : temp) {
            subInterests.add(s);
        }
    }

    private void setAptBG(String temp, TextView view) {
        if (interests.contains(temp)) {
            interests.remove(temp);
            view.setBackgroundColor(getResources().getColor(R.color.white));
            view.setTextColor(getResources().getColor(R.color.black));
        } else {
            interests.add(temp);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            view.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.btnPostImage)
    public void postImage(){
        uploadToFirebase();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("video/mpeg")
                    .build();
            if (filePath == null) {
                Log.e("selected video path", "null");
                finish();
            } else {
                Log.v("selectedVideoPath", filePath.toString());
                if (filePath != null) {
//                    StorageReference videoGalleryRef = storageRefVideo.child(selectedVideoUri + "_video");
//                    sendFileFirebaseVideo(videoGalleryRef, selectedVideoUri, metadata);
                }
            }
        }
    }

    private void uploadToFirebase() {
        if (mSelectedItems.size() < 1) {
            Toast.makeText(context, "Please Select Atleast One Subcategory.", Toast.LENGTH_SHORT).show();
        } else {
            if (filePath != null) {
                if(etCaption.length()>=1) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();

                    mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("tags");
                    final String postID = mDatabaseReferenceTag.push().getKey();
                    StorageReference riversRef = mStorageReference.child(postID);
                    riversRef.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata().getSizeBytes());
                                    Log.d(TAG, "onSuccess: " + taskSnapshot.getDownloadUrl());

                                    size = taskSnapshot.getMetadata().getSizeBytes();
                                    lang = taskSnapshot.getMetadata().getContentLanguage();
                                    encoding = taskSnapshot.getMetadata().getContentEncoding();
                                    bucket = taskSnapshot.getMetadata().getBucket();
                                    downloadUrl = taskSnapshot.getDownloadUrl();

//                            progressDialog.dismiss();
                                    Toast.makeText(SelectVideoActivity.this, "File Uploaded ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
//                                progressDialog.dismiss();
                                    Toast.makeText(SelectVideoActivity.this, "Upload Failed. Please Try Again!", Toast.LENGTH_SHORT).show();
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


                    Photo photo = new Photo("Rishabh Shukla", size,
                            lang, encoding,
                            bucket, mFirebaseUser.getEmail(),
                            System.currentTimeMillis(), 0, 0, null, etCaption.getText().toString(),
                            mFirebaseUser.getUid(), mSelectedItems, downloadUrl);

                    for (String subInt : mSelectedItems) {
                        mDatabaseReferenceTag.child(subInt.toLowerCase()).child("video").child(postID).setValue(photo);
                    }

                    mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
                    mDatabaseReferenceUser.child("users").child(mFirebaseUser.getUid()).child("posts").child("photo").push().setValue(photo);

                    Toast.makeText(this, "Post successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(this, "Write a caption", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "uploadToFirebase: No file chosen!");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

}
