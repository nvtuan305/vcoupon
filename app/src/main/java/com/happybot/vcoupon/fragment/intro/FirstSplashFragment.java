package com.happybot.vcoupon.fragment.intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happybot.vcoupon.R;

/**
 * Created by Admin on 12/15/2016.
 */
public class FirstSplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_splash_page_2, container, false);

        return rootView;
    }
}
