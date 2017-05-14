package com.example.sasuke.dailysuvichar.view.adapter;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Guru;
import com.example.sasuke.dailysuvichar.view.VHGurus;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;

import java.util.ArrayList;

public class RVGuruAdapter extends RecyclerView.Adapter<VHGurus> {

    ArrayList<Guru> list;


    public RVGuruAdapter(Context context, ArrayList<Guru> list ) {
        this.context = context;
        this.list = list;
    }

    private Context context;


    @Override
    public VHGurus onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_guru, parent, false);
        return new VHGurus(view);
    }

    @Override
    public void onBindViewHolder(final VHGurus holder, int position) {
        final Guru item = list.get(position);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(item.getName()!=null) {
                    holder.setName(item.getName());
                }
                holder.setFollowersCount(item.getFollowersCount());

//                if(item.getStorageReference()!=null && context!=null) {
//                    holder.setImage(item.getStorageReference(), context);
//                }
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.intent();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
}
