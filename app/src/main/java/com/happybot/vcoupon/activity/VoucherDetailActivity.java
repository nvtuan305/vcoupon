package com.happybot.vcoupon.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.fragment.MapFragment;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.squareup.picasso.Picasso;

public class VoucherDetailActivity extends AppCompatActivity {

    private Promotion promotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        Intent intent = getIntent();
        promotion = intent.getParcelableExtra("DetailPromotion");

        TextView voucher_detail_title = (TextView)findViewById(R.id.voucher_detail_title);
        voucher_detail_title.setText(promotion.getTitle());

        TextView voucher_detail_provider_name = (TextView)findViewById(R.id.voucher_detail_provider_name);
        voucher_detail_provider_name.setText(promotion.getProvider().getName());

        TextView voucher_detail_time = (TextView)findViewById(R.id.voucher_detail_time);
        voucher_detail_time.setText(DateTimeConverter.getRemainTime(promotion.getStartDate()) + " - " +  DateTimeConverter.getRemainTime(promotion.getEndDate()));

        TextView voucher_detail_sale_percent = (TextView)findViewById(R.id.voucher_detail_sale_percent);
        voucher_detail_sale_percent.setText(promotion.getDiscount() + " " + promotion.getDiscountType());

        int curVoucher = promotion.getAmountLimit() - promotion.getAmountRegistered();
        TextView voucher_detail_number = (TextView)findViewById(R.id.voucher_detail_number);
        voucher_detail_number.setText("" + curVoucher);

        TextView voucher_detail_place_provider_name = (TextView)findViewById(R.id.voucher_detail_place_provider_name);
        voucher_detail_place_provider_name.setText(promotion.getProvider().getName());

        TextView voucher_detail_place_provider_address = (TextView)findViewById(R.id.voucher_detail_place_provider_address);
        voucher_detail_place_provider_address.setText(promotion.getProvider().getAddress());

        TextView voucher_detail_condition = (TextView)findViewById(R.id.voucher_detail_condition);
        voucher_detail_condition.setText(promotion.getCondition());

        ImageView voucher_detail_avatar = (ImageView)findViewById(R.id.voucher_detail_avatar);
        Picasso.with(getApplicationContext())
                .load(promotion.getProvider().getAvatar())
                .into(voucher_detail_avatar);

        ImageView imvProviderAvatar = (ImageView)findViewById(R.id.imvProviderAvatar);
        Picasso.with(getApplicationContext())
                .load(promotion.getProvider().getAvatar())
                .into(imvProviderAvatar);

        ImageView voucher_detail_background = (ImageView)findViewById(R.id.voucher_detail_background);
        Picasso.with(getApplicationContext())
                .load(promotion.getCover())
                .into(voucher_detail_background);

        MapFragment fragment = new MapFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mapLayout, fragment);
        fragmentTransaction.commit();
    }
}
