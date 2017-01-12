package com.happybot.vcoupon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.PromotionRequestBody;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.UserRequestBody;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

/**
 * Created by Admin on 1/4/2017.
 */

public class EditProfileFragment extends Fragment {

    /*
        List info can edit
        "email": "support@goixe.vn",
        "phoneNumber": "19002022",
        "address": "121 Điện Biên Phủ, Q.1, HCM",
        "password": "provider",
        "fanpage": "www.facebook.com/Goi-Xe/",
        "website": "www.goixe.com",
        "avatar": "http://www.oxfordeagle.com/wp-content/uploads/2016/06/uber.png",
        "name": "Go-iXe"
    */

    private EditText etName;
    private EditText etPassword;
    private EditText etRePassword;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etAddress;
    private EditText etWebsite;
    private EditText etFanpage;
    private TextView tvUrlAvatar;
    private Button btnChooseAvatar;
    private Button btnSave;

    private Button btnPin;
    private Button btnUnpin;

    private UserRequestBody userRequestBody = null;
    private PromotionRequestBody promotionRequestBody = null;

    private UpdateUserInfoDelegate updateUserInfoDelegate = null;
    private PinPromotionDelegate pinPromotionDelegate = null;
    private UnpinPromotionDelegate unpinPromotionDelegate = null;

    private Context mContext = null;

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etName          = (EditText) view.findViewById(R.id.etName);
        etPassword      = (EditText) view.findViewById(R.id.etPassword);
        etRePassword    = (EditText) view.findViewById(R.id.etRePassword);
        etEmail         = (EditText) view.findViewById(R.id.etEmail);
        etPhoneNumber   = (EditText) view.findViewById(R.id.etPhoneNumber);
        etAddress       = (EditText) view.findViewById(R.id.etAddress);
        etWebsite       = (EditText) view.findViewById(R.id.etWebsite);
        etFanpage       = (EditText) view.findViewById(R.id.etFanpage);
        tvUrlAvatar     = (TextView) view.findViewById(R.id.tvUrlAvatar);
        btnChooseAvatar = (Button) view.findViewById(R.id.btnChooseAvatar);
        btnSave         = (Button) view.findViewById(R.id.btnSave);

        btnPin          = (Button) view.findViewById(R.id.btnPin);
        btnUnpin        = (Button) view.findViewById(R.id.btnUnpin);
        promotionRequestBody = new PromotionRequestBody(new String("5856a5f1a4ae7504c1856a37"));

        updateUserInfoDelegate = new UpdateUserInfoDelegate((BaseActivity) getActivity());
        pinPromotionDelegate = new PinPromotionDelegate((BaseActivity) getActivity());
        unpinPromotionDelegate = new UnpinPromotionDelegate((BaseActivity) getActivity());

        mContext = view.getContext();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userRequestBody = new UserRequestBody(etName.getText().toString(), tvUrlAvatar.getText().toString(),
                        etPassword.getText().toString(), etEmail.getText().toString(),
                        etPhoneNumber.getText().toString(), etAddress.getText().toString(),
                        etWebsite.getText().toString(), etFanpage.getText().toString());

                updateUserInfo();

            }
        });

        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinPromotion();
            }
        });

        btnUnpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unpinPromotion();
            }
        });


        return view;
    }

    public void updateUserInfo() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        helper.initializeSampleAuth();

        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.updateUserInfo(helper.getUserId(), userRequestBody, updateUserInfoDelegate);
    }

    private class UpdateUserInfoDelegate extends ForegroundTaskDelegate<User> {

        UpdateUserInfoDelegate(BaseActivity activity) {
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
                Toast.makeText(mContext, user.getName(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }

    // Test PIN
    public void pinPromotion() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        helper.initializeSampleAuth();

        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
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
                Toast.makeText(mContext, responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }

    // Test UNPIN
    public void unpinPromotion() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        helper.initializeSampleAuth();

        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.unpinPromotion(helper.getUserId(), 1, promotionRequestBody, unpinPromotionDelegate);
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
                Toast.makeText(mContext, responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }
}