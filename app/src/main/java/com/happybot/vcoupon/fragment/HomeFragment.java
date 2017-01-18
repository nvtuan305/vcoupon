package com.happybot.vcoupon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.adapter.CustomSwipeAdapter;
import com.happybot.vcoupon.fragment.category.CategoryFragment;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Nguyễn Phương Tuấn on 05-Dec-16.
 */

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    RelativeLayout near_by, food, clothes, technology;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //set up slide show
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        adapter = new CustomSwipeAdapter(this.getActivity());
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        near_by = (RelativeLayout) view.findViewById(R.id.home_nearby);
        food = (RelativeLayout) view.findViewById(R.id.home_food);
        clothes = (RelativeLayout) view.findViewById(R.id.home_clothes);
        technology = (RelativeLayout) view.findViewById(R.id.home_technology);

        near_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("position", "0");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "home");
                ft.addToBackStack("home");
                ft.commit();
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("position", "1");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "food");
                ft.addToBackStack("food");
                ft.commit();
            }
        });

        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("position", "2");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "clothes");
                ft.addToBackStack("clothes");
                ft.commit();
            }
        });

        technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("position", "3");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "technology");
                ft.addToBackStack("technology");
                ft.commit();
            }
        });

        return view;
    }
}
