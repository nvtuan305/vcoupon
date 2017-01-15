package com.happybot.vcoupon.service;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.happybot.vcoupon.application.VCouponApplication;
import com.happybot.vcoupon.service.retrofitinterface.RetrofitInterfaceService;
import com.happybot.vcoupon.service.retrofitutil.AddHeaderInterceptor;
import com.happybot.vcoupon.service.retrofitutil.GsonDateFormatAdapter;
import com.happybot.vcoupon.service.retrofitutil.RetryInterceptor;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VCouponRetrofitService {

    // API base url
    private final String BASE_URL = "https://vcoupon.herokuapp.com/api/v1/";
    //private final String BASE_URL = "http://192.168.11.112:3000/api/v1/";

    // Logger
    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private WeakReference<Context> contextWeakReference = null;
    private final HttpLoggingInterceptor defaultLogging;
    private final Gson defaultGson;

    // Okhttp time out
    private static final long connectionTimeOut = 10;
    private static final long readTimeOut = 20;

    // Helper for get user id and access token from local
    private SharePreferenceHelper spHelper = null;

    public VCouponRetrofitService(Context context) {
        contextWeakReference = new WeakReference<>(context);
        spHelper = new SharePreferenceHelper(contextWeakReference.get());

        defaultLogging = newDefaultLogging();
        defaultGson = newDefaultGson();
    }

    public VCouponRetrofitService(Context context,
                                  HttpLoggingInterceptor loggingInterceptor,
                                  Gson gson) {
        contextWeakReference = new WeakReference<>(context);
        defaultLogging = loggingInterceptor;
        defaultGson = gson;
    }

    /**
     * Set up default logging interceptor
     *
     * @return HttpLoggingInterceptor
     */
    protected HttpLoggingInterceptor newDefaultLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (VCouponApplication.isInDebugMode()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        return logging;
    }

    /**
     * Set up default gson
     *
     * @return Gson
     */
    protected Gson newDefaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonDateFormatAdapter())
                .create();
    }

    protected Retrofit getRetrofit() {
        String token = spHelper.getAccessToken();

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AddHeaderInterceptor(token))
                .addInterceptor(defaultLogging)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .connectTimeout(connectionTimeOut, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .client(client)
                .build();
    }
}