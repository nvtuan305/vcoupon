package com.happybot.vcoupon.exception;

/**
 * Created by btloc on 12/2/16.
 */

public class ServiceException extends BaseException {

    private int code;

    public ServiceException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
