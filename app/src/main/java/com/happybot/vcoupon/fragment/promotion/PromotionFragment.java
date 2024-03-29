package com.happybot.vcoupon.fragment.promotion;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happybot.vcoupon.R;

import java.util.ArrayList;
import java.util.List;

public class PromotionFragment extends Fragment {

    public PromotionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.voucher_viewpager);

        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.voucher_tabs);
        tabs.setBackgroundColor(Color.WHITE);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new ReceivedPromotionFragment(), getString(R.string.voucher_received_text));
        adapter.addFragment(new PinnedPromotionFragment(), getString(R.string.voucher_pinned_text));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}