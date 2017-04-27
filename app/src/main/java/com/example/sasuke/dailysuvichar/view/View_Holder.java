package com.example.sasuke.dailysuvichar.view;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.utils.ItemClickListener;

public class View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public CardView cardView;
    public ImageView drawable, bg;
    public TextView header;
    public LinearLayout ll;
    private ItemClickListener clickListener;

    public View_Holder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);
        drawable = (ImageView) itemView.findViewById(R.id.imageView);
        header = (TextView) itemView.findViewById(R.id.textView);
        bg = (ImageView) itemView.findViewById(R.id.imageView2);
        ll = (LinearLayout) itemView.findViewById(R.id.ll);

        itemView.setOnClickListener(this);
    }

    public void setClickListener(ItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View view) {
        clickListener.onCLick(view, getAdapterPosition(), Boolean.FALSE);
    }
}
