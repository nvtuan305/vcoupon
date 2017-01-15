package com.happybot.vcoupon.model.retrofit;

import com.happybot.vcoupon.model.Voucher;

public class VoucherResponse extends ResponseObject {
    private Voucher voucher;

    public Voucher getVoucher() {
        return voucher;
    }
}