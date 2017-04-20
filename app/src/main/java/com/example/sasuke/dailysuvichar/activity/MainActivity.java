package com.example.sasuke.dailysuvichar.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sasuke.dailysuvichar.R;

import java.util.Locale;

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
    private static final String TAG = "FORCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rb_eng)
    public void selectedEng(){
        Locale l= new Locale("en");
        Locale.setDefault(l);
        Configuration config = new Configuration();
        config.locale = l;
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(config, displayMetrics);
    }

    @OnClick(R.id.rb_hindi)
    public void selectedHindi(){
//        String locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayName();
//        Log.e(TAG, locale);
//        Log.e(TAG, getResources().getString(R.string.hindi_text));

        Locale l= new Locale("hi");
        Locale.setDefault(l);
        Configuration config = new Configuration();
        config.locale = l;
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        resources.updateConfiguration(config, displayMetrics);

//        locale = getApplicationContext().getResources().getConfiguration().locale.getDisplayName();
//        Log.e(TAG, locale);
//        Log.e(TAG, getResources().getString(R.string.hindi_text));
    }

    @OnClick(R.id.btn_next)
    public void openLoginActivity() {
        startActivity(ChooseInterestActivity.newIntent(this));
    }
}
