package com.happybot.vcoupon.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceHelper {
    private static final String SPName = "VCouponSharePreference";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER_ID = "user_id";


    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    public SharePreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void initializeSampleAuth() {
        saveUserId("5867d9c050fb07001111397b");
        saveAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ODY3ZDljMDUwZmIwNzAwMTExMTM5N2IiLCJwaG9uZU51bWJlciI6IjE5OSIsImlhdCI6MTQ4MzI1NjE3NiwiZXhwIjoxNDkxODk2MTc2fQ.ZtupyQcoUApFF3foGegT1l_CtPZX81vFJZYD5qZT3kw");
    }

    public void saveAccessToken(String accessToken) {
        if (editor != null) {
            editor.putString(ACCESS_TOKEN, accessToken);
            editor.commit();
        }
    }

    public String getAccessToken() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(ACCESS_TOKEN, "");
        }
        return "";
    }

    public void saveUserId(String userID) {
        if (editor != null) {
            editor.putString(USER_ID, userID);
            editor.commit();
        }
    }

    public String getUserId() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(USER_ID, "");
        }
        return "";
    }
}