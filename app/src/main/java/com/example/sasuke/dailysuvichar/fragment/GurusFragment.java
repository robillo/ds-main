package com.example.sasuke.dailysuvichar.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        return R.layout.fragment_gurus;
    }

    public static GurusFragment newInstance() {
        return new GurusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setHasOptionsMenu(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv.setLayoutManager(gridLayoutManager);

        guruList = new ArrayList<>();

        mRvGuruAdapter = new RVGuruAdapter(getActivity(), guruList);
        rv.setAdapter(mRvGuruAdapter);

        guruList.add(new Guru("Guru Robin", 721));
        guruList.add(new Guru("Shankar Ji", 210));
        guruList.add(new Guru("Baba Ramdev", 4324));
        guruList.add(new Guru("Baba Afsal", 251));
        guruList.add(new Guru("Baba ABC", 321));
        guruList.add(new Guru("Guru Tagore", 11));
        guruList.add(new Guru("Nafsar Guru", 110));
        guruList.add(new Guru("Guru shiv", 9));
        guruList.add(new Guru("Guru Kant", 72));
        guruList.add(new Guru("Guru Narayan", 121));

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search: {

                break;
            }
            case R.id.action_sort: {
                showPopup();
                break;
            }
            case R.id.action_settings: {

                break;
            }
            case R.id.move_to_first: {

            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Sorting Option:")
                .setItems(new CharSequence[]{"Followers", "Astrology Gurus", "Yoga Gurus", "Pandits",
                "Motivation Gurus", "Ayurveda Gurus"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0: {

                                break;
                            }
                            case 1: {

                                break;
                            }
                            case 2: {

                                break;
                            }
                            case 3: {

                                break;
                            }
                            case 4: {

                                break;
                            }
                            case 5: {

                                break;
                            }
                            case 6: {

                                break;
                            }
                        }
                    }
                });
        builder.show();
    }
}