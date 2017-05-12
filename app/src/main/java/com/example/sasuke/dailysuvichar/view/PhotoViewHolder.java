package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo)
    public ImageView mIvPhoto;
    @BindView(R.id.button_like)
    public LikeButton mBtnLike;
    @BindView(R.id.tv_like)
    public TextView tv_like;
    @BindView(R.id.comment)
    public LinearLayout comment;
    @BindView(R.id.invisible)
    public LinearLayout invisible;
    @BindView(R.id.edittext)
    public EditText editText;
    @BindView(R.id.post_comment)
    public Button postComment;

    public PhotoViewHolder(View itemView) {
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

    public void setImage(int photo) {
        Picasso.with(itemView.getContext()).load(photo).fit().into(mIvPhoto);
    }
}
