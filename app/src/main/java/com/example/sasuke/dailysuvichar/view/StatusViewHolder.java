package com.example.sasuke.dailysuvichar.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.example.sasuke.dailysuvichar.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class StatusViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.status)
    TextView mTvStatus;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void setStatus(String status) {
        if (status != null && status.length() > 0)
            mTvStatus.setText(status);
    }

}
