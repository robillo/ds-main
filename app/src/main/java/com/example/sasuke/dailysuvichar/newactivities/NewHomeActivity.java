package com.example.sasuke.dailysuvichar.newactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.HomeActivity;
import com.example.sasuke.dailysuvichar.newfragments.AllPhotosFragment;
import com.example.sasuke.dailysuvichar.newfragments.AllStatusFragment;
import com.example.sasuke.dailysuvichar.newfragments.AllVideosFragment;
import com.example.sasuke.dailysuvichar.utils.GooeyMenu;
import com.example.sasuke.dailysuvichar.view.adapter.ViewPagerAdapter;

import butterknife.ButterKnife;

public class NewHomeActivity extends AppCompatActivity implements GooeyMenu.GooeyMenuInterface{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GooeyMenu mGooeyMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        ButterKnife.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mGooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        mGooeyMenu.setOnMenuListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllStatusFragment(), "Status");
        adapter.addFragment(new AllPhotosFragment(), "Photos");
        adapter.addFragment(new AllVideosFragment(), "Videos");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, NewMainActivity.class));
    }

    @Override
    public void menuOpen() {

    }

    @Override
    public void menuClose() {
    }

    @Override
    public void menuItemClicked(int menuNumber) {
        if(menuNumber==1){
            //UPLOAD STATUS
            showToast("UPLOAD STATUS HERE");
        }
        else if(menuNumber==2){
            //UPLOAD PHOTO
            showToast("UPLOAD PHOTOS HERE");
        }
        else if(menuNumber==3){
            //UPLOAD VIDEO
            showToast("UPLOAD VIDEOS HERE");
        }
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
