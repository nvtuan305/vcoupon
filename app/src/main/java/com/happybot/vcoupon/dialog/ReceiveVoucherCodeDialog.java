package com.happybot.vcoupon.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.adapter.SquareImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by Admin on 1/17/2017.
 */

public class ReceiveVoucherCodeDialog extends DialogFragment {

    ImageView imgVoucherCode;
    TextView tvNameVoucher;
    TextView tvAddress;
    TextView tvDate;
    Button btnSaveVoucher;

    LinearLayout loReceiveVoucher;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_receive_voucher_code, null);

        imgVoucherCode = (ImageView) view.findViewById(R.id.imgVoucherCode);
        tvNameVoucher = (TextView) view.findViewById(R.id.tvNameVoucher);
        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvDate = (TextView) view.findViewById(R.id.tvDate);
        btnSaveVoucher = (Button) view.findViewById(R.id.btnSaveVoucher);

        Picasso.with(getContext()).load(getArguments().getString("qrCode")).into(imgVoucherCode);
        tvNameVoucher.setText(getArguments().getString("nameVoucher"));
        tvAddress.setText(getArguments().getString("address"));
        tvDate.setText(getArguments().getString("date"));

        loReceiveVoucher = (LinearLayout) view.findViewById(R.id.loReceiveVoucher);

        btnSaveVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSaveVoucher.setVisibility(View.INVISIBLE);

                loReceiveVoucher.setDrawingCacheEnabled(true);
                Bitmap bitmap = loReceiveVoucher.getDrawingCache();

                btnSaveVoucher.setVisibility(View.VISIBLE);

                saveImage(bitmap, tvNameVoucher.getText().toString());
            }
        });

        builder.setView(view);
        return builder.create();
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {
        String fname = "Image-" + image_name + ".jpg";
        Log.i("LOAD", fname);
        MediaStore.Images.Media.insertImage(this.getContext().getContentResolver(), finalBitmap, fname , "QRCode" + image_name);
        btnSaveVoucher.setEnabled(false);
        btnSaveVoucher.setText("Đã lưu");
        btnSaveVoucher.setBackground(this.getResources().getDrawable(R.drawable.unselector_get_coupon_button));
    }
}

