package com.robillo.sasuke.dailysuvichar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.robillo.sasuke.dailysuvichar.newactivities.NewMainActivity;
import com.robillo.sasuke.dailysuvichar.utils.SharedPrefs;
import com.facebook.AccessToken;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPrefs.setDefaults();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        if (SharedPrefs.getLoginToken() != null) {
            if (AccessToken.getCurrentAccessToken() != null) {
                Log.e("CASE", "LOGIN TOKEN NULL, ACCESS TOKEN IS: " + AccessToken.getCurrentAccessToken());
                startActivity(NewMainActivity.newIntent(this));
            } else {
                Log.e("CASE", "LOGIN TOKEN NOT NULL AND ACCESS TOKEN NULL");
                startActivity(MainActivity.newIntent(this));
            }
        }
        else {
            Log.e("CASE", "LOGIN TOKEN AND THEREFORE, ACCESS TOKEN NULL");
            startActivity(MainActivity.newIntent(this));
        }
    }
}