package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.view.RVTags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity {

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
    @BindView(R.id.etStatus)
    EditText etStatus;


    private ArrayList<String> interests, subInterests, data, mSelectedItems;
    private Context context;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReferenceTag, mDatabaseReferenceUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = getApplicationContext();
        interests = new ArrayList<>();
        subInterests = new ArrayList<>();
        mSelectedItems = new ArrayList<>();
    }

    @OnClick(R.id.diet)
    public void diet() {
        setAptBG("diet", diet);
    }

    @OnClick(R.id.yoga)
    public void yoga() {
        setAptBG("yoga", yoga);
    }

    @OnClick(R.id.health)
    public void health() {
        setAptBG("health", health);
    }

    @OnClick(R.id.religion)
    public void religion() {
        setAptBG("religion", religion);
    }

    @OnClick(R.id.motivation)
    public void motivation() {
        setAptBG("motivation", motivation);
    }

    @OnClick(R.id.ayurveda)
    public void ayurveda() {
        setAptBG("ayurveda", ayurveda);
    }

    @OnClick(R.id.astrology)
    public void astrology() {
        setAptBG("astrology", astrology);
    }

    @OnClick(R.id.next)
    public void nextIsSubinterests() {
        if (interests.size() < 1) {
            Toast.makeText(context, "Please Choose The Interest Category", Toast.LENGTH_SHORT).show();
        } else {
            if (interests.contains("diet")) {
                String[] temp = getResources().getStringArray(R.array.diet_array);
                addToSubinterests(temp);
            }
            if (interests.contains("yoga")) {
                String[] temp = getResources().getStringArray(R.array.yoga_array);
                addToSubinterests(temp);
            }
            if (interests.contains("health")) {
                String[] temp = getResources().getStringArray(R.array.health_array);
                addToSubinterests(temp);
            }
            if (interests.contains("religion")) {
                String[] temp = getResources().getStringArray(R.array.religion_array);
                addToSubinterests(temp);
            }
            if (interests.contains("motivation")) {
                String[] temp = getResources().getStringArray(R.array.motivation_array);
                addToSubinterests(temp);
            }
            if (interests.contains("ayurveda")) {
                String[] temp = getResources().getStringArray(R.array.ayurveda_array);
                addToSubinterests(temp);
            }
            if (interests.contains("astrology")) {
                String[] temp = getResources().getStringArray(R.array.astrology_array);
                addToSubinterests(temp);
            }
            grid.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
//            data = fillWithData();
            subInterests.add("Others");
            recyclerView.setAdapter(new RVTags(context, subInterests, mSelectedItems));
//            submit.setVisibility(View.VISIBLE);
        }
    }

    private void addToSubinterests(String[] temp) {
        for (String s : temp) {
            subInterests.add(s);
        }
    }

    private void setAptBG(String temp, TextView view) {
        if (interests.contains(temp)) {
            interests.remove(temp);
            view.setBackgroundColor(getResources().getColor(R.color.white));
            view.setTextColor(getResources().getColor(R.color.black));
        } else {
            interests.add(temp);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            view.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @OnClick(R.id.btnPostStatus)
    public void postStatus() {
        if (mSelectedItems.size() < 1) {
            Toast.makeText(context, "Please Select Atleast One Subcategory.", Toast.LENGTH_SHORT).show();
        } else {
            if (etStatus.getText().length() >= 1) {
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                Status status = new Status(mFirebaseUser.getUid(),mSelectedItems,"Rishabh", System.currentTimeMillis(),
                        0,0,null,etStatus.getText().toString(),mFirebaseUser.getEmail());

                mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("tags");
                String postID = mDatabaseReferenceTag.push().getKey();
                for(String subInt: mSelectedItems){
                    mDatabaseReferenceTag.child(subInt.toLowerCase()).child("status").child(postID).setValue(status);
                }

                mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
                mDatabaseReferenceUser.child("users").child(mFirebaseUser.getUid()).child("posts").child("status").push().setValue(status);

                Toast.makeText(context,"Post successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,HomeActivity.class));
                finish();
            }
        }

    }

}
