package com.happybot.vcoupon.model.retrofit;

/**
 * Created by Nguyễn Phương Tuấn on 18-Jan-17.
 */

public class CommentBody {
    private String _user;
    private String message;

    public String get_user() {
        return _user;
    }

    public void set_user(String _user) {
        this._user = _user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommentBody(String _user, String message) {

        this._user = _user;
        this.message = message;
    }
}
