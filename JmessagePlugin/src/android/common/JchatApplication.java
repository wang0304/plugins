package com.baimei.jmessage.common;
/**
 * Created by baimei on 16/9/18.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.baimei.jmessage.utils.SqLog;

import java.util.List;

/**
 * author:
 * create: 16/9/18
 */
public class JchatApplication {

    private static final String JCHAT_CONFIGS = "JChat_configs";
    public static final String TARGET_APP_KEY = "targetAppKey";
    public static final String TARGET_ID = "targetId";
    public static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_NAME = "groupName";
    public static final String STATUS = "status";
    public static final String POSITION = "position";
    public static final String MsgIDs = "msgIDs";
    public static final String DRAFT = "draft";
    public static final String DELETE_MODE = "deleteMode";
    public static final String MEMBERS_COUNT = "membersCount";
    public static String PICTURE_DIR = "sdcard/JChatDemo/pictures/";

    public static JchatApplication instance;
    public static JchatApplication getInstance(){
        if(instance == null){
            instance = new JchatApplication();
        }
        return instance;
    }

    /**
     * 获取JMessage AppKey
     * @param activity
     * @return
     */
    public String getAppKey(Activity activity){
        ApplicationInfo appInfo = null;
        try {
            appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("JPUSH_APPKEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取应用包名
     * @return
     */
    public String getPackageName(Context context){
        PackageItemInfo PackageItemInfo = new PackageItemInfo(context.getApplicationInfo());
        return PackageItemInfo.packageName;
    }


    /** 通过包名去启动一个Activity*/
    public  void openApp(Context mainContext, String packageName) {
        // TODO 把应用杀掉然后再启动，保证进入的是第一个页面
        PackageInfo pi = null;
        try {
            pi = mainContext.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        PackageManager pManager = mainContext.getApplicationContext().getPackageManager();
        List apps = pManager.queryIntentActivities(resolveIntent,
                0);

        ResolveInfo ri = (ResolveInfo) apps.iterator().next();
        if (ri != null) {
            String startappName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            SqLog.i("jchat", "@@@ 启动的activity是: " + startappName + ":" + className);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName(startappName, className);

            intent.setComponent(cn);
            mainContext.getApplicationContext().startActivity(intent);
        }
    }

    /** 通过包名去启动一个Activity*/
    public Intent getMainActivityIntent(Context mainContext, String packageName) {
        // TODO 把应用杀掉然后再启动，保证进入的是第一个页面
        PackageInfo pi = null;
        try {
            pi = mainContext.getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        PackageManager pManager = mainContext.getApplicationContext().getPackageManager();
        List apps = pManager.queryIntentActivities(resolveIntent,
                0);

        ResolveInfo ri = (ResolveInfo) apps.iterator().next();
        if (ri != null) {
            String startappName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            SqLog.i("jchat", "@@@ 启动的activity是: " + startappName + ":" + className);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName cn = new ComponentName(startappName, className);

            intent.setComponent(cn);

            return intent;
            //mainContext.getApplicationContext().startActivity(intent);
        }

        return null;
    }
}
