package com.robillo.sasuke.dailysuvichar.newactivities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.robillo.sasuke.dailysuvichar.R;
import com.robillo.sasuke.dailysuvichar.activity.SelectStatusActivity;
import com.robillo.sasuke.dailysuvichar.activity.SelectPhotoActivity;
import com.robillo.sasuke.dailysuvichar.activity.SelectVideoActivity;
import com.robillo.sasuke.dailysuvichar.newfragments.AllPhotosFragment;
import com.robillo.sasuke.dailysuvichar.newfragments.AllStatusFragment;
import com.robillo.sasuke.dailysuvichar.newfragments.AllVideosFragment;
import com.robillo.sasuke.dailysuvichar.utils.GooeyMenu;
import com.robillo.sasuke.dailysuvichar.view.adapter.ViewPagerAdapter;

public class NewHomeyActivity extends AppCompatActivity implements GooeyMenu.GooeyMenuInterface{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GooeyMenu mGooeyMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_homey);

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
        Fragment fragment = new AllStatusFragment();
        Bundle args = new Bundle();
        args.putString("from", "HOME");
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
            i.putExtra("from", 2);
            startActivity(i);
        }
        else if(menuNumber==2){
            //UPLOAD PHOTO
            Intent i = new Intent(this, SelectPhotoActivity.class);
            i.putExtra("from", 2);
            startActivity(i);
        }
        else if(menuNumber==3){
            //UPLOAD VIDEO
            Intent i = new Intent(this, SelectVideoActivity.class);
            i.putExtra("from", 2);
            startActivity(i);
        }
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
