package com.happybot.vcoupon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.dialog.RegisterSplashDialog;
import com.happybot.vcoupon.fragment.intro.FirstSplashFragment;
import com.happybot.vcoupon.fragment.intro.SecondSplashFragment;
import com.happybot.vcoupon.fragment.intro.ZeroSplashFragment;
import com.happybot.vcoupon.util.SharePreferenceHelper;

public class IntroActivity extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    Button btnSignup;
    Button btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new SplashAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        btnSignup = (Button) findViewById(R.id.btnSignUp);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterSplashDialog();
            }
        });

        btnSignin = (Button) findViewById(R.id.btnSignIn);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceHelper spHelper = new SharePreferenceHelper(getApplicationContext());
                spHelper.saveShowIntro(true);

                if (spHelper.isLoggedIn())
                    goToHome();
                else
                    goToSignIn();
            }
        });
    }

    public void goToHome() {
        Intent intent = new Intent(IntroActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToSignIn() {
        Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void showRegisterSplashDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new RegisterSplashDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class SplashAdapter extends FragmentStatePagerAdapter {
        public SplashAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1: {
                    return new FirstSplashFragment();
                }
                case 2: {
                    return new SecondSplashFragment();
                }
                default: {
                    return new ZeroSplashFragment();
                }
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}

