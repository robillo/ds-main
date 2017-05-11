package com.example.sasuke.dailysuvichar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by robinkamboj on 11/05/17.
 */

public class SelectPhotoActivity extends BaseActivity{

    @BindView(R.id.imgView)
    private ImageView mImageView;

    private static final String TAG = "PHOTO_POST";
    private static final int PICK_IMAGE_REQUEST = 250;
    private Uri filePath;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        ButterKnife.bind(this);
        showFileChooser();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @OnClick(R.id.btnPostImage)
    public void postImage(){
        uploadToFirebase();
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                mImageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadToFirebase(){
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

//            mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("tags");
//            String postID = mDatabaseReferenceTag.push().getKey();
            StorageReference riversRef = mStorageReference.child("posts/images/"+mFirebaseUser.getUid());
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
//                            taskSnapshot.getMetadata().
                            Toast.makeText(SelectPhotoActivity.this, "File Uploaded ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(SelectPhotoActivity.this, "Upload Failed. Please Try Again!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: "+exception.getMessage());

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
            Toast.makeText(this,"Post successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,HomeActivity.class));
        }
        else {
            Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "uploadToFirebase: No file chosen!");
        }
    }
}
