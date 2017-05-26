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
import android.view.Menu;
import android.view.MenuItem;
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
    private String[] names, descriptions, photoUrls;
    private static final int NUM_PAGES = 3;
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
        list.add(new Feature("Your Feeds",
                "See What You Have Posted On Daily Suvichar So Far.",
                "https://3.bp.blogspot.com/-FTKj7QUV61w/WJBgEaJclgI/AAAAAAAAAFw/dX-wb54JX-AYiDGPPB1Z3lvS7ZCoUNKBACLcB/s1600/ph6.png", 1));
        list.add(new Feature("Your Profile",
                "See Data That You Have Provided Us.",
                "https://3.bp.blogspot.com/-FTKj7QUV61w/WJBgEaJclgI/AAAAAAAAAFw/dX-wb54JX-AYiDGPPB1Z3lvS7ZCoUNKBACLcB/s1600/ph6.png", 2));
        list.add(new Feature("Review App?",
                "Like the App? Or Do You Want an additional feature to be added to be app? Here's The Place You Seek.",
                "www.google.com", 3));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new RVAFeature(getApplicationContext(), list));
    }

    private void assignStringValues(){
        names = new String[]{"HOME", "Guru List", "Explore"};
        descriptions = new String[]{"A place where you get the feeds based on your Interests.",
        "See a List of Spiritual Leaders, their Profile and their Posts.",
        "See The Personalised Posts of Those you Follow."};
        photoUrls = new String[]{"https://1.bp.blogspot.com/-KOrgmRC8Nj0/WSh6_7z7ryI/AAAAAAAAASc/_O4UwRnmDsAZ2IFzIAh_eKXuVh9gti_CACK4B/s320/yoga2.jpg",
        "https://3.bp.blogspot.com/-O-kG5WpcVa8/WSh-Fh2m0_I/AAAAAAAAAS8/TwAT0D23cx0sQ0oCu_cKkPVL8MdAACtWgCK4B/s320/Buddha%2BSpiritual%2BBuddha%2B%2BAbstract%2B3D%2Band%2BCG%2BHD%2BDesktop%2BWallpaper.jpg",
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
        args.putString("level", names[position]);
        args.putString("levelHeader", descriptions[position]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.about: {

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
