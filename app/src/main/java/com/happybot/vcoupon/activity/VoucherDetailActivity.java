package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.dialog.ReceiveVoucherCodeDialog;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.fragment.MapFragment;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.PromotionRequestBody;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.happybot.vcoupon.util.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

public class VoucherDetailActivity extends BaseActivity {

    private Promotion promotion;

    private LinearLayout btnPinVoucher;
    private LinearLayout btnUnpinVoucher;
    private LinearLayout btnGetVoucher;

    private PinPromotionDelegate pinPromotionDelegate = null;
    private UnpinPromotionDelegate unpinPromotionDelegate = null;
    private ReceiveVoucherDelegate receiveVoucherDelegate = null;

    private PromotionRequestBody promotionRequestBody = null;
    private Voucher voucherPromotion;

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

        // ----------- Pin / Unpin / GetVoucher -----------
        btnPinVoucher = (LinearLayout) findViewById(R.id.btnPinVoucher);
        btnUnpinVoucher = (LinearLayout) findViewById(R.id.btnUnpinVoucher);
        btnGetVoucher = (LinearLayout) findViewById(R.id.btnGetVoucher);

        if (promotion.isPinned()) {
            //Toast.makeText(getApplicationContext(), "Pinned", Toast.LENGTH_SHORT).show();
            setViewUnpin();
        } else {
            //Toast.makeText(getApplicationContext(), "No pinned", Toast.LENGTH_SHORT).show();
            setViewPin();
        }

        if (promotion.isRegistered()) {
            //Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();
            btnGetVoucher.setBackgroundResource(R.drawable.selector_get_coupon_button);
            btnGetVoucher.setClickable(false);
            btnGetVoucher.setEnabled(false);
        }

        pinPromotionDelegate = new PinPromotionDelegate((BaseActivity) this);
        unpinPromotionDelegate = new UnpinPromotionDelegate((BaseActivity) this);
        receiveVoucherDelegate = new ReceiveVoucherDelegate((BaseActivity) this);

        promotionRequestBody = new PromotionRequestBody(promotion.getId());

        btnPinVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Click thành công!", Toast.LENGTH_SHORT).show();
                pinPromotion();
            }
        });

        btnUnpinVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Click thành công!", Toast.LENGTH_SHORT).show();
                unpinPromotion();
            }
        });

        btnGetVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), "Click thành công!", Toast.LENGTH_SHORT).show();

                receiveVoucher(promotion.getId());
            }
        });
    }

    private void setViewUnpin() {
        LinearLayout.LayoutParams paramsTv;

        paramsTv = (LinearLayout.LayoutParams) btnPinVoucher.getLayoutParams();
        paramsTv.weight = 0;
        paramsTv.leftMargin = 0;
        paramsTv.rightMargin = 0;
        btnPinVoucher.setLayoutParams(paramsTv);

        paramsTv = (LinearLayout.LayoutParams) btnUnpinVoucher.getLayoutParams();
        paramsTv.weight = 1;
        paramsTv.leftMargin = Math.round(pxFromDp(getApplicationContext(), 10));
        paramsTv.rightMargin = Math.round(pxFromDp(getApplicationContext(), 5));
        btnUnpinVoucher.setLayoutParams(paramsTv);
    }

    private void setViewPin() {
        LinearLayout.LayoutParams paramsTv;

        paramsTv = (LinearLayout.LayoutParams) btnUnpinVoucher.getLayoutParams();
        paramsTv.weight = 0;
        paramsTv.leftMargin = 0;
        paramsTv.rightMargin = 0;
        btnUnpinVoucher.setLayoutParams(paramsTv);

        paramsTv = (LinearLayout.LayoutParams) btnPinVoucher.getLayoutParams();
        paramsTv.weight = 1;
        paramsTv.leftMargin = Math.round(pxFromDp(getApplicationContext(), 10));
        paramsTv.rightMargin = Math.round(pxFromDp(getApplicationContext(), 5));
        btnPinVoucher.setLayoutParams(paramsTv);
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // PIN
    public void pinPromotion() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        UserRetrofitService userRetrofitService = new UserRetrofitService(getApplicationContext());
        userRetrofitService.pinPromotion(helper.getUserId(), 1, promotionRequestBody, pinPromotionDelegate);
    }

    private class PinPromotionDelegate extends ForegroundTaskDelegate<ResponseObject> {

        PinPromotionDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                Toast.makeText(getApplicationContext(), responseObject.getResultMessage(), Toast.LENGTH_LONG).show();

                setViewUnpin();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }

    // UNPIN
    public void unpinPromotion() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        UserRetrofitService userRetrofitService = new UserRetrofitService(getApplicationContext());
        userRetrofitService.unpinPromotion(helper.getUserId(), promotion.getId(), promotionRequestBody, unpinPromotionDelegate);
    }

    private class UnpinPromotionDelegate extends ForegroundTaskDelegate<ResponseObject> {

        UnpinPromotionDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                Toast.makeText(getApplicationContext(), responseObject.getResultMessage(), Toast.LENGTH_LONG).show();

                setViewPin();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }

    // RECEIVE VOUCHER
    public void receiveVoucher(String promotionId) {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        UserRetrofitService userRetrofitService = new UserRetrofitService(getApplicationContext());
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
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                voucherPromotion = voucher;

                btnGetVoucher.setBackgroundResource(R.drawable.selector_get_coupon_button);
                btnGetVoucher.setClickable(false);

                Bundle args = new Bundle();
                args.putString("nameVoucher", voucherPromotion.getVoucherCode());
                args.putString("qrCode", voucherPromotion.getQrCode());
                args.putString("address", promotion.getProvider().getAddress());
                args.putString("date", DateTimeConverter.getRemainTime(promotion.getStartDate()) + " - " +  DateTimeConverter.getRemainTime(promotion.getEndDate()));
                showReceiveVoucherCodeDialog(args);
            }

            // Show empty layout without any promotions
            // showVoucher(voucher);
        }
    }

    private void showVoucher(Voucher voucher) {

    }

    public void showReceiveVoucherCodeDialog(Bundle args) {
        DialogFragment dialog = new ReceiveVoucherCodeDialog();
        // Supply num input as an argument.
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }
}
