package com.happybot.vcoupon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happybot.vcoupon.R;

/**
 * Created by Nguyễn Phương Tuấn on 12-Jan-17.
 */

public class ProviderManagerVoucherFragment extends Fragment {

    public ProviderManagerVoucherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_manager_voucher, container, false);


        return view;
    }
}