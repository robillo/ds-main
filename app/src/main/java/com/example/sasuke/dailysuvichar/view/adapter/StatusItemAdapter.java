package com.example.sasuke.dailysuvichar.view.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.view.StatusViewHolder;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class StatusItemAdapter extends ItemViewBinder<Status, StatusViewHolder> {

    @NonNull
    @Override
    protected StatusViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.cell_status, parent, false);
        return new StatusViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull StatusViewHolder holder, @NonNull Status item) {
        holder.setStatus(item.getStatus());
    }
}
