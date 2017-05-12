package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo)
    public ImageView mIvPhoto;
    @BindView(R.id.button_like)
    LikeButton mBtnLike;
    @BindView(R.id.tv_like)
    TextView tv_like;


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
