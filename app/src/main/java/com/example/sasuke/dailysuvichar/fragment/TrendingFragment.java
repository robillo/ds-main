package com.example.sasuke.dailysuvichar.fragment;

import com.example.sasuke.dailysuvichar.R;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class TrendingFragment extends BaseFragment {

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_gurus;
    }

    public static TrendingFragment newInstance() {
        return new TrendingFragment();
    }
}
