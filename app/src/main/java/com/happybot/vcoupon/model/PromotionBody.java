package com.happybot.vcoupon.model;

import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 14-Jan-17.
 */

public class PromotionBody {
    private String _category;
    private String _provider;
    private String title;
    private String cover;
    private String condition;
    private long startDate;
    private long endDate;
    private int amountLimit;
    private boolean isOneCode;
    private int discount;
    private String discountType;

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    private List<Address> addresses;

    public PromotionBody(String _category, String _provider, String title, String cover, String condition, long startDate, long endDate, int amountLimit, boolean isOneCode, int discount, int discountType, List<Address> addresses) {
        this._category = _category;
        this._provider = _provider;
        this.title = title;
        this.cover = cover;
        this.condition = condition;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountLimit = amountLimit;
        this.isOneCode = isOneCode;
        this.discount = discount;
        this.addresses = addresses;
        if ( discountType == 1) {
            this.discountType = "VND";
        }
        else {
            this.discountType = "%";
        }
    }
    public boolean isOneCode() {
        return isOneCode;
    }

    public void setOneCode(boolean oneCode) {
        isOneCode = oneCode;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public String get_provider() {
        return _provider;
    }

    public void set_provider(String _provider) {
        this._provider = _provider;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(int amountLimit) {
        this.amountLimit = amountLimit;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
