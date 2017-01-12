package com.happybot.vcoupon.service.retrofitinterface;

import com.happybot.vcoupon.model.retrofit.PromotionListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PromotionInterfaceService {

    /**
     * Get promotion by category page by page
     * default page size is 15 promotion
     * @param categoryId: Id of category
     * @param page: Pinned promotion page
     * @return PromotionListResponse
     */
    @GET("categories/{categoryId}/promotions")
    Call<PromotionListResponse> getPromotionByCategory(@Path("categoryId") String categoryId,
                                                   @Query("page") int page);

    /**
     * Get promotion by search query page by page
     * default page size is 15 promotion
     * @param searchQuery: searchQuery
     * @param page: promotion page
     * @return PromotionListResponse
     */
    @GET("promotions")
    Call<PromotionListResponse> getPromotionBySearch(@Query("search") String searchQuery,
                                                       @Query("page") int page);

}
