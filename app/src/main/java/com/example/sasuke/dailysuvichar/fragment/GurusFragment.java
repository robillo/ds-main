package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Guru;
import com.example.sasuke.dailysuvichar.view.adapter.RVGuruAdapter;

import java.util.ArrayList;

import butterknife.BindView;

public class GurusFragment extends BaseFragment {

    private RVGuruAdapter mRvGuruAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private ArrayList<Guru> guruList;

    @BindView(R.id.recyclerview)
    public RecyclerView rv;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_feeds;
    }

    public static GurusFragment newInstance() {
        return new GurusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv.setLayoutManager(gridLayoutManager);

        guruList = new ArrayList<>();

        mRvGuruAdapter = new RVGuruAdapter(getActivity(), guruList);
        rv.setAdapter(mRvGuruAdapter);

        guruList.add(new Guru("Guru Robin",721));
        guruList.add(new Guru("Shankar Ji",210));
        guruList.add(new Guru("Baba Ramdev",4324));
        guruList.add(new Guru("Baba Afsal",251));
        guruList.add(new Guru("Baba ABC",321));
        guruList.add(new Guru("Guru Tagore",11));
        guruList.add(new Guru("Nafsar Guru",110));
        guruList.add(new Guru("Guru shiv",9));
        guruList.add(new Guru("Guru Kant",72));
        guruList.add(new Guru("Guru Narayan",121));

        return v;
    }
}
