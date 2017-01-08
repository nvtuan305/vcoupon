package com.happybot.vcoupon.service.retrofitinterface;

import com.happybot.vcoupon.model.SubscribeBody;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.retrofit.PromotionListResponse;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.model.retrofit.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInterfaceService {

    /**
     * Get pinned promotion of the user page by page
     * default page size is 15 promotion
     * @param userId: Id of user
     * @param page: Pinned promotion page
     * @return PromotionListResponse
     */
    @GET("users/{userId}/pinned-promotion")
    Call<PromotionListResponse> getPinnedPromotion(@Path("userId") String userId,
                                                   @Query("page") int page);

    @Headers("Content-Type:application/json")
    @POST("users")
    Call<UserResponse> createUser(@Body User user);

    @Headers("Content-Type:application/json")
    @POST("users/sign-in")
    Call<UserResponse> getUser(@Body User user);

    @Headers("Content-Type:application/json")
    @POST("users/{userId}/subscribing-topic")
    Call<ResponseObject> followPromotion(@Path("userId") String userId, @Body SubscribeBody subscribeBody);
}
