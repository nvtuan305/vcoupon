package com.happybot.vcoupon.service.retrofitutil;

import android.util.Log;

import com.google.gson.Gson;
import com.happybot.vcoupon.exception.BaseException;
import com.happybot.vcoupon.exception.NetworkException;
import com.happybot.vcoupon.exception.ServiceException;
import com.happybot.vcoupon.model.retrofit.ResponseObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;


public class TranslateRetrofitException {

    private static final Logger LOG = LoggerFactory.getLogger(TranslateRetrofitException.class);

    public static <T extends ResponseObject> BaseException translateRetrofitException(Call<T> call, Throwable throwable) {
        if (call.isCanceled()) {
            return null;
        }

        LOG.error(throwable.getMessage());
        LOG.trace(Log.getStackTraceString(throwable));

        if (throwable instanceof IOException) {
            return new NetworkException("Có lỗi xảy ra với kết nối mạng. Vui lòng kiểm tra lại kết nối", throwable);
        } else {
            //return new BaseException("Có lỗi xảy ra. Vui lòng thử lại!", throwable);
            return new BaseException(throwable.getMessage(), throwable);
        }
    }

    public static <T extends ResponseObject> BaseException translateServiceException(Call<T> call, Response<T> response) {

        if (response.body() == null) {
            // This case should not happen. You must have something wrong!
            String msg = "There is something wrong with request. Please check your request again";

            try {
                ResponseObject responseObject = new Gson().fromJson(response.errorBody().string(), ResponseObject.class);
                return new ServiceException(response.code(), responseObject.getResultMessage(), null);

            } catch (Exception e) {

                LOG.error(e.getMessage());
                LOG.trace(Log.getStackTraceString(e));

                return new ServiceException(response.code(), msg, null);
            }
        } else {
            return null;
        }
    }
}
