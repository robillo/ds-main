package com.example.sasuke.dailysuvichar.view;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.klinker.android.simple_videoview.SimpleVideoView;
import com.like.LikeButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by rishabhshukla on 15/05/17.
 */


public class CustomVideoVH extends RecyclerView.ViewHolder{

    private static final String SAMPLE_VIDEO =
            "https://firebasestorage.googleapis.com/v0/b/dailysuvichar-708d4.appspot.com/o/posts%2Fvideos%2F-KkM_JU-RlzvykGl9HUc?alt=media&token=95994318-8a65-402a-a8cb-f8c91c0dba6a.mp4";

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
    public Uri videoUrl;

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
    public void setCaption(String caption){
        if(caption!=null && caption.length()>0) {
//            tvCaption.setText(caption);
        }
    }

    public void setVideo(StorageReference storageReference){
//        final Uri videoUrl;

        Log.d(TAG, "setVideo: STORAGEREF "+storageReference);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                videoUrl = uri;
                Log.d(TAG, "onSuccess: VIDEOURL"+videoUrl);
            }
        });

        Log.d(TAG, "onSuccess: VIDEOURL"+videoUrl);

        videoView.setErrorTracker(new SimpleVideoView.VideoPlaybackErrorTracker() {
            @Override
            public void onPlaybackError(Exception e) {
                e.printStackTrace();
//                Snackbar.make(videoView, "Uh oh, error playing!", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        if(videoUrl!=null) {
            videoView.start(videoUrl.toString()+".mp4");
//            videoView.start(SAMPLE_VIDEO);
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
}
