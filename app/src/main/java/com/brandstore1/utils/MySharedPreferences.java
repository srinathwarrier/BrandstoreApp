package com.brandstore1.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by I076324 on 7/22/2015.
 */
public class MySharedPreferences {
    private static String PREFERENCES_FILE="BrandstoreApp";
    private static String PREFERENCES_USER_ID="userid";
    private static String PREFERENCES_USER_JSON_OBJECT="userjsonobject";
    private static String PREFERENCES_HAS_LOGGED_IN="hasLoggedIn";
    private static String PREFERENCES_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    public MySharedPreferences(){
        //fetch userId from SharedPreferences

    }

    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }
    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

    public static boolean readSharedSettingBoolean(Context ctx, String settingName, boolean defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(settingName, defaultValue);
    }
    public static void saveSharedSettingBoolean(Context ctx, String settingName, boolean settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(settingName, settingValue);
        editor.apply();
    }

    public static String getUserId(Context mContext ){
        // null is default value to return if userid doesn't exist
        return readSharedSetting(mContext, PREFERENCES_USER_ID, null);
    }

    public static void setUserId(Context mContext , String userId ){
        saveSharedSetting(mContext, PREFERENCES_USER_ID, userId);
    }

    public static String getUserJsonObjectString(Context mContext ){
        // null is default value to return if doesn't exist
        return readSharedSetting(mContext, PREFERENCES_USER_JSON_OBJECT, null);
    }

    public static void setUserJsonObjectString(Context mContext , String userJsonObjectString ){
        saveSharedSetting(mContext, PREFERENCES_USER_JSON_OBJECT, userJsonObjectString);
    }


    public static boolean getHasLoggedIn(Context mContext ){
        // false is default value to return if doesn't exist
        return readSharedSettingBoolean(mContext, PREFERENCES_HAS_LOGGED_IN, false);
    }

    public static void setHasLoggedIn(Context mContext , boolean hasLoggedIn){
        saveSharedSettingBoolean(mContext, PREFERENCES_HAS_LOGGED_IN, hasLoggedIn);
    }

    //
    public static String getUserLearnedDrawer(Context mContext ){
        // "false" is default value to return if it doesn't exist
        return readSharedSetting(mContext, PREFERENCES_USER_LEARNED_DRAWER, "false");
    }

    public static void setUserLearnedDrawer(Context mContext , String isUserLearnedDrawer ){
        saveSharedSetting(mContext, PREFERENCES_USER_LEARNED_DRAWER, isUserLearnedDrawer);
    }





}
