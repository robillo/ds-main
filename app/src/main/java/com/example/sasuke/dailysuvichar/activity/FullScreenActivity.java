package com.example.sasuke.dailysuvichar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.newactivities.NewExploreyActivity;
import com.example.sasuke.dailysuvichar.newactivities.NewHomeyActivity;
import com.example.sasuke.dailysuvichar.newactivities.YourFeedsActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.IndeterminateCircularProgressDrawable;

public class FullScreenActivity extends BaseActivity{

    private int from = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        ButterKnife.bind(this);

        //0 YOUR    1 HOME      2 EXPLORE

        from = getIntent().getIntExtra("from", 0);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = getIntent().getStringExtra("path");
        StorageReference gsReference = storage.getReferenceFromUrl(path);

        Glide.with(getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .placeholder(new IndeterminateCircularProgressDrawable(getApplicationContext()))
                .into((ImageView) findViewById(R.id.imageView));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from == 0){
            startActivity(new Intent(this, YourFeedsActivity.class));
        }
        else if(from == 1){
            startActivity(new Intent(this, NewHomeyActivity.class));
        }
        else if(from == 2){
            startActivity(new Intent(this, NewExploreyActivity.class));
        }
    }
}
