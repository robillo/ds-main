package com.robillo.sasuke.dailysuvichar.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robillo.sasuke.dailysuvichar.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RVTags extends RecyclerView.Adapter<VHTags>{

    private Context context, pContext;
    private List<String> list = Collections.emptyList();
    private List<String> mSelectedItems = new ArrayList<>();
    private Boolean[] isSelected;

        public RVTags(Context context, List<String> list, List<String> mSelectedItems) {
        this.context = context;
        this.list = list;
        this.mSelectedItems = mSelectedItems;
    }

    @Override
    public VHTags onCreateViewHolder(ViewGroup parent, int viewType) {
        pContext = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tags, parent, false);
        isSelected = new Boolean[list.size()];
        for(int i = 0; i<list.size(); i++){
            isSelected[i] = false;
        }
        return new VHTags(v);
    }

    @Override
    public void onBindViewHolder(final VHTags holder, final int position) {
        holder.tag.setText(list.get(position));
        holder.tag.setBackgroundColor(pContext.getResources().getColor(R.color.white));
        holder.tag.setTextColor(pContext.getResources().getColor(R.color.black));
        if(mSelectedItems.contains(holder.tag.getText().toString())){
            holder.tag.setBackgroundColor(pContext.getResources().getColor(R.color.black));
            holder.tag.setTextColor(pContext.getResources().getColor(R.color.white));
        }
        holder.tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedItems.contains(holder.tag.getText().toString()) && isSelected[position]){
                    mSelectedItems.remove(holder.tag.getText().toString());
                    holder.tag.setBackgroundColor(pContext.getResources().getColor(R.color.white));
                    holder.tag.setTextColor(pContext.getResources().getColor(R.color.black));
                }
                else {
                    isSelected[position] = true;
                    mSelectedItems.add(holder.tag.getText().toString());
                    holder.tag.setBackgroundColor(pContext.getResources().getColor(R.color.black));
                    holder.tag.setTextColor(pContext.getResources().getColor(R.color.white));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
