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
public class ProviderHomeFragment extends Fragment {

    public ProviderHomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_home, container, false);


        return view;
    }
}
