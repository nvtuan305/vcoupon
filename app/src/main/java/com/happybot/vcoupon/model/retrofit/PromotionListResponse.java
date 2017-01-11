package com.happybot.vcoupon.model.retrofit;

import com.happybot.vcoupon.model.Promotion;

import java.util.List;

public class PromotionListResponse extends ResponseObject {
    List<Promotion> promotions;

    public List<Promotion> getPromotions() {
        return promotions;
    }
}
