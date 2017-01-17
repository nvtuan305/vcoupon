package com.happybot.vcoupon.model.retrofit;

import com.happybot.vcoupon.model.SubscribingTopic;

import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 17-Jan-17.
 */

public class SubscribingTopicResponse extends ResponseObject {
    List<SubscribingTopic> subscribingTopic;

    public List<SubscribingTopic> getSubscribingTopic() {
        return subscribingTopic;
    }
}
