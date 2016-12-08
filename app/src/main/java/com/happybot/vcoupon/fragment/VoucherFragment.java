package com.happybot.vcoupon.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.happybot.vcoupon.R;

/**
 * Created by Nguyễn Phương Tuấn on 05-Dec-16.
 */

public class VoucherFragment extends Fragment {

    public VoucherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.coupon_item, container, false);


        return view;
    }

}


