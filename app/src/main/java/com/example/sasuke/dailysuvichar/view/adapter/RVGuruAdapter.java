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
    private Context context;
    private int filterCategory = 100; //noFilter100 followers0 astrology1 yoga2 pandits3 motivation4 ayurveda5
    private String MOTIVATION_GURU = "Motivation Guru";
    private String AYURVEDA_GURU = "Ayurveda Guru";
    private String YOGA_GURU = "Yoga Guru";
    private String PANDIT = "Pandit";
    private String ASTROLOGY_GURU = "Astrology Guru";

    public RVGuruAdapter(Context context, ArrayList<Guru> list) {
        this.context = context;
        this.list = list;
    }

    public RVGuruAdapter(ArrayList<Guru> list, Context context, int filterCategory) {
        this.list = list;
        this.context = context;
        this.filterCategory = filterCategory;
    }

    @Override
    public VHGurus onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_guru, parent, false);
        return new VHGurus(view);
    }

    @Override
    public void onBindViewHolder(final VHGurus holder, int position) {

        final Guru item = list.get(position);

        switch (filterCategory){
            case 100:{
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
                break;
            }
            case 0:{

                break;
            }
            case 1:{
                if(item.getSpecialization().equals(ASTROLOGY_GURU)){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(item.getName()!=null) {
                                holder.setName(item.getName());
                            }
                            holder.setFollowersCount(item.getFollowersCount());
                        }
                    });
                }
                break;
            }
            case 2:{
                if(item.getSpecialization().equals(YOGA_GURU)){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(item.getName()!=null) {
                                holder.setName(item.getName());
                            }
                            holder.setFollowersCount(item.getFollowersCount());
                        }
                    });
                }
                break;
            }
            case 3:{
                if(item.getSpecialization().equals(PANDIT)){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(item.getName()!=null) {
                                holder.setName(item.getName());
                            }
                            holder.setFollowersCount(item.getFollowersCount());
                        }
                    });
                }
                break;
            }
            case 4:{
                if(item.getSpecialization().equals(MOTIVATION_GURU)){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(item.getName()!=null) {
                                holder.setName(item.getName());
                            }
                            holder.setFollowersCount(item.getFollowersCount());
                        }
                    });
                }
                break;
            }
            case 5:{
                if(item.getSpecialization().equals(AYURVEDA_GURU)){
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(item.getName()!=null) {
                                holder.setName(item.getName());
                            }
                            holder.setFollowersCount(item.getFollowersCount());
                        }
                    });
                }
                break;
            }
        }

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
