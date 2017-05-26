package com.example.sasuke.dailysuvichar.activity;

import android.app.Activity;
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
        if (SharedPrefs.getLoginToken() != null) {

            Log.e("CASE", "LOGIN TOKEN NOT NULL");

            if(SharedPrefs.getIsProfileSet().equals("TRUE") && SharedPrefs.getIsSubinterestsSelected().equals("TRUE")){
                startActivity(NewMainActivity.newIntent(this));
            }
            else if(SharedPrefs.getIsProfileSet().equals("TRUE") && SharedPrefs.getIsSubinterestsSelected().equals("FALSE")){
                startActivity(ChooseInterestActivity.newIntent(this));
            }
            else if(!SharedPrefs.getIsProfileSet().equals("TRUE")){
                startActivity(ProfileActivity.newIntent(this));
            }
            else {
                startActivity(NewMainActivity.newIntent(this));
            }
        }
        else {

            Log.e("CASE", "LOGIN TOKEN NULL");

            if (AccessToken.getCurrentAccessToken() != null) {
                if(SharedPrefs.getIsProfileSet().equals("TRUE") && SharedPrefs.getIsSubinterestsSelected().equals("TRUE")){
                    Log.e("CASE", "LOGIN TOKEN NULL PROFILE AND SUBINT SET");
                    startActivity(NewMainActivity.newIntent(this));
                }
                else if(SharedPrefs.getIsProfileSet().equals("TRUE") && SharedPrefs.getIsSubinterestsSelected().equals("FALSE")){

                    Log.e("CASE", "LOGIN TOKEN NULL PROFILE SCREEN SET SUNINT NOT SET");
                    startActivity(ChooseInterestActivity.newIntent(this));
                }
                else if(!SharedPrefs.getIsProfileSet().equals("TRUE")){
                    Log.e("CASE", "LOGIN TOKEN NULL PROFILE NOT SET");

                    startActivity(ProfileActivity.newIntent(this));
                }
                else {
                    Log.e("CASE", "LOGIN TOKEN NULL NOTHING SET");
                    startActivity(NewMainActivity.newIntent(this));
                }
            } else {
                Log.e("CASE", "VERY START");
                startActivity(MainActivity.newIntent(this));
            }
        }
    }
}