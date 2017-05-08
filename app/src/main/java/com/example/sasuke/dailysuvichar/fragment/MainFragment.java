package com.example.sasuke.dailysuvichar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.sasuke.dailysuvichar.R;
import com.example.sasuke.dailysuvichar.event.DoubleTabEvent;
import com.example.sasuke.dailysuvichar.view.TabBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Sasuke on 4/19/2017.
 */

public class MainFragment extends BaseFragment implements TabBar.OnItemSelect {

    @BindView(R.id.tab_bar)
    TabBar tabBar;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main;
    }

    public static MainFragment newInstance()

    {
        return new MainFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabBar.setOnItemSelectListner(MainFragment.this);
        tabBar.gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(getContext(), "DOUBLE TAP ON TAB BAR", Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new DoubleTabEvent());
                return true;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Toast.makeText(getContext(), "DOUBLE TAP EVENT", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onItemSelect(int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                replaceFragment(HomeFragment.newInstance(), HomeFragment.class.getName());
                break;
            case R.id.tab_gurus:
                replaceFragment(GurusFragment.newInstance(), GurusFragment.class.getName());
                break;
            case R.id.tab_trending:
                replaceFragment(TrendingFragment.newInstance(), TrendingFragment.class.getName());
                break;
//            case R.id.tab_notification:
//                replaceFragment(NotificationFragment.newInstance(), NotificationFragment.class.getName());
//                break;
        }
    }


    private void replaceFragment(final Fragment fragment, final String tag) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f != null) {
                    transaction.hide(f);
                }
            }
        }
        Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            f = fragment;
            transaction.add(R.id.child_fragment_container, f, tag);
        } else {
            transaction.show(f);
        }
        transaction.commit();
    }
}
