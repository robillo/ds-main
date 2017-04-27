package com.example.sasuke.dailysuvichar.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.facebook.AccessToken;

/**0
 * Created by Sasuke on 4/27/2017.
 */

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPrefs.getLoginToken() != null) {
            startActivity(HomeActivity.newIntent(this));
        } else {
            if (AccessToken.getCurrentAccessToken() != null) {
                startActivity(HomeActivity.newIntent(this));
            } else {
                startActivity(MainActivity.newIntent(this));
            }
        }
    }
}