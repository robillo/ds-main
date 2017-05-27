package com.example.sasuke.dailysuvichar.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Data;
import com.example.sasuke.dailysuvichar.newactivities.NewMainActivity;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;
import com.example.sasuke.dailysuvichar.view.adapter.RecyclerViewAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.SubInterestAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class SubInterestFragment extends BaseFragment {

    @BindView(R.id.rv_sub_interest)
    RecyclerView mRvSubInterest;
    private int[] allColors, allDrawables;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private List<Data> data;
    private RecyclerViewAdapter adapter;

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ArrayList<String> mSubInterests;
    private ArrayList<String> mSelectedSubInterests = null;
    private boolean mVisitedAlready = false;
    private HashMap<String,ArrayList<String>> mAllInterests;

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

        allDrawables = new int[]{R.drawable.ic_diet, R.drawable.ic_yoga,
                R.drawable.ic_health, R.drawable.ic_god, R.drawable.ic_motivation
                , R.drawable.ic_peace, R.drawable.ic_astrology};
        allColors = getResources().getIntArray(R.array.rvItemColors);

//        mRvSubInterest.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        mAdapter = new SubInterestAdapter();
//        mRvSubInterest.setAdapter(mAdapter);


        mDatabase.child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mSubInterests = new ArrayList<String>();
                mAllInterests = new HashMap<>();
                if(dataSnapshot.child("mSelectedSubInterests").getValue()!=null){
                    mSubInterests.addAll((Collection<? extends String>) dataSnapshot.child("mSelectedSubInterests").getValue());
                }
                if(dataSnapshot.child("mAllInterests").getValue()!=null){
                    mAllInterests.putAll((Map<? extends String, ? extends ArrayList<String>>) dataSnapshot.child("mAllInterests").getValue());
                }

//                User user = dataSnapshot.getValue(User.class);
//                mSubInterests = user.getAllSubInterests();

//                mAllInterests = user.getAllInterests();

                staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                mRvSubInterest.setLayoutManager(gridLayoutManager);

                if(!mVisitedAlready) {
                    mVisitedAlready = true;
                    Log.d(TAG, "onDataChange: CALL VISITED ");
                    fillWithData();
                }
                Log.d(TAG, "onDataChange: CALL ADA ");
                adapter = new RecyclerViewAdapter(getActivity(), data);
                mRvSubInterest.setAdapter(adapter);

//                if (getActivity() != null) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            mAdapter.setItems(mSubInterests);
//                        }
//                    });
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }
    private void fillWithData(){
         data = new ArrayList<>();

//        if(mSubInterests!=null) {
//            for (int i = 0; i < mSubInterests.size(); i++) {
//                data.add(new Data(mSubInterests.get(i), allDrawables[0], i + 1, allColors[5], false));
//            }
//        }
        if(mAllInterests!=null){
            int i=0;
                if(mAllInterests.containsKey(getResources().getString(R.string.astrology).toLowerCase())){
                    ArrayList aList = mAllInterests.get(getResources().getString(R.string.astrology).toLowerCase());
                    Log.d(TAG, "fillWithData: ASTROLOGY "+aList.size());

                    for (int j = 0; j < aList.size(); j++) {
                        data.add(new Data((String) aList.get(j), allDrawables[6], i + 1, allColors[6], false));
                    }
                }
                if(mAllInterests.containsKey(getResources().getString(R.string.diet).toLowerCase())){
                    ArrayList aList = mAllInterests.get(getResources().getString(R.string.diet).toLowerCase());
                    for (int j = 0; j < aList.size(); j++) {
                        data.add(new Data((String) aList.get(j), allDrawables[0], i + 1, allColors[0], false));
                    }
                }
                if(mAllInterests.containsKey(getResources().getString(R.string.yoga).toLowerCase())){
                    ArrayList aList = mAllInterests.get(getResources().getString(R.string.yoga).toLowerCase());
                    for (int j = 0; j < aList.size(); j++) {
                        data.add(new Data((String) aList.get(j), allDrawables[1], i + 1, allColors[1], false));
                    }
                }
                if(mAllInterests.containsKey(getResources().getString(R.string.health).toLowerCase())){
                    ArrayList aList = mAllInterests.get(getResources().getString(R.string.health).toLowerCase());
                    for (int j = 0; j < aList.size(); j++) {
                        data.add(new Data((String) aList.get(j), allDrawables[2], i + 1, allColors[2], false));
                    }
                }
                if(mAllInterests.containsKey(getResources().getString(R.string.religion).toLowerCase())){
                    ArrayList aList = mAllInterests.get(getResources().getString(R.string.religion).toLowerCase());
                    for (int j = 0; j < aList.size(); j++) {
                        data.add(new Data((String) aList.get(j), allDrawables[3], i + 1, allColors[3], false));
                    }
                }
                if(mAllInterests.containsKey(getResources().getString(R.string.motivation).toLowerCase())){
                    ArrayList aList = mAllInterests.get(getResources().getString(R.string.motivation).toLowerCase());
                    for (int j = 0; j < aList.size(); j++) {
                        data.add(new Data((String) aList.get(j), allDrawables[4], i + 1, allColors[4], false));
                    }
                }
                if(mAllInterests.containsKey(getResources().getString(R.string.ayurveda).toLowerCase())){
                    ArrayList aList = mAllInterests.get(getResources().getString(R.string.ayurveda).toLowerCase());
                    for (int j = 0; j < aList.size(); j++) {
                        data.add(new Data((String) aList.get(j), allDrawables[5], i + 1, allColors[5], false));
                    }
                }
            }

    }

    @OnClick(R.id.tv_next)
    public void openHomeActivity() {
        if(data!=null){
            mSelectedSubInterests= new ArrayList<>();
            for(Data d:data){
                if(d.getSelected()){
                    mSelectedSubInterests.add(d.getHeader().toLowerCase());
                }
            }
        }
        else {
            Toast.makeText(getActivity(), R.string.incomplete, Toast.LENGTH_SHORT).show();
        }

        if(mSelectedSubInterests!=null && mSelectedSubInterests.size()<3){
            Toast.makeText(getActivity().getApplicationContext(), R.string.please_three, Toast.LENGTH_SHORT).show();
        }
        else if(mSelectedSubInterests==null){
            Toast.makeText(getContext().getApplicationContext(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
        }
        else {
            if(isOnline()) {
                mDatabase.child(mFirebaseUser.getUid()).child("mAllInterests").setValue(mAllInterests);
                mDatabase.child(mFirebaseUser.getUid()).child("mSelectedSubInterests").setValue(mSelectedSubInterests);
                SharedPrefs.setIsSubinterestsSelected("TRUE");
                startActivity(new Intent(getActivity().getApplicationContext(), NewMainActivity.class));
            }else{
                Toast.makeText(getActivity(), getString(R.string.no_inter), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
