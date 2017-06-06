package com.example.sasuke.dailysuvichar.newactivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.SelectStatusActivity;
import com.example.sasuke.dailysuvichar.activity.SelectPhotoActivity;
import com.example.sasuke.dailysuvichar.activity.SelectVideoActivity;
import com.example.sasuke.dailysuvichar.newfragments.AllPhotosFragment;
import com.example.sasuke.dailysuvichar.newfragments.AllStatusFragment;
import com.example.sasuke.dailysuvichar.newfragments.AllVideosFragment;
import com.example.sasuke.dailysuvichar.utils.GooeyMenu;
import com.example.sasuke.dailysuvichar.view.adapter.ViewPagerAdapter;

import butterknife.ButterKnife;

public class NewExploreyActivity extends AppCompatActivity implements GooeyMenu.GooeyMenuInterface{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GooeyMenu mGooeyMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_explorey);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mGooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        mGooeyMenu.setOnMenuListener(this);

        menuClose();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment fragment = new AllStatusFragment();
        Bundle args = new Bundle();
        args.putString("from", "EXPLORE");
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
        Intent i = new Intent(this, NewMainActivity.class);
        i.putExtra("pageNumber", 2);
        startActivity(i);
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
            Intent i = new Intent(this, SelectStatusActivity.class);
            i.putExtra("from", 1);
            startActivity(i);
        }
        else if(menuNumber==2){
            //UPLOAD PHOTO
            Intent i = new Intent(this, SelectPhotoActivity.class);
            i.putExtra("from", 1);
            startActivity(i);
        }
        else if(menuNumber==3){
            //UPLOAD VIDEO
            Intent i = new Intent(this, SelectVideoActivity.class);
            i.putExtra("from", 1);
            startActivity(i);
        }
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
