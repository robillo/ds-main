package com.example.sasuke.dailysuvichar.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.readystatesoftware.viewbadger.BadgeView;

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
//        CountBadge.Factory circleFactory = new CountBadge.Factory(BadgeShape.circle(.7f, Gravity.CENTER | Gravity.CENTER), 0xffff0000, 0xffffffff);
//
//        Badger.sett(imageView1, circleFactory).setCount(followers);

//        setBadgeCount(this, imageView1, "9");

        BadgeView badge = new BadgeView(context, imageView1);
        badge.setText(String.valueOf(followers));
        badge.show();
    }


    public void setImage(StorageReference storageReference, Context ctx) {
//        Picasso.with(itemView.getContext()).load(photo).fit().into(mIvPhoto);
        if(storageReference!=null) {
            Glide.with(ctx).
                    using(new FirebaseImageLoader())
                    .load(storageReference)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView1);
        }
    }

    public void setName(String name){
        if(name!=null && name.length()>0){
            guruName.setText(name);
        }
    }

}
