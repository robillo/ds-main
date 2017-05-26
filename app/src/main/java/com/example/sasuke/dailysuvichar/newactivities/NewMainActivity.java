package com.example.sasuke.dailysuvichar.newactivities;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.models.Feature;
import com.example.sasuke.dailysuvichar.newadapters.RVAFeature;
import com.example.sasuke.dailysuvichar.newfragments.PagerFragment;
import com.example.sasuke.dailysuvichar.utils.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class NewMainActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int intent_page;
    private int[] layouts;
    private RecyclerView recyclerView;
    private List<Feature> list;
    private String[] levels, levelHeaders, photoUrls;
    private static final int NUM_PAGES = 10;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        assignStringValues();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mPager = (ViewPager) findViewById(R.id.pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        layouts = new int[]{
                R.layout.fragment_pager,
                R.layout.fragment_pager,
                R.layout.fragment_pager};
        addBottomDots(0);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setClipToPadding(false);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.addOnPageChangeListener(viewPagerPageChangeListener);

        list = new ArrayList<>();
        list.add(new Feature("Your Twisters", "Use Your Imagination To Create Your Own Twisters.", "https://3.bp.blogspot.com/-FTKj7QUV61w/WJBgEaJclgI/AAAAAAAAAFw/dX-wb54JX-AYiDGPPB1Z3lvS7ZCoUNKBACLcB/s1600/ph6.png", 1));
        list.add(new Feature("Share App With Friends", "Enable Your Friends Gain Access To The Vast Collection Of Tongue Twisters.", "https://3.bp.blogspot.com/-FTKj7QUV61w/WJBgEaJclgI/AAAAAAAAAFw/dX-wb54JX-AYiDGPPB1Z3lvS7ZCoUNKBACLcB/s1600/ph6.png", 2));
        list.add(new Feature("Review App?", "Like the App? Or Do You Want an additional feature to be added to be app? Here's The Place You Seek.", "https://3.bp.blogspot.com/-FTKj7QUV61w/WJBgEaJclgI/AAAAAAAAAFw/dX-wb54JX-AYiDGPPB1Z3lvS7ZCoUNKBACLcB/s1600/ph6.png", 3));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new RVAFeature(getApplicationContext(), list));
    }

    private void assignStringValues(){
        levels = new String[]{"HOME", "Guru List", "Explore"};
        levelHeaders = new String[]{"A place where you get the feeds based on your Interests.",
        "See a List of Spiritual Leaders, their Profile and their Posts.",
        "See The Personalised Posts of Those you Follow."};
        photoUrls = new String[]{"https://2.bp.blogspot.com/-IY-IOkq2V5A/WJBfTUFfrTI/AAAAAAAAAFY/rurHHcN20r0EVurB8NFU3BB15B1wubJJgCLcB/s1600/placeholder.png",
        "https://1.bp.blogspot.com/-e3cvqWcBdhc/WJBgDvUM6BI/AAAAAAAAAFk/SOlb9rdvi7gm5iThWrCoH273_kn0rQo3gCLcB/s1600/ph2.png",
        "https://1.bp.blogspot.com/-OYwFn64dzV0/WJBfWvYaXaI/AAAAAAAAAFc/1egquqeEF30HrOgarJflAGp8fXqyBeKDQCLcB/s1600/ph4.jpg"};
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }


    @Override
    public void onBackPressed() {
        /*
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }*/
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:{
                    return assignFragment(0);
                }
                case 1:{
                    return assignFragment(1);
                }
                case 2:{
                    return assignFragment(2);
                }
                case 3:{
                    return assignFragment(3);
                }
                case 4:{
                    return assignFragment(4);
                }
                case 5:{
                    return assignFragment(5);
                }
                case 6:{
                    return assignFragment(6);
                }
                case 7:{
                    return assignFragment(7);
                }
                case 8:{
                    return assignFragment(8);
                }
                case 9:{
                    return assignFragment(9);
                }
                default:{
                    return assignFragment(0);
                }
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public float getPageWidth(int position) {
            return 1.0f;
        }

    }

    private Fragment assignFragment(int position){
        Fragment fragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putString("level", levels[position]);
        args.putString("levelHeader", levelHeaders[position]);
        args.putString("photoUrl", photoUrls[position]);
        args.putInt("levelNumber", position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
                //btnNext.setText(getString(R.string.start));
                //btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                //btnNext.setText(getString(R.string.next));
                //btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}
