package com.happybot.vcoupon.model.retrofit;

import com.happybot.vcoupon.model.Promotion;

public class PromotionResponse extends ResponseObject {
    private Promotion promotion;

    public Promotion getPromotion() {
        return promotion;
    }
}