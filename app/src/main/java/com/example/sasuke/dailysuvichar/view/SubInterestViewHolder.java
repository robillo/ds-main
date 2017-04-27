package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.example.sasuke.dailysuvichar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sasuke on 4/26/2017.
 */

public class SubInterestViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_sub_interest)
    public CheckedTextView mTvSubInterest;
    @BindView(R.id.ll_container)
    public LinearLayout mLlContainer;

    public SubInterestViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
