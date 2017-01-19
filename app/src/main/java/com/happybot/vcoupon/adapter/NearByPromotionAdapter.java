package com.happybot.vcoupon.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.dialog.ReceiveVoucherCodeDialog;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by don on 1/15/17.
 */

public class NearByPromotionAdapter extends PagerAdapter {

    List<Promotion> promotions;
    Context context;
    ImageView imvProviderAvatar;
    TextView tvTitle, tvDiscount, tvProviderAddress, tvCommentCount, tvPinnedCount, tvRemainTime;
    Button btnGetVoucher;
    private ReceiveVoucherDelegate receiveVoucherDelegate = null;
    private Voucher voucherPromotion;
    private Promotion currentPromotion;

    public NearByPromotionAdapter(Context context) {
        this.context = context;
        this.promotions = new ArrayList<>();
    }

    public NearByPromotionAdapter(Context context, List<Promotion> promotions) {
        this.context = context;
        this.promotions = promotions;
    }

    public void addData(List<Promotion> promotionList) {
        promotions.addAll(promotionList);
        notifyDataSetChanged();
    }

    public void updateData(List<Promotion> promotionList) {
        promotions.clear();
        promotions.addAll(promotionList);
        notifyDataSetChanged();
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return promotions.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
        currentPromotion = promotions.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_nearby_promotion, null);

        addControls(view);


        tvTitle.setText(currentPromotion.getTitle());
        tvCommentCount.setText(currentPromotion.getCommentCount() + "");
        tvPinnedCount.setText(currentPromotion.getPinnedCount() + "");

        String discountType = currentPromotion.getDiscountType();
        if (discountType.equals("VND"))
            tvDiscount.setText(currentPromotion.getDiscount() + "k");
        else
            tvDiscount.setText("-" + currentPromotion.getDiscount() + discountType);

        String remainTime = DateTimeConverter.getRemainTime(currentPromotion.getEndDate());
        tvRemainTime.setText(remainTime);

        Picasso.with(context)
                .load(currentPromotion.getProvider().getAvatar())
                .fit()
                .centerCrop()
                .into(imvProviderAvatar);
        tvProviderAddress.setText(currentPromotion.getProvider().getAddress());

        receiveVoucherDelegate = new ReceiveVoucherDelegate((BaseActivity) context);
        btnGetVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveVoucher(currentPromotion.getId());
            }
        });

        // Disable or enabled get voucher button
        if (DateTimeConverter.getCurrentDateInMillis() >= currentPromotion.getEndDate()
                || currentPromotion.isRegistered()) {
            setViewDisableRegister(btnGetVoucher);
        } else {
            setViewRegister(btnGetVoucher);
        }

        ((ViewPager) container).addView(view, position);
        return view;
    }

    void setViewDisableRegister(Button button) {
        button.setEnabled(false);
        button.setBackground(context.getResources().getDrawable(R.drawable.unselector_get_coupon_button));
    }

    void setViewRegister(Button button) {
        button.setEnabled(true);
        button.setBackground(context.getResources().getDrawable(R.drawable.selector_get_coupon_button));
    }

    public void receiveVoucher(String promotionId) {
        // Initialize auth info for testing
        UserRetrofitService userRetrofitService = new UserRetrofitService(context.getApplicationContext());
        userRetrofitService.receiveVoucher(promotionId, receiveVoucherDelegate);
    }

    private class ReceiveVoucherDelegate extends ForegroundTaskDelegate<Voucher> {

        ReceiveVoucherDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(Voucher voucher, Throwable throwable) {
            super.onPostExecute(voucher, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && voucher != null && shouldHandleResultForActivity()) {
                voucherPromotion = voucher;

                setViewDisableRegister(btnGetVoucher);

                Bundle args = new Bundle();
                args.putString("nameVoucher", voucherPromotion.getVoucherCode());
                args.putString("qrCode", voucherPromotion.getQrCode());
                args.putString("address", currentPromotion.getProvider().getAddress());
                args.putString("date", DateTimeConverter.getRemainTime(currentPromotion.getStartDate()) + " - " + DateTimeConverter.getRemainTime(currentPromotion.getEndDate()));
                showReceiveVoucherCodeDialog(args);
            }
        }

        public void showReceiveVoucherCodeDialog(Bundle args) {
            DialogFragment dialog = new ReceiveVoucherCodeDialog();
            // Supply num input as an argument.
            dialog.setArguments(args);
            dialog.show(((FragmentActivity)context).getSupportFragmentManager(), "NoticeDialogFragment");
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((View) obj);
    }

    void addControls(View view) {
        imvProviderAvatar = (ImageView) view.findViewById(R.id.imvProviderAvatar);
        tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
        tvCommentCount = (TextView) view.findViewById(R.id.tvCommentCount);
        tvPinnedCount = (TextView) view.findViewById(R.id.tvPinnedCount);
        tvProviderAddress = (TextView) view.findViewById(R.id.tvProviderAddress);
        tvRemainTime = (TextView) view.findViewById(R.id.tvRemainTime);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnGetVoucher = (Button) view.findViewById(R.id.btnGetVoucher);
    }
}

