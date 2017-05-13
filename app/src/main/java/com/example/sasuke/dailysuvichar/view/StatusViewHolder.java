package com.example.sasuke.dailysuvichar.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.sasuke.dailysuvichar.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_post_time)
    TextView mTvPostTime;
    @BindView(R.id.tv_likes)
    TextView tvLikes;
    @BindView(R.id.tv_comments)
    TextView tvComments;
    @BindView(R.id.status)
    TextView mTvStatus;
    @BindView(R.id.button_like)
    LikeButton mBtnLike;
    @BindView(R.id.tv_like)
    TextView tv_like;
    @BindView(R.id.comment)
    public LinearLayout comment;
//    @BindView(R.id.invisible)
//    public LinearLayout invisible;
//    @BindView(R.id.edittext)
//    public EditText editText;
//    @BindView(R.id.post_comment)
//    public Button postComment;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mBtnLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
            }
        });

        tv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBtnLike.isLiked()){
                    mBtnLike.setLiked(false);
                }else{
                    mBtnLike.setLiked(true);
                }
            }
        });
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

    public void setStatus(String status) {
        if (status != null && status.length() > 0)
            mTvStatus.setText(status);
    }
    public void setLikes(int likes){
        tvLikes.setText(String.valueOf(likes)+" likes");
    }
    public void setComments(int comments){
        tvComments.setText(String.valueOf(comments));
    }
}

