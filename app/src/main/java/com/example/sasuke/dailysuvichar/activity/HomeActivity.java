package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.fragment.HomeFragment;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class HomeActivity extends SingleFragmentActivity {

    private static final int DELAY_TIME = 2000;
    private static long back_pressed;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return HomeFragment.newInstance();
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

    @Override
    public void onBackPressed() {
        if (back_pressed + DELAY_TIME > System.currentTimeMillis()) {
            back_pressed = 0;
            super.onBackPressed();
        } else {
            Toast.makeText(this, getResources().getString(R.string.click_back_again_to_exit), Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }

    }
}
