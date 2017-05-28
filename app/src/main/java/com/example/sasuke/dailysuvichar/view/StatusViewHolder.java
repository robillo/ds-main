package com.example.sasuke.dailysuvichar.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_post_time)
    TextView mTvPostTime;
//    @BindView(R.id.tv_likes)
//    TextView tvLikes;
//    @BindView(R.id.tv_comments)
//    TextView tvComments;
    @BindView(R.id.status)
    TextView mTvStatus;
//    @BindView(R.id.button_like)
//    public LikeButton mBtnLike;
//    @BindView(R.id.comment)
//    public LinearLayout comment;
    @BindView(R.id.iv_profile_dp)
    ImageView mStatusDP;

    private Context context;
    private StorageReference mStorageReferenceDP;
    private DatabaseReference mUsersDatabase;

//    @BindView(R.id.tv_like)
//    TextView tv_like;
//    @BindView(R.id.invisible)
//    public LinearLayout invisible;
//    @BindView(R.id.edittext)
//    public EditText editText;
//    @BindView(R.id.post_comment)
//    public Button postComment;

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mStorageReferenceDP = FirebaseStorage.getInstance().getReference("profile").child("user").child("dp");
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

    public void setStatusDP(final String UID){
        mUsersDatabase.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("photoUrl").getValue()!=null) {
                    Activity a = (Activity) context;
                    if(context!=null&&!a.isDestroyed()) {
                        Log.e("GLIDE","YES ");
                        Glide.with(context).
                                using(new FirebaseImageLoader())
                                .load(mStorageReferenceDP.child(UID))
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(mStatusDP);
                    }
                }
                else {
                    Log.e("PHOTOURL", "NULL");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("GLIDINGGGGGG", "CANCELLED");
            }
        });
    }

    public void setStatus(String status) {
        if (status != null && status.length() > 0)
            mTvStatus.setText(status);
    }
    public void setLikes(int likes){
//        tvLikes.setText(String.valueOf(likes)+" likes");
    }
    public void setComments(int comments){
//        tvComments.setText(String.valueOf(comments)+" comments");
    }
}

