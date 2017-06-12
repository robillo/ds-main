package com.example.sasuke.dailysuvichar.newactivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.activity.ProfileActivity;
import com.example.sasuke.dailysuvichar.activity.SelectPhotoActivity;
import com.example.sasuke.dailysuvichar.activity.SelectStatusActivity;
import com.example.sasuke.dailysuvichar.activity.SelectVideoActivity;
import com.example.sasuke.dailysuvichar.newnewactivities.GuruActivity;
import com.example.sasuke.dailysuvichar.newnewfragments.CommonFragment;
import com.example.sasuke.dailysuvichar.newnewfragments.GuruFragment;
import com.example.sasuke.dailysuvichar.utils.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class NewMainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private String from;

    @BindView(R.id.conditional)
    LinearLayout conditional;
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
                    header.setText(R.string.home_header);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_guru:{
//                    header.setText(R.string.guru_header);
//                    addGuruFragment();
                    hideConditional();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(NewMainActivity.this, GuruActivity.class));
                        }
                    }, 200);
                    return true;
                }
                case R.id.navigation_explore:{
                    header.setText(R.string.explore_header);
                    from = getString(R.string.title_explore);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_your_feeds:{
                    header.setText(R.string.your_feeds_header);
                    from = getString(R.string.title_your_feeds);
                    addFragment(from);
                    showConditional();
                    return true;
                }
                case R.id.navigation_profile:{
//                    header.setText(R.string.profile_header);
                    hideConditional();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(NewMainActivity.this, ProfileActivity.class);
                            i.putExtra("fromHome", 1);
                            startActivity(i);
                        }
                    }, 200);
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

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        CommonFragment commonFragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putString("from", from);
        commonFragment.setArguments(args);

        fragmentTransaction.replace(R.id.fragment_container_new_main, commonFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addGuruFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        GuruFragment guruFragment = new GuruFragment();

        fragmentTransaction.replace(R.id.fragment_container_new_main, guruFragment);
//        fragmentTransaction.addToBackStack(null);
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

        from = getString(R.string.title_home);

        addFragment(getResources().getString(R.string.title_home));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if(SharedPrefs.getIsCoachmarkShowedOnetime()==null){
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(findViewById(R.id.navigation_home))
                    .setPrimaryText(getString(R.string.home))
                    .setSecondaryText(getString(R.string.homed))
                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                        @Override
                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                            new MaterialTapTargetPrompt.Builder(NewMainActivity.this)
                                    .setTarget(findViewById(R.id.navigation_explore))
                                    .setPrimaryText(getString(R.string.explore))
                                    .setSecondaryText(getString(R.string.explored))
                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                        @Override
                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                            new MaterialTapTargetPrompt.Builder(NewMainActivity.this)
                                                    .setTarget(findViewById(R.id.navigation_guru))
                                                    .setPrimaryText(getString(R.string.gurus))
                                                    .setSecondaryText(getString(R.string.gurud))
                                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                        @Override
                                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                            new MaterialTapTargetPrompt.Builder(NewMainActivity.this)
                                                                    .setTarget(findViewById(R.id.navigation_your_feeds))
                                                                    .setPrimaryText(getString(R.string.your_feedsss))
                                                                    .setSecondaryText(getString(R.string.see_what))
                                                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                        @Override
                                                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                            new MaterialTapTargetPrompt.Builder(NewMainActivity.this)
                                                                                    .setTarget(findViewById(R.id.navigation_profile))
                                                                                    .setPrimaryText(getString(R.string.profile))
                                                                                    .setSecondaryText(R.string.profile_desc)
                                                                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                                        @Override
                                                                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                                            new MaterialTapTargetPrompt.Builder(NewMainActivity.this)
                                                                                                    .setTarget(findViewById(R.id.action_language))
                                                                                                    .setPrimaryText("ROBILLO")
                                                                                                    .setSecondaryText("IS TESTING FOR PROMPT")
                                                                                                    .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener() {
                                                                                                        @Override
                                                                                                        public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                                                                                                            SharedPrefs.setIsCoachmarkShowedOnetime("TRUE");
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onHidePromptComplete() {

                                                                                                        }
                                                                                                    }).show();
                                                                                        }

                                                                                        @Override
                                                                                        public void onHidePromptComplete() {

                                                                                        }
                                                                                    }).show();
                                                                        }

                                                                        @Override
                                                                        public void onHidePromptComplete() {

                                                                        }
                                                                    }).show();

                                                        }

                                                        @Override
                                                        public void onHidePromptComplete() {

                                                        }
                                                    }).show();
                                        }

                                        @Override
                                        public void onHidePromptComplete() {

                                        }
                                    }).show();
                        }

                        @Override
                        public void onHidePromptComplete() {

                        }
                    }).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
//            finish();
            moveTaskToBack(true);
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
