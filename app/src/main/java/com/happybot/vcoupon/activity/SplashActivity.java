package com.happybot.vcoupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.util.SharePreferenceHelper;

public class SplashActivity extends AppCompatActivity {

    SharePreferenceHelper spHelper = null;
    Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize SharePreferenceHelper
        spHelper = new SharePreferenceHelper(this);

        // Initialize handler
        mHandler = new Handler();

        // Wait 3 seconds then go to the activity
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (spHelper != null) {
                    boolean isShownIntro = spHelper.getShowIntro();

                    // Check show intro
                    if (isShownIntro) {

                        // Check logged in
                        if (spHelper.isLoggedIn())
                            goToHome();
                        else
                            goToSignIn();

                    } else {
                        goToIntro();
                    }
                }
            }
        }, 3000);
    }

    public void goToHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToIntro() {
        Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSignIn() {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        // Remove all callback of handler
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }

        spHelper = null;
        super.onDestroy();
    }
}
