package com.happybot.vcoupon.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.happybot.vcoupon.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 05-Dec-16.
 */

public class SearchFragment extends Fragment {

    private EditText edittext_search;
    SearchFragment.Adapter adapter;
    SearchProviderFragment searchProviderFragment = new SearchProviderFragment();
    SearchPromotionFragment searchPromotionFragment = new SearchPromotionFragment();
    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //adapter
        adapter = new SearchFragment.Adapter(getChildFragmentManager());
        adapter.addFragment(searchProviderFragment, getString(R.string.search_tab_title_1));
        adapter.addFragment(searchPromotionFragment, getString(R.string.search_tab_title_2));

        //view pager
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.search_viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabs = (TabLayout) view.findViewById(R.id.search_tabs);
        tabs.setBackgroundColor(Color.WHITE);
        tabs.setupWithViewPager(viewPager);

        //click search in keyboard
        edittext_search = (EditText) view.findViewById(R.id.edittext_search);
        edittext_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //hide keyboard
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(edittext_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //update query
                    searchProviderFragment.updateSearch(v.getText().toString());
                    searchPromotionFragment.updateSearch(v.getText().toString());
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private FragmentManager mFragmentManager;


        public Adapter(FragmentManager manager) {
            super(manager);
            mFragmentManager = manager;
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


