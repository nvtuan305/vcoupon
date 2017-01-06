package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.fragment.FacebookFragment;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.retrofit.UserResponse;
import com.happybot.vcoupon.service.SignInSignUpRetrofitService;
import com.happybot.vcoupon.service.retrofitinterface.UserInterfaceService;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends BaseActivity {

    private Call<UserResponse> userResponseCall;
    private GetSigninDelegate getSigninDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FacebookFragment fragment = new FacebookFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutFacebook, fragment);
        fragmentTransaction.commit();

        Button btnLoginNormal = (Button)findViewById(R.id.btnLoginNormal);

        final EditText etUsername = (EditText)findViewById(R.id.etUsernameLogin);
        final EditText etPassword = (EditText)findViewById(R.id.etPasswordLogin);

        btnLoginNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUsername.getText().length() == 0)
                    etUsername.setError("Chưa nhập Username");
                else if (!validEmail(etUsername.getText().toString()) && !isInteger(etUsername.getText().toString()))
                    etUsername.setError("Chưa đúng định dạng email");

                if (etPassword.getText().length() == 0)
                    etPassword.setError("Chưa nhập Password");
                else if (etPassword.getText().length() < 6)
                    etPassword.setError("Password nhỏ hơn 6 kí tự");

                if (etUsername.getError() == null && etPassword.getError() == null)
                {
                    if (!isNetworkConnected())
                        Toast.makeText(getApplicationContext(), "Không có kết nối internet", Toast.LENGTH_LONG).show();
                    else
                    {
                        User userSignIn = new User(etUsername.getText().toString(), etPassword.getText().toString());

                        HttpLoggingInterceptor defaultLogging = newDefaultLogging();

                        UserInterfaceService retrofitService = SignInSignUpRetrofitService.getClient(defaultLogging).create(UserInterfaceService.class);

                        userResponseCall = retrofitService.getUser(userSignIn);

                        getSigninDelegate = new GetSigninDelegate(SignInActivity.this);

                        showProgressDialog();

                        userResponseCall.enqueue(getSigninDelegate);
                    }
                }
            }
        });

        Button btnLoginFacebook = (Button)findViewById(R.id.btnLoginFacebook);
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private HttpLoggingInterceptor newDefaultLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    private boolean isInteger (String phone){
        try {
            int integer = Integer.parseInt(phone);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private class GetSigninDelegate implements Callback<UserResponse> {
        private final WeakReference<SignInActivity> activityWeakReference;

        public GetSigninDelegate(@NonNull final SignInActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

            SignInActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.dismissProgressDialog();
                if (response.code() == 200)
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        }

        @Override
        public void onFailure(Call<UserResponse> call, Throwable t) {
            SignInActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.dismissProgressDialog();
                if (t != null) {
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
