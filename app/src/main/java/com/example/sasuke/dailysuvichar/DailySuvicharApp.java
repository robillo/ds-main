package com.example.sasuke.dailysuvichar;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.sasuke.dailysuvichar.activity.LoginActivity;
import com.example.sasuke.dailysuvichar.event.TokenExpiredEvent;
import com.facebook.FacebookSdk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class DailySuvicharApp extends Application {

    private static Context sContext;
    private static DailySuvicharApp instance;

    public static Context getAppContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sContext = getApplicationContext();
        EventBus.getDefault().register(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    @Subscribe
    public void onTokenExpired(TokenExpiredEvent event) {
        Intent intent = LoginActivity.newIntent(getAppContext());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public static boolean hasNetwork() {
        return instance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
