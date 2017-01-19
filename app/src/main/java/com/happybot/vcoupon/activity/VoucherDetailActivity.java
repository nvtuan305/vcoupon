package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.adapter.CommentAdapter;
import com.happybot.vcoupon.dialog.ReceiveVoucherCodeDialog;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.fragment.MapFragment;
import com.happybot.vcoupon.model.Address;
import com.happybot.vcoupon.model.Comment;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.PromotionRequestBody;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.model.retrofit.CommentBody;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.PromotionRetrofitService;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.happybot.vcoupon.util.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VoucherDetailActivity extends BaseActivity {

    private Promotion promotion;

    private LinearLayout btnPinVoucher;
    private LinearLayout btnUnpinVoucher;
    private LinearLayout btnGetVoucher;
    private TextView tvBtnGetVoucher;

    private PinPromotionDelegate pinPromotionDelegate = null;
    private UnpinPromotionDelegate unpinPromotionDelegate = null;
    private ReceiveVoucherDelegate receiveVoucherDelegate = null;

    private PromotionRequestBody promotionRequestBody = null;
    private Voucher voucherPromotion;

    private CommentAdapter adapter = new CommentAdapter();
    private LinearLayoutManager mLinearLayoutManager = null;
    private RecyclerView voucher_detail_comment_recyclerview = null;
    private PromotionRetrofitService promotionRetrofitService;
    private PostCommentDelegate postCommentDelegate;
    private EditText voucher_detail_edittext_new_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_detail);

        Intent intent = getIntent();
        promotion = intent.getParcelableExtra("DetailPromotion");

        TextView voucher_detail_title = (TextView) findViewById(R.id.voucher_detail_title);
        voucher_detail_title.setText(promotion.getTitle());

        TextView voucher_detail_provider_name = (TextView) findViewById(R.id.voucher_detail_provider_name);
        voucher_detail_provider_name.setText(promotion.getProvider().getName());

        TextView voucher_detail_time = (TextView) findViewById(R.id.voucher_detail_time);
        voucher_detail_time.setText(" " + DateTimeConverter.getDate(promotion.getStartDate()) + " - " + DateTimeConverter.getDate(promotion.getEndDate()));

        TextView voucher_detail_sale_percent = (TextView) findViewById(R.id.voucher_detail_sale_percent);
        voucher_detail_sale_percent.setText(" " + promotion.getDiscount() + " " + promotion.getDiscountType());

        int curVoucher = promotion.getAmountLimit() - promotion.getAmountRegistered();
        TextView voucher_detail_number = (TextView) findViewById(R.id.voucher_detail_number);
        voucher_detail_number.setText(" " + curVoucher);

        // Set address
        TextView tvAddresses = (TextView) findViewById(R.id.tvAddresses);
        setAddress(tvAddresses);

        // Set provider rating
        TextView tvProviderRating = (TextView) findViewById(R.id.tvProviderRating);
        tvProviderRating.setText(promotion.getProvider().getRating() + "");

        TextView voucher_detail_condition = (TextView) findViewById(R.id.voucher_detail_condition);
        voucher_detail_condition.setText(promotion.getCondition());

        ImageView voucher_detail_avatar = (ImageView) findViewById(R.id.voucher_detail_avatar);
        Picasso.with(getApplicationContext())
                .load(promotion.getProvider().getAvatar())
                .into(voucher_detail_avatar);

        ImageView voucher_detail_background = (ImageView) findViewById(R.id.voucher_detail_background);
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
        tvBtnGetVoucher = (TextView) findViewById(R.id.tvBtnGetVoucher);

        if (promotion.isPinned()) {
            setViewUnpin();
        } else {
            setViewPin();
        }

        if (promotion.isRegistered()) {
            tvBtnGetVoucher.setText("Đã nhận mã");
            btnGetVoucher.setEnabled(false);
            setViewDisableRegister();
        } else if (DateTimeConverter.getCurrentDateInMillis() >= promotion.getEndDate()) {
            tvBtnGetVoucher.setText("Đã hết hạn");
            btnGetVoucher.setEnabled(false);
            setViewDisableRegister();
        } else {
            tvBtnGetVoucher.setText("Nhận mã voucher");
            btnGetVoucher.setEnabled(true);
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

        //Close View
        ImageView voucher_detail_close = (ImageView) findViewById(R.id.voucher_detail_close);
        voucher_detail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        voucher_detail_comment_recyclerview = (RecyclerView) findViewById(R.id.voucher_detail_comment_recyclerview);
        voucher_detail_comment_recyclerview.setNestedScrollingEnabled(false);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        voucher_detail_comment_recyclerview.setLayoutManager(mLinearLayoutManager);
        voucher_detail_comment_recyclerview.setAdapter(adapter);

        getComment();

        postCommentDelegate = new PostCommentDelegate(this);
        voucher_detail_edittext_new_comment = (EditText) findViewById(R.id.voucher_detail_edittext_new_comment);
        ImageButton voucher_detail_post_comment = (ImageButton) findViewById(R.id.voucher_detail_post_comment);

        voucher_detail_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
                CommentBody commentBody = new CommentBody(helper.getUserId(), voucher_detail_edittext_new_comment.getText().toString());
                promotionRetrofitService.postComment(promotion.getId(), commentBody, postCommentDelegate);
            }
        });
    }

    private void setAddress(TextView tvAddresses) {
        List<Address> addresses = promotion.getAddresses();
        String address = "";
        for (int i = 0; i < addresses.size(); i++) {
            address += "⊙  " + addresses.get(i).getFullAddress() + "\n\n";
        }

        tvAddresses.setText(address);
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

    private void setViewDisableRegister() {
        btnGetVoucher.setClickable(false);
        btnGetVoucher.setBackground(getResources().getDrawable(R.drawable.unselector_get_coupon_button));
    }

    public float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    // PIN
    public void pinPromotion() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        UserRetrofitService userRetrofitService = new UserRetrofitService(getApplicationContext());
        userRetrofitService.pinPromotion(helper.getUserId(), 1, promotionRequestBody, pinPromotionDelegate);
    }

    class PinPromotionDelegate extends ForegroundTaskDelegate<ResponseObject> {

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

    public void getComment() {
        promotionRetrofitService = new PromotionRetrofitService(getApplicationContext());
        promotionRetrofitService.getPromotionComment(promotion.getId(), new GetPromotionCommentDelegate(this));
    }

    private class GetPromotionCommentDelegate extends ForegroundTaskDelegate<List<Comment>> {

        GetPromotionCommentDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {

        }

        @Override
        public void onPostExecute(List<Comment> comments, Throwable throwable) {
            super.onPostExecute(comments, throwable);
            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && comments != null && shouldHandleResultForActivity()) {
                adapter.updateData(comments);
            }
        }
    }

    private class PostCommentDelegate extends ForegroundTaskDelegate<ResponseObject> {

        PostCommentDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {

        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                Toast.makeText(getApplicationContext(), responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
                voucher_detail_edittext_new_comment.setText("");
                getComment();
            } else {
                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
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
                voucherPromotion = voucher;

                setViewDisableRegister();

                Bundle args = new Bundle();
                args.putString("nameVoucher", voucherPromotion.getVoucherCode());
                args.putString("qrCode", voucherPromotion.getQrCode());
                args.putString("address", promotion.getProvider().getAddress());
                args.putString("date", DateTimeConverter.getDate(promotion.getStartDate())
                        + " - " + DateTimeConverter.getDate(promotion.getEndDate()));

                btnGetVoucher.setEnabled(false);
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
