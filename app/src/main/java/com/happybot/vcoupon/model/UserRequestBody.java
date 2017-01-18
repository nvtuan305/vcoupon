package com.happybot.vcoupon.model;

/**
 * Created by Admin on 1/9/2017.
 */

public class UserRequestBody   {

    private String name;
    private String avatar;
    private String email;
    private String address;
    private String website;
    private String fanpage;

    public UserRequestBody(String name, String avatar, String email, String address,
                           String website, String fanpage) {
        this.name = name;
        this.avatar = avatar;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
