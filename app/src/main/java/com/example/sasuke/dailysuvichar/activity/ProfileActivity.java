package com.example.sasuke.dailysuvichar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.sasuke.dailysuvichar.R;

import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }
}
