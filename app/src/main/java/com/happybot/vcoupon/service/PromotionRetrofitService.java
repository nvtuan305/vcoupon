package com.happybot.vcoupon.service;

import android.content.Context;

import com.google.gson.Gson;
import com.happybot.vcoupon.service.retrofitinterface.PromotionInterfaceService;
import com.happybot.vcoupon.service.retrofitinterface.RetrofitInterfaceService;

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
    // Best practice for Singleton pattern:
    // https://medium.com/@huynhquangthao/m%E1%BA%ABu-thi%E1%BA%BFt-k%E1%BA%BF-singleton-5997128c71b9#.ysh3n52lt
    @Override
    public RetrofitInterfaceService getService() {
        RetrofitInterfaceService mService = service;
        if (mService == null) {
            synchronized (RetrofitInterfaceService.class) {
                mService = service;

                if (mService == null) {
                    Retrofit retrofit = getRetrofit();
                    service = retrofit.create(PromotionInterfaceService.class);
                }
            }
        }

        return mService;
    }

    // TODO Cài đặt hàm gọi api tại đây
}
