package com.happybot.vcoupon.service.retrofitutil;

import com.happybot.vcoupon.exception.BaseException;
import com.happybot.vcoupon.model.retrofit.ResponseObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class TranslateRetrofitCallback<T extends ResponseObject> implements Callback<T> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        // Catch exception for response
        BaseException exception = TranslateRetrofitException.translateServiceException(call, response);

        // Finish request
        onFinish(call, response.body(), exception);
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        // Catch exception
        BaseException exception = TranslateRetrofitException.translateRetrofitException(call, throwable);

        // Finish request
        onFinish(call, null, exception);
    }

    public void onFinish(Call<T> call, T responseObject, BaseException exception) {
        if (exception == null) {
            LOG.debug("Response is OK. Start parsing json data");
        } else {
            LOG.debug("Request failed. Throw exception");
        }
    }
}
