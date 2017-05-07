package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Photo;
import com.example.sasuke.dailysuvichar.models.Status;
import com.example.sasuke.dailysuvichar.models.Video;
import com.example.sasuke.dailysuvichar.view.adapter.PhotoItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.StatusItemAdapter;
import com.example.sasuke.dailysuvichar.view.adapter.VideoItemAdapter;

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

        mRvHome.setLayoutManager(new LinearLayoutManager(getContext()));
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

        video = new Video("", "v6IAJOOmDMg", "");
        items.add(video);

        status = new Status();
        status.setStatus("Playing cricket with Jatin Verma and 10 others.");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.ayurveda);
        items.add(photo);

        video = new Video("", "NGLxoKOvzu4", "");
        items.add(video);

        status = new Status();
        status.setStatus("I got a laptop in my back pocket. :)");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.health);
        items.add(photo);

        video = new Video("", "JGwWNGJdvx8", "");
        items.add(video);

        status = new Status();
        status.setStatus("LAKSHAY DEEP");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.yoga);
        items.add(photo);

        video = new Video("", "papuvlVeZg8", "");
        items.add(video);

        status = new Status();
        status.setStatus("ANMOL VARSHNEY");
        items.add(status);

        photo = new Photo();
        photo.setPhoto(R.drawable.motivation);
        items.add(photo);

        video = new Video("", "weeI1G46q0o", "");
        items.add(video);

        photo = new Photo();
        photo.setPhoto(R.drawable.religion);
        items.add(photo);

        video = new Video("", "cHOrHGpL4u0", "");
        items.add(video);

        mAdapter.setItems(items);
        mAdapter.notifyDataSetChanged();
    }

}
