package com.happybot.vcoupon.model;

/**
 * Created by Admin on 12/16/2016.
 */
public class Notify {

    private int avatar;
    private String description;
    private String time;

    public Notify(int avatar, String description, String time) {
        this.avatar = avatar;
        this.description = description;
        this.time = time;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
