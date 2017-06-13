package com.robillo.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.robillo.sasuke.dailysuvichar.fragment.SubInterestFragment;
import com.robillo.sasuke.dailysuvichar.newactivities.NewMainActivity;
import com.robillo.sasuke.dailysuvichar.utils.SharedPrefs;

public class SubInterestActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return SubInterestFragment.newInstance();
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
        return new Intent(context, SubInterestActivity.class);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SharedPrefs.getIsSubinterestsSelected()!=null){
            if(SharedPrefs.getIsSubinterestsSelected().equals("TRUE")){
                startActivity(new Intent(this, NewMainActivity.class));
            }
        }
    }
}
