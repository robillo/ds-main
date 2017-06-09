package com.example.sasuke.dailysuvichar.view.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Data;
import com.example.sasuke.dailysuvichar.utils.ItemClickListener;
import com.example.sasuke.dailysuvichar.view.View_Holder;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<View_Holder>{

    private Context context;
    private List<Data> list = Collections.emptyList();
    private ArrayList<String> mSelectedItems = new ArrayList<>();

    public RecyclerViewAdapter(Context context, List<Data> list ) {
        this.context = context;
        this.list = list;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, int position) {
        animateView(holder.cardView);
        Glide.with(context)
                .load(list.get(position).getDrawable())
                .into(holder.drawable);
        holder.header.setText(list.get(position).getHeader());
        holder.drawable.setBackgroundColor(list.get(position).getColor());
        if(mSelectedItems.contains(holder.header.getText().toString())){
            Glide.with(context)
                    .load(R.drawable.ic_check_circle_white_24dp)
                    .into(holder.drawable);
            holder.drawable.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight));
        }
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position, Boolean isLongClick) {
                if(!isLongClick){
                    if(!mSelectedItems.contains(holder.header.getText().toString()) && !list.get(position).getSelected()){
                        mSelectedItems.add(holder.header.getText().toString());
                        list.get(position).setSelected(true);
                        Glide.with(context)
                                .load(R.drawable.ic_check_circle_white_24dp)
                                .into(holder.drawable);
                        holder.drawable.setBackgroundColor(context.getResources().getColor(R.color.colorHighlight));
                    }
                    else {
                        mSelectedItems.remove(holder.header.getText().toString());
                        list.get(position).setSelected(false);
                        Glide.with(context)
                                .load(list.get(position).getDrawable())
                                .into(holder.drawable);
                        holder.drawable.setBackgroundColor(list.get(position).getColor());
                    }
                }
            }
        });
    }

    private void animateView(CardView linearLayout) {

        Animator[] animators = new Animator[]{
                DefaultAnimations.shrinkAnimator(linearLayout, 800L),
                DefaultAnimations.fadeInAnimator(linearLayout, 800L)
        };

        LinearSort linearSort = new LinearSort(100L, false, LinearSort.Direction.TOP_TO_BOTTOM);

        Animator spruceAnimator = new Spruce
                .SpruceBuilder(linearLayout)
                .sortWith(linearSort)
                .animateWith(animators)
                .start();
    }

//    // Custom method to get a random number between a range
//    protected int getRandomIntInRange(int max, int min){
//        return mRandom.nextInt((max-min)+min)+min;
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
