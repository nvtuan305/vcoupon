package com.happybot.vcoupon.service.retrofitinterface;

import com.happybot.vcoupon.model.PromotionRequestBody;
import com.happybot.vcoupon.model.UserRequestBody;
import com.happybot.vcoupon.model.retrofit.PromotionListResponse;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.model.retrofit.SubscribingTopicResponse;
import com.happybot.vcoupon.model.retrofit.UserResponse;
import com.happybot.vcoupon.model.SubscribeBody;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.retrofit.LoginRequestBody;
import com.happybot.vcoupon.model.retrofit.UserListResponse;
import com.happybot.vcoupon.model.retrofit.VoucherListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInterfaceService {

    /**
     * Get pinned promotion of the user page by page
     * default page size is 15 promotion
     *
     * @param userId: Id of user
     * @param page:   Pinned promotion page
     * @return PromotionListResponse
     */
    @GET("users/{userId}/pinned-promotion")
    Call<PromotionListResponse> getPinnedPromotion(@Path("userId") String userId,
                                                   @Query("page") int page);

    /**
     * Pin promotion of the user page by page
     * default page size is 15 promotion
     *
     * @param userId:               Id of user
     * @param page:                 Pinned promotion page
     * @param promotionRequestBody: Promotion Request Body
     * @return PromotionResponse
     */
    @POST("users/{userId}/pinned-promotion")
    Call<ResponseObject> pinPromotion(@Path("userId") String userId,
                                      @Query("page") int page,
                                      @Body PromotionRequestBody promotionRequestBody);

    /**
     * Unpin promotion of the user page by page
     * default page size is 15 promotion
     *
     * @param userId:               Id of user
     * @param page:                 Pinned promotion page
     * @param promotionRequestBody: Promotion Request Body
     * @return PromotionResponse
     */
    @HTTP(method = "DELETE", path = "users/{userId}/pinned-promotion", hasBody = true)
    Call<ResponseObject> unpinPromotion(@Path("userId") String userId,
                                        @Query("page") int page,
                                        @Body PromotionRequestBody promotionRequestBody);


    /**
     * Get profile user
     *
     * @param userId: Id of user
     * @return UserResponse
     */
    @GET("users/{userId}")
    Call<UserResponse> getUserInfo(@Path("userId") String userId);

    /**
     * Get profile user
     *
     * @param userId:          Id of user
     * @param userRequestBody: Body
     * @return UserResponse
     */
    @PUT("users/{userId}")
    Call<UserResponse> updateUserInfo(@Path("userId") String userId, @Body UserRequestBody userRequestBody);

    /**
     * Sign up new user account
     *
     * @param user: User info
     * @return UserResponse
     */
    @POST("users")
    Call<UserResponse> signUp(@Body User user);

    /**
     * Sign in account
     *
     * @param loginBody: Phone number and password to login
     * @return UserResponse
     */
    @POST("users/sign-in")
    Call<UserResponse> signIn(@Body LoginRequestBody loginBody);

    /**
     * Sign in with facebook
     * User send user's facebook access token to server, server return user info with status code:
     * 200 - This user has logged in before
     * 202 - The first time user login
     *
     * @param fbAccessToken
     * @return
     */
    @FormUrlEncoded
    @POST("users/sign-in-facebook")
    Call<UserResponse> signInWithFacebook(@Field("fbAccessToken") String fbAccessToken);

    /**
     * Change user phone number
     *
     * @param userId
     * @param phoneNumber
     * @return
     */
    @FormUrlEncoded
    @POST("users/{userId}/change-phone-number")
    Call<UserResponse> updatePhoneNumber(@Path("userId") String userId, @Field("phoneNumber") String phoneNumber);

    /**
     * Get received voucher of user page by page
     */
    @GET("users/{userId}/vouchers")
    Call<VoucherListResponse> getReceivedVoucher(@Path("userId") String userId, @Query("page") int page);

    @POST("users/{userId}/follows")
    Call<ResponseObject> followPromotion(@Path("userId") String userId, @Body SubscribeBody subscribeBody);

    @DELETE("users/{userId}/follows/{publisherId}")
    Call<ResponseObject> unfollowPromotion(@Path("userId") String userId, @Path("publisherId") String publisherId);

    @GET("users/providers")
    Call<UserListResponse> getSearchProvider(@Query("searchText") String searchQuery, @Query("page") int page);

    @GET("users/{userId}/follows")
    Call<SubscribingTopicResponse> getSubscribingTopic(@Path("userId") String userId);
}