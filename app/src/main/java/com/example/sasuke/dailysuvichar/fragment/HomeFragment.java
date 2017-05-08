package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

        slide_down = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRvHome.setLayoutManager(mLayoutManager);
        mAdapter = new MultiTypeAdapter();
        mAdapter.register(Status.class, new StatusItemAdapter());
        mAdapter.register(Photo.class, new PhotoItemAdapter());
        mAdapter.register(Video.class, new VideoItemAdapter(getActivity()));
        mRvHome.setAdapter(mAdapter);

        Items items = new Items();

        Status status;
        Photo photo;
        Video video;

        status = new Status();
        status.setStatus("Watching bahubali 2 with Aditya Tyagi and 2 others at PVR.");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.astrology);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        status = new Status();
        status.setStatus("Playing cricket with Jatin Verma and 10 others.");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.ayurveda);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        status = new Status();
        status.setStatus("I got a laptop in my back pocket. :)");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.health);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        status = new Status();
        status.setStatus("LAKSHAY DEEP");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.yoga);
        items.add(photo);

        video = new Video("", "R_HNRK9t3lI", "");
        items.add(video);

        status = new Status();
        status.setStatus("ANMOL VARSHNEY");
        items.add(status);

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

        mRvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0) {
                    if (CHECK == 1) {
                        CHECK++;
                    } else {
//                        mRlMenu.startAnimation(slide_up);
                        mRlMenu.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mRlMenu.startAnimation(slide_down);
                    mRlMenu.setVisibility(View.VISIBLE);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

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
