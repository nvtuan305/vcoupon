package com.happybot.vcoupon.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Nguyễn Phương Tuấn on 05-Dec-16.
 */

public class ProfileFragment extends Fragment {

    private CircleImageView civAvatar;
    private TextView tvName;
    private TextView tvPromotion;
    private TextView tvFollowing;
    private TextView tvEmail;
    private TextView tvPhoneNumber;

    // Bonus info provider
    private TextView tvFollowed;

    private LinearLayout loAddress;
    private LinearLayout loWebsite;
    private LinearLayout loFacebook;

    private TextView tvAdress;
    private TextView tvWebsite;
    private TextView tvFacebook;


    private GetUserInfoDelegate getUserInfoDelegate = null;

    private Context mContext = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        civAvatar = (CircleImageView) view.findViewById(R.id.civAvatar);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvPromotion = (TextView) view.findViewById(R.id.tvPromotion);
        tvFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);

        tvFollowed = (TextView) view.findViewById(R.id.tvFollowed);
        tvAdress = (TextView) view.findViewById(R.id.tvAddress);
        tvWebsite = (TextView) view.findViewById(R.id.tvWebsite);
        tvFacebook = (TextView) view.findViewById(R.id.tvFacebook);

        loAddress = (LinearLayout) view.findViewById(R.id.loAddress);
        loWebsite = (LinearLayout) view.findViewById(R.id.loWebsite);
        loFacebook = (LinearLayout) view.findViewById(R.id.loFacebook);

        getUserInfoDelegate = new GetUserInfoDelegate((BaseActivity) getActivity());

        mContext = view.getContext();

        loadUserInfo();

        return view;
    }

    public void loadUserInfo() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.getUserInfo(helper.getUserId(), getUserInfoDelegate);
    }

    private class GetUserInfoDelegate extends ForegroundTaskDelegate<User> {

        GetUserInfoDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && user != null && shouldHandleResultForActivity()) {
                //Toast.makeText(mContext, user.getRole(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            showView(user);
        }
    }

    /**
     * Show empty layout if server response empty data
     */
    public void showView(User user) {

        if (user == null)
            return;

        new DownloadImageTask(civAvatar).execute(user.getAvatar());
        tvName.setText(user.getName());
        tvPromotion.setText(String.valueOf(user.getPromotionCount()) + "\nvoucher");
        tvFollowing.setText(String.valueOf(user.getFollowingCount()) + "\nđang theo dõi");
        tvEmail.setText(user.getEmail());
        tvPhoneNumber.setText(user.getPhoneNumber());

        if (user.getRole().equals("PROVIDER")) {
            Toast.makeText(mContext, user.getRole(), Toast.LENGTH_LONG).show();
            ViewGroup.LayoutParams params;

            params = loAddress.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            loAddress.setLayoutParams(params);
            tvAdress.setText(user.getAddress());

            params = loWebsite.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            loWebsite.setLayoutParams(params);
            tvWebsite.setText(user.getWebsite());

            params = loFacebook.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            loFacebook.setLayoutParams(params);
            tvFacebook.setText(user.getFanpage());

            LinearLayout.LayoutParams paramsTv;

            paramsTv = (LinearLayout.LayoutParams)tvFollowed.getLayoutParams();
            paramsTv.weight = 0.33f;
            tvFollowed.setLayoutParams(paramsTv);
            tvFollowed.setText(String.valueOf(user.getFollowedCount()) + "\ntheo dõi");

            paramsTv = (LinearLayout.LayoutParams)tvPromotion.getLayoutParams();
            paramsTv.weight = 0.34f;
            tvPromotion.setLayoutParams(paramsTv);

            paramsTv = (LinearLayout.LayoutParams)tvFollowing.getLayoutParams();
            paramsTv.weight = 0.33f;
            tvFollowing.setLayoutParams(paramsTv);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


