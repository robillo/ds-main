package com.example.sasuke.dailysuvichar.activity;

import android.support.v4.app.Fragment;

import com.example.sasuke.dailysuvichar.R;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class HomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return null;
    }

    @Override
    protected String setActionBarTitle() {
        return getResources().getString(R.string.home);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    protected int setActionBarColor() {
        return R.color.blue_text;
    }
}
