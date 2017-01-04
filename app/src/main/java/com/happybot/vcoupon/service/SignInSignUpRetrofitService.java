package com.happybot.vcoupon.service;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TQV on 1/3/2017.
 */

public class SignInSignUpRetrofitService {

    public static final String API_END_POINT_FORMAT = "https://vcoupon.herokuapp.com/api/v1/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(HttpLoggingInterceptor defaultLogging) {
        if (retrofit==null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(defaultLogging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_END_POINT_FORMAT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
