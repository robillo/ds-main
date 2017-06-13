package com.robillo.sasuke.dailysuvichar.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.activity.GuruDetailActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.readystatesoftware.viewbadger.BadgeView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class VHGurus extends RecyclerView.ViewHolder{

    private Context context;

    @BindView(R.id.imageViewMain)
    public CircleImageView imageView1;
    @BindView(R.id.guru_name)
    public TextView guruName;
    @BindView(R.id.follow)
    public Button follow;
    @BindView(R.id.cardView)
    public CardView cardView;

    public VHGurus(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    public void setFollowersCount(int followers){

        BadgeView badge = new BadgeView(context, imageView1);
        badge.setText(String.valueOf(followers));
        badge.show();
    }


    public void setImage(StorageReference storageReference, final Context ctx) {
//        Picasso.with(itemView.getContext()).load(photo).fit().into(mIvPhoto);
//        if(storageReference!=null) {
//            Glide.with(ctx).
//                    using(new FirebaseImageLoader())
//                    .load(storageReference)
//                    .centerCrop()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imageView1);
//
//        }
        if(storageReference!=null){
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(ctx)
                            .load(uri)
                            .into(imageView1);
                }
            });
        }
    }

    public void setName(String name){
        if(name!=null && name.length()>0){
            guruName.setText(name);
        }
    }

    public void intent(String uid, boolean isFollowing){
        Intent i = new Intent(context, GuruDetailActivity.class);
        if(uid!=null) {
            i.putExtra("uid", uid);
            i.putExtra("isfollowing", isFollowing);
        }
        context.startActivity(i);
    }
}
