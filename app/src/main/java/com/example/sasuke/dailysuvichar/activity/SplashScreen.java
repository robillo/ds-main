package com.example.sasuke.dailysuvichar.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Sasuke on 4/27/2017.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(MainActivity.newIntent(this));
    }
}