package com.happybot.vcoupon.model;

import java.util.Date;
import java.util.List;

public class Promotion {
    private String _id;
    private String _category;
    private User _provider;
    private String title;
    private String cover;
    private String condition;
    private Date startDate;
    private Date endDate;
    private int amountLimit;
    private int amountRegistered;
    private int discount;
    private String discountType;
    private List<Address> addresses;
    private int commentCount;
    private int pinnedCount;
    private Date createdAt;

    /*
    {
    "success":true,
    "resultMessage":"Thực hiện thành công",
    "promotion":[
        {
            "_id":"5856aae2a4ae7504c1856a3a",
            "_category":"5842fbab0f0bc105b77eb74e",
            "_provider":{
                "_id":"585256f1b10aa019d8d66740",
                "email":"support@lotteria.vn",
                "phoneNumber":"18008099",
                "address":"3 Nguyễn Lương Bằng, P.Tân Phú, Quận 7, HCM",
                "rating":0,
                "fanpage":"www.facebook.com/ilovelotteria",
                "website":"www.lotteria.vn/",
                "avatar":"http://www.diachibotui.com/Thumbnail/ExtraLarge/Upload/2015/11/10/lotteria-le-duan-635827458028540625.png",
                "name":"Lotteria"
            },
            "title":"KFC khuyến mại món mới gà zòn húng quế giảm giá món gọi thêm chỉ 1000 đồng",
            "__v":0,
            "createdAt":1482074850,
            "commentCount":0,
            "pinnedCount":0,
            "addresses":[
                {
                    "number":"227",
                    "street":"Nguyễn Văn Cừ",
                    "ward":"Phường 4",
                    "district":"Quận 5",
                    "province":"HCM",
                    "latitude":10.762867,
                    "longitude":106.682305,
                    "_id":"5856aae2a4ae7504c1856a3c",
                    "country":"Việt Nam"
                }
            ],
            "discountType":"%",
            "discount":30,
            "amountRegistered":0,
            "amountLimit":500,
            "endDate":1483228800,
            "startDate":1482624000,
            "condition":"Áp dụng cho tất cả khách hàng trong nước",
            "cover":"....."
        }
    ]
} */

    public Promotion(String _category, User _provider, String title, String cover, String condition,
                     Date startDate, Date endDate, int amountLimit, int amountRegistered,
                     int discount, String discountType, List<Address> addresses) {
        this._category = _category;
        this._provider = _provider;
        this.title = title;
        this.cover = cover;
        this.condition = condition;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amountLimit = amountLimit;
        this.amountRegistered = amountRegistered;
        this.discount = discount;
        this.discountType = discountType;
        this.addresses = addresses;
    }

    public String getId() {
        return _id;
    }

    public String getCategory() {
        return _category;
    }

    public void setCategory(String _category) {
        this._category = _category;
    }

    public User getProvider() {
        return _provider;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getAmountLimit() {
        return amountLimit;
    }

    public void setAmountLimit(int amountLimit) {
        this.amountLimit = amountLimit;
    }

    public int getAmountRegistered() {
        return amountRegistered;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getPinnedCount() {
        return pinnedCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }
}
