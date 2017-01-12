package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.fragment.FacebookFragment;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

public class SignInActivity extends BaseActivity {

    private UserRetrofitService userRetrofitService = null;
    private SignInDelegate signinDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_in);

        // Initialize facebook login fragment
        initializeFacebookFragment();

        Button btnLoginNormal = (Button) findViewById(R.id.btnLogin);
        Button btnLoginFacebook = (Button) findViewById(R.id.btnLoginFacebook);

        final EditText etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);

        // Transform normal edit text to password
        etPassword.setTypeface(Typeface.DEFAULT);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        // Init service
        userRetrofitService = new UserRetrofitService(getApplicationContext());
        signinDelegate = new SignInDelegate(this);

        btnLoginNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etPhoneNumber.getText().toString();
                String password = etPassword.getText().toString();

                // Check data input
                if (userName.length() == 0) {
                    etPhoneNumber.setError("Bạn chưa nhập số điện thoại");
                    return;
                }

                if (password.length() == 0) {
                    etPassword.setError("Bạn chưa nhập mật khẩu");
                    return;
                }

                userRetrofitService.signIn(userName, password, signinDelegate);
            }
        });
    }

    private void initializeFacebookFragment() {
        FacebookFragment fragment = new FacebookFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutFacebook, fragment);
        fragmentTransaction.commit();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    class SignInDelegate extends ForegroundTaskDelegate<User> {

        SignInDelegate(BaseActivity activity) {
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
                saveAuthenticationInfo(user.getAccessToken(), user.getId());
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }
    }

    /**
     * Save authentication info when login successfully
     * @param accessToken: Access token
     * @param userID: Id of user
     */
    public void saveAuthenticationInfo(String accessToken, String userID) {
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        helper.saveAccessToken(accessToken);
        helper.saveUserId(userID);
    }
}
