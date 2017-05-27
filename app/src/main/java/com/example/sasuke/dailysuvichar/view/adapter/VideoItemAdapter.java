package com.example.sasuke.dailysuvichar.view.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Video;
import com.example.sasuke.dailysuvichar.view.VideoViewHolder;
import com.like.LikeButton;
import com.like.OnLikeListener;

import me.drakeet.multitype.ItemViewBinder;

public class VideoItemAdapter extends ItemViewBinder<Video, VideoViewHolder> {

    private Activity activity;

    public VideoItemAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    protected VideoViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.cell_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final VideoViewHolder holder, @NonNull final Video item) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                holder.mVideoView.loadYoutubeVideo(activity, item.getVideoUrl());
                holder.caption.setText(item.getTitle());
//                holder.mBtnLike.setOnLikeListener(new OnLikeListener() {
//                    @Override
//                    public void liked(LikeButton likeButton) {
//                        likeButton.setLiked(true);
//                    }
//
//                    @Override
//                    public void unLiked(LikeButton likeButton) {
//                        likeButton.setLiked(false);
//                    }
//                });
            }
        });
    }
}
