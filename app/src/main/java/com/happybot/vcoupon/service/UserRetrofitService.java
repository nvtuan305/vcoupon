package com.happybot.vcoupon.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.happybot.vcoupon.exception.BaseException;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.PromotionRequestBody;
import com.happybot.vcoupon.model.SubscribingTopic;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.UserRequestBody;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.model.retrofit.SubscribingTopicResponse;
import com.happybot.vcoupon.model.retrofit.UserResponse;
import com.happybot.vcoupon.model.SubscribeBody;
import com.happybot.vcoupon.model.retrofit.LoginRequestBody;
import com.happybot.vcoupon.model.retrofit.PromotionListResponse;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.model.retrofit.UserListResponse;
import com.happybot.vcoupon.model.retrofit.VoucherListResponse;
import com.happybot.vcoupon.service.retrofitinterface.UserInterfaceService;
import com.happybot.vcoupon.service.retrofitutil.RetrofitServiceCallback;
import com.happybot.vcoupon.service.retrofitutil.TranslateRetrofitCallback;

import java.util.List;
import java.util.Vector;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Body;

public class UserRetrofitService extends VCouponRetrofitService {

    // Service
    protected UserInterfaceService service = null;

    public UserRetrofitService(Context context) {
        super(context);
    }

    public UserRetrofitService(Context context,
                               HttpLoggingInterceptor loggingInterceptor,
                               Gson gson) {
        super(context, loggingInterceptor, gson);
    }

    // Get service
    public UserInterfaceService getService() {
        UserInterfaceService mService = service;

        if (mService == null) {
            synchronized (UserInterfaceService.class) {
                mService = service;

                if (mService == null) {
                    Retrofit retrofit = getRetrofit();
                    service = retrofit.create(UserInterfaceService.class);
                    mService = service;
                }
            }
        }

        return mService;
    }

