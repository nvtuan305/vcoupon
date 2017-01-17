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

/**
 * Created by Admin on 1/17/2017.
 */

public class ReceiveVoucherCodeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_receive_voucher_code, null);

        builder.setView(v);
        return builder.create();
    }
}

