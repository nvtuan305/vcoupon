package com.happybot.vcoupon.service;

import android.content.Context;

import com.google.gson.Gson;
import com.happybot.vcoupon.service.retrofitinterface.RetrofitInterfaceService;
import com.happybot.vcoupon.service.retrofitinterface.PromotionInterfaceService;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


public class PromotionRetrofitService extends VCouponRetrofitService implements RetrofitInterfaceService {
    public PromotionRetrofitService(Context context) {
        super(context);
    }

    public PromotionRetrofitService(Context context, HttpLoggingInterceptor loggingInterceptor, Gson gson) {
        super(context, loggingInterceptor, gson);
    }

    // Get API interface service
    @Override
    public RetrofitInterfaceService getService() {
        Retrofit retrofit = getRetrofit();
        return retrofit.create(PromotionInterfaceService.class);
    }

    // TODO Cài đặt hàm gọi api tại đây
}
