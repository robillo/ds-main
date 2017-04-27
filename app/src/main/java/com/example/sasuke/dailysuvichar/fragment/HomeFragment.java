package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.sasuke.dailysuvichar.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import berlin.volders.badger.BadgeShape;
import berlin.volders.badger.Badger;
import berlin.volders.badger.CountBadge;
import butterknife.BindView;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class HomeFragment extends BaseFragment {


    @BindView(R.id.tab_bar)
    SmartTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.iv_friend_requests)
    ImageView mIvFriendrequest;
    @BindView(R.id.iv_messages)
    ImageView mIvMessages;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewPager();
        mTabLayout.setViewPager(mViewPager);

        CountBadge.Factory circleFactory = new CountBadge.Factory(BadgeShape.circle(.5f, Gravity.END | Gravity.TOP), 0xffff0000, 0xffffffff);
        Badger.sett(mIvFriendrequest, circleFactory).setCount(10);
        Badger.sett(mIvMessages, circleFactory).setCount(4);
    }

    public void setupViewPager() {
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(), FragmentPagerItems.with(getContext())
                .add(R.string.home, FeedsFragment.class)
                .add(R.string.trending, TrendingFragment.class)
                .add(R.string.gurus, GurusFragment.class)
                .create());
        mViewPager.setAdapter(adapter);
    }

}
