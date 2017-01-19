package com.happybot.vcoupon.model;

/**
 * Created by Admin on 1/9/2017.
 */

public class PromotionRequestBody {

    private String _promotionId;

    public PromotionRequestBody(String promotionId) {
        this._promotionId = promotionId;
    }


    public String get_promotionId() {
        return _promotionId;
    }

    public void set_promotionId(String _promotionId) {
        this._promotionId = _promotionId;
    }
}
