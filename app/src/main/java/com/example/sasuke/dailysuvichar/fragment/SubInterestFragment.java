package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.HomeActivity;
import com.example.sasuke.dailysuvichar.models.User;
import com.example.sasuke.dailysuvichar.view.adapter.SubInterestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Sasuke on 4/24/2017.
 */

public class SubInterestFragment extends BaseFragment {

    @BindView(R.id.rv_sub_interest)
    RecyclerView mRvSubInterest;

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ArrayList<String> mSubInterests;
    private SubInterestAdapter mAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_sub_interest;
    }

    public static SubInterestFragment newInstance() {
        return new SubInterestFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvSubInterest.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new SubInterestAdapter();
        mRvSubInterest.setAdapter(mAdapter);

        mDatabase.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mSubInterests = user.getAllSubInterests();
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setItems(mSubInterests);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @OnClick(R.id.action_bar_home_icon)
    public void returnToPreviousActivity() {
        getActivity().finish();
    }

    @OnClick(R.id.tv_next)
    public void openHomeActivity() {
        startActivity(HomeActivity.newIntent(getActivity()));
    }
}
