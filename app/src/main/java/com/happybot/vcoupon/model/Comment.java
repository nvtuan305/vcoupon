package com.happybot.vcoupon.model;

/**
 * Created by Nguyễn Phương Tuấn on 18-Jan-17.
 */

public class Comment {

    private String _id;
    private String _promotion;
    private User _user;
    private String message;
    private int __v;
    private double commentedAt;

    public Comment(String _id, String _promotion, User _user, String message, int __v, double commentedAt) {

        this._id = _id;
        this._promotion = _promotion;
        this._user = _user;
        this.message = message;
        this.__v = __v;
        this.commentedAt = commentedAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_promotion() {
        return _promotion;
    }

    public void set_promotion(String _promotion) {
        this._promotion = _promotion;
    }

    public User get_user() {
        return _user;
    }

    public void set_user(User _user) {
        this._user = _user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public double getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(double commentedAt) {
        this.commentedAt = commentedAt;
    }
}
