package com.happybot.vcoupon.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceHelper {
    private static final String SPName = "VCouponSharePreference";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER_ID = "user_id";
    private static final String SHOW_INTRO = "show_intro";
    private static final String USER_ROLE = "user_role";


    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    public SharePreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SPName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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

    public void saveShowIntro(boolean isShown) {
        if (editor != null) {
            editor.putBoolean(SHOW_INTRO, isShown);
            editor.commit();
        }
    }

    public boolean getShowIntro() {
        return sharedPreferences != null && sharedPreferences.getBoolean(SHOW_INTRO, false);
    }

    public boolean isLoggedIn() {
        String accessToken = getAccessToken();
        String userId = getUserId();

        return !accessToken.equals("") && !userId.equals("");
    }

    public void saveUserRole(String role) {
        if (editor != null) {
            editor.putString(USER_ROLE, role);
            editor.commit();
        }
    }

    public String getUserRole() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(USER_ROLE, USER_ROLE);
        }
        return USER_ROLE;
    }
}