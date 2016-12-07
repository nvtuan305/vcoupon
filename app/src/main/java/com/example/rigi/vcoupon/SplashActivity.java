package com.example.rigi.vcoupon;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.example.rigi.vcoupon.dialog.RegisterSplashDialog;
import com.example.rigi.vcoupon.fragment.FirstSplashFragment;
import com.example.rigi.vcoupon.fragment.SecondSplashFragment;
import com.example.rigi.vcoupon.fragment.ZeroSpashFragment;

public class SplashActivity extends FragmentActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new SplashAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterSplashDialog();
            }
        });
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
                    return new ZeroSpashFragment();
                }
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
