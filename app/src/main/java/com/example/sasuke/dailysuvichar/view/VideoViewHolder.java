package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;
import com.like.LikeButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.webview)
    public YoutubeWebView mVideoView;
    @BindView(R.id.text)
    public TextView caption;
//    @BindView(R.id.button_like)
//    public LikeButton mBtnLike;
//    @BindView(R.id.tv_like)
//    TextView tv_like;


    public VideoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

