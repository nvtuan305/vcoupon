package com.happybot.vcoupon.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.fragment.EditProfileFragment;
import com.happybot.vcoupon.fragment.HomeFragment;
import com.happybot.vcoupon.fragment.NotificationFragment;
import com.happybot.vcoupon.fragment.ProfileFragment;
import com.happybot.vcoupon.fragment.ProviderAddVoucherFragment;
import com.happybot.vcoupon.fragment.ProviderHomeFragment;
import com.happybot.vcoupon.fragment.ProviderManagerVoucherFragment;
import com.happybot.vcoupon.fragment.search.SearchFragment;
import com.happybot.vcoupon.fragment.promotion.PromotionFragment;
import com.happybot.vcoupon.util.SharePreferenceHelper;

public class HomeActivity extends BaseActivity {

    private final String USER_ROLE_PROVIDER = "PROVIDER";
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private String userRole = null;
    private SharePreferenceHelper spHelper = null;
    private TextView titleBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get user role
        spHelper = new SharePreferenceHelper(this);
        userRole = spHelper.getUserRole();

        //set up toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        titleBar = (TextView) mainToolbar.findViewById(R.id.titleBar);
        setUpFontTitleBar();

        //startActivity(new Intent(getApplicationContext(),HomeActivity.class));

        if (userRole.equals(USER_ROLE_PROVIDER)) {
            fragment = new ProviderHomeFragment();
        } else {
            fragment = new HomeFragment();
        }

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

        if (userRole.equals(USER_ROLE_PROVIDER)) {
            // initialize BottomNavigation For provider User
            initializeBottomNavigationForProviderUser();
        } else {
            // initialize BottomNavigation For Normal User
            initializeBottomNavigationForNormalUser();
        }
    }

    private void setUpFontTitleBar() {
        if (titleBar != null) {
            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/font_axis_extra_bold.otf");
            titleBar.setTypeface(type);
        }
    }

    private void initializeBottomNavigationForNormalUser() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create normal_items
        AHBottomNavigationItem normal_item1 = new AHBottomNavigationItem("Home", R.drawable.ic_dock_home_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem normal_item2 = new AHBottomNavigationItem("Voucher", R.drawable.ic_dock_store_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem normal_item3 = new AHBottomNavigationItem("Search", R.drawable.ic_dock_search_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem normal_item4 = new AHBottomNavigationItem("Notification", R.drawable.ic_tab_bar_notification, R.color.colorUnSelected);
        AHBottomNavigationItem normal_item5 = new AHBottomNavigationItem("Profile", R.drawable.ic_dock_profile_whiteout, R.color.colorUnSelected);

        //add normal_items
        bottomNavigation.addItem(normal_item1);
        bottomNavigation.addItem(normal_item2);
        bottomNavigation.addItem(normal_item3);
        bottomNavigation.addItem(normal_item4);
        bottomNavigation.addItem(normal_item5);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        fragment = new HomeFragment();
                        getSupportActionBar().show();
                        break;
                    case 1:
                        fragment = new PromotionFragment();
                        getSupportActionBar().show();
                        break;
                    case 2:
                        fragment = new SearchFragment();
                        getSupportActionBar().hide();
                        break;
                    case 3:
                        fragment = new NotificationFragment();
                        getSupportActionBar().show();
                        break;
                    case 4:
                        fragment = new ProfileFragment();
                        getSupportActionBar().show();
                        break;
                }
                clearBackStack();
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.main_container, fragment).commit();
                return true;
            }
        });

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


        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });
    }

    private void initializeBottomNavigationForProviderUser() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        // Create normal_items
        AHBottomNavigationItem provider_item1 = new AHBottomNavigationItem("Home", R.drawable.ic_dock_home_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem provider_item2 = new AHBottomNavigationItem("Manager", R.drawable.ic_dock_provider_qr, R.color.colorUnSelected);
        AHBottomNavigationItem provider_item3 = new AHBottomNavigationItem("Add", R.drawable.ic_dock_provider_add, R.color.colorUnSelected);
        AHBottomNavigationItem provider_item4 = new AHBottomNavigationItem("Notification", R.drawable.ic_tab_bar_notification, R.color.colorUnSelected);
        AHBottomNavigationItem provider_item5 = new AHBottomNavigationItem("Profile", R.drawable.ic_dock_profile_whiteout, R.color.colorUnSelected);

        //add normal_items
        bottomNavigation.addItem(provider_item1);
        bottomNavigation.addItem(provider_item2);
        bottomNavigation.addItem(provider_item3);
        bottomNavigation.addItem(provider_item4);
        bottomNavigation.addItem(provider_item5);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        fragment = new ProviderHomeFragment();
                        break;
                    case 1:
                        fragment = new ProviderManagerVoucherFragment();
                        break;
                    case 2:
                        fragment = new ProviderAddVoucherFragment();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
