package com.example.sasuke.dailysuvichar.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sasuke.dailysuvichar.R;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;

import butterknife.ButterKnife;

public class FullScreenActivity extends BaseActivity{

    private Bitmap bitmap;
    private BitmapDrawable drawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);

        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));

//        bitmap = getIntent().getParcelableExtra("imageviewBitmap");
//        drawable = new BitmapDrawable(getResources(), bitmap);
//
//        BigImageView bigImageView = (BigImageView) findViewById(R.id.mBigImage);
        Glide.with(getApplicationContext())
                .load(bitmap)
                .into((ImageView) findViewById(R.id.imageView));
    }
}
