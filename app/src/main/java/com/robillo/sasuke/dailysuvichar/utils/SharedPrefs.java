package com.robillo.sasuke.dailysuvichar.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.robillo.sasuke.dailysuvichar.DailySuvicharApp;

public class SharedPrefs {

    private static final String LOGIN_TOKEN = "login_token";
    private static final String FACEBOOK_TOKEN = "facebook_token";
    private static final String USER_TYPE = "STANDARD";
    private static final String IS_LOGGED_IN = "true_or_false_login";
    private static final String IS_PROFILE_SET = "true_or_false_profile";
    private static final String IS_INTERESTS_SELECTED = "true_or_false_interest";
    private static final String IS_SUBINTERESTS_SELECTED = "true_or_false_subinterest";
    private static  final String IS_LANGUAGE_SET = "true_or_false_languageset";
    private static final String IS_COACHMARK_SHOWED_ONETIME = "true_or_false_coachmark_done";

//    public static void setDefaults(){
//        if(getIsLoggedIn()==null){
//            setIsLoggedIn("FALSE");
//        }
//        if(getIsProfileSet()==null){
//            setIsProfileSet("FALSE");
//        }
//        if(getIsSubinterestsSelected()==null){
//            setIsSubinterestsSelected("FALSE");
//        }
//        if(getIsInterestsSelected()==null){
//            setIsInterestsSelected("FALSE");
//        }
//    }

    public static void setIsCoachmarkShowedOnetime(String token){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(IS_COACHMARK_SHOWED_ONETIME, token);
        editor.apply();
    }

    public static void setIsLanguageSet(String token){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(IS_LANGUAGE_SET, token);
        editor.apply();
    }

    public static void setIsLoggedIn(String token){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(IS_LOGGED_IN, token);
        editor.apply();
    }

    public static void setIsProfileSet(String token){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(IS_PROFILE_SET, token);
        editor.apply();
    }

    public static void setIsInterestsSelected(String token){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(IS_INTERESTS_SELECTED, token);
        editor.apply();
    }

    public static void setIsSubinterestsSelected(String token){
        SharedPreferences.Editor editor = getPrefrences().edit();
        editor.putString(IS_SUBINTERESTS_SELECTED, token);
        editor.apply();
    }

    public static String getIsCoachmarkShowedOnetime(){
        return getPrefrences().getString(IS_COACHMARK_SHOWED_ONETIME, null);
    }

    public static String getIsLoggedIn() {
        return getPrefrences().getString(IS_LOGGED_IN, null);
    }

    public static String getIsLanguageSet() {
        return getPrefrences().getString(IS_LANGUAGE_SET, null);
    }

    public static String getIsProfileSet() {
        return getPrefrences().getString(IS_PROFILE_SET, null);
    }

    public static String getIsInterestsSelected() {
        return getPrefrences().getString(IS_INTERESTS_SELECTED, null);
    }

    public static String getIsSubinterestsSelected() {
        return getPrefrences().getString(IS_SUBINTERESTS_SELECTED, null);
    }

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

