package com.example.sasuke.dailysuvichar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.newactivities.NewHomeyActivity;
import com.example.sasuke.dailysuvichar.newactivities.NewExploreyActivity;
import com.example.sasuke.dailysuvichar.view.RVTags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectActivity extends BaseActivity {

    private static final String TAG = "SELECT";
    @BindView(R.id.name)
    TextView name;
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

    private int from = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getName();
        from = getIntent().getIntExtra("from", 1);
        context = getApplicationContext();
        interests = new ArrayList<>();
        subInterests = new ArrayList<>();
        mSelectedItems = new ArrayList<>();
    }

    @OnClick(R.id.diet)
    public void diet() {
        setAptBG(getString(R.string.diett), diet);
    }

    @OnClick(R.id.yoga)
    public void yoga() {
        setAptBG(getString(R.string.yogaa), yoga);
    }

    @OnClick(R.id.health)
    public void health() {
        setAptBG(getString(R.string.healthh), health);
    }

    @OnClick(R.id.religion)
    public void religion() {
        setAptBG(getString(R.string.religionn), religion);
    }

    @OnClick(R.id.motivation)
    public void motivation() {
        setAptBG(getString(R.string.motivationn), motivation);
    }

    @OnClick(R.id.ayurveda)
    public void ayurveda() {
        setAptBG(getString(R.string.ayurvedaa), ayurveda);
    }

    @OnClick(R.id.astrology)
    public void astrology() {
        setAptBG(getString(R.string.astrologyy), astrology);
    }

    @OnClick(R.id.next)
    public void nextIsSubinterests() {
        if (interests.size() < 1) {
            Toast.makeText(context, R.string.pluese, Toast.LENGTH_SHORT).show();
        } else {
            if (interests.contains(getString(R.string.diett))) {
                String[] temp = getResources().getStringArray(R.array.diet_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.yogaa))) {
                String[] temp = getResources().getStringArray(R.array.yoga_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.healthh))) {
                String[] temp = getResources().getStringArray(R.array.health_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.religionn))) {
                String[] temp = getResources().getStringArray(R.array.religion_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.motivationn))) {
                String[] temp = getResources().getStringArray(R.array.motivation_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.ayurvedaa))) {
                String[] temp = getResources().getStringArray(R.array.ayurveda_array);
                addToSubinterests(temp);
            }
            if (interests.contains(getString(R.string.astrologyy))) {
                String[] temp = getResources().getStringArray(R.array.astrology_array);
                addToSubinterests(temp);
            }
            grid.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
//            data = fillWithData();
            subInterests.add(getString(R.string.others));
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
            Toast.makeText(context, R.string.pleeease, Toast.LENGTH_SHORT).show();
        } else {
            if (etStatus.getText().length() >= 1) {
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                Status status = null;
                Log.d(TAG, "postStatus: NAMEE "+name);
                if(name.getText()!="") {

                    status = new Status(mFirebaseUser.getUid(), mSelectedItems, name.getText().toString(), System.currentTimeMillis(),
                            0, 0, null, etStatus.getText().toString(), mFirebaseUser.getEmail());
                }else{

                    status = new Status(mFirebaseUser.getUid(), mSelectedItems, "Unknown User", System.currentTimeMillis(),
                            0, 0, null, etStatus.getText().toString(), mFirebaseUser.getEmail());
                }

                mDatabaseReferenceTag = FirebaseDatabase.getInstance().getReference("tags");
                String postID = mDatabaseReferenceTag.push().getKey();
                for(String subInt: mSelectedItems){
                    mDatabaseReferenceTag.child(subInt.toLowerCase()).child("status").child(postID).setValue(status);
                }

                mDatabaseReferenceUser = FirebaseDatabase.getInstance().getReference();
                mDatabaseReferenceUser.child("users").child(mFirebaseUser.getUid()).child("posts").child("status").push().setValue(status);

                Toast.makeText(context, getString(R.string.success), Toast.LENGTH_SHORT).show();
                if(from == 1){
                    startActivity(new Intent(this, NewExploreyActivity.class));
                }
                else if(from == 2){
                    startActivity(new Intent(this, NewHomeyActivity.class));
                }
                finish();
            }
        }

    }

    public void getName(){

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").getValue()!=null){
                    Log.d(TAG, "onDataChange: NAMEE"+(dataSnapshot.child("name").getValue()));
                    name.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from == 1){
            startActivity(new Intent(this, NewExploreyActivity.class));
        }
        else if(from == 2){
            startActivity(new Intent(this, NewHomeyActivity.class));
        }
    }
}
