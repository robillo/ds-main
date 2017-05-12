package com.example.sasuke.dailysuvichar.fragment;

import com.example.sasuke.dailysuvichar.R;

public class ProfileFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_feeds;
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }
}
