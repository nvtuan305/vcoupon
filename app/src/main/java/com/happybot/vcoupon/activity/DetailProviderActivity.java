package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.SubscribeBody;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

public class DetailProviderActivity extends BaseActivity {

    private User provider;
    public static boolean followedProvider = false;
    public static int position;
    private Button btnFollowProvider;
    private ImageButton btnBack;
    private Context mContext;
    private SubscribeDelegate subscribeDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_provider);

        mContext = getApplicationContext();
        subscribeDelegate = new SubscribeDelegate((BaseActivity) this);

        Intent intent = getIntent();
        provider = intent.getParcelableExtra("DetailProvider");
        followedProvider = intent.getExtras().getBoolean("Followed");
        position = intent.getExtras().getInt("Position");

        btnFollowProvider = (Button)findViewById(R.id.btnFollowProvider);
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        if (!followedProvider)
            btnFollowProvider.setText(R.string.follow_title);
        else
            btnFollowProvider.setText(R.string.unfollow_title);

        ImageView provider_detail_avatar = (ImageView)findViewById(R.id.provider_detail_avatar);
        Picasso.with(getApplicationContext())
                .load(provider.getAvatar())
                .into(provider_detail_avatar);

        ImageView provider_detail_background = (ImageView)findViewById(R.id.provider_detail_background);
        Picasso.with(getApplicationContext())
                .load(provider.getAvatar())
                .into(provider_detail_background);

        TextView provider_detail_name = (TextView)findViewById(R.id.provider_detail_name);
        provider_detail_name.setText(provider.getName());

        TextView provider_detail_followed = (TextView)findViewById(R.id.provider_detail_followed);
        provider_detail_followed.setText(provider.getFollowedCount() + "\ntheo dõi");

        TextView provider_detail_promotion_count = (TextView)findViewById(R.id.provider_detail_promotion_count);
        provider_detail_promotion_count.setText(provider.getPromotionCount() + "\nvoucher");

        TextView provider_detail_following = (TextView)findViewById(R.id.provider_detail_following);
        provider_detail_following.setText(provider.getFollowingCount() + "\nđang theo dõi");

        TextView provider_detail_email = (TextView)findViewById(R.id.provider_detail_email);
        provider_detail_email.setText(provider.getEmail());

        TextView provider_detail_phone = (TextView)findViewById(R.id.provider_detail_phone);
        provider_detail_phone.setText(provider.getPhoneNumber());

        TextView provider_detail_address = (TextView)findViewById(R.id.provider_detail_address);
        provider_detail_address.setText(provider.getAddress());

        TextView provider_detail_website = (TextView)findViewById(R.id.provider_detail_website);
        provider_detail_website.setText(provider.getWebsite());

        TextView provider_detail_fanpage = (TextView)findViewById(R.id.provider_detail_fanpage);
        provider_detail_fanpage.setText(provider.getFanpage());

        btnFollowProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
                UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);

                if (followedProvider) {
                    userRetrofitService.unfollowPromotion(helper.getUserId(), provider.getId(), subscribeDelegate);
                } else {
                    SubscribeBody subscribeBody = new SubscribeBody(provider.getId(), "PROVIDER");
                    userRetrofitService.followPromotion(helper.getUserId(), subscribeBody, subscribeDelegate);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class SubscribeDelegate extends ForegroundTaskDelegate<ResponseObject> {

        //AppCompatActivity activitySubscribe;

        SubscribeDelegate(BaseActivity activity){
            super(activity);
            //this.activitySubscribe = activity;
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                //Toast.makeText((Context) activitySubscribe, responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
                if (followedProvider) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("PROVIDER_" + provider.getId());
                    btnFollowProvider.setText(R.string.follow_title);
                    followedProvider = false;
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic("PROVIDER_" + provider.getId());
                    btnFollowProvider.setText(R.string.unfollow_title);
                    followedProvider = true;
                }
            } else {
                //Toast.makeText((Context) activitySubscribe, throwable.getMessage(), Toast.LENGTH_LONG).show();
                if (followedProvider) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("PROVIDER_" + provider.getId());
                    btnFollowProvider.setText(R.string.follow_title);
                    followedProvider = false;
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic("PROVIDER_" + provider.getId());
                    btnFollowProvider.setText(R.string.unfollow_title);
                    followedProvider = true;
                }
            }
        }
    }
}
