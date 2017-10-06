package com.atme.plugins.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import android.util.Log;

import com.alipay.sdk.*;
import com.alipay.sdk.app.PayTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 支付宝支付中心
 */
public class AliPayCenter {

    private static final String TAG = "AliPayCenter";
    private Activity activity;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                	if(null == msg.obj){
                		Toast.makeText(activity, "支付失败!", 0).show();
                		return;
                	}

                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(activity, "支付成功!", 0).show();
                        Log.d(TAG, "alipay-支付成功");
                        notifyPaySuccess(resultStatus, resultInfo);

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {

                            Toast.makeText(activity, "支付结果确认中!", 0).show();
                            Log.d(TAG, "alipay-支付结果确认中");

                            notifyPayFailed(resultStatus, resultInfo);

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(activity, "支付失败!", 0).show();
                            Log.d(TAG, "alipay-支付失败");

                            notifyPayFailed(resultStatus, resultInfo);
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    //ToastUtils.showLongToast("检查结果为：" + msg.obj);
                    Toast.makeText(activity, "检查结果为：" + msg.obj, 0).show();
                    Log.d(TAG, "alipay-检查结果为：" + msg.obj);
                    break;
                }
                default:
                    break;
            }
        };
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     *
     */
    public void doAlipay(final Activity alipayActivity, String alipayParams) {
        Log.i(TAG, "========== doAlipay ==========");
        activity = alipayActivity;

        final String payInfo = alipayParams;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
            	// 构造PayTask 对象
                PayTask alipay = new PayTask(alipayActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    // alipay

    // 商户PID
    public static final String PARTNER = "";
    // 商户收款账号
    public static final String SELLER = "";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE =
            "-----BEGIN RSA PRIVATE KEY-----MIICXQIBAAKBgQC0KPH2NSUkbX1KadBNa4mUi2L66eEzksncirwrmp4C2oo/Ts07zn+tgL4ulI5xQuKBUvKpDo+hDTziQiEDJi13733urUxZA6J9oum+nK0r3ZrKuSDCmlKKFONS5tEyNyXHY5M39NOb//KwydSrfckOdxaQmfCIgj1qMPRIz3mszwIDAQABAoGBAJay+VLduafy/i7UvC1GmtrqjW0dXgdp7fjRA/3lY+83JxU1sW8pmJtlabBFC7GqJRXQcLMBPxuUmy0X681ajgSv5xCTSZ7+m7qePZxvgBvqu26jxhgLHx0dePadBSnMKSdnemDCNdLnodVvWlaYiw7x/qZ1BHuJvoWvSNXJZ51hAkEA7cHZCCPv3k8/Y4P4GX2ZIRMMLgUdwCzK2oQT4fxA0gs6cSN+IzC539YHAF47I6uXPA+g96E5FwjaVtcuAKPx+wJBAMH7vc3UxdTcUy2OyGAMKPE1fi2dVdU9HRuF1lSrEcdX1uU9kh9i3TS7ALcG+GYP6C3xshGM0ZDaaxBiTnuvzD0CQG9MWmO0JvqjkGHuijqsj/1qpD36ySWIEmHeiEw2wMVS2kXSPp8MlUaSM27ZrceR8Nb93fVt9IKy8zrJulA0bvcCQQC3kga9OUR5uoKQU1V15yv7j7a3bfPjFrFr8Uyx1S2+6nY3e/vC7ekN1IXJrrP0ycrbZ6UujIdpP/kfiRhiXiu1AkALlVYpCv4FCGqT8oGBerPgR3bw2LA4ev8bvkm43H5eHd4nDOvhELMvY5oDRPnY4rEuFwLUWALfcfbFyuHGPb4L-----END RSA PRIVATE KEY-----";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";

//    /**
//     * check whether the device has authentication alipay account. 查询终端设备是否存在支付宝认证账户
//     *
//     */
//    public void check(final Activity alipayActivity) {
//        Runnable payRunnable = new Runnable() {
//            @Override
//            public void run() {
//            	// 构造PayTask 对象
//                PayTask payTask = new PayTask(alipayActivity);
//                // 调用查询接口，获取查询结果
//                boolean isExist = payTask.checkAccountIfExist();
//
//                Message msg = new Message();
//                msg.what = SDK_CHECK_FLAG;
//                msg.obj = isExist;
//                mHandler.sendMessage(msg);
//            }
//        };
//
//        // 必须异步调用
//        Thread payThread = new Thread(payRunnable);
//        payThread.start();
//    }

    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public void getSDKVersion(final Activity alipayActivity) {
        PayTask payTask = new PayTask(alipayActivity);
        String version = payTask.getVersion();
        //ToastUtils.showShortToast(version);
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public String getOrderInfoT(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private ArrayList<OnPayResultCallback> mOnPayResultCallbacks = new ArrayList<OnPayResultCallback>();

    public void addOnPayResultCallback(OnPayResultCallback callback) {
        if (null != callback && !mOnPayResultCallbacks.contains(callback)) {
            mOnPayResultCallbacks.add(callback);
        }
    }

    public void removeOnPayResultCallback(OnPayResultCallback callback) {
        if (null != callback && mOnPayResultCallbacks.contains(callback)) {
            mOnPayResultCallbacks.remove(callback);
        }
    }

    private void notifyPaySuccess(String status, String msg) {
        for (OnPayResultCallback callback : mOnPayResultCallbacks) {
            callback.onSuccess(status, msg);
        }
    }

    private void notifyPayFailed(String status, String msg) {
        for (OnPayResultCallback callback : mOnPayResultCallbacks) {
            callback.onFailed(status, msg);
        }
    }

    // ------------------------------------------------------------------
    private static AliPayCenter mInstance;
    public static AliPayCenter getInstance() {
        if (null == mInstance) {
            synchronized (AliPayCenter.class) {
                mInstance = new AliPayCenter();
            }
        }
        return mInstance;
    }

    private AliPayCenter() {
    }
}
