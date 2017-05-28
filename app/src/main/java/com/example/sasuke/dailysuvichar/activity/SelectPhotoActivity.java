package com.example.sasuke.dailysuvichar.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.newactivities.NewHomeyActivity;
import com.example.sasuke.dailysuvichar.newactivities.NewExploreyActivity;
import com.example.sasuke.dailysuvichar.view.RVTags;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPhotoActivity extends BaseActivity{

    @BindView(R.id.upload)
    ImageView upload;
    @BindView(R.id.name)
    TextView name;
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
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReferenceTag,mDatabaseReferenceUser;
    private StorageReference mStorageReference;
    ProgressDialog progressDialog;
    Long size;
    String bucket, encoding, lang;
    Uri downloadUrl;

    private int from = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getName();

        from = getIntent().getIntExtra("from", 1);

        showFileChooser();


        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("posts").child("images");
//        mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();


        context = getApplicationContext();
        interests = new ArrayList<>();
        subInterests = new ArrayList<>();
        mSelectedItems = new ArrayList<>();

    }

    @OnClick(R.id.diet)
    public void diet() {
        setAptBG(getString(R.string.diett), diet);
    }

    @OnClick(R.id.yoga)
    public void yoga() {
        setAptBG(getString(R.string.yogaa), yoga);
    }

    @OnClick(R.id.health)
    public void health() {
        setAptBG(getString(R.string.healthh), health);
    }

    @OnClick(R.id.religion)
    public void religion() {
        setAptBG(getString(R.string.religionn), religion);
    }

    @OnClick(R.id.motivation)
    public void motivation() {
        setAptBG(getString(R.string.motivationn), motivation);
    }

    @OnClick(R.id.ayurveda)
    public void ayurveda() {
        setAptBG(getString(R.string.ayurvedaa), ayurveda);
    }

    @OnClick(R.id.astrology)
    public void astrology() {
        setAptBG(getString(R.string.astrologyy), astrology);
    }

    @OnClick(R.id.next)
    public void nextIsSubinterests() {
        if (interests.size() < 1) {
            Toast.makeText(context, R.string.categ_inter, Toast.LENGTH_SHORT).show();
        } else {
            if (interests.contains(getString(R.string.diett))) {
                String[] temp = getResources().getStringArray(R.array.diet_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.yogaa))) {
                String[] temp = getResources().getStringArray(R.array.yoga_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.healthh))) {
                String[] temp = getResources().getStringArray(R.array.health_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.religionn))) {
                String[] temp = getResources().getStringArray(R.array.religion_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.motivationn))) {
                String[] temp = getResources().getStringArray(R.array.motivation_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.ayurvedaa))) {
                String[] temp = getResources().getStringArray(R.array.ayurveda_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.astrologyy))) {
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
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectt)), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                upload.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(context, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    private void uploadToFirebase() {
        if (mSelectedItems.size() < 1) {
            Toast.makeText(context, R.string.please__, Toast.LENGTH_SHORT).show();
        } else {
            if (filePath != null) {
                if(etCaption.length()>=1) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle(getString(R.string.uploadinggg));
                    progressDialog.show();

                    mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("tags");
                    final String postID = mDatabaseReferenceTag.push().getKey();
                    getName();
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
                                    Toast.makeText(SelectPhotoActivity.this, getString(R.string.uploaded), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
//                                progressDialog.dismiss();
                                    Toast.makeText(SelectPhotoActivity.this, getString(R.string.upload_failed), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: " + exception.getMessage());

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                                }
                            });


                    Photo photo=null;
                    if(name.getText()!="") {
                        photo = new Photo(name.getText().toString(), size,
                                lang, encoding,
                                bucket, mFirebaseUser.getEmail(),
                                System.currentTimeMillis(), 0, 0, null, etCaption.getText().toString(),
                                mFirebaseUser.getUid(), mSelectedItems, downloadUrl);
                    }else{
                        photo = new Photo(getString(R.string.unknown), size,
                                lang, encoding,
                                bucket, mFirebaseUser.getEmail(),
                                System.currentTimeMillis(), 0, 0, null, etCaption.getText().toString(),
                                mFirebaseUser.getUid(), mSelectedItems, downloadUrl);
                    }

                    for (String subInt : mSelectedItems) {
                        mDatabaseReferenceTag.child(subInt.toLowerCase()).child("photo").child(postID).setValue(photo);
                    }

                    mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
                    mDatabaseReferenceUser.child("users").child(mFirebaseUser.getUid()).child("posts").child("photo").child(postID).setValue(photo);

                    Toast.makeText(this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    if(from == 1){
                        startActivity(new Intent(this, NewExploreyActivity.class));
                    }
                    else if(from == 2){
                        startActivity(new Intent(this, NewHomeyActivity.class));
                    }
                    finish();
                }else{
                    Toast.makeText(this, getString(R.string.caption), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "uploadToFirebase: No file chosen!");
            }
        }
    }


    public void getName(){

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").getValue()!=null){
                    Log.d(TAG, "onDataChange: NAMEE"+(dataSnapshot.child("name").getValue()));
                    name.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        return null;
    }

    @Override
    protected void onDestroy() {
        if(progressDialog!=null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from == 1){
            startActivity(new Intent(this, NewExploreyActivity.class));
        }
        else if(from == 2){
            startActivity(new Intent(this, NewHomeyActivity.class));
        }
    }
}
