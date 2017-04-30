package com.example.sasuke.dailysuvichar.fragment;

import com.example.sasuke.dailysuvichar.R;

/**
 * Created by Sasuke on 4/30/2017.
 */

public class HomeFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_feeds;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}
