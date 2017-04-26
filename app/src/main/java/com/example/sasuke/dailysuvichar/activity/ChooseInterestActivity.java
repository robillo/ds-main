package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.sasuke.dailysuvichar.fragment.ChooseInterestFragment;

/**
 * Created by Sasuke on 4/19/2017.
 */

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
}
