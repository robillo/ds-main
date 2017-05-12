package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.fragment.MainFragment;
import com.example.sasuke.dailysuvichar.fragment.NotificationFragment;
import com.example.sasuke.dailysuvichar.fragment.ProfileFragment;
import com.example.sasuke.dailysuvichar.fragment.SettingsFragment;
import com.example.sasuke.dailysuvichar.fragment.YourFeedsFragment;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.List;

import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.iv_choose_language)
    ImageView mIvChooseLanguage;
    @BindView(R.id.iv_messages)
    ImageView mIvMessages;

    private static final int DELAY_TIME = 2000;
    private static long back_pressed;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        CountBadge.Factory circleFactory = new CountBadge.Factory(BadgeShape.circle(.5f, Gravity.END | Gravity.TOP), 0xffff0000, 0xffffffff);
        Badger.sett(mIvMessages, circleFactory).setCount(8);

        replaceFragment(MainFragment.newInstance(), MainFragment.class.getName());
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (back_pressed + DELAY_TIME > System.currentTimeMillis()) {
            back_pressed = 0;
            super.onBackPressed();
        } else {
            Toast.makeText(this, getResources().getString(R.string.click_back_again_to_exit), Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }

    }

    @OnClick(R.id.nav_home)
    public void onMyFeedClick() {
        closeDrawer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceFragment(MainFragment.newInstance(), MainFragment.class.getName());
            }
        }, 250);
    }

    @OnClick(R.id.nav_updates)
    public void onMyTripsClick() {
        closeDrawer();
        replaceFragment(NotificationFragment.newInstance(), NotificationFragment.class.getName());
    }

    @OnClick(R.id.nav_profile)
    public void onProfileClick() {
        closeDrawer();
        replaceFragment(ProfileFragment.newInstance(), ProfileFragment.class.getName());
    }

    @OnClick(R.id.your_feeds)
    public void onYourFeedsClick() {
        closeDrawer();
        replaceFragment(YourFeedsFragment.newInstance(), YourFeedsFragment.class.getName());
    }

    @OnClick(R.id.nav_settings)
    public void onSettingsClick() {
        closeDrawer();
        replaceFragment(SettingsFragment.newInstance(), SettingsFragment.class.getName());
    }

    @OnClick(R.id.nav_log_out)
    public void onLogOutClick() {
        closeDrawer();
        AccessToken facebookAccessToken = AccessToken.getCurrentAccessToken();
        if (facebookAccessToken != null) {
            facebookLogout();
        } else {
            removeUser();
        }
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    public void removeUser() {
        SharedPrefs.clearLoggedInUser();
        Intent intent = LoginActivity.newIntent(this);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void facebookLogout() {
        LoginManager.getInstance().logOut();
        removeUser();
    }

    private void replaceFragment(final Fragment fragment, final String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f != null) {
                    transaction.hide(f);
                }
            }
        }
        Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            f = fragment;
            transaction.add(R.id.fragment_container, f, tag);
        } else {
            transaction.show(f);
        }
        transaction.commit();
    }

    @OnClick(R.id.iv_notification)
    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

}
