package com.example.sasuke.dailysuvichar.activity;

import android.content.Intent;
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
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.IndeterminateCircularProgressDrawable;

public class FullScreenActivity extends BaseActivity{

    private int from = 0;
    private String path = null;
    private StorageReference gsReference = null;

    @BindView(R.id.download)
    TextView download;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);

        //0 YOUR    1 HOME      2 EXPLORE

        from = getIntent().getIntExtra("from", 0);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = getIntent().getStringExtra("path");
        gsReference = storage.getReferenceFromUrl(path);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .placeholder(new IndeterminateCircularProgressDrawable(getApplicationContext()))
                .into((ImageView) findViewById(R.id.imageView));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.download)
    public void setDownload(){

        Toast.makeText(getApplicationContext(), "Downloading image..", Toast.LENGTH_SHORT).show();

        File rootPath = new File(Environment.getExternalStorageDirectory(), "image");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"imageName.jpg");

        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
//                getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
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
}
