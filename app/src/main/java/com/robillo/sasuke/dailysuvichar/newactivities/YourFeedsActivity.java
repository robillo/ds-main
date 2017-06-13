package com.robillo.sasuke.dailysuvichar.newactivities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.newfragments.AllPhotosFragment;
import com.robillo.sasuke.dailysuvichar.newfragments.AllStatusFragment;
import com.robillo.sasuke.dailysuvichar.newfragments.AllVideosFragment;
import com.robillo.sasuke.dailysuvichar.view.adapter.ViewPagerAdapter;

public class YourFeedsActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_feeds);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragment = new AllStatusFragment();
        Bundle args = new Bundle();
        args.putString("from", "YOUR");
        fragment.setArguments(args);

        adapter.addFragment(fragment, getString(R.string.statuss));

        fragment = new AllPhotosFragment();
        fragment.setArguments(args);

        adapter.addFragment(fragment, getString(R.string.photoss));

        fragment = new AllVideosFragment();
        fragment.setArguments(args);

        adapter.addFragment(fragment, getString(R.string.videoss));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, NewMainActivity.class));
    }
}
