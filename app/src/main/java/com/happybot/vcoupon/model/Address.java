package com.happybot.vcoupon.model;

public class Address {
    /*
    "number": "227",
    "street": "Nguyễn Văn Cừ",
    "ward": "Phường 4",
    "district": "Quận 5",
    "province": "HCM",
    "latitude": 10.762867,
    "longitude": 106.682305,
    "_id": "5856aae2a4ae7504c1856a3c",
    "country": "Việt Nam"
    */

    private String number;
    private String street;
    private String ward;
    private String district;
    private String province;
    private String country;
    private String latitude;
    private String longitude;

    public Address() {
    }

    public Address(String number, String street, String ward, String district, String province,
                   String country, String latitude, String longitude) {
        this.number = number;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.province = province;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}