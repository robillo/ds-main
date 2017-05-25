package com.example.sasuke.dailysuvichar.fragment;

import com.example.sasuke.dailysuvichar.R;

public class NotificationFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_gurus;
    }

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }
}
