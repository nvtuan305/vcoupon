package com.happybot.vcoupon.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.adapter.CommentAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.fragment.MapFragment;
import com.happybot.vcoupon.model.Comment;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.retrofit.CommentBody;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.PromotionRetrofitService;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.happybot.vcoupon.util.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VoucherDetailActivity extends BaseActivity {

    private Promotion promotion;
    private  CommentAdapter adapter = new CommentAdapter();
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
        postCommentDelegate =  new PostCommentDelegate(this);
        voucher_detail_edittext_new_comment = (EditText) findViewById(R.id.voucher_detail_edittext_new_comment);
        ImageButton voucher_detail_post_comment = (ImageButton) findViewById(R.id.voucher_detail_post_comment);

        voucher_detail_post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
                CommentBody commentBody = new CommentBody(helper.getUserId(),voucher_detail_edittext_new_comment.getText().toString());
                promotionRetrofitService.postComment(promotion.getId(), commentBody, postCommentDelegate);
            }
        });
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
}
