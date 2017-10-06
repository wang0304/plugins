/**
 * 
 */
package cn.jmessage.android.uikit.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

/**
 *
 */
public class PhoneUtil {

    public static void sms(Context context, String smsNumber, String smsBody) {
        Uri uri = Uri.parse("smsto:" + smsNumber);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.addCategory(Intent.CATEGORY_DEFAULT);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    /**
     * 
     */
    public static void call(Context context, String telNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        context.startActivity(intent);
    }

    /**
     * 
     */
    public static void invokeBrowser(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 
     */
    public static void invokeVibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = { 100, 400, 100, 400 }; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);
    }

    @SuppressWarnings("deprecation")
    public static int getSdkVersionInt() {
        int SDK_VERSION_CODE = -1;
        try {
            SDK_VERSION_CODE = Integer.parseInt(Build.VERSION.SDK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SDK_VERSION_CODE;
    }

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
