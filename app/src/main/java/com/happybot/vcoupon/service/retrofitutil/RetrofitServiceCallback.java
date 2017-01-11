package com.happybot.vcoupon.service.retrofitutil;

import android.os.AsyncTask;

import retrofit2.Call;

public interface RetrofitServiceCallback<Result> {

    void setAsyncTask(AsyncTask task);

    void setCall(Call call);

    void cancel();

    void onPreExecute();

    void onPostExecute(Result result, Throwable throwable);
}