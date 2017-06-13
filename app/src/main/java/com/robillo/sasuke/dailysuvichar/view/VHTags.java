package com.robillo.sasuke.dailysuvichar.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.robillo.sasuke.dailysuvichar.R;

public class VHTags extends RecyclerView.ViewHolder{

    public TextView tag;

    public VHTags(View itemView) {
        super(itemView);
        tag = (TextView) itemView.findViewById(R.id.tag);
    }
}
