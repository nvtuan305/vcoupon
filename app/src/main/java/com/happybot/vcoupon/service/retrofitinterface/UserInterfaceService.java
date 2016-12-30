package com.happybot.vcoupon.service.retrofitinterface;

import com.happybot.vcoupon.model.retrofit.PromotionListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInterfaceService extends RetrofitInterfaceService {

    /**
     * Get pinned promotion of the user page by page
     * default page size is 15 promotion
     * @param userId: Id of user
     * @param page: Pinned promotion page
     * @return
     */
    @GET("users/{userId}/pinned-promotion")
    Call<PromotionListResponse> getPinnedPromotion(@Path("userId") String userId,
                                                   @Query("page") int page);

}
