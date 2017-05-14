package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sasuke.dailysuvichar.R;
import com.like.LikeButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.webview)
    public YoutubeWebView mVideoView;
    @BindView(R.id.button_like)
    public LikeButton mBtnLike;
//    @BindView(R.id.tv_like)
//    TextView tv_like;


    public VideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}

