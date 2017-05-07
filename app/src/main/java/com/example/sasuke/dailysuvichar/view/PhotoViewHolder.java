package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.sasuke.dailysuvichar.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo)
    ImageView mIvPhoto;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setImage(int photo) {
        Picasso.with(itemView.getContext()).load(photo).fit().into(mIvPhoto);
    }
}
