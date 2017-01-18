package com.happybot.vcoupon.model;

/**
 * Created by Nguyễn Phương Tuấn on 17-Jan-17.
 */

public class SubscribingTopic {
    private String subscribeType;
    private String  _publisherId;
    private String _id;

    public String getSubscribeType() {
        return subscribeType;
    }

    public void setSubscribeType(String subscribeType) {
        this.subscribeType = subscribeType;
    }

    public String get_publisherId() {
        return _publisherId;
    }

    public void set_publisherId(String _publisherId) {
        this._publisherId = _publisherId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
