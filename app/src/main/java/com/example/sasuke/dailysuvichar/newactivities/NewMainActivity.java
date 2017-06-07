package com.example.sasuke.dailysuvichar.newactivities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.AboutActivity;
import com.example.sasuke.dailysuvichar.activity.MainActivity;
import com.example.sasuke.dailysuvichar.activity.SelectPhotoActivity;
import com.example.sasuke.dailysuvichar.activity.SelectStatusActivity;
import com.example.sasuke.dailysuvichar.activity.SelectVideoActivity;
import com.example.sasuke.dailysuvichar.models.Feature;
import com.example.sasuke.dailysuvichar.newadapters.RVAFeature;
import com.example.sasuke.dailysuvichar.newfragments.PagerFragment;
import com.example.sasuke.dailysuvichar.newnewfragments.CommonFragment;
import com.example.sasuke.dailysuvichar.newnewfragments.GuruFragment;
import com.example.sasuke.dailysuvichar.utils.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewMainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private FragmentTransaction fragmentTransaction;
    private String from = "HOME";

    @BindView(R.id.conditional)
    LinearLayout conditional;
    @BindView(R.id.fragment_container)
    FrameLayout container;
    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.status_select)
    TextView status_select;
    @BindView(R.id.photos_select)
    TextView photos_select;
    @BindView(R.id.videos_select)
    TextView videos_select;

    @OnClick(R.id.status_select)
    public void setStatus_select(){
        Intent i = new Intent(this, SelectStatusActivity.class);
        i.putExtra("page", 1);
        startActivity(i);
    }

    @OnClick(R.id.photos_select)
    public void setPhotos_select(){
        Intent i = new Intent(this, SelectPhotoActivity.class);
        i.putExtra("page", 1);
        startActivity(i);
    }

    @OnClick(R.id.videos_select)
    public void setVideos_select(){
        Intent i = new Intent(this, SelectVideoActivity.class);
        i.putExtra("page", 1);
        startActivity(i);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:{
                    from = getString(R.string.title_home);
                    header.setText("Daily Suvichar : Home");
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_guru:{
                    header.setText("Daily Suvichar : Guru List");
                    addGuruFragment();
                    hideConditional();
                    return true;
                }
                case R.id.navigation_explore:{
                    header.setText("Daily Suvichar : Explore");
                    from = getString(R.string.title_explore);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_your_feeds:{
                    header.setText("Daily Suvichar : Your Feeds");
                    from = getString(R.string.title_your_feeds);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_profile:{
                    header.setText("Daily Suvichar : Profile");
                    hideConditional();
                    return true;
                }
            }
            return false;
        }

    };

    private void hideConditional(){
        if(conditional.getVisibility()== View.VISIBLE){
            conditional.setVisibility(View.INVISIBLE);
        }
    }
    private void showConditional(){
        if(conditional.getVisibility()== View.INVISIBLE){
            conditional.setVisibility(View.VISIBLE);
        }
    }

    private void addFragment(String from){
        CommonFragment commonFragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putString("from", from);
        commonFragment.setArguments(args);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, commonFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addGuruFragment(){
        GuruFragment guruFragment = new GuruFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, guruFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, NewMainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        addFragment(getResources().getString(R.string.title_home));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.click_back, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
