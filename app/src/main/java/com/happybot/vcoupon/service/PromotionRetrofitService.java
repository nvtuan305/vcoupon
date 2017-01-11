package com.happybot.vcoupon.service;

import android.content.Context;

import com.google.gson.Gson;
import com.happybot.vcoupon.service.retrofitinterface.PromotionInterfaceService;
import com.happybot.vcoupon.service.retrofitinterface.RetrofitInterfaceService;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;


public class PromotionRetrofitService extends VCouponRetrofitService {

    // Service
    protected PromotionInterfaceService service = null;

    public PromotionRetrofitService(Context context) {
        super(context);
    }

    public PromotionRetrofitService(Context context,
                                    HttpLoggingInterceptor loggingInterceptor,
                                    Gson gson) {
        super(context, loggingInterceptor, gson);
    }

    // Get API interface service
    public PromotionInterfaceService getService() {
        PromotionInterfaceService mService = service;

        if (mService == null) {
            synchronized (PromotionRetrofitService.class) {
                mService = service;

                if (mService == null) {
                    Retrofit retrofit = getRetrofit();
                    service = retrofit.create(PromotionInterfaceService.class);
                    mService = service;
                }
            }
        }

        return mService;
    }

    // TODO Cài đặt hàm gọi api tại đây
}