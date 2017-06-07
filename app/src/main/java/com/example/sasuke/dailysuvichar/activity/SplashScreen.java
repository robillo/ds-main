package com.example.sasuke.dailysuvichar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.sasuke.dailysuvichar.newactivities.NewMainActivity;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.facebook.AccessToken;

/**0
 * Created by Sasuke on 4/27/2017.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPrefs.setDefaults();

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