package com.example.sasuke.dailysuvichar.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.CustomVideo;
import com.example.sasuke.dailysuvichar.view.CustomVideoVH;
import com.klinker.android.simple_videoview.SimpleVideoView;

import java.util.ArrayList;

import me.drakeet.multitype.ItemViewBinder;

public class CustomVideoAdapter  extends ItemViewBinder<CustomVideo, CustomVideoVH> {

    private Context context, pContext;
    ArrayList<CustomVideo> videos;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    protected SimpleVideoView currentlyPlaying;

    public void releaseVideo() {
        if (currentlyPlaying != null) {
            currentlyPlaying.release();
            currentlyPlaying.setVisibility(View.GONE);
        }
    }
//    @NonNull
//    @Override
//    protected CustomVideoVH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
//        View view = inflater.inflate(R.layout.row_video, parent, false);
//        context = parent.getContext();
//        pContext = parent.getContext();
//        return new CustomVideoVH(view);
//
//    }
//
//    @Override
//    protected void onBindViewHolder(@NonNull final CustomVideoVH holder, @NonNull final CustomVideo item) {
//
//
//
//    }
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

//    @Override
    public CustomVideoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        pContext = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_video, parent, false);
        return new CustomVideoVH(view);
    }

//    @Override
    public void onBindViewHolder(final CustomVideoVH holder, int position) {

        final CustomVideo item = videos.get(position);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if(item.getTimestamp()!=null) {
                    holder.setPostTime(getTimeAgo(Math.abs(item.getTimestamp())));
                }
                holder.setName(item.getName());
                if(item.getCaption()!=null){
                    holder.setCaption(item.getCaption());
                }
                if(item.getStorageReference()!=null && context!=null) {
                    holder.setVideo(item.getStorageReference());
                }
//                holder.setImageView();
            }
        });

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.videoView != currentlyPlaying) {
                    releaseVideo();
//                    holder.imageView.setVisibility(View.INVISIBLE);
                    holder.videoView.setVisibility(View.VISIBLE);
//                    if(holder.videoUrl!=null) {
//                        holder.videoView.start(holder.videoUrl.toString() + ".mp4");
//                    }
                    Log.e("URI", " " + item.getVideoURI());
                    if(item.getVideoURI()!=null){
                        holder.videoView.start(Uri.parse(item.getVideoURI()));
                        currentlyPlaying = holder.videoView;
                    }
                    else {
                        Toast.makeText(context, "Sorry. This Video Cannot Be Played", Toast.LENGTH_SHORT).show();
                    }
                }else{
//                    holder.imageView.setVisibility(View.INVISIBLE);
//                    holder.videoView.setVisibility(View.VISIBLE);
//                    holder.videoView.start(Uri.parse(item.getVideoURI()));
                }
            }
        });
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.videoView.isPlaying())
                    holder.videoView.pause();
                else
                    holder.videoView.play();
            }
        });
        if(item.getUid()!=null) {
            holder.setStatusDP(item.getUid());
        }

//        holder.videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (holder.videoView.isPlaying()) {
//                    holder.videoView.pause();
//                } else {
//                    if (holder.videoView != currentlyPlaying) {
//                        releaseVideo();
//                        holder.videoView.start(Uri.parse(String.valueOf(item.getVideoURI())));
//                        currentlyPlaying = holder.videoView;
//                    }else {
//                        holder.videoView.setVisibility(View.VISIBLE);
////                    if(holder.videoUrl!=null) {
////                        holder.videoView.start(holder.videoUrl.toString() + ".mp4");
////                    }
//                        holder.videoView.play();
//
////                            } else {
////                                Toast.makeText(context, "Sorry. This Video Cannot Be Played", Toast.LENGTH_SHORT).show();
////                            }
//                    }
//                }
//            }
//        });
    }

//    @Override
//    public int getItemCount() {
//        if(videos!=null) {
//            return videos.size();
//        }else{
//            return 0;
//        }
//    }

    public void setItems(ArrayList<CustomVideo> list) {
        this.videos = list;
//        notifyDataSetChanged();
    }

    @NonNull
    @Override
    protected CustomVideoVH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        context = parent.getContext();
        pContext = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_video, parent, false);
        return new CustomVideoVH(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CustomVideoVH holder, @NonNull final CustomVideo item) {

//        final CustomVideo item = videos.get(position);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                if(item.getTimestamp()!=null) {
                    holder.setPostTime(getTimeAgo(Math.abs(item.getTimestamp())));
                }
                holder.setName(item.getName());
                if(item.getCaption()!=null){
                    holder.setCaption(item.getCaption());
                }
                if(item.getStorageReference()!=null && context!=null) {
                    holder.setVideo(item.getStorageReference());
                }
//                holder.setImageView();
            }
        });

        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.videoView != currentlyPlaying) {
                    releaseVideo();
//                    holder.imageView.setVisibility(View.INVISIBLE);
                    holder.videoView.setVisibility(View.VISIBLE);
//                    if(holder.videoUrl!=null) {
//                        holder.videoView.start(holder.videoUrl.toString() + ".mp4");
//                    }
                    Log.e("URI", " " + item.getVideoURI());
                    if(item.getVideoURI()!=null){
                        holder.videoView.start(Uri.parse(item.getVideoURI()));
                        currentlyPlaying = holder.videoView;
                    }
                    else {
                        Toast.makeText(context, "Sorry. This Video Cannot Be Played", Toast.LENGTH_SHORT).show();
                    }
                }else{
//                    holder.imageView.setVisibility(View.INVISIBLE);
//                    holder.videoView.setVisibility(View.VISIBLE);
//                    holder.videoView.start(Uri.parse(item.getVideoURI()));
                }
            }
        });
        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.videoView.isPlaying())
                    holder.videoView.pause();
                else
                    holder.videoView.play();
            }
        });
        if(item.getUid()!=null) {
            holder.setStatusDP(item.getUid());
        }
    }
}
