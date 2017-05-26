package com.example.sasuke.dailysuvichar.newactivities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.newfragments.AllPhotosFragment;
import com.example.sasuke.dailysuvichar.newfragments.AllStatusFragment;
import com.example.sasuke.dailysuvichar.newfragments.AllVideosFragment;
import com.example.sasuke.dailysuvichar.view.adapter.ViewPagerAdapter;

public class ExploreActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllStatusFragment(), "Status");
        adapter.addFragment(new AllPhotosFragment(), "Photos");
        adapter.addFragment(new AllVideosFragment(), "Videos");
        viewPager.setAdapter(adapter);
    }
}
