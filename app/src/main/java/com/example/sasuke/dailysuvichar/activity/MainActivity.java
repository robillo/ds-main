package com.example.sasuke.dailysuvichar.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sasuke.dailysuvichar.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rg_language)
    RadioGroup mRgLanguage;
    @BindView(R.id.rb_eng)
    RadioButton mRbEnglish;
    @BindView(R.id.rb_hindi)
    RadioButton mRbHindi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_next)
    public void openLoginActivity() {
        startActivity(ChooseInterestActivity.newIntent(this));
    }
}