    /**
     * Sign in account
     *
     * @param phoneNumber: Phone number to login
     * @param password:    Password to login
     * @param callback:    Listener for response data
     */
    public void signIn(String phoneNumber,
                       String password,
                       final RetrofitServiceCallback<User> callback) {

        Call<UserResponse> signInCall = ((UserInterfaceService) getService())
                .signIn(new LoginRequestBody(phoneNumber, password));

        callback.setCall(signInCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        signInCall.enqueue(new TranslateRetrofitCallback<UserResponse>() {
            @Override
            public void onFinish(Call<UserResponse> call, UserResponse responseObject, BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.getUser() == null) {
                        callback.onPostExecute(new User(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getUser(), exception);
                    }

                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Sign in with facebook
     * User send user's facebook access token to server, server return user info with status code:
     * 200 - This user has logged in before
     * 202 - The first time user login
     */
    public void signInWithFacebook(String fbAccessToken,
                                   final RetrofitServiceCallback<User> callback) {

        Call<UserResponse> signInCall = ((UserInterfaceService) getService())
                .signInWithFacebook(fbAccessToken);

        callback.setCall(signInCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        signInCall.enqueue(new TranslateRetrofitCallback<UserResponse>() {
            @Override
            public void onFinish(Call<UserResponse> call, UserResponse responseObject, BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.getUser() == null) {
                        callback.onPostExecute(new User(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getUser(), exception);
                    }

                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Update user phone number
     */
    public void updatePhoneNumber(String userId, String newPhoneNumber,
                                  final RetrofitServiceCallback<User> callback) {

        Call<UserResponse> updatePhoneNumberCall = ((UserInterfaceService) getService())
                .updatePhoneNumber(userId, newPhoneNumber);

        callback.setCall(updatePhoneNumberCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        updatePhoneNumberCall.enqueue(new TranslateRetrofitCallback<UserResponse>() {
            @Override
            public void onFinish(Call<UserResponse> call, UserResponse responseObject, BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.getUser() == null) {
                        callback.onPostExecute(new User(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getUser(), exception);
                    }
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Get received voucher of user page by page
     */
    public void getReceivedVoucher(String userId, int page,
                                  final RetrofitServiceCallback<List<Voucher>> callback) {

        Call<VoucherListResponse> voucherListResponseCall = ((UserInterfaceService) getService())
                .getReceivedVoucher(userId, page);

        callback.setCall(voucherListResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        voucherListResponseCall.enqueue(new TranslateRetrofitCallback<VoucherListResponse>() {

            @Override
            public void onFinish(Call<VoucherListResponse> call, VoucherListResponse responseObject, BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.getVouchers() == null) {
                        callback.onPostExecute(new Vector<Voucher>(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getVouchers(), exception);
                    }
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
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
     * Pin promotion
     *
     * @param userId
     * @param page
     * @param promotionRequestBody
     * @param callback
     */
    public void pinPromotion(@NonNull String userId,
                             @NonNull int page,
                             @Body PromotionRequestBody promotionRequestBody,
                             final RetrofitServiceCallback<ResponseObject> callback) {

        Call<ResponseObject> objectResponseCall
                = ((UserInterfaceService) getService()).pinPromotion(userId, page, promotionRequestBody);

        callback.setCall(objectResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request

        objectResponseCall.enqueue(new TranslateRetrofitCallback<ResponseObject>() {

            @Override
            public void onFinish(Call<ResponseObject> call,
                                 ResponseObject responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.isSuccess() == false) {
                        callback.onPostExecute(new ResponseObject(), exception);
                    } else {
                        callback.onPostExecute(responseObject, exception);
                    }
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Unpin promotion
     *
     * @param userId
     * @param page
     * @param promotionRequestBody
     * @param callback
     */
    public void unpinPromotion(@NonNull String userId,
                               @NonNull int page,
                               @Body PromotionRequestBody promotionRequestBody,
                               final RetrofitServiceCallback<ResponseObject> callback) {

        Call<ResponseObject> objectResponseCall
                = ((UserInterfaceService) getService()).unpinPromotion(userId, page, promotionRequestBody);

        callback.setCall(objectResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        objectResponseCall.enqueue(new TranslateRetrofitCallback<ResponseObject>() {

            @Override
            public void onFinish(Call<ResponseObject> call,
                                 ResponseObject responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.isSuccess() == false) {
                        callback.onPostExecute(new ResponseObject(), exception);
                    } else {
                        callback.onPostExecute(responseObject, exception);
                    }
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Get user info
     *
     * @param userId
     * @param callback
     */
    public void getUserInfo(@NonNull String userId,
                            final RetrofitServiceCallback<User> callback) {
        Call<UserResponse> userInfoResponseCall
                = ((UserInterfaceService) getService()).getUserInfo(userId);

        callback.setCall(userInfoResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        userInfoResponseCall.enqueue(new TranslateRetrofitCallback<UserResponse>() {
            @Override
            public void onFinish(Call<UserResponse> call,
                                 UserResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.getUser() == null) {
                        callback.onPostExecute(new User(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getUser(), exception);
                    }

                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Update user info
     *
     * @param userId
     * @param userRequestBody
     * @param callback
     */
    public void updateUserInfo(@NonNull String userId,
                               @Body UserRequestBody userRequestBody,
                               final RetrofitServiceCallback<User> callback) {
        Call<UserResponse> userInfoResponseCall
                = ((UserInterfaceService) getService()).updateUserInfo(userId, userRequestBody);

        callback.setCall(userInfoResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        userInfoResponseCall.enqueue(new TranslateRetrofitCallback<UserResponse>() {
            @Override
            public void onFinish(Call<UserResponse> call,
                                 UserResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.updateUser() == null) {
                        callback.onPostExecute(new User(), exception);
                    } else {
                        callback.onPostExecute(responseObject.updateUser(), exception);
                    }

                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Follow promotion
     *
     * @param userId
     * @param subscribeBody
     * @param callback
     */

    public void followPromotion(@NonNull String userId, @NonNull SubscribeBody subscribeBody,
                                final RetrofitServiceCallback<ResponseObject> callback) {

        Call<ResponseObject> followPromotionResponseCall
                = ((UserInterfaceService) getService()).followPromotion(userId, subscribeBody);

        callback.setCall(followPromotionResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        followPromotionResponseCall.enqueue(new TranslateRetrofitCallback<ResponseObject>() {
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
     * Unfollow promotion
     *
     * @param userId
     * @param publisherId
     * @param callback
     */

    public void unfollowPromotion(@NonNull String userId, @NonNull String publisherId,
                                  final RetrofitServiceCallback<ResponseObject> callback) {

        Call<ResponseObject> followPromotionResponseCall
                = ((UserInterfaceService) getService()).unfollowPromotion(userId, publisherId);

        callback.setCall(followPromotionResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        followPromotionResponseCall.enqueue(new TranslateRetrofitCallback<ResponseObject>() {
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
     * Get search provider
     *
     * @param searchQuery
     * @param page
     * @param callback
     */

    public void getSearchProvider(@NonNull String searchQuery,
                                  @NonNull int page,
                                  final RetrofitServiceCallback<List<User>> callback) {

        Call<UserListResponse> userListResponseCall
                = ((UserInterfaceService) getService()).getSearchProvider(searchQuery, page);

        callback.setCall(userListResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        userListResponseCall.enqueue(new TranslateRetrofitCallback<UserListResponse>() {
            @Override
            public void onFinish(Call<UserListResponse> call,
                                 UserListResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());

                    if (responseObject.getUsers() == null) {
                        callback.onPostExecute(new Vector<User>(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getUsers(), exception);
                    }

                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    /**
     * Get user SubscribingTopic
     *
     * @param userId
     * @param callback
     */

    public void getSubscribingTopic(@NonNull String userId,
                                  final RetrofitServiceCallback<List<SubscribingTopic>> callback) {

        Call<SubscribingTopicResponse> subscribingTopicResponseCall
                = ((UserInterfaceService) getService()).getSubscribingTopic(userId);

        callback.setCall(subscribingTopicResponseCall);

        // Show progress dialog
        callback.onPreExecute();

        // Make asynchronous request
        subscribingTopicResponseCall.enqueue(new TranslateRetrofitCallback<SubscribingTopicResponse>() {
            @Override
            public void onFinish(Call<SubscribingTopicResponse> call,
                                 SubscribingTopicResponse responseObject,
                                 BaseException exception) {
                super.onFinish(call, responseObject, exception);
                if (exception == null && responseObject != null) {
                    LOG.debug("RESPONSE MESSAGE ==> " + responseObject.getResultMessage());
                    callback.onPostExecute(responseObject.getSubscribingTopic(), exception);
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }
}
