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

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


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
                if(item.getTimestamp()!=null) {
                    holder.setPostTime(getTimeAgo(item.getTimestamp()));
                }
                holder.setName(item.getName());
                if(item.getLikes()!=null) {
                    holder.setLikes(item.getLikes());
                }
                if(item.getComments()!=null) {
                    holder.setComments(item.getComments().size());
                }
                if(item.getCaption()!=null){
                    holder.setCaption(item.getCaption());
                }
                if(item.getStorageReference()!=null && context!=null) {
                    holder.setImage(item.getStorageReference(), context);
                }
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.invisible.setVisibility(View.VISIBLE);
            }
        });
    }

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }
}
