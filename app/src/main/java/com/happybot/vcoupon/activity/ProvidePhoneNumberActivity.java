package com.happybot.vcoupon.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.exception.BaseException;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.Constants;
import com.happybot.vcoupon.util.SharePreferenceHelper;

public class ProvidePhoneNumberActivity extends BaseActivity {

    private String userId = "";
    private Button btnContinue = null;
    private EditText etPhoneNumber = null;
    private UpdatePhoneNumberDelegate updatePhoneNumberDelegate = null;
    private UserRetrofitService userRetrofitService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_phone_number);

        btnContinue = (Button) findViewById(R.id.btnContinue);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);

        // Get user ID
        userId = getUserId();

        // Initialize updatePhoneNumberDelegate
        userRetrofitService = new UserRetrofitService(getApplicationContext());
        updatePhoneNumberDelegate = new UpdatePhoneNumberDelegate(this);
        listOfForegroundTaskDelegates.add(updatePhoneNumberDelegate);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = etPhoneNumber.getText().toString();

                if (phoneNumber.length() == 0) {
                    etPhoneNumber.setError("Bạn chưa nhập số điện thoại");
                    return;
                }

                userRetrofitService.updatePhoneNumber(userId, phoneNumber, updatePhoneNumberDelegate);
            }
        });
    }

    public String getUserId() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_USER_ID))
            return intent.getStringExtra(Constants.EXTRA_USER_ID);

        return "";
    }

    class UpdatePhoneNumberDelegate extends ForegroundTaskDelegate<User> {

        public UpdatePhoneNumberDelegate(BaseActivity activity) {
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
                ProvidePhoneNumberActivity activity = (ProvidePhoneNumberActivity) activityWeakReference.get();
                activity.saveAuthenticationInfo(user);
                activity.goToHome();
            }
        }
    }

    private void goToHome() {
        Intent intent = new Intent(ProvidePhoneNumberActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Save authentication info when login successfully
     */
    public void saveAuthenticationInfo(User user) {
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        helper.saveAccessToken(user.getAccessToken());
        helper.saveUserId(user.getId());
        helper.saveUserRole(user.getRole());
    }
}
