package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.view.RVTags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity{

    @BindView(R.id.diet)
    TextView diet;
    @BindView(R.id.yoga)
    TextView yoga;
    @BindView(R.id.health)
    TextView health;
    @BindView(R.id.religion)
    TextView religion;
    @BindView(R.id.motivation)
    TextView motivation;
    @BindView(R.id.ayurveda)
    TextView ayurveda;
    @BindView(R.id.astrology)
    TextView astrology;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.grid)
    GridLayout grid;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.submit_subinterests)
    Button submit;

    private List<String> interests, subInterests, data, mSlectedItems;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);

        context = getApplicationContext();
        interests = new ArrayList<>();
        subInterests = new ArrayList<>();
        mSlectedItems = new ArrayList<>();
    }

    @OnClick(R.id.diet)
    public void diet(){
        setAptBG("diet", diet);
    }

    @OnClick(R.id.yoga)
    public void yoga(){
        setAptBG("yoga", yoga);
    }

    @OnClick(R.id.health)
    public void health(){
        setAptBG("health", health);
    }

    @OnClick(R.id.religion)
    public void religion(){
        setAptBG("religion", religion);
    }

    @OnClick(R.id.motivation)
    public void motivation(){
        setAptBG("motivation", motivation);
    }

    @OnClick(R.id.ayurveda)
    public void ayurveda(){
        setAptBG("ayurveda", ayurveda);
    }

    @OnClick(R.id.astrology)
    public void astrology(){
        setAptBG("astrology", astrology);
    }

    @OnClick(R.id.next)
    public void nextIsSubinterests(){
        if(interests.size()<1){
            Toast.makeText(context, "Please Choose The Interest Category", Toast.LENGTH_SHORT).show();
        }
        else{
            if(interests.contains("diet")){
                String[] temp = getResources().getStringArray(R.array.diet_array);
                addToSubinterests(temp);
            }
            if(interests.contains("yoga")){
                String[] temp = getResources().getStringArray(R.array.yoga_array);
                addToSubinterests(temp);
            }
            if(interests.contains("health")){
                String[] temp = getResources().getStringArray(R.array.health_array);
                addToSubinterests(temp);
            }
            if(interests.contains("religion")){
                String[] temp = getResources().getStringArray(R.array.religion_array);
                addToSubinterests(temp);
            }
            if(interests.contains("motivation")){
                String[] temp = getResources().getStringArray(R.array.motivation_array);
                addToSubinterests(temp);
            }
            if(interests.contains("ayurveda")){
                String[] temp = getResources().getStringArray(R.array.ayurveda_array);
                addToSubinterests(temp);
            }
            if(interests.contains("astrology")){
                String[] temp = getResources().getStringArray(R.array.astrology_array);
                addToSubinterests(temp);
            }
            grid.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
//            data = fillWithData();
            recyclerView.setAdapter(new RVTags(context, subInterests, mSlectedItems));
            submit.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.submit_subinterests)
    public void setSubmit(){
        if(mSlectedItems.size()<1){
            Toast.makeText(context, "Please Select Atleast One Subcategory.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, " " + mSlectedItems.size(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addToSubinterests(String[] temp){
        for(String s: temp){
            subInterests.add(s);
        }
    }

    private void setAptBG(String temp, TextView view){
        if(interests.contains(temp)){
            interests.remove(temp);
            view.setBackgroundColor(getResources().getColor(R.color.white));
            view.setTextColor(getResources().getColor(R.color.black));
        }
        else {
            interests.add(temp);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            view.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
