package com.example.sasuke.dailysuvichar.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.view.PhotoViewHolder;

import me.drakeet.multitype.ItemViewBinder;

public class PhotoItemAdapter extends ItemViewBinder<Photo, PhotoViewHolder> {

    private Context context;

    @NonNull
    @Override
    protected PhotoViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.cell_photo, parent, false);
        context = parent.getContext();
        return new PhotoViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PhotoViewHolder holder, @NonNull final Photo item) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                holder.setImage(item.getPhoto());
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.invisible.setVisibility(View.VISIBLE);
            }
        });
    }
}
