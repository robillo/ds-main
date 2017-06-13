package com.robillo.sasuke.dailysuvichar.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.models.Photo;
import com.robillo.sasuke.dailysuvichar.view.PhotoViewHolder;

import me.drakeet.multitype.ItemViewBinder;

public class PhotoItemAdapter extends ItemViewBinder<Photo, PhotoViewHolder> {

    private Context context, pContext;


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    @NonNull
    @Override
    protected PhotoViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.cell_photo, parent, false);
        context = parent.getContext();
        pContext = parent.getContext();

        return new PhotoViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final PhotoViewHolder holder, @NonNull final Photo item) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(item.getTimestamp()!=null) {
                    holder.setPostTime(getTimeAgo(Math.abs(item.getTimestamp())));
                }
                holder.setName(item.getName());
//                if(item.getLikedUsers()==null) {
//                    holder.setLikes(0);
//                }else{
//                    holder.setLikes(item.getLikedUsers().size());
//                }
                if(item.getComments()!=null) {
                    holder.setComments(item.getComments().size());
                }else{
                    holder.setComments(0);
                }
                if(item.getCaption()!=null){
                    holder.setCaption(item.getCaption());
                }
                if(item.getStorageReference()!=null && context!=null) {
                    holder.setImage(item.getStorageReference(), context);
                }
                if(item.getUid()!=null) {
                    holder.setStatusDP(item.getUid());
                }
            }
        });

//        if (holder.containsLikedUser(item.getLikedUsers())) {
//            holder.likeButton.setLiked(true);
//        }
//        holder.likeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(holder.likeButton.isLiked()){
//                    holder.likeButton.setLiked(false);
//                    Log.d(TAG, "onClick: UNLIKE");
//
//                    if(item.getPostUid()!=null) {
//
//                        holder.setLikedUser(item.getUid(),item.getPostUid(), false, item.getLikedUsers());
//                    }
//
//                    // DECREASE HOLDER.COUNT BY ONE IN ADAPTER
//                    // DECREASE HOLDER COUNT IN FIREBASE FOR THIS POST
//                    // REMOVE UID OF THIS USER FROM THIS POST
//                }
//                else {
//                    holder.likeButton.setLiked(true);
//                    Log.d(TAG, "onClick: LIKE");
//
//                    if(item.getPostUid()!=null) {
//
//                        holder.setLikedUser(item.getUid(),item.getPostUid(), true, item.getLikedUsers());
//                    }
//                    // INCREASE HOLDER.COUNT BY ONE IN ADAPTER
//                    // INCREASE HOLDER COUNT IN FIREBASE FOR THIS POST
//                    // ADD UID OF THIS USER FROM THIS POST
//                }
//            }
//        });

        holder.mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.fullScreenIntent();
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
