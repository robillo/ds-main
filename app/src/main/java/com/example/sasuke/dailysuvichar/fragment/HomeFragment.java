package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.event.DoubleTabEvent;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.models.Video;
import com.example.sasuke.dailysuvichar.view.adapter.PhotoItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.StatusItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.VideoItemAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import butterknife.BindView;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by Sasuke on 4/30/2017.
 */

public class HomeFragment extends BaseFragment {

    private MultiTypeAdapter mAdapter;

    @BindView(R.id.rv_home)
    RecyclerView mRvHome;
    @BindView(R.id.rl_menu)
    RelativeLayout mRlMenu;

    private LinearLayoutManager mLayoutManager;
    private FirebaseUser mFirebaseUser;
    private String uid;
    private HashMap<String, String> userStatus;
    private DatabaseReference mDatabaseReference;
    private static  final String TAG = "ROBILLO", STATUS = "status";

    private Animation slide_down;
    private Animation slide_up;
    private int CHECK = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = mFirebaseUser.getUid();

        Log.e(TAG, uid);

        slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Status.class, new StatusItemAdapter());
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mAdapter.register(Video.class, new VideoItemAdapter(getActivity()));
        mRvHome.setAdapter(mAdapter);

        Items items = new Items();
        HashMap<String, Status> statuses = new HashMap<>();
        Status status;


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Log.e(TAG, uid);
        Log.e(TAG, mDatabaseReference.toString());

        Photo photo;
        Video video;

        status = new Status();
        status.setStatus("Watching bahubali 2 with Aditya Tyagi and 2 others at PVR.");
        mDatabaseReference.child("users").child(uid).child("status").push().setValue(status);
        statuses.put(mFirebaseUser.getEmail(), status);
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.astrology);
        items.add(photo);

        video = new Video("", "-2eiKIUyTKk", "");
        items.add(video);

        status = new Status();
        status.setStatus("ROBILLO");
        statuses.put(mFirebaseUser.getEmail(), status);
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.ayurveda);
        items.add(photo);

        video = new Video("", "-2eiKIUyTKk", "");
        items.add(video);

        status = new Status();
        status.setStatus("I got a laptop in my back pocket. :) Playing cricket with Jatin Verma and 10 others.");
        mDatabaseReference.child("users").child(uid).child("status").push().setValue(status);
        statuses.put(mFirebaseUser.getEmail(), status);
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.health);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        status = new Status();
        status.setStatus("ROBILLO is the username for ROBIN");
        mDatabaseReference.child("users").child(uid).child("status").push().setValue(status);
        statuses.put(mFirebaseUser.getEmail(), status);
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.yoga);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        photo = new Photo();
        photo.setPhoto(R.drawable.motivation);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        photo = new Photo();
        photo.setPhoto(R.drawable.religion);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();

//        mRvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0 || dy < 0) {
//                    if (CHECK == 1) {
//                        CHECK++;
//                    } else {
////                        mRlMenu.startAnimation(slide_up);
//                        mRlMenu.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    mRlMenu.startAnimation(slide_down);
//                    mRlMenu.setVisibility(View.VISIBLE);
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DoubleTabEvent event) {
        mRvHome.getLayoutManager().scrollToPosition(1);
    }

}
