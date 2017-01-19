package com.happybot.vcoupon.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.fragment.HomeFragment;
import com.happybot.vcoupon.fragment.NotificationFragment;
import com.happybot.vcoupon.fragment.profile.ProfileFragment;
import com.happybot.vcoupon.fragment.ProviderAddVoucherFragment;
import com.happybot.vcoupon.fragment.ProviderManagerVoucherFragment;
import com.happybot.vcoupon.fragment.promotion.PromotionFragment;
import com.happybot.vcoupon.fragment.search.SearchFragment;
import com.happybot.vcoupon.util.Constants;
import com.happybot.vcoupon.util.FCMNotification;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private final String USER_ROLE_PROVIDER = "PROVIDER";

    private String userRole = null;
    private SharePreferenceHelper spHelper = null;

    private TextView titleBar = null;
    private AHBottomNavigation bottomNavigation = null;

    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FCMNotification fcmNotification = new FCMNotification(this);
        fcmNotification.updateSubscribeFCMTopic();

        // get user role
        spHelper = new SharePreferenceHelper(this);
        userRole = spHelper.getUserRole();

        //set up toolbar
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        titleBar = (TextView) mainToolbar.findViewById(R.id.titleBar);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        setUpFontTitleBar();

        // Initialize home
        initHomeFragment();

        initializeBottomNavigation();

        Constants.RS_PERM = checkPermissions();
    }

    private void initHomeFragment() {
        /*if (userRole.equals(USER_ROLE_PROVIDER)) {
            homeFrag = new ProviderHomeFragment();

        } else {
            homeFrag = new HomeFragment();
        }*/

        fragment = new HomeFragment();
    }

    private void setUpFontTitleBar() {
        if (titleBar != null) {
            Typeface type = Typeface.createFromAsset(getAssets(), "fonts/font_axis_extra_bold.otf");
            titleBar.setTypeface(type);
        }
    }

    private void initializeBottomNavigation() {
        // Create normal_items
        AHBottomNavigationItem normal_item1 = new AHBottomNavigationItem("Home", R.drawable.ic_dock_home_whiteout, R.color.colorUnSelected);
        AHBottomNavigationItem normal_item4 = new AHBottomNavigationItem("Notification", R.drawable.ic_tab_bar_notification, R.color.colorUnSelected);
        AHBottomNavigationItem normal_item5 = new AHBottomNavigationItem("Profile", R.drawable.ic_dock_profile_whiteout, R.color.colorUnSelected);

        AHBottomNavigationItem normal_item2 = null;
        AHBottomNavigationItem normal_item3 = null;

        if (userRole.equals(USER_ROLE_PROVIDER)) {
            normal_item2 = new AHBottomNavigationItem("Manager", R.drawable.ic_dock_provider_qr, R.color.colorUnSelected);
            normal_item3 = new AHBottomNavigationItem("Search", R.drawable.ic_add_green, R.color.colorUnSelected);
        } else {
            normal_item2 = new AHBottomNavigationItem("Voucher", R.drawable.ic_dock_store_whiteout, R.color.colorUnSelected);
            normal_item3 = new AHBottomNavigationItem("Search", R.drawable.ic_dock_search_whiteout, R.color.colorUnSelected);
        }

        //add normal_items
        bottomNavigation.addItem(normal_item1);
        bottomNavigation.addItem(normal_item2);
        bottomNavigation.addItem(normal_item3);
        bottomNavigation.addItem(normal_item4);
        bottomNavigation.addItem(normal_item5);

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
        changeFragment(fragment, bottomNavigation.getItem(0).getTitle(this));

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.RED);

        // Add notification for each item, see more ahbottomnavigation.notification.AHNotification
        bottomNavigation.setNotification("3", 3);

        // Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                String tag = bottomNavigation.getItem(position).getTitle(HomeActivity.this);

                switch (position) {
                    case 0:
                        fragment = new HomeFragment();
                        break;

                    case 1:
                        if (userRole.equals(USER_ROLE_PROVIDER)) {
                            fragment = new ProviderManagerVoucherFragment();
                        } else {
                            fragment = new PromotionFragment();
                        }
                        break;

                    case 2:
                        if (userRole.equals(USER_ROLE_PROVIDER)) {
                            fragment = new ProviderAddVoucherFragment();
                        } else {
                            fragment = new SearchFragment();
                        }
                        break;

                    case 3:
                        fragment = new NotificationFragment();
                        break;

                    case 4:
                        fragment = new ProfileFragment();
                        break;
                }

                if (fragment != null) {
                    clearBackStack();
                    changeFragment(fragment, tag);

                    if (position == 2)
                        getSupportActionBar().hide();
                    else
                        getSupportActionBar().show();
                }

                return true;
            }
        });
    }

    public void changeFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_in_right);
        transaction.replace(R.id.main_container, fragment);
        //transaction.addToBackStack(tag);
        transaction.commit();
    }

    // Check all permission
    public boolean checkPermissions() {
        String[] requestPermission = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        List<String> permissionNeedRequest = new ArrayList<>();
        int rs;

        for (String permission : requestPermission) {
            Log.d("CheckPermission", "Granted = " + false);
            rs = ContextCompat.checkSelfPermission(this, permission);
            if (rs != PackageManager.PERMISSION_GRANTED)
                permissionNeedRequest.add(permission);
        }

        if (!permissionNeedRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionNeedRequest.toArray(new String[permissionNeedRequest.size()]),
                    Constants.RC_HANDLE_ALL_PERM);
            return false;
        }

        return true;
    }

    // Handle result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case Constants.RC_HANDLE_ALL_PERM:
                Constants.RS_PERM = grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;

            default:
                break;
        }
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
