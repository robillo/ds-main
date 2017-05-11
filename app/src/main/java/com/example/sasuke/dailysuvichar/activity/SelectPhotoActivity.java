package com.example.sasuke.dailysuvichar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.sasuke.dailysuvichar.R;

import butterknife.ButterKnife;

/**
 * Created by robinkamboj on 11/05/17.
 */

public class SelectPhotoActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        ButterKnife.bind(this);
    }
}
