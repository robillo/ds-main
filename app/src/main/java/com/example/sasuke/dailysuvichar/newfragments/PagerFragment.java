package com.example.sasuke.dailysuvichar.newfragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.newactivities.ExploreActivity;
import com.example.sasuke.dailysuvichar.newactivities.NewGurusActivity;
import com.example.sasuke.dailysuvichar.newactivities.NewHomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment extends Fragment {

    private TextView levelTV, levelHeaderTV;
    private ImageView Photo;
    private CardView cardView;
    private int levelNumber;

    public PagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pager, container, false);

        levelTV = (TextView) v.findViewById(R.id.level);
        levelHeaderTV = (TextView) v.findViewById(R.id.level_header);
        Photo = (ImageView) v.findViewById(R.id.imageView);
        cardView = (CardView) v.findViewById(R.id.cardView);

        Bundle args = getArguments();
        setPlayground(args);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (levelNumber){
                    case 1:{
                        Intent i = new Intent(getActivity(), ExploreActivity.class);
                        getActivity().startActivity(i);
                        break;
                    }
                    case 2:{
                        Intent i = new Intent(getActivity(), NewGurusActivity.class);
                        getActivity().startActivity(i);
                        break;
                    }
                    case 3:{
                        Intent i = new Intent(getActivity(), NewHomeActivity.class);
                        getActivity().startActivity(i);
                        break;
                    }
                }
            }
        });

        return v;
    }

    private void setPlayground(Bundle args){
        String Level = args.getString("level");
        String levelHeader = args.getString("levelHeader");
        String photoUrl = args.getString("photoUrl");
        levelNumber = args.getInt("levelNumber");

        levelTV.setText(Level);
        levelHeaderTV.setText(levelHeader);
        Glide.with(this)
                .load(photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .into(Photo);
    }
}
