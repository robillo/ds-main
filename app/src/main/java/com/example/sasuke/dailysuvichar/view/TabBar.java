package com.example.sasuke.dailysuvichar.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.example.sasuke.dailysuvichar.R;

/**
 * Created by Sasuke on 4/24/2017.
 */

public class TabBar extends LinearLayout implements View.OnClickListener {

    private int mBackgroundColor;
    private int mIconsColor;
    private int mSelectedTabColor;

    private float mTextSize;
    private float mCornerRadius;

    private Tab mSelectedTab;

    private OnItemSelect mOnItemSelect;
    private int mDefaultSelectedTab;

    public GestureDetector gestureDetector;

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        gestureDetector = new GestureDetector(context, new TabBar.GestureListener());
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.tabbar,
                0, 0);

        mTextSize = (int) a.getDimension(R.styleable.tabbar_text_size, 8) / getResources().getDisplayMetrics().density;

        mIconsColor = a.getColor(R.styleable.tabbar_icons_color, Color.BLUE);
        mBackgroundColor = a.getColor(R.styleable.tabbar_background_color, Color.WHITE);
        mSelectedTabColor = a.getColor(R.styleable.tabbar_select_color, Color.BLUE);
        mCornerRadius = a.getColor(R.styleable.tabbar_corner_radius, 0);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        for (int i = 0; i < getChildCount(); i++) {
            Tab tab = (Tab) getChildAt(i);
            tab.setBackgroundColor(mBackgroundColor);
            tab.mTextView.setTextSize(mTextSize);
            tab.mTextView.setTextColor(mIconsColor);
            tab.mImageView.setColorFilter(mIconsColor);
            tab.setOnClickListener(TabBar.this);
            if (mCornerRadius > 0) {
                if (i == 0) {
                    //set radius to left Corners
                }
                if (i == getChildCount()) {
                    //set radius to right Corners
                }
            }
        }
        setDefaultTabSelected();
    }


    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (mSelectedTab != null) {
            mSelectedTab.setBackgroundColor(mBackgroundColor);
            mSelectedTab.mImageView.setColorFilter(mIconsColor);
            mSelectedTab.mTextView.setTextColor(mIconsColor);
        }
        mSelectedTab = (Tab) view;
        mSelectedTab.setBackgroundColor(mSelectedTabColor);
        mSelectedTab.mTextView.setTextColor(Color.WHITE);
        mSelectedTab.mImageView.setColorFilter(Color.WHITE);
        if (mOnItemSelect != null)
            mOnItemSelect.onItemSelect(view.getId());
    }

    public void removeSelectListner() {
        mOnItemSelect = null;
    }


    public interface OnItemSelect {
        void onItemSelect(int tabId);
    }

    public void setOnItemSelectListner(OnItemSelect listner) {
        mOnItemSelect = listner;
        setDefaultTabSelected();
    }

    private void setDefaultTabSelected() {
        getChildAt(mDefaultSelectedTab).callOnClick();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mOnItemSelect = null;
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return gestureDetector.onTouchEvent(e);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

            return true;
        }
    }
}
