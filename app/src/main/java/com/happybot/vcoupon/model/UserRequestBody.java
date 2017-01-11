package com.happybot.vcoupon.model;

/**
 * Created by Admin on 1/9/2017.
 */

public class UserRequestBody   {

    private String name;
    private String avatar;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private String website;
    private String fanpage;

    public UserRequestBody(String name, String avatar,
                           String password, String email, String phoneNumber, String address,
                           String website, String fanpage) {
        this.name = name;
        this.avatar = avatar;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.website = website;
        this.fanpage = fanpage;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
