package com.happybot.vcoupon.model;

/**
 * Created by don on 1/16/17.
 */

public class AddressRequestBody {
    private double longitude;
    private double latitude;
    private String country;
    private String province;

    public AddressRequestBody() {
    }

    public AddressRequestBody(double latitude, double longitude, String country, String province) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.country = country;
        this.province = province;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
