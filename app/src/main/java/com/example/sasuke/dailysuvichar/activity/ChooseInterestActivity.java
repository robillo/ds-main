package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.sasuke.dailysuvichar.fragment.ChooseInterestFragment;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;

public class ChooseInterestActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return ChooseInterestFragment.newInstance();
    }

    @Override
    protected String setActionBarTitle() {
        return null;
    }

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected int setActionBarColor() {
        return 0;
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ChooseInterestActivity.class);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPrefs.getIsInterestsSelected()!=null){
            if(SharedPrefs.getIsInterestsSelected().equals("TRUE")){
                startActivity(new Intent(this, SubInterestActivity.class));
            }
        }
    }
}
