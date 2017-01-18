package com.happybot.vcoupon.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable{
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
    private double latitude;
    private double longitude;

    public Address() {
    }

    public Address(String number, String street, String ward, String district, String province, String country, double latitude, double longitude) {
        this.number = number;
        this.street = street;
        this.ward = ward;
        this.district = district;
        this.province = province;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {

        return latitude;
    }

    public Address(Parcel in){
        number = in.readString();
        street = in.readString();
        ward = in.readString();
        district = in.readString();
        province = in.readString();
        country = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel parcel) {
            return new Address(parcel);
        }

        @Override
        public Address[] newArray(int i) {
            return new Address[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(number);
        parcel.writeString(street);
        parcel.writeString(ward);
        parcel.writeString(district);
        parcel.writeString(province);
        parcel.writeString(country);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public String toString() {
        return number + " " + street + ", " + ward + ", " + district;
    }
}