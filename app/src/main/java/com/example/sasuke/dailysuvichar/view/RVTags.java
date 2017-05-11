package com.example.sasuke.dailysuvichar.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RVTags extends RecyclerView.Adapter<VHTags>{

    private Context context, pContext;
    private List<String> list = Collections.emptyList();
    private ArrayList<String> mSelectedItems = new ArrayList<>();

    public RVTags(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public VHTags onCreateViewHolder(ViewGroup parent, int viewType) {
        pContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tags, parent, false);
        return new VHTags(v);
    }

    @Override
    public void onBindViewHolder(VHTags holder, final int position) {
        holder.tag.setText(list.get(position));
        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(pContext, list.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
