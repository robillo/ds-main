package com.example.sasuke.dailysuvichar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sasuke.dailysuvichar.R;

public class Tab extends LinearLayout {

    public ImageView mImageView;
    public TextView mTextView;

    public Tab(final Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.tab,
                0, 0);

        mTextView = new TextView(context);
        mImageView = new ImageView(context);

        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setPadding(0, 10, 0, 10);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1.0f;
        mImageView.setLayoutParams(layoutParams);

        layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        setPadding(0,10,0,10);
        layoutParams.weight = 2.0f;
        mTextView.setLayoutParams(layoutParams);
        mTextView.setGravity(Gravity.CENTER );

        addView(mImageView);
        addView(mTextView);

        try {
            mTextView.setText(a.getText(R.styleable.tab_name));
            mImageView.setImageDrawable(a.getDrawable(R.styleable.tab_image_drawable));
        } finally {
            a.recycle();
        }

    }
}
