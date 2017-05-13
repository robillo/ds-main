package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;

public class VHGurus extends RecyclerView.ViewHolder{

    public ImageView imageView1, imageView2;
    public TextView guruName;
    public Button follow;
    public CardView cardView;

    public VHGurus(View itemView) {
        super(itemView);
        imageView1 = (ImageView) itemView.findViewById(R.id.imageView);
        imageView2 = (ImageView) itemView.findViewById(R.id.imageView2);
        guruName = (TextView) itemView.findViewById(R.id.guru_name);
        follow = (Button) itemView.findViewById(R.id.follow);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
    }
}
