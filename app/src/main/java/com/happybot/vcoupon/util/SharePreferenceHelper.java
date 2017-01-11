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
        // Normal
        //saveUserId("5867d9c050fb07001111397b");
        //saveAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ODY3ZDljMDUwZmIwNzAwMTExMTM5N2IiLCJwaG9uZU51bWJlciI6IjE5OSIsImlhdCI6MTQ4MzI1NjE3NiwiZXhwIjoxNDkxODk2MTc2fQ.ZtupyQcoUApFF3foGegT1l_CtPZX81vFJZYD5qZT3kw");

        // Normal
        saveUserId("58525876b10aa019d8d66742");
        saveAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ODUyNTg3NmIxMGFhMDE5ZDhkNjY3NDIiLCJwaG9uZU51bWJlciI6IjA5Njg3MzAxODQiLCJpYXQiOjE0ODE3OTE2MDYsImV4cCI6MTQ5MDQzMTYwNn0.u1dUojW4l1iW-9red1nlhJphVtlskIyEnc3VYIgSJ7I");

        // Provider
        //saveUserId("585256f1b10aa019d8d66740");
        //saveAccessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ODUyNTZmMWIxMGFhMDE5ZDhkNjY3NDAiLCJwaG9uZU51bWJlciI6IjE4MDA4MDk5IiwiaWF0IjoxNDgxNzk4MDM0LCJleHAiOjE0OTA0MzgwMzR9.K2Iej37lysf3ddW5H6W2-O4AXOHhC6qO6VrojW-bvIg");

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