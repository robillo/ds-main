package com.example.sasuke.dailysuvichar.view.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Video;
import com.example.sasuke.dailysuvichar.view.VideoViewHolder;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Sasuke on 5/7/2017.
 */

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
    protected void onBindViewHolder(@NonNull final VideoViewHolder holder, @NonNull Video item) {
        holder.mVideoView.loadYoutubeVideo(activity, item.getVideoUrl());
    }
}
