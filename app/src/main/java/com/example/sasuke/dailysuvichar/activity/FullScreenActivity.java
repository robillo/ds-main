package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.newactivities.NewExploreyActivity;
import com.example.sasuke.dailysuvichar.newactivities.NewHomeyActivity;
import com.example.sasuke.dailysuvichar.newactivities.YourFeedsActivity;
import com.example.sasuke.dailysuvichar.utils.BasicImageDownloader;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.IndeterminateCircularProgressDrawable;

public class FullScreenActivity extends BaseActivity{

    private int from = 0;
    private String path = null;
    private StorageReference gsReference = null;
    private ImageView mImageView;
    private String uri;
    private String internalStoragePath;

    @BindView(R.id.download)
    TextView download;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);

        //0 YOUR    1 HOME      2 EXPLORE

        mImageView =(ImageView) findViewById(R.id.imageView);

        from = getIntent().getIntExtra("from", 0);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = getIntent().getStringExtra("path");
        gsReference = storage.getReferenceFromUrl(path);

        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .into(mImageView);
                FullScreenActivity.this.uri = uri.toString();
                Log.e("URI IS", uri.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.download)
    public void setDownload(){

        Toast.makeText(getApplicationContext(), "Downloading image..", Toast.LENGTH_SHORT).show();

//        final long ONE_MEGABYTE = 1024 * 1024;
//        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                mImageView.setImageBitmap(bitmap);
//                scanGallery(getApplicationContext(), uri);
//                Toast.makeText(getApplicationContext(), "FILE DOWNLOADED", Toast.LENGTH_SHORT).show();
//            }
//        });

//        try {
//            final File localFile = File.createTempFile("images", "jpg");
//            gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    scanGallery(getApplicationContext(), localFile.toString());
//                    Bitmap bitmap = BitmapFactory.decodeFile(internalStoragePath);
//                    mImageView.setImageBitmap(bitmap);
//                    Toast.makeText(getApplicationContext(), "FILE DOWNLOADED", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                }
//            });
//        } catch (IOException e ) {}
//
//        Toast.makeText(getApplicationContext(), "Downloading image..", Toast.LENGTH_SHORT).show();
//
        File rootPath = new File(Environment.getExternalStorageDirectory(), "image");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath, generateRandomName() + ".jpg");

        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                scanGallery(getApplicationContext(), localFile.toString());
                Toast.makeText(getApplicationContext(), "Image Download Successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());

                Toast.makeText(getApplicationContext(), "Network error.. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void scanGallery(final Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    //unimplemeted method
                    Log.e("PATH IS", path);
                    internalStoragePath = path;
                    Toast.makeText(getApplicationContext(), "PATH IS" + internalStoragePath, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateRandomName(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
