package com.example.sasuke.dailysuvichar.newnewfragments;


import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.AboutActivity;

import java.util.Locale;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommonFragment extends Fragment {

    public CommonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_common, container, false);

        setHasOptionsMenu(true);

        String from = getArguments().getString("from");

        if(from.equals(getString(R.string.title_home))){
            Log.e("FROM", from);
        }
        else if(from.equals(getString(R.string.title_explore))){
            Log.e("FROM", from);
        }
        else if(from.equals(getString(R.string.title_your_feeds))){
            Log.e("FROM", from);
        }

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_common, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_language: {
                new MaterialDialog.Builder(getActivity().getApplicationContext())
                        .title(R.string.choose_lang)
                        .items(new String[]{getString(R.string.english_lang), getString(R.string.hindi_lang)})
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which){
                                    case 0:{
                                        //FETCH ENGLIH FEEDS
                                        break;
                                    }
                                    case 1:{
                                        //FETCH HINDI FEEDS
                                        break;
                                    }
                                }
                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .show();
                break;
            }
            case R.id.about: {
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
