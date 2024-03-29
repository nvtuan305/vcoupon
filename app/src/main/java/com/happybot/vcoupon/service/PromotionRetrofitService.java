package com.happybot.vcoupon.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.happybot.vcoupon.exception.BaseException;
import com.happybot.vcoupon.model.AddressRequestBody;
import com.happybot.vcoupon.model.Comment;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.PromotionBody;
import com.happybot.vcoupon.model.retrofit.CommentBody;
import com.happybot.vcoupon.model.retrofit.CommentListResponse;
import com.happybot.vcoupon.model.retrofit.PromotionListResponse;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.retrofitinterface.PromotionInterfaceService;
import com.happybot.vcoupon.service.retrofitutil.RetrofitServiceCallback;
import com.happybot.vcoupon.service.retrofitutil.TranslateRetrofitCallback;

import java.util.List;
import java.util.Vector;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;


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
  
    /**
     * Get promotion by category
     *
     * @param categoryId
     * @param page
     * @param callback
     */

    public void getPromotionByCategory(@NonNull String categoryId,
                                   @NonNull int page,
                                   final RetrofitServiceCallback<List<Promotion>> callback) {

        Call<PromotionListResponse> promotionListResponseCall
                = ((PromotionInterfaceService) getService()).getPromotionByCategory(categoryId, page);

        callback.setCall(promotionListResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        promotionListResponseCall.enqueue(new TranslateRetrofitCallback<PromotionListResponse>() {
            @Override
            public void onFinish(Call<PromotionListResponse> call,
                                 PromotionListResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

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

    /**
     * Get promotion by search
     *
     * @param searchQuery
     * @param page
     * @param callback
     */

    public void getPromotionBySearch(@NonNull String searchQuery,
                                       @NonNull int page,
                                       final RetrofitServiceCallback<List<Promotion>> callback) {

        Call<PromotionListResponse> promotionListResponseCall
                = ((PromotionInterfaceService) getService()).getPromotionBySearch(searchQuery, page);

        callback.setCall(promotionListResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        promotionListResponseCall.enqueue(new TranslateRetrofitCallback<PromotionListResponse>() {
            @Override
            public void onFinish(Call<PromotionListResponse> call,
                                 PromotionListResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

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

    /**
     * Get promotion by search
     *
     * @param addressRequestBody
     * @param callback
     */

    public void getNearByPromotion(@Body AddressRequestBody addressRequestBody,
                                   final RetrofitServiceCallback<List<Promotion>> callback) {
        Call<PromotionListResponse> promotionListResponseCall
                = ((PromotionInterfaceService) getService()).getNearByPromotion(addressRequestBody);

        callback.setCall(promotionListResponseCall);

        // Show progress dialog

        // Make asynchronous request
        promotionListResponseCall.enqueue(new TranslateRetrofitCallback<PromotionListResponse>() {
            @Override
            public void onFinish(Call<PromotionListResponse> call,
                                 PromotionListResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

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

    /**
     * Post promotion
     * @param promotionBody
     * @param callback
     */

    public void postPromotion(@NonNull PromotionBody promotionBody,
                                       final RetrofitServiceCallback<ResponseObject> callback) {

        Call<ResponseObject> postPromotionResponseCall
                = ((PromotionInterfaceService) getService()).postPromotion(promotionBody);

        callback.setCall(postPromotionResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        postPromotionResponseCall.enqueue(new TranslateRetrofitCallback<ResponseObject>() {
            @Override
            public void onFinish(Call<ResponseObject> call,
                                 ResponseObject responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);
                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());
                    callback.onPostExecute(responseObject, exception);
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Post promotion
     * @param promotionId
     * @param callback
     */

    public void getPromotionComment(@NonNull String promotionId,
                              final RetrofitServiceCallback<List<Comment>> callback) {

        Call<CommentListResponse> getPromotionCommentResponseCall
                = ((PromotionInterfaceService) getService()).getPromotionComment(promotionId);

        callback.setCall(getPromotionCommentResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        getPromotionCommentResponseCall.enqueue(new TranslateRetrofitCallback<CommentListResponse>() {
            @Override
            public void onFinish(Call<CommentListResponse> call,
                                 CommentListResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);
                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());
                    callback.onPostExecute(responseObject.getComments(), exception);
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Post promotion
     * @param promotionId
     * @param commentBody
     */
    public void postComment(@NonNull String promotionId,
                            @NonNull CommentBody commentBody,
                              final RetrofitServiceCallback<ResponseObject> callback) {

        Call<ResponseObject> postCommentResponseCall
                = ((PromotionInterfaceService) getService()).postComment(promotionId, commentBody);

        callback.setCall(postCommentResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        postCommentResponseCall.enqueue(new TranslateRetrofitCallback<ResponseObject>() {
            @Override
            public void onFinish(Call<ResponseObject> call,
                                 ResponseObject responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);
                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());
                    callback.onPostExecute(responseObject, exception);
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }
}