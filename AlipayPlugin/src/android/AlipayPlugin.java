package com.atme.plugins.alipay;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import android.app.Activity;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import com.atme.plugins.alipay.OnPayResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class echoes a string called from JavaScript.
 */
public class AlipayPlugin extends CordovaPlugin {

    private static Activity cordovaActivity;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        cordovaActivity = cordova.getActivity();
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        // 调用初始化
        if (action.equals("init")) {

            return true;
        }

        if (action.equals("doAlipay")) {
            String signParam = args != null ? args.getString(0) : "";
            this.doAlipay(signParam, callbackContext);
            return true;
        }
        callbackContext.error("method not find!");
        return false;
    }

    /**
     * 初始化插件
     */
    public void init(){

    }

    /**
     * 支付宝支付方法
     * @param signParam
     * @param callbackContext
     */
    public void doAlipay(final String signParam, final CallbackContext callbackContext){
        AliPayCenter.getInstance().doAlipay(cordovaActivity, signParam);
        AliPayCenter.getInstance().addOnPayResultCallback(
            new OnPayResultCallback(){
                // 支付成功
                public void onSuccess(String status, String resultInfo){
                    callbackContext.success(status);
                }
                // 支付失败
                public void onFailed(String status, String resultInfo){
                    callbackContext.success(status);
                }
            });
    }
}
