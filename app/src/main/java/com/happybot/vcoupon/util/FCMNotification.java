package com.happybot.vcoupon.util;

import android.app.Activity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.SubscribingTopic;
import com.happybot.vcoupon.service.UserRetrofitService;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Nguyễn Phương Tuấn on 18-Jan-17.
 */

public class FCMNotification {

    private BaseActivity activity;

    public FCMNotification(BaseActivity activity) {
        this.activity = activity;
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d("FIREBASE FCM ", "Refreshed token: " + refreshedToken);
    }

    public void updateSubscribeFCMTopic() {
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        UserRetrofitService userRetrofitService = new UserRetrofitService(activity);
        userRetrofitService.getSubscribingTopic(helper.getUserId(), new SubscribingTopicDelegate(activity));
    }

    public void unsubscribeFCMTopic() {
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        UserRetrofitService userRetrofitService = new UserRetrofitService(activity);
        userRetrofitService.getSubscribingTopic(helper.getUserId(), new UnsubscribingTopicDelegate(activity));
    }

    class SubscribingTopicDelegate extends ForegroundTaskDelegate<List<SubscribingTopic>> {

        SubscribingTopicDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(List<SubscribingTopic> subscribingTopics, Throwable throwable) {
            super.onPostExecute(subscribingTopics, throwable);
            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && subscribingTopics != null && shouldHandleResultForActivity()) {
                //Subscribe FCM Topic
                for (SubscribingTopic subscribingTopic :subscribingTopics) {
                    FirebaseMessaging.getInstance().subscribeToTopic(subscribingTopic.getSubscribeType() + "_" + subscribingTopic.get_publisherId());
                }
            }
        }
    }

    class UnsubscribingTopicDelegate extends ForegroundTaskDelegate<List<SubscribingTopic>> {

        UnsubscribingTopicDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(List<SubscribingTopic> subscribingTopics, Throwable throwable) {
            super.onPostExecute(subscribingTopics, throwable);
            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && subscribingTopics != null && shouldHandleResultForActivity()) {
                //Subscribe FCM Topic
                for (SubscribingTopic subscribingTopic :subscribingTopics) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(subscribingTopic.getSubscribeType() + "_" + subscribingTopic.get_publisherId());
                }
            }
        }
    }
}
