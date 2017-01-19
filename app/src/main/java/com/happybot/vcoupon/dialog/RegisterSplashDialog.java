package com.happybot.vcoupon.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.SignUpNormalUserActivity;
import com.happybot.vcoupon.activity.SignUpProviderActivity;

public class RegisterSplashDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_splash_register, null);

        Button btnProvider = (Button) v.findViewById(R.id.btnProvider);
        btnProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpProvider();
            }
        });

        Button btnNormalUser = (Button) v.findViewById(R.id.btnNormalUser);
        btnNormalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUp();
            }
        });

        builder.setView(v);
        return builder.create();
    }

    public void goToSignUp() {
        Intent intent = new Intent(this.getActivity(), SignUpNormalUserActivity.class);
        startActivity(intent);
    }

    public void goToSignUpProvider() {
        Intent intent = new Intent(this.getActivity(), SignUpProviderActivity.class);
        startActivity(intent);
    }
}

