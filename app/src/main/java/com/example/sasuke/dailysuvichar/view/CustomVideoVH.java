package com.example.sasuke.dailysuvichar.view;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;
import com.google.firebase.storage.StorageReference;
import com.klinker.android.simple_videoview.SimpleVideoView;
import com.like.LikeButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rishabhshukla on 15/05/17.
 */


public class CustomVideoVH extends RecyclerView.ViewHolder{

    private static final String SAMPLE_VIDEO =
            "https://video.twimg.com/ext_tw_video/703677246528221184/pu/vid/180x320/xnI48eAV8iPFW9aA.mp4";

    @BindView(R.id.play_button)
    public TextView play;
    @BindView(R.id.video_view)
    public SimpleVideoView videoView;
//    @BindView(R.id.caption)
//    TextView tvCaption;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_post_time)
    TextView mTvPostTime;
    @BindView(R.id.tv_likes)
    TextView tvLikes;
    @BindView(R.id.tv_comments)
    TextView tvComments;
    @BindView(R.id.button_like)
    public LikeButton mBtnLike;

    @BindView(R.id.comment)
    public LinearLayout comment;

    private Context context;
    private StorageReference storageReference;

    public CustomVideoVH(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    public void setName(String name){
        if(name!=null && name.length()>0){
            mTvUserName.setText(name);
        }
    }
    public void setPostTime(String postTime){
        if(mTvPostTime!=null && mTvPostTime.length()>0){
            mTvPostTime.setText(postTime);
        }
    }
    public void setLikes(int likes){
        tvLikes.setText(String.valueOf(likes)+" likes");
    }
    public void setComments(int comments){
        tvComments.setText(String.valueOf(comments)+" comments");
    }

    public void setCaption(String caption){
        if(caption!=null && caption.length()>0) {
//            tvCaption.setText(caption);
        }
    }

    public void setVideo(Uri videoURI){
        videoView.setErrorTracker(new SimpleVideoView.VideoPlaybackErrorTracker() {
            @Override
            public void onPlaybackError(Exception e) {
                e.printStackTrace();
                Snackbar.make(videoView, "Uh oh, error playing!", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        videoView.start(SAMPLE_VIDEO);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView.isPlaying())
                    videoView.pause();
                else
                    videoView.play();
            }
        });
    }
}