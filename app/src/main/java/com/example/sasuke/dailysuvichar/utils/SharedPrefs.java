package com.example.sasuke.dailysuvichar.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sasuke.dailysuvichar.DailySuvicharApp;

public class SharedPrefs {

    private static final String LOGIN_TOKEN = "login_token";
    private static final String FACEBOOK_TOKEN = "facebook_token";
    private static final String USER_TYPE = "STANDARD";

    private SharedPrefs() {
    }

    public static String getFacebookToken() {
        return getPrefrences().getString(FACEBOOK_TOKEN, null);
    }

    public static void setFacebookToken(String token){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(FACEBOOK_TOKEN, token);
        editor.apply();
    }

    private static SharedPreferences getPrefrences() {
        Context context = DailySuvicharApp.getAppContext();
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static String getLoginToken() {
        return getPrefrences().getString(LOGIN_TOKEN, null);
    }

    public static String getUserType() {
        return getPrefrences().getString(USER_TYPE, null);
    }

    public static void setUserType(String userType){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(USER_TYPE, userType);
        editor.apply();
    }

    public static void setLoginToken(String token) {
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(LOGIN_TOKEN, token);
        editor.apply();
    }

    public static void clearLoggedInUser() {
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.remove(LOGIN_TOKEN);
        editor.apply();
    }
}

