package com.happybot.vcoupon.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.dialog.RegisterSplashDialog;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.Constants;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class SignInActivity extends BaseActivity {

    private UserRetrofitService userRetrofitService = null;
    private SignInDelegate signinDelegate = null;
    private SignInWithFBDelegate signinWithFBDelegate = null;

    private Button btnLoginWithFacebook = null;
    private Button btnLoginNormal = null;
    private LinearLayout btnSignUp = null;
    private EditText etPhoneNumber = null;
    private EditText etPassword = null;

    private CallbackManager loginFBCallbackManager = null;
    private ArrayList<String> FBPermissions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Facebook login
        initializeLoginWithFacebook();

        setContentView(R.layout.activity_sign_in);

        // Bind view
        btnLoginNormal = (Button) findViewById(R.id.btnLogin);
        btnLoginWithFacebook = (Button) findViewById(R.id.btnLoginWithFacebook);
        btnSignUp = (LinearLayout) findViewById(R.id.btnSignUp);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);

        // Transform normal edit text to password
        etPassword.setTypeface(Typeface.DEFAULT);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());

        // Init service
        userRetrofitService = new UserRetrofitService(getApplicationContext());
        signinDelegate = new SignInDelegate(this);
        signinWithFBDelegate = new SignInWithFBDelegate(this);

        // Add foreground task to stop all foreground task when activity is destroyed
        listOfForegroundTaskDelegates.add(signinDelegate);
        listOfForegroundTaskDelegates.add(signinWithFBDelegate);

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

        // Initialize FB permissions
        initializeFBPermissions();

        btnLoginWithFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, FBPermissions);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterOptionDialog();
            }
        });
    }

    public void initializeFBPermissions() {
        FBPermissions.clear();
        FBPermissions.add("public_profile");
        FBPermissions.add("email");
        FBPermissions.add("user_location");
    }

    private void initializeLoginWithFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        loginFBCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(loginFBCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                LOG.debug("Login with facebook successfully: " + AccessToken.getCurrentAccessToken().getToken());
                userRetrofitService.signInWithFacebook(AccessToken.getCurrentAccessToken().getToken(),
                        signinWithFBDelegate);
            }

            @Override
            public void onCancel() {
                LOG.debug("User CANCEL login with facebook");
            }

            @Override
            public void onError(FacebookException error) {
                LOG.error("ERROR: Login with facebook failed - " + error.toString());
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });

        //getHashKey();
    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.happybot.vcoupon",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginFBCallbackManager.onActivityResult(requestCode, resultCode, data);
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

            SignInActivity activity = (SignInActivity) activityWeakReference.get();

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && user != null && shouldHandleResultForActivity() && activity != null) {
                activity.saveAuthenticationInfo(user);
                activity.goToHome();
            } else {
                if (activity != null)
                    Toast.makeText(activity.getApplicationContext(),
                            "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_LONG).show();
            }
        }
    }

    class SignInWithFBDelegate extends ForegroundTaskDelegate<User> {

        SignInWithFBDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            SignInActivity activity = (SignInActivity) activityWeakReference.get();

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && user != null
                    && shouldHandleResultForActivity() && activity != null) {
                String phoneNumber = user.getPhoneNumber();

                // The first time login
                if (phoneNumber.equals(Constants.USER_NO_PHONE_NUMBER)) {
                    SharePreferenceHelper helper = new SharePreferenceHelper(activity.getApplicationContext());
                    helper.saveAccessToken(user.getAccessToken());
                    activity.goToProvidePhoneNumberActivity(user.getId());

                } else {
                    activity.saveAuthenticationInfo(user);
                    activity.goToHome();
                }
            } else {

                if (activity != null)
                    Toast.makeText(activity.getApplicationContext(),
                            "Có lỗi xảy ra. Vui lòng thử lại!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void goToHome() {
        Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToProvidePhoneNumberActivity(String userId) {
        Intent intent = new Intent(SignInActivity.this, ProvidePhoneNumberActivity.class);
        intent.putExtra(Constants.EXTRA_USER_ID, userId);
        startActivity(intent);
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

    public void showRegisterOptionDialog() {
        DialogFragment dialog = new RegisterSplashDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }
}