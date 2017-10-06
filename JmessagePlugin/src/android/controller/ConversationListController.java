package com.baimei.jmessage.controller;
/**
 * Created by baimei on 16/9/14.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.baimei.jmessage.common.DicConstants;
import com.baimei.jmessage.common.InterfaceFactory;
import com.baimei.jmessage.common.ResultObject;
import com.baimei.jmessage.model.MyConversation;
import com.baimei.jmessage.model.MyMessage;
import com.baimei.jmessage.utils.ResponseCode;
import com.baimei.jmessage.utils.SqLog;

import org.apache.cordova.CallbackContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jmessage.android.uikit.chatting.utils.Event;
import cn.jmessage.android.uikit.conversation.SortConvList;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import de.greenrobot.event.EventBus;

/**
 * 会话列表
 * author:
 * create: 16/9/14
 */
public class ConversationListController {

    private final String TAG = "jchat";
    private Activity context;
    private List<Conversation> mDatas;
    private InterfaceFactory.OnConversatioListNotifyCallback notifyCallback;
    private InterfaceFactory.RequestDataCallback addFriendCallback;
    // 线程池
    private ExecutorService mLoaderThreadPool = Executors.newFixedThreadPool(1);

    public static ConversationListController instance;
    public static ConversationListController getInstance(){
        if(instance == null){
            instance = new ConversationListController();
        }
        return instance;
    }


    /**
     * 初始化会话列表
     */
    public void initCtrl(Activity context, InterfaceFactory.OnConversatioListNotifyCallback callback){
        this.context = context;
        this.notifyCallback = callback;

        //
        initHandler();

        // 接收广播
        initReceiver();

        // 监听事件
        initNotifyEvent();
    }

    /**
     * 初始化通知事件
     */
    private void initNotifyEvent(){
        //订阅接收消息,子类只要重写onEvent就能收到消息
        JMessageClient.registerEventReceiver(this);
    }


    /**
     * 获取会话列表
     * @param callback
     */
    public void getConversationList(final InterfaceFactory.OnConversatioListDataCallback callback){
        mLoaderThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                getConversationListBySyn(callback);
            }
        });
    }

    /**
     * 异步加载数据
     * @param callback
     */
    private void getConversationListBySyn(InterfaceFactory.OnConversatioListDataCallback callback){
        mDatas = JMessageClient.getConversationList();
        if(null != mDatas){
            //对会话列表进行时间排序
            if (null != mDatas && mDatas.size() > 1) {
                SortConvList sortList = new SortConvList();
                Collections.sort(mDatas, sortList);
            }
        } else {
            if(callback != null){
                callback.callback(0, new ResultObject(null));
            }
            SqLog.i(TAG, TAG + "((((((((((((((((((((((((( 没有会话列表数据 ))))))))))))))))))))))");
            return;
        }

        SqLog.i(TAG, TAG + "_会话列表mDatas1：" + (mDatas != null ? mDatas.toString() : ""));
        SqLog.i(TAG, TAG + "/////////////////////////////////////////////////");

        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        List<String> newStrList = new ArrayList<String>();
        List<MyConversation> newConverList = new ArrayList<MyConversation>();
        List<JSONObject> newJsonList = new ArrayList<JSONObject>();

        int unReadCount = 0;
        try {
            for(Conversation info: mDatas){

                unReadCount += info.getUnReadMsgCnt();
                //String jsonStr = StringUtil.format2JSONStr(info.toString(), "Conversation");

                MyConversation myConversation = MyConversation.getInstance().setConversation(info);
                newConverList.add(myConversation);
                //SqLog.i(TAG, TAG + "_自定义会话信息MyConversation：" + JSONObject.toJSONString(myConversation));
                //SqLog.i(TAG, TAG + "********************************************");

                String jsonStr = JSONObject.toJSONString(myConversation);
                newStrList.add(jsonStr);

                //SqLog.i(TAG, TAG + "_会话信息Conversation_json：" + jsonStr);
                //SqLog.i(TAG, TAG + "                                      ");

                newJsonList.add(JSONObject.parseObject(jsonStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SqLog.i(TAG, TAG + "/////////////////////////////////////////////////");
        SqLog.i(TAG, TAG + "_会话列表List<Map>：" + newJsonList.toString());

        if(callback != null){
            callback.callback(unReadCount, new ResultObject(newJsonList));
        }
    }

    /**
     * 添加好友
     * @param targetId
     * @param callback
     */
    public void addFriend(final Context context, final String targetId, final InterfaceFactory.RequestDataCallback callback){
        this.addFriendCallback = callback;

        // 用户名为空
        if (TextUtils.isEmpty(targetId)) {
            String isNull = "用户名不能为空";
            if(callback != null) callback.error(0, isNull);
            return;
        }
        // 用户名是否是自己
        else if (targetId.equals(JMessageClient.getMyInfo().getUserName())
                || targetId.equals(JMessageClient.getMyInfo().getNickname())) {
            String cantMySelf = "不能添加自己为好友";
            if(callback != null) callback.error(0, cantMySelf);
            return;
        }
        // 好友已存在
        else if (isExistConv(targetId, null)) {
            String cantMySelf = "好友已存在";
            if(callback != null) callback.error(0, cantMySelf);
            return;
        }

        // 添加好友
        else {
            SqLog.i(TAG, TAG + "_plugin -------- 添加好友 ---------");
            JMessageClient.getUserInfo(targetId, new GetUserInfoCallback() {
                @Override
                public void gotResult(final int status, String desc, final UserInfo userInfo) {
                    SqLog.i(TAG, TAG + "_plugin -------- addFriend_status: " + status);
                    if (status == 0) {
                        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
                                @Override
                                public void gotResult(int status, String desc, Bitmap bitmap) {
                                    if (status == 0) {
                                        //mController.getAdapter().notifyDataSetChanged();
                                    } else {
                                        //if(callback != null) callback.error(status, desc);
                                    }
                                }
                            });
                        }

                        Conversation conversation = Conversation.createSingleConversation(targetId);
                        SqLog.i(TAG, TAG + "_plugin -------- addFriend_conver: " + conversation);
                        if (null != conversation) {
                            MyConversation myConversation = MyConversation.getInstance().setConversation(conversation);
                            if(callback != null) callback.success(status, new ResultObject(myConversation));
                        } else {
                            if(callback != null) callback.success(status, null);
                        }
                    } else {
                        if(callback != null) callback.error(status, ResponseCode.onHandle(context, status));
                    }
                }
            });
        }
    }

    /**
     * 判断用户是否存在
     * @param targetId
     * @return
     */
    private boolean isExistConv(String targetId, final CallbackContext callbackContext) {
        SqLog.i(TAG, TAG + "_plugin -------- isExistConv ---------");
        Conversation conv = JMessageClient.getSingleConversation(targetId);
        return conv != null;
    }

    private HandlerThread mThread;
    private void initHandler(){
        mThread = new HandlerThread("Work on MainActivity");
        mThread.start();
        mBackgroundHandler = new BackgroundHandler(mThread.getLooper());
    }

    private NetworkReceiver mReceiver;
    /**
     * 初始化广播
     */
    private void initReceiver() {
        mReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        context.registerReceiver(mReceiver, filter);
    }

    //监听网络状态的广播
    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeInfo = manager.getActiveNetworkInfo();
                if (null == activeInfo) {
                    if(notifyCallback != null) notifyCallback.callback(DicConstants.NOTIFY_NETWORK_ERROR, null);
                } else {
                    if(notifyCallback != null) notifyCallback.callback(DicConstants.NOTIFY_NETWORK_OK, null);
                }
            }
        }

    }




    /**
     * 在会话列表中接收消息
     *
     * @param event 消息事件
     */
    public void onEvent(MessageEvent event) {
        Message msg = event.getMessage();
        String msgJson = JSONObject.toJSONString(MyMessage.getInstance().setMessage(msg));
        Log.d(TAG, "收到消息：msg = " + msgJson);

        mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST,
                msgJson));

