package com.robillo.sasuke.dailysuvichar.view.adapter;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.view.SubInterestViewHolder;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.LinearSort;

import java.util.ArrayList;

public class SubInterestAdapter extends RecyclerView.Adapter<SubInterestViewHolder> {

    private ArrayList<String> mSubInterestList;
    private ArrayList<String> mSelectedItem = new ArrayList<>();

    @Override
    public SubInterestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_sub_interest, null);
        return new SubInterestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SubInterestViewHolder holder, int position) {
        final String item = mSubInterestList.get(position);
        animateView(holder.mLlContainer);
        holder.mTvSubInterest.setChecked(false);
        holder.mTvSubInterest.setText(item);
        if (mSelectedItem.contains(holder.mTvSubInterest.getText().toString())) {
            holder.mTvSubInterest.setChecked(true);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSelectedItem.contains(item) && !holder.mTvSubInterest.isChecked()) {
                    mSelectedItem.add(item);
                    holder.mTvSubInterest.setChecked(true);
                } else {
                    mSelectedItem.remove(item);
                    holder.mTvSubInterest.setChecked(false);
                }
            }
        });
    }

    private void animateView(LinearLayout linearLayout) {

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

    @Override
    public int getItemCount() {
        if (mSubInterestList != null && mSubInterestList.size() > 0) {
            return mSubInterestList.size();
        }
        return 0;
    }

    public void setItems(ArrayList<String> list) {
        mSubInterestList = list;
        notifyDataSetChanged();
    }
}
