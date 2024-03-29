package com.happybot.vcoupon.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    /*
   "_id": "5867d9c050fb07001111397b",
    "email": "nvtuan@vcoupon.vn",
    "phoneNumber": "199",
    "address": "68/62 Đồng Nai, P.15, Quận 10, TP.HCM",
    "provider": "facebook",
    "providerId": "12453aghgahs...",
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ODY3ZDljMDUwZmIwNzAwMTExMTM5N2IiLCJwaG9uZU51bWJlciI6IjE5OSIsImlhdCI6MTQ4MzI1NjE3NiwiZXhwIjoxNDkxODk2MTc2fQ.ZtupyQcoUApFF3foGegT1l_CtPZX81vFJZYD5qZT3kw",
    "promotionCount": 0,
    "followedCount": 0,
    "followingCount": 0,
    "role": "NORMAL",
    "rating": 0,
    "fanpage": "www.facebook.com/uberVN/",
    "website": "www.uber.com",
    "gender": "Khác",
    "avatar": "http://www.oxfordeagle.com/wp-content/uploads/2016/06/uber.png",
    "name": "Tuan Nguyen"
     */
    private String _id;
    private String name;
    private String avatar;
    private String gender;
    private String email;
    private String phoneNumber;
    private String password;
    private String address;
    private String website;
    private String fanpage;
    private String role;
    private String provider;
    private int promotionCount;
    private int followedCount;
    private int followingCount;
    private int rating;
    private String accessToken;
    private boolean isFollowing;

    public User() {
        this.name = "VCoupon";
        this.gender = "";
        this.email = "";
        this.phoneNumber = "";
        this.address = "";
        this.website = "";
        this.fanpage = "";
        this.password = "";
        this.role = "NORMAL";
        this.provider = "vcoupon";
    }

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User(String name, String gender, String email, String phoneNumber, String address,
                String website, String fanpage, String password, String role, String provider) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.website = website;
        this.fanpage = fanpage;
        this.password = password;
        this.role = role;
        this.provider = provider;
    }

    public User(String name, String gender, String email, String phoneNumber, String address,
                String website, String fanpage, String password, String role, String provider, boolean isFollowing) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.website = website;
        this.fanpage = fanpage;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.isFollowing = isFollowing;
    }

    public User (Parcel in){
        _id = in.readString();
        name = in.readString();
        avatar = in.readString();
        gender = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        password = in.readString();
        address = in.readString();
        website = in.readString();
        fanpage = in.readString();
        role = in.readString();
        provider = in.readString();
        promotionCount = in.readInt();
        followedCount = in.readInt();
        followingCount = in.readInt();
        rating = in.readInt();
        accessToken = in.readString();
        isFollowing = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_id);
        parcel.writeString(name);
        parcel.writeString(avatar);
        parcel.writeString(gender);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeString(password);
        parcel.writeString(address);
        parcel.writeString(website);
        parcel.writeString(fanpage);
        parcel.writeString(role);
        parcel.writeString(provider);
        parcel.writeInt(promotionCount);
        parcel.writeInt(followedCount);
        parcel.writeInt(followingCount);
        parcel.writeInt(rating);
        parcel.writeString(accessToken);
        parcel.writeByte((byte) (isFollowing ? 1 : 0));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFanpage() {
        return fanpage;
    }

    public void setFanpage(String fanpage) {
        this.fanpage = fanpage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return _id;
    }

    public int getPromotionCount() {
        return promotionCount;
    }

    public int getFollowedCount() {
        return followedCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public int getRating() {
        return rating;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