//        // 获取会话对象
//        ConversationType convType = msg.getTargetType();
//        if (convType == ConversationType.group) {
//            long groupID = ((GroupInfo) msg.getTargetInfo()).getGroupID();
//            Conversation conv = JMessageClient.getGroupConversation(groupID);
//            if (conv != null) {
//                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST,
//                        conv));
//            }
//        }
//
//        else {
//            final UserInfo userInfo = (UserInfo) msg.getTargetInfo();
//            final String targetID = userInfo.getUserName();
//            final Conversation conv = JMessageClient.getSingleConversation(targetID, userInfo.getAppKey());
//            if (conv != null) {
//                context.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //如果设置了头像
//                        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
//                            //如果本地不存在头像
//                            userInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
//                                @Override
//                                public void gotResult(int status, String desc, Bitmap bitmap) {
//                                    if (status == 0) {
//                                        // mConvListController.getAdapter().notifyDataSetChanged();
//                                        // 刷新列表
//                                    } else {
//                                        // 提示异常信息
//                                       String tipMsg = ResponseCode.onHandle(context, status);
//                                    }
//                                }
//                            });
//                        }
//                    }
//                });
//
//                mBackgroundHandler.sendMessage(mBackgroundHandler.obtainMessage(REFRESH_CONVERSATION_LIST,
//                        conv));
//            }
//        }
    }

    private static final int REFRESH_CONVERSATION_LIST = 0x3000;
    private BackgroundHandler mBackgroundHandler;
    private class BackgroundHandler extends Handler {
        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_CONVERSATION_LIST:
                    //Conversation conv = (Conversation) msg.obj;
                    //mConvListController.getAdapter().setToTop(conv);

                    SqLog.i("jchat", "----------- refresh conver list ----------");

                    // 刷新会话列表
                    String msgJson = (String) msg.obj;
                    if(notifyCallback != null) notifyCallback.callback(DicConstants.NOTIFY_CONVER_JMSGMESSAGE, new ResultObject(msgJson));
                    break;
            }
        }
    }

    // -------------------------  onEvent ----------------------------
    /**
     * 收到创建单聊的消息
     *
     * @param event 可以从event中得到targetID
     */
    public void onEventMainThread(Event.StringEvent event) {
        Log.d(TAG, "StringEvent execute");
        String targetId = event.getTargetId();
        String appKey = event.getAppKey();
        Conversation conv = JMessageClient.getSingleConversation(targetId, appKey);
        if (conv != null) {
//            mConvListController.getAdapter().addNewConversation(conv);
        }
    }

    /**
     * 收到创建或者删除群聊的消息
     *
     * @param event 从event中得到groupID以及flag
     */
    public void onEventMainThread(Event.LongEvent event) {
        long groupId = event.getGroupId();
        Conversation conv = JMessageClient.getGroupConversation(groupId);
        if (conv != null && event.getFlag()) {
//            mConvListController.getAdapter().addNewConversation(conv);
        } else {
//            mConvListController.getAdapter().deleteConversation(groupId);
        }
    }

    /**
     * 收到保存为草稿事件
     * @param event 从event中得到Conversation Id及草稿内容
     */
    public void onEventMainThread(Event.DraftEvent event) {
        String draft = event.getDraft();
        String targetId = event.getTargetId();
        String targetAppKey = event.getTargetAppKey();
        Conversation conv;
        if (targetId != null) {
            conv = JMessageClient.getSingleConversation(targetId, targetAppKey);
        } else {
            long groupId = event.getGroupId();
            conv = JMessageClient.getGroupConversation(groupId);
        }
        //如果草稿内容不为空，保存，并且置顶该会话
        if (!TextUtils.isEmpty(draft)) {
//            mConvListController.getAdapter().putDraftToMap(conv.getId(), draft);
//            mConvListController.getAdapter().setToTop(conv);
            //否则删除
        } else {
//            mConvListController.getAdapter().delDraftFromMap(conv.getId());
        }
    }


    /**
     * 退出会话
     */
    public void exitConversationList(){
        EventBus.getDefault().unregister(this);
        context.unregisterReceiver(mReceiver);
        mBackgroundHandler.removeCallbacksAndMessages(null);
        mThread.getLooper().quit();
    }
}
