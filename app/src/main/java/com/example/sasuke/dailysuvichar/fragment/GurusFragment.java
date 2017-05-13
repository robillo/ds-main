package com.example.sasuke.dailysuvichar.fragment;

import com.example.sasuke.dailysuvichar.R;

public class GurusFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_feeds;
    }

    public static GurusFragment newInstance() {
        return new GurusFragment();
    }
}
