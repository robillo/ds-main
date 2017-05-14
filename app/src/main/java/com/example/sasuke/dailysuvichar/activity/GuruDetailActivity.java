package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.sasuke.dailysuvichar.R;

import butterknife.ButterKnife;

public class GuruDetailActivity extends BaseActivity{

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guru_detail);
        ButterKnife.bind(this);

        context = getApplicationContext();
    }
}
