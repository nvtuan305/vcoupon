package com.happybot.vcoupon.model;

public class Voucher {
    private String _id;
    private String _user;
    private Promotion _promotion;
    private String voucherCode;
    private String qrCode;
    private boolean isChecked;
    private long registeredDate;

    public String getId() {
        return _id;
    }

    public String getUser() {
        return _user;
    }

    public Promotion getPromotion() {
        return _promotion;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public long getRegisteredDate() {
        return registeredDate;
    }
}
