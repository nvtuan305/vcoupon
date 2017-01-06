package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

public class SignUpNormalUserActivity extends BaseActivity {

    private RadioGroup radioGroup;
    private Call<UserResponse> userResponseCall;
    private GetSignupDelegate getSignupDelegate;
    private String genderString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_normal_user);

        radioGroup = (RadioGroup)findViewById(R.id.rgGender);

        final EditText etEmailSUN = (EditText)findViewById(R.id.etEmailSUN) ;
        final EditText etPasswordSUN = (EditText)findViewById(R.id.etPasswordSUN);
        final EditText etNameSUN = (EditText)findViewById(R.id.etNameSUN) ;
        final EditText etPhoneSUN = (EditText)findViewById(R.id.etPhoneSUN) ;


        Button btnSignupN = (Button)findViewById(R.id.btnSignupN);
        btnSignupN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmailSUN.getText().length() == 0)
                    etEmailSUN.setError("Chưa nhập Username");
                else if (!validEmail(etEmailSUN.getText().toString()))
                    etEmailSUN.setError("Chưa đúng định dạng email");

                if (etPasswordSUN.getText().length() == 0)
                    etPasswordSUN.setError("Chưa nhập Password");
                else if (etPasswordSUN.getText().length() < 6)
                    etPasswordSUN.setError("Password nhỏ hơn 6 kí tự");

                if (etEmailSUN.getError() == null && etPasswordSUN.getError() == null)
                {
                    if (!isNetworkConnected())
                        Toast.makeText(getApplicationContext(), "Không có kết nối internet", Toast.LENGTH_LONG).show();
                    else
                    {
                        if (radioGroup.getCheckedRadioButtonId() != -1)
                        {
                            int id= radioGroup.getCheckedRadioButtonId();
                            View radioButton = radioGroup.findViewById(id);
                            int radioId = radioGroup.indexOfChild(radioButton);
                            RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                            genderString = (String) btn.getText();
                        }

                        User user = new User(etNameSUN.getText().toString(), genderString, etEmailSUN.getText().toString(),
                                etPhoneSUN.getText().toString(), "", "", "", etPasswordSUN.getText().toString(),
                                "NORMAL", "vcoupon");

                        HttpLoggingInterceptor defaultLogging = newDefaultLogging();

                        UserInterfaceService retrofitService = SignInSignUpRetrofitService.getClient(defaultLogging).create(UserInterfaceService.class);

                        userResponseCall = retrofitService.createUser(user);

                        getSignupDelegate = new GetSignupDelegate(SignUpNormalUserActivity.this);

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
        private final WeakReference<SignUpNormalUserActivity> activityWeakReference;

        public GetSignupDelegate(@NonNull final SignUpNormalUserActivity activity){
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

            SignUpNormalUserActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                //Toast.makeText(activity, response.raw().toString(), Toast.LENGTH_LONG).show();
                activity.dismissProgressDialog();
                if (response.code() == 200)
                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
            }
        }

        @Override
        public void onFailure(Call<UserResponse> call, Throwable t) {
            SignUpNormalUserActivity activity = activityWeakReference.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                activity.dismissProgressDialog();
                if (t != null) {
                    Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
