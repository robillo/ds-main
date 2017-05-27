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
import com.example.sasuke.dailysuvichar.newactivities.NewGurusActivity;
import com.example.sasuke.dailysuvichar.view.VHGurus;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;

import java.util.ArrayList;
import java.util.List;

public class RVGuruAdapter extends RecyclerView.Adapter<VHGurus> {

    ArrayList<Guru> list, temp;
    private Context context;
    private int filterCategory = 100; //noFilter100 followers0 astrology1 yoga2 pandits3 motivation4 ayurveda5
    private String MOTIVATION_GURU = "Motivation Guru";
    private String AYURVEDA_GURU = "Ayurveda Guru";
    private String YOGA_GURU = "Yoga Guru";
    private String PANDIT = "Pandit";
    private String ASTROLOGY_GURU = "Astrology Guru";
    private List<Boolean> isFollowing;
    private FirebaseUser mFirebaseUser;

    public RVGuruAdapter(Context context, ArrayList<Guru> list) {
        this.context = context;
        this.list = list;
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public RVGuruAdapter(Context context, ArrayList<Guru> list, int filterCategory) {
        this.filterCategory = filterCategory;
        temp = new ArrayList<>();
        switch (filterCategory){
            case 100:{
                this.list = list;
                break;
            }
            case 0:{

                break;
            }
            case 1:{
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getSpecialization().equals(ASTROLOGY_GURU)){
                        temp.add(list.get(i));
                    }
                }
                this.list = temp;
                break;
            }
            case 2:{
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getSpecialization().equals(YOGA_GURU)){
                        temp.add(list.get(i));
                    }
                }
                this.list = temp;
                break;
            }
            case 3:{
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getSpecialization().equals(PANDIT)){
                        temp.add(list.get(i));
                    }
                }
                this.list = temp;
                break;
            }
            case 4:{
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getSpecialization().equals(MOTIVATION_GURU)){
                        temp.add(list.get(i));
                    }
                }
                this.list = temp;
                break;
            }
            case 5:{
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getSpecialization().equals(AYURVEDA_GURU)){
                        temp.add(list.get(i));
                    }
                }
                this.list = temp;
                break;
            }
            case 6:{
                this.list = list;
                break;
            }
        }
        this.context = context;
    }

    @Override
    public VHGurus onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_guru, parent, false);
        isFollowing =new ArrayList<>();
        for(int i = 0; i<=list.size(); i++){
            isFollowing.add(false);
        }
        return new VHGurus(view);
    }

    @Override
    public void onBindViewHolder(final VHGurus holder, final int position) {

        final Guru item = list.get(position);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if(item.getName()!=null) {
                    holder.setName(item.getName());
                }
                holder.setFollowersCount(item.getFollowersCount());

                if(item.getStorageReference()!=null && context!=null) {
                    holder.setImage(item.getStorageReference(), context);
                }
            }
        });
        if(item.getFollowers()!=null) {
            if (item.getFollowers().contains(mFirebaseUser.getUid())) {
                isFollowing.set(position, true);
            }
        }

        if(!isFollowing.get(position) && holder.follow.getText().equals("FOLLOWING")){
            holder.follow.setText("FOLLOW");
            holder.follow.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }
        else if(isFollowing.get(position) && holder.follow.getText().equals("FOLLOW")){
            holder.follow.setText("FOLLOWING");
            holder.follow.setBackgroundColor(context.getResources().getColor(R.color.green));
        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFollowing.get(position) && holder.follow.getText().equals("FOLLOW")){
                    isFollowing.set(position, true);
                    holder.follow.setText("FOLLOWING");
                    holder.follow.setBackgroundColor(context.getResources().getColor(R.color.green));
                    NewGurusActivity.setFollowing(item.getFollowers(), item.getUid(),true,item.getGuruUid());
//                    holder.setFollowersCount(item.getFollowersCount());

                }
                else if(isFollowing.get(position) && holder.follow.getText().equals("FOLLOWING")){
                    isFollowing.set(position, false);
                    holder.follow.setText("FOLLOW");
                    holder.follow.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    NewGurusActivity.setFollowing(item.getFollowers(),item.getUid(),false,item.getGuruUid());
//                    holder.setFollowersCount(item.getFollowersCount());
                }
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.intent(item.getUid(), isFollowing.get(position));
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
