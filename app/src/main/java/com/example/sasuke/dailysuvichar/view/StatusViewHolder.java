package com.example.sasuke.dailysuvichar.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.utils.ItemClickListener;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class StatusViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_post_time)
    TextView mTvPostTime;
    @BindView(R.id.tv_likes_count)
    TextView tvLikes;
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
    private DatabaseReference mUsersDatabase, mDBrefLikes;
    private FirebaseUser mFirebaseUser;

//    @BindView(R.id.tv_like)
//    TextView tv_like;
//    @BindView(R.id.invisible)
//    public LinearLayout invisible;
//    @BindView(R.id.edittext)
//    public EditText editText;
//    @BindView(R.id.post_comment)
//    public Button postComment;

    public LinearLayout llLikeComment;
    public TextView likeCount;
    @BindView(R.id.like_button)
    public LikeButton likeButton;
    private ItemClickListener clickListener;


    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        itemView.setTag(itemView);
//        itemView.setOnClickListener(this);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDBrefLikes = FirebaseDatabase.getInstance().getReference("allPosts");
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

    public void setName(String name) {
        if (name != null && name.length() > 0) {
            mTvUserName.setText(name);
        }
    }

    public void setPostTime(String postTime) {
        if (mTvPostTime != null && mTvPostTime.length() > 0) {
            mTvPostTime.setText(postTime);
        }
    }

    public void setStatusDP(final String UID) {
        mUsersDatabase.child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("photoUrl").getValue() != null) {
                    Activity a = (Activity) context;
                    if (context != null && !a.isDestroyed()) {
                        Log.e("GLIDE", "YES ");
                        Glide.with(context).
                                using(new FirebaseImageLoader())
                                .load(mStorageReferenceDP.child(UID))
                                .centerCrop()
                                .placeholder(R.mipmap.ic_launcher)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(mStatusDP);
                    }
                } else {
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

    public void setComments(int comments) {
//        tvComments.setText(String.valueOf(comments)+" comments");
    }

//    @Override
//    public void onClick(View view) {
//        clickListener.onClick(view, getAdapterPosition(), false);
//    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setLikedUser(String authorUid, String uid, final boolean liked, ArrayList<String> likedUsers) {

        Log.d(TAG, "setLikedUser: ");


//        final ArrayList<String> finalLikedUsers1 = likedUsers;
//        mDBrefLikes.child(uid).child("likes").runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                if (mutableData != null) {
//                    int likes = mutableData.getValue(Integer.class);
//
//                    Log.d(TAG, "doTransaction: " + finalLikedUsers1);
//                    if (liked) {
//                        if (!finalLikedUsers1.contains(mFirebaseUser.getUid())) {
//                            likes++;
////                            likedUsers.add(mFirebaseUser.getUid());
//                        }
//                    }else{
//                        Log.d(TAG, "doTransaction: 2 " + finalLikedUsers1);
//
//                        if (finalLikedUsers1.contains(mFirebaseUser.getUid())) {
//                            likes--;
////                            likedUsers.remove(mFirebaseUser.getUid());
//                        }
//                    }
//                    Log.d(TAG, "doTransaction: " + finalLikedUsers1);
//                mutableData.setValue(finalLikedUsers1);
//            }
//                return Transaction.success(mutableData);
//
//            }
//
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
//                if (liked) {
//                    likeButton.setLiked(true);
//                } else {
//                    likeButton.setLiked(false);
//                }
//                Log.d(TAG, "onComplete: " + b);
//            }
//        });

//        mUsersDatabase.child(mFirebaseUser.getUid()).child("userPosts").child(uid).child("likes").runTransaction(new Transaction.Handler() {
//            @Override
//            public Transaction.Result doTransaction(MutableData mutableData) {
//                if (mutableData != null) {
//                    int likes = mutableData.getValue(Integer.class);
//                    if (liked) {
////                        if(finalLikedUsers.contains(mFirebaseUser.getUid())) {
////                            return Transaction.success(mutableData);
////                        }else {
//                            likes++;
////                        }
//                    } else if (!liked && likes > 0) {
//                        likes--;
//                    }
//                    mutableData.setValue(likes);
//                }
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//
////                if(liked){
////                    likeButton.setLiked(true);
////                }else{
////                    likeButton.setLiked(false);
////                }
//                Log.d(TAG, "onComplete: " + b);
//            }
//        });

        if (liked) {
            if (likedUsers == null) {
                likedUsers = new ArrayList<>();
            }
            if (!likedUsers.contains(mFirebaseUser.getUid())) {
                likedUsers.add(mFirebaseUser.getUid());
                mDBrefLikes.child(uid).child("likedUsers").setValue(likedUsers);
                mUsersDatabase.child(authorUid).child("userPosts").child(uid).child("likedUsers").setValue(likedUsers);
            }
        } else if (!liked) {

            Log.d(TAG, "setLikedUser: likedusers " + likedUsers);
            if (likedUsers == null) {
//                likedUsers.remove(mFirebaseUser.getUid());
                return;
            } else {
                if (likedUsers.contains(mFirebaseUser.getUid())) {
                    likedUsers.remove(mFirebaseUser.getUid());
                    mDBrefLikes.child(uid).child("likedUsers").setValue(likedUsers);
                    mUsersDatabase.child(authorUid).child("userPosts").child(uid).child("likedUsers").setValue(likedUsers);
                }
            }
        }

        if (liked) {
            likeButton.setLiked(true);
        } else {
            likeButton.setLiked(false);
        }
    }

    public Boolean containsLikedUser(ArrayList<String> likedUsers) {
        if (likedUsers != null) {
            return likedUsers.contains(mFirebaseUser.getUid());
        }
        return false;
    }

    public void setLikes(int likes) {
        tvLikes.setText(String.valueOf(likes) + " like this");
    }

}

