package com.happybot.vcoupon.model.retrofit;

import java.util.ArrayList;
import java.util.List;

public class ResponseObject {

    // Call api successfully
    private boolean success;

    // Result message
    private String resultMessage;

    // List error message if server response error
    private List<String> errorMessage;

    public ResponseObject() {
        success = false;
        resultMessage = "";
        errorMessage = new ArrayList<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }
}
