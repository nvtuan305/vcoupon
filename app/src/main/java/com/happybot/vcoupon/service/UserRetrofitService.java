package com.happybot.vcoupon.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.happybot.vcoupon.exception.BaseException;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.retrofit.PromotionListResponse;
import com.happybot.vcoupon.service.retrofitinterface.RetrofitInterfaceService;
import com.happybot.vcoupon.service.retrofitinterface.UserInterfaceService;
import com.happybot.vcoupon.service.retrofitutil.RetrofitServiceCallback;
import com.happybot.vcoupon.service.retrofitutil.TranslateRetrofitCallback;

import java.util.List;
import java.util.Vector;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

public class UserRetrofitService extends VCouponRetrofitService implements RetrofitInterfaceService {

    public UserRetrofitService(Context context) {
        super(context);
    }

    public UserRetrofitService(Context context, HttpLoggingInterceptor loggingInterceptor, Gson gson) {
        super(context, loggingInterceptor, gson);
    }

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
                    service = retrofit.create(UserInterfaceService.class);
                }
            }
        }

        return mService;
    }

    /**
     * Get pinned promotion
     *
     * @param userId
     * @param page
     * @param callback
     */

    public void getPinnedPromotion(@NonNull String userId,
                                   @NonNull int page,
                                   final RetrofitServiceCallback<List<Promotion>> callback) {

        Call<PromotionListResponse> promotionListResponseCall
                = ((UserInterfaceService) getService()).getPinnedPromotion(userId, page);

        callback.setCall(promotionListResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        promotionListResponseCall.enqueue(new TranslateRetrofitCallback<PromotionListResponse>() {
            @Override
            public void onFinish(Call<PromotionListResponse> call, PromotionListResponse responseObject, BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {

                    if (responseObject.getPromotions() == null) {
                        callback.onPostExecute(new Vector<Promotion>(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getPromotions(), exception);
                    }

                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }
}
