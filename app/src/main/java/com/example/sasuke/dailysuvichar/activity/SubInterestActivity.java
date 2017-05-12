package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.sasuke.dailysuvichar.fragment.SubInterestFragment;

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
}
