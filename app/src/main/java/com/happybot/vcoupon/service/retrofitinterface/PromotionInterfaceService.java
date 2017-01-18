package com.happybot.vcoupon.service.retrofitinterface;

import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.PromotionBody;
import com.happybot.vcoupon.model.AddressRequestBody;
import com.happybot.vcoupon.model.PromotionRequestBody;
import com.happybot.vcoupon.model.retrofit.CommentBody;
import com.happybot.vcoupon.model.retrofit.CommentListResponse;
import com.happybot.vcoupon.model.retrofit.PromotionListResponse;
import com.happybot.vcoupon.model.retrofit.ResponseObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    /**
     * Post promotion
     * @param promotionBody
     * @return ResponseObject
     */
    @POST("promotions")
    Call<ResponseObject> postPromotion(@Body PromotionBody promotionBody);

    /**
     * Get nearby Promotion
     * @param addressRequestBody: Address Request Body
     * @return PromotionListResponse
     */
    @POST("promotions/near-promotion")
    Call<PromotionListResponse> getNearByPromotion(@Body AddressRequestBody addressRequestBody);

    /**
     * Get comment page by page
     * default page size is 15 promotion
     * @param promotionId: Id of category
     * @return ResponseObject
     */
    @GET("promotions/{promotionId}/comments")
    Call<CommentListResponse> getPromotionComment(@Path("promotionId") String promotionId);

    /**
     * Post comment
     * default page size is 15 promotion
     * @param promotionId: Id of category
     * @param commentBody: message
     * @return ResponseObject
     */
    @POST("promotions/{promotionId}/comments")
    Call<ResponseObject> postComment(@Path("promotionId") String promotionId, @Body CommentBody commentBody);
}