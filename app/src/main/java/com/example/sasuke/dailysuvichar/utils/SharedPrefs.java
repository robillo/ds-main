package com.example.sasuke.dailysuvichar.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sasuke.dailysuvichar.DailySuvicharApp;

/**
 * Created by Sasuke on 4/27/2017.
 */

public class SharedPrefs {

    private static final String LOGIN_TOKEN = "login_token";

    private SharedPrefs() {
    }

    private static SharedPreferences getPrefrences() {
        Context context = DailySuvicharApp.getAppContext();
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static String getLoginToken() {
        return getPrefrences().getString(LOGIN_TOKEN, null);
    }

    public static void setLoginToken(String token) {
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(LOGIN_TOKEN, token);
        editor.commit();
    }
}

