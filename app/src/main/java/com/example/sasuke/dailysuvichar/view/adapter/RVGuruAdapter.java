package com.example.sasuke.dailysuvichar.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Guru;
import com.example.sasuke.dailysuvichar.view.VHGurus;

import java.util.ArrayList;
import java.util.List;

public class RVGuruAdapter extends RecyclerView.Adapter<VHGurus>{

    private List<Guru> list = new ArrayList<>();

    @Override
    public VHGurus onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_guru, parent, false);
        return new VHGurus(v);
    }

    @Override
    public void onBindViewHolder(VHGurus holder, int position) {
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
