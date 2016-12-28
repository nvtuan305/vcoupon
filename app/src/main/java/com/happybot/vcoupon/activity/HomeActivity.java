package com.happybot.vcoupon.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.fragment.HomeFragment;
import com.happybot.vcoupon.fragment.NotificationFragment;
import com.happybot.vcoupon.fragment.ProfileFragment;
import com.happybot.vcoupon.fragment.VoucherDetailFragment;
import com.happybot.vcoupon.fragment.VoucherFragment;

public class HomeActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        //startActivity(new Intent(getApplicationContext(),HomeActivity.class));

        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

        // Initialize bottom navigation
        initializeBottomNavigation();
    }

    private void initializeBottomNavigation() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_dock_home_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Voucher", R.drawable.ic_dock_store_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Search", R.drawable.ic_dock_search_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Notification", R.drawable.ic_tab_bar_notification, R.color.colorUnSelected);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Profile", R.drawable.ic_dock_profile_whiteout, R.color.colorUnSelected);

        //add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Set background color
        bottomNavigation.setDefaultBackgroundResource(R.color.white);

        // Change colors default
        bottomNavigation.setAccentColor(ContextCompat.getColor(HomeActivity.this, R.color.colorSelected));
        bottomNavigation.setInactiveColor(ContextCompat.getColor(HomeActivity.this, R.color.colorUnSelected));


        //Manage titles
        //SHOW_WHEN_ACTIVE ALWAYS_SHOW
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // Set default item
        bottomNavigation.setCurrentItem(0);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.RED);

        // Add notification for each item, see more ahbottomnavigation.notification.AHNotification
        //  bottomNavigation.setNotification("3", 0);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        fragment = new HomeFragment();
                        break;
                    case 1:
                        fragment = new VoucherFragment();
                        break;
                    case 2:
                        fragment = new VoucherDetailFragment();
                        break;
                    case 3:
                        fragment = new NotificationFragment();
                        break;
                    case 4:
                        fragment = new ProfileFragment();
                        break;
                }
                clearBackStack();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

    public void clearBackStack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(0);
            getSupportFragmentManager().popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().executePendingTransactions();
        }
    }
}
