package com.example.sasuke.dailysuvichar.view.adapter;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.view.PhotoViewHolder;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class PhotoItemAdapter extends ItemViewBinder<Photo, PhotoViewHolder> {

    @NonNull
    @Override
    protected PhotoViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.cell_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PhotoViewHolder holder, @NonNull final Photo item) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                holder.setImage(item.getPhotoURL());
            }
        });
    }
}
