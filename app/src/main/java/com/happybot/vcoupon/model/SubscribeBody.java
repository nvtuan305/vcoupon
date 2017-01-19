package com.happybot.vcoupon.model;

/**
 * Created by Nguyễn Phương Tuấn on 08-Jan-17.
 */

public class SubscribeBody {

    public SubscribeBody(String _publisherId) {
        this._publisherId = _publisherId;
        this.subscribeType = null;
    }

    public SubscribeBody(String _publisherId, String subscribeType) {
        this._publisherId = _publisherId;
        this.subscribeType = subscribeType;
    }

    public String get_publisherId() {
        return _publisherId;
    }

    public void set_publisherId(String _publisherId) {
        this._publisherId = _publisherId;
    }

    private String _publisherId;

    public String getSubscribeType() {
        return subscribeType;
    }

    public void setSubscribeType(String subscribeType) {
        this.subscribeType = subscribeType;
    }

    private String subscribeType;
}
