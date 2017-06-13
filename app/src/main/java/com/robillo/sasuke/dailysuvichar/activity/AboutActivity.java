package com.robillo.sasuke.dailysuvichar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.robillo.sasuke.dailysuvichar.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
