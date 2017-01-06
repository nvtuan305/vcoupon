package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.happybot.vcoupon.R;
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

public class SignUpProviderActivity extends BaseActivity {

    private Call<UserResponse> userResponseCall;
    private GetSignupDelegate getSignupDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_provider);

        final EditText etEmailSU = (EditText)findViewById(R.id.etEmailSU);
        final EditText etPasswordSU = (EditText)findViewById(R.id.etPasswordSU);
        final EditText etNameSU = (EditText)findViewById(R.id.etNameSU);
        final EditText etPhoneSU = (EditText)findViewById(R.id.etPhoneSU);
        final EditText etAddressSU = (EditText)findViewById(R.id.etAddressSU);
        final EditText etWebsiteSU = (EditText)findViewById(R.id.etWebsiteSU);
        final EditText etFacebookSU = (EditText)findViewById(R.id.etFacebookSU);

        Button btnSignup = (Button)findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmailSU.getText().length() == 0)
                    etEmailSU.setError("Chưa nhập Username");
                else if (!validEmail(etEmailSU.getText().toString()))
                    etEmailSU.setError("Chưa đúng định dạng email");

                if (etPasswordSU.getText().length() == 0)
                    etPasswordSU.setError("Chưa nhập Password");
                else if (etPasswordSU.getText().length() < 6)
                    etPasswordSU.setError("Password nhỏ hơn 6 kí tự");

                if (etEmailSU.getError() == null && etPasswordSU.getError() == null)
                {
                    if (!isNetworkConnected())
                        Toast.makeText(getApplicationContext(), "Không có kết nối internet", Toast.LENGTH_LONG).show();
                    else
                    {
                        User user = new User(etNameSU.getText().toString(), "Khác", etEmailSU.getText().toString(),
                                etPhoneSU.getText().toString(), etAddressSU.getText().toString(), etWebsiteSU.getText().toString(),
                                etFacebookSU.getText().toString(), etPasswordSU.getText().toString(),
                                "PROVIDER", "vcoupon");

                        HttpLoggingInterceptor defaultLogging = newDefaultLogging();

                        UserInterfaceService retrofitService = SignInSignUpRetrofitService.getClient(defaultLogging).create(UserInterfaceService.class);

                        userResponseCall = retrofitService.createUser(user);

                        getSignupDelegate = new GetSignupDelegate(SignUpProviderActivity.this);

                        showProgressDialog();

                        userResponseCall.enqueue(getSignupDelegate);
                    }
                }
            }
        });
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
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

    private HttpLoggingInterceptor newDefaultLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    private class GetSignupDelegate implements Callback<UserResponse> {
        private final WeakReference<SignUpProviderActivity> activityWeakReference;

        public GetSignupDelegate(@NonNull final SignUpProviderActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

            SignUpProviderActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                //Toast.makeText(activity, response.raw().toString(), Toast.LENGTH_LONG).show();
                activity.dismissProgressDialog();
                if (response.code() == 200)
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            }
        }

        @Override
        public void onFailure(Call<UserResponse> call, Throwable t) {
            SignUpProviderActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.dismissProgressDialog();
                if (t != null) {
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
