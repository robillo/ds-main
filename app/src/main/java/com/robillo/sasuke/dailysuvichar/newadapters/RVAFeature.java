package com.robillo.sasuke.dailysuvichar.newadapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.models.Feature;
import com.robillo.sasuke.dailysuvichar.view.VHFeature;

import java.util.Collections;
import java.util.List;

import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;

public class RVAFeature extends RecyclerView.Adapter<VHFeature>{

    private Context context;
    private List<Feature> list = Collections.emptyList();
    private int pos;

    public RVAFeature(Context context, List<Feature> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VHFeature onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_features, parent, false);
        return new VHFeature(v);
    }

    @Override
    public void onBindViewHolder(final VHFeature holder, final int position) {
        pos = position;
        holder.header.setText(list.get(position).getHeader() + ": " + list.get(position).getDescription());
        Glide.with(context)
                .load(list.get(position).getPhotoLink())
                .bitmapTransform(new BrightnessFilterTransformation(context, -0.4f))
                .into(holder.photo);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.intent(list.get(position).getPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
