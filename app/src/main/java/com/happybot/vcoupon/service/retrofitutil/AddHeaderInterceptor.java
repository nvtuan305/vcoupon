package com.happybot.vcoupon.service.retrofitutil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddHeaderInterceptor implements Interceptor {

    private String accessToken;
    private String userId;

    public AddHeaderInterceptor(String userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("user_id", userId);
        builder.addHeader("access_token", accessToken);
        return chain.proceed(builder.build());
    }
}
