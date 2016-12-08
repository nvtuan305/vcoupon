package com.happybot.vcoupon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.adapter.CustomSwipeAdapter;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Nguyễn Phương Tuấn on 05-Dec-16.
 */

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    GridView gridView;

    static final String[] CATEGORY = new String[] {
            "Quanh đây", "Ăn uống","Quần áo", "Công nghệ" };

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        adapter = new CustomSwipeAdapter(this.getActivity());
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        return view;
    }

}
