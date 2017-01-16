package com.happybot.vcoupon.model.retrofit;

import com.happybot.vcoupon.model.Voucher;

import java.util.List;

public class VoucherListResponse extends ResponseObject {
    private List<Voucher> vouchers;

    public List<Voucher> getVouchers() {
        return vouchers;
    }
}
