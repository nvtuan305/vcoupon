package com.happybot.vcoupon.model.retrofit;

import com.happybot.vcoupon.model.User;

import java.util.List;

public class UserListResponse extends ResponseObject {
    List<User> users;

    public List<User> getUsers() {
        return users;
    }
}
