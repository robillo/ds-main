package com.example.sasuke.dailysuvichar.fragment;

import com.example.sasuke.dailysuvichar.R;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class HomeFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}
