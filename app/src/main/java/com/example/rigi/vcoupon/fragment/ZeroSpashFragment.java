package com.example.rigi.vcoupon.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rigi.vcoupon.R;
import com.example.rigi.vcoupon.dialog.RegisterSplashDialog;

/**
 * Created by Rigi on 12/5/2016.
 */
public class ZeroSpashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_splash_zero, container, false);

        return rootView;
    }
}
