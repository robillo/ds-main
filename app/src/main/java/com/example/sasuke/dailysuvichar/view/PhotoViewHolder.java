package com.example.sasuke.dailysuvichar.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.FullScreenActivity;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.photo)
    public ImageView mIvPhoto;
    @BindView(R.id.caption)
    TextView tvCaption;
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
//    @BindView(R.id.tv_like)
//    TextView tv_like;
    @BindView(R.id.comment)
    public LinearLayout comment;
//    @BindView(R.id.invisible)
//    public LinearLayout invisible;
//    @BindView(R.id.edittext)
//    public EditText editText;
//    @BindView(R.id.post_comment)
//    public Button postComment;

    private Context context;
    private StorageReference storageReference;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
//        mBtnLike.setOnLikeListener(new OnLikeListener() {
//            @Override
//            public void liked(LikeButton likeButton) {
//                likeButton.setLiked(true);
//            }
//
//            @Override
//            public void unLiked(LikeButton likeButton) {
//                likeButton.setLiked(false);
//            }
//        });
//        tv_like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mBtnLike.isLiked()){
//                    mBtnLike.setLiked(false);
//                }else{
//                    mBtnLike.setLiked(true);
//                }
//            }
//        });
    }

    public void setImage(StorageReference storageReference, Context ctx) {
//        Picasso.with(itemView.getContext()).load(photo).fit().into(mIvPhoto);
        if(storageReference!=null) {
            this.storageReference = storageReference;
            Glide.with(ctx).
                    using(new FirebaseImageLoader())
                    .load(storageReference)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mIvPhoto);
        }
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
            tvCaption.setText(caption);
        }
    }

    public void fullScreenIntent(Bitmap bitmap){
        Intent i = new Intent(context, FullScreenActivity.class);
        context.startActivity(i);
    }
}
