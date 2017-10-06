package com.atme.plugins.alipay;

/**
 * 支付结果回调
 */
public interface OnPayResultCallback {

    public void onSuccess(String status, String resultInfo);

    public void onFailed(String status, String resultInfo);
}