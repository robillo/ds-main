package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.fragment.HomeFragment;
import com.example.sasuke.dailysuvichar.view.TabBar;

import java.util.List;

import berlin.volders.badger.BadgeDrawable;
import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class HomeActivity extends BaseActivity implements TabBar.OnItemSelect {

    @BindView(R.id.tab_bar)
    TabBar mTabBar;
    @BindView(R.id.iv_friend_requests)
    ImageView mIvFriendrequest;
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
        Badger.sett(mIvFriendrequest, circleFactory).setCount(10);
        Badger.sett(mIvMessages, circleFactory).setCount(4);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabBar.setOnItemSelectListner(HomeActivity.this);
            }
        }, 250);
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

    @Override
    public void onItemSelect(int tabId) {

    }
}
