package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.ButterKnife;

public class FullScreenActivity extends BaseActivity{

    private Bitmap bitmap;
    private BitmapDrawable drawable;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);
//
//        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = getIntent().getStringExtra("path");
        StorageReference gsReference = storage.getReferenceFromUrl(path);
//
//        BigImageView bigImageView = (BigImageView) findViewById(R.id.mBigImage);
//        bigImageView.showImage(Uri.parse(path));

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .into((ImageView) findViewById(R.id.imageView));
    }


}
