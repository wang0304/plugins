package com.baimei.jmessage.controller;
/**
 * Created by baimei on 16/9/18.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.baimei.jmessage.common.DicConstants;
import com.baimei.jmessage.common.InterfaceFactory;
import com.baimei.jmessage.common.JchatApplication;
import com.baimei.jmessage.common.ResultObject;
import com.baimei.jmessage.model.MyConversation;
import com.baimei.jmessage.utils.JMessageJSONUtils;
import com.baimei.jmessage.utils.SqLog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import cn.jmessage.android.uikit.chatting.utils.FileHelper;
import cn.jmessage.android.uikit.chatting.utils.IdHelper;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 会话详情
 * author:
 * create: 16/9/18
 */
public class ConversationDetailController {

    private Activity activity;
    private InterfaceFactory.OnConversatioListNotifyCallback notifyCallback;

    private MyConversation conversation;

    protected float mDensity;
    protected int mDensityDpi;
    protected int mAvatarSize;
    protected int mWidth;
    protected int mHeight;

    private static final String TAG = "jchat";
    private static final String MEMBERS_COUNT = "membersCount";
    private static final String GROUP_NAME = "groupName";
    private static final String DRAFT = "draft";
    private static final String MsgIDs = "msgIDs";
    private static final String NAME = "name";
    public static final String NICKNAME = "nickname";
    private static final String TARGET_ID = "targetId";
    private static final String TARGET_APP_KEY = "targetAppKey";
    private static final String GROUP_ID = "groupId";
    private static final int REQUEST_CODE_TAKE_PHOTO = 4;
    private static final int REQUEST_CODE_SELECT_PICTURE = 6;
    private static final int RESULT_CODE_SELECT_PICTURE = 8;
    private static final int REQUEST_CODE_CHAT_DETAIL = 14;
    private static final int RESULT_CODE_CHAT_DETAIL = 15;
    private static final int RESULT_CODE_FRIEND_INFO = 17;
    private static final int REFRESH_LAST_PAGE = 0x1023;
    private static final int REFRESH_CHAT_TITLE = 0x1024;
    private static final int REFRESH_GROUP_NAME = 0x1025;
    private static final int REFRESH_GROUP_NUM = 0x1026;

    private UIHandler mUIHandler;
    private boolean mIsSingle = true;
    private boolean isInputByKeyBoard = true;
    private boolean mShowSoftInput = false;
    private MsgDataAdapter mChatAdapter;
//    private ChatView mChatView;
    private Context mContext;
    private Conversation mConv;
    private Dialog mDialog;
    private MyReceiver mReceiver;
    private long mGroupId;
    private String mGroupName;
    private GroupInfo mGroupInfo;
    private String mTargetId;
    private String mTargetAppKey;
    private String mPhotoPath = null;

    Window mWindow;
    InputMethodManager mImm;

    public static ConversationDetailController instance;
    public static ConversationDetailController getInstance(){
        if(instance == null){
            instance = new ConversationDetailController();
        }
        return instance;
    }

    public void initCtrl(Activity context, String targetId, String conversationType, InterfaceFactory.OnConversatioListNotifyCallback callback){
        this.activity = context;
        this.notifyCallback = callback;

        // 群聊
        if("2".equals(conversationType)){
            this.mTargetId = "";
            this.mGroupId = Integer.parseInt(targetId);
        }

        // 单聊
        else if("1".equals(conversationType)) {
            this.mTargetId = targetId;
            this.mGroupId = 0;
        } else;

        init(context);

        initDisplayMetries();
    }

    /**
     * 初始化屏幕分辨率
     */
    private void initDisplayMetries(){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mDensityDpi = dm.densityDpi;
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mAvatarSize = (int) (50 * mDensity);
    }

    private void init(Activity activity) {
        mContext = activity;
        mUIHandler = new UIHandler(this);

        //注册接收消息事件
        JMessageClient.registerEventReceiver(this);


//        mChatView = (ChatView) findViewById(IdHelper.getViewID(this, "jmui_chat_view"));
//        mChatView.initModule(mDensity, mDensityDpi);
//        this.mWindow = getWindow();
//
//        this.mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        mChatView.setListeners(this);
//        mChatView.setOnTouchListener(this);
//        mChatView.setOnSizeChangedListener(this);
//        mChatView.setOnKbdStateListener(this);



        initReceiver();

        mTargetAppKey = JchatApplication.getInstance().getAppKey(activity);


        if (!TextUtils.isEmpty(mTargetId)) {
            mIsSingle = true;
            mConv = JMessageClient.getSingleConversation(mTargetId, mTargetAppKey);
            if (mConv != null) {
                UserInfo userInfo = (UserInfo)mConv.getTargetInfo();
                if (TextUtils.isEmpty(userInfo.getNickname())) {
                    // 刷新标题
                    if(notifyCallback != null) {
                        String title = JMessageJSONUtils.formatTitleToJSONString(userInfo.getUserName(), 0);
                        notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                    }
                    //mChatView.setChatTitle(userInfo.getUserName());
                }else {
                    // 刷新标题
                    if(notifyCallback != null) {
                        String title = JMessageJSONUtils.formatTitleToJSONString(userInfo.getNickname(), 0);
                        notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                    }
                    //mChatView.setChatTitle(userInfo.getNickname());
                }
            } else {
                mConv = Conversation.createSingleConversation(mTargetId, mTargetAppKey);
                UserInfo userInfo = (UserInfo)mConv.getTargetInfo();
                if (TextUtils.isEmpty(userInfo.getNickname())) {
                    //mChatView.setChatTitle(userInfo.getUserName());
                    // 刷新标题
                    if(notifyCallback != null) {
                        String title = JMessageJSONUtils.formatTitleToJSONString(userInfo.getUserName(), 0);
                        notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                    }

                }else {
                    // 刷新标题
                    //mChatView.setChatTitle(userInfo.getNickname());
                    if(notifyCallback != null) {
                        String title = JMessageJSONUtils.formatTitleToJSONString(userInfo.getNickname(), 0);
                        notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                    }
                }
            }

            mChatAdapter = new MsgDataAdapter(mContext, mTargetId, mTargetAppKey, longClickListener,
                    new InterfaceFactory.OnConversatioDetailNotifyCallback(){
                        @Override
                        public void callback(String type, ResultObject obj) {
                            if(notifyCallback != null) notifyCallback.callback(type, obj);
                        }
                    });

        } else {
            mIsSingle = false;
            SqLog.d(TAG, "GroupId : " + mGroupId);

            //UIKit 直接用getGroupInfo更新标题,而不用考虑从创建群聊跳转过来
            JMessageClient.getGroupInfo(mGroupId, new GetGroupInfoCallback() {
                @Override
                public void gotResult(int status, String desc, GroupInfo groupInfo) {
                    if (status == 0) {
                        mGroupInfo = groupInfo;
                        // 刷新标题
                        //mChatView.setChatTitle(groupInfo.getGroupName());
                        if(notifyCallback != null) {
                            String title = JMessageJSONUtils.formatTitleToJSONString(groupInfo.getGroupName(), 0);
                            notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                        }
                    }
                }
            });

            mConv = JMessageClient.getGroupConversation(mGroupId);
            if (mConv == null) {
                mConv = Conversation.createGroupConversation(mGroupId);
            }

            mChatAdapter = new MsgDataAdapter(mContext, mGroupId, longClickListener,
                    new InterfaceFactory.OnConversatioDetailNotifyCallback(){
                        @Override
                        public void callback(String type, ResultObject obj) {
                            if(notifyCallback != null) notifyCallback.callback(type, obj);
                        }
                    });
        }


        // 显示草稿
//        String draft = intent.getStringExtra(DRAFT);
//        if (draft != null && !TextUtils.isEmpty(draft)) {
//            mChatView.setInputText(draft);
//        }
//        mChatView.setChatListAdapter(mChatAdapter);



//        mChatAdapter.initMediaPlayer();
//        //监听下拉刷新
//        mChatView.getListView().setOnDropDownListener(new DropDownListView.OnDropDownListener() {
//            @Override
//            public void onDropDown() {
//                mUIHandler.sendEmptyMessageDelayed(REFRESH_LAST_PAGE, 1000);
//            }
//        });
//        // 滑动到底部
//        mChatView.setToBottom();

        SqLog.i("jmessage", "jmessage_ChatActivity_conv: " + mConv.toString());


        onResume(mTargetId, mGroupId);
    }

    // 监听耳机插入
    private void initReceiver() {
        mReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        activity.registerReceiver(mReceiver, filter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent data) {
            if (data != null) {
                //插入了耳机
                if (data.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                    if(mChatAdapter != null) mChatAdapter.setAudioPlayByEarPhone(data.getIntExtra("state", 0));
                }
            }
        }
    }

//    public void onClick(View v) {
//        // 返回
//        if (v.getId() == IdHelper.getViewID(mContext, "jmui_return_btn")) {
//            mConv.resetUnreadCount();
//            dismissSoftInput();
//            JMessageClient.exitConversation();
//
//            //发送保存为草稿事件到会话列表
//            if (mIsSingle) {
//                EventBus.getDefault().post(new Event.DraftEvent(mTargetId, mTargetAppKey, mChatView.getChatInput()));
//            } else {
//                EventBus.getDefault().post(new Event.DraftEvent(mGroupId, mChatView.getChatInput()));
//            }
//            //finish();
//
//        }
//
//        //
//        else if (v.getId() == IdHelper.getViewID(mContext, "jmui_right_btn")) {
//            if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
//                mChatView.dismissMoreMenu();
//            }
//            dismissSoftInput();
//            //TODO
//            //startChatDetailActivity(mTargetId, mTargetAppKey, mGroupId);
//        }
//
//        // 切换输入
//        else if (v.getId() == IdHelper.getViewID(mContext, "jmui_switch_voice_ib")) {
//            mChatView.dismissMoreMenu();
//            isInputByKeyBoard = !isInputByKeyBoard;
//            //当前为语音输入，点击后切换为文字输入，弹出软键盘
//            if (isInputByKeyBoard) {
//                mChatView.isKeyBoard();
//                showSoftInputAndDismissMenu();
//            } else {
//                //否则切换到语音输入
//                mChatView.notKeyBoard(mConv, mChatAdapter, mChatView);
//                if (mShowSoftInput) {
//                    if (mImm != null) {
//                        mImm.hideSoftInputFromWindow(mChatView.getInputView().getWindowToken(), 0); //强制隐藏键盘
//                        mShowSoftInput = false;
//                    }
//                } else if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
//                    mChatView.dismissMoreMenu();
//                }
//                SqLog.i(TAG, "setConversation success");
//            }
//        }
//
//        // 发送文本消息
//        else if (v.getId() == IdHelper.getViewID(mContext, "jmui_send_msg_btn")) {
//            String msgContent = mChatView.getChatInput();
//            mChatView.clearInput();
//            mChatView.setToBottom();
//            if (msgContent.equals("")) {
//                return;
//            }
//            TextContent content = new TextContent(msgContent);
//            final Message msg = mConv.createSendMessage(content);
//            mChatAdapter.addMsgToList(msg);
//            JMessageClient.sendMessage(msg);
//
//        }
//
//        // 点击添加按钮，弹出更多选项菜单
//        else if (v.getId() == IdHelper.getViewID(mContext, "jmui_add_file_btn")) {
//            //如果在语音输入时点击了添加按钮，则显示菜单并切换到输入框
//            if (!isInputByKeyBoard) {
//                mChatView.isKeyBoard();
//                isInputByKeyBoard = true;
//                mChatView.showMoreMenu();
//            } else {
//                //如果弹出软键盘 则隐藏软键盘
//                if (mChatView.getMoreMenu().getVisibility() != View.VISIBLE) {
//                    dismissSoftInputAndShowMenu();
//                    mChatView.focusToInput(false);
//                    //如果弹出了更多选项菜单，则隐藏菜单并显示软键盘
//                } else {
//                    showSoftInputAndDismissMenu();
//                }
//            }
//        }
//
//        // 拍照
//        else if (v.getId() == IdHelper.getViewID(mContext, "jmui_pick_from_camera_btn")) {
//            takePhoto();
//            if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
//                mChatView.dismissMoreMenu();
//            }
//        }
//
//        // 选择本地图片
//        else if (v.getId() == IdHelper.getViewID(mContext, "jmui_pick_from_local_btn")){
//            if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
//                mChatView.dismissMoreMenu();
//            }
//            Intent intent = new Intent();
//            if (mIsSingle) {
//                intent.putExtra(TARGET_ID, mTargetId);
//                intent.putExtra(TARGET_APP_KEY, mTargetAppKey);
//            } else {
//                intent.putExtra(GROUP_ID, mGroupId);
//            }
////            if (!FileHelper.isSdCardExist()) {
////                Toast.makeText(this, IdHelper.getString(mContext, "sdcard_not_exist_toast"), Toast.LENGTH_SHORT).show();
////            } else {
////                intent.setClass(this, PickPictureTotalActivity.class);
////                startActivityForResult(intent, REQUEST_CODE_SELECT_PICTURE);
////            }
//        }
//    }

    private InterfaceFactory.OnConversatioDetailNotifyCallback loadMoreConverMsgListCallback;
    private String currentStart = "0";
    private String currentOffset = "18";
    /**
     * 加载更多消息
     */
    public void loadMoreConverDetailMsgList(String aStart, String aOffset,
                                            InterfaceFactory.OnConversatioDetailNotifyCallback callback){
        this.currentStart = aStart;
        this.currentOffset = aOffset;
        this.loadMoreConverMsgListCallback = callback;
        if(mChatAdapter != null) mChatAdapter.dropDownToRefresh(callback);
    }


    /**
     * 发送文本信息
     * @param text
     */
    public void sendTextMessage(String text){
        String msgContent = text;

//        mChatView.clearInput();
//        mChatView.setToBottom();

        if (msgContent.equals("")) {
            return;
        }
        TextContent content = new TextContent(msgContent);
        final Message msg = mConv.createSendMessage(content);

        // 加入列表
        if(mChatAdapter != null){
          mChatAdapter.addMsgToList(msg);
          //mChatAdapter.notifyDataSetChanged();
        }

        JMessageClient.sendMessage(msg);
    }

    /**
     * 拍照／选择图片
     */
    private void takePhoto() {
        if (FileHelper.isSdCardExist()) {
            mPhotoPath = FileHelper.createAvatarPath(null);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mPhotoPath)));
            try {
                activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            } catch (ActivityNotFoundException anf) {
                Toast.makeText(mContext, IdHelper.getString(mContext, "camera_not_prepared"),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, IdHelper.getString(mContext, "sdcard_not_exist_toast"),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理发送图片，刷新界面
     *
     * @param data intent
     */
    private void handleImgRefresh(Intent data) {
//        if(mChatAdapter != null) mChatAdapter.setSendImg(data.getIntArrayExtra(MsgIDs));
//        mChatView.setToBottom();
    }


//    public void onBackPressed() {
//        SqLog.d(TAG, "onBackPressed!");
//        if (RecordVoiceButton.mIsPressed) {
//            mChatView.dismissRecordDialog();
//            mChatView.releaseRecorder();
//            RecordVoiceButton.mIsPressed = false;
//        }
//        if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
//            mChatView.dismissMoreMenu();
//        } else {
//            if (mConv != null) {
//                mConv.resetUnreadCount();
//            }
//        }
//
//        //发送保存为草稿事件到会话列表界面,作为UIKit使用可以去掉
//        if (mIsSingle) {
//            EventBus.getDefault().post(new Event.DraftEvent(mTargetId,
//                    mTargetAppKey, mChatView.getChatInput()));
//        } else {
//            EventBus.getDefault().post(new Event.DraftEvent(mGroupId, mChatView.getChatInput()));
//        }
//    }
//
//
//    @Override
//    protected void onPause() {
//        RecordVoiceButton.mIsPressed = false;
//        JMessageClient.exitConversation();
//        SqLog.i(TAG, "[Life cycle] - onPause");
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        mChatAdapter.stopMediaPlayer();
//        if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
//            mChatView.dismissMoreMenu();
//        }
//        if (mConv != null) {
//            mConv.resetUnreadCount();
//        }
//        SqLog.i(TAG, "[Life cycle] - onStop");
//        super.onStop();
//    }

    protected void onResume(String targetId, long groupId) {
//        if (!RecordVoiceButton.mIsPressed) {
//            mChatView.dismissRecordDialog();
//        }


        if (!mIsSingle) {
            if (groupId != 0) {
                JMessageClient.enterGroupConversation(groupId);
            }
        } else if (null != targetId) {
            JMessageClient.enterSingleConversation(targetId, mTargetAppKey);
        }

        // 初始化音频播放器
        if(mChatAdapter != null) mChatAdapter.initMediaPlayer();
    }


//    /**
//     * 用于处理拍照发送图片返回结果以及从其他界面回来后刷新聊天标题
//     * 或者聊天消息
//     *
//     * @param requestCode 请求码
//     * @param resultCode  返回码
//     * @param data        intent
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
//            final Conversation conv = mConv;
//            try {
//                String originPath = mPhotoPath;
//                Bitmap bitmap = BitmapLoader.getBitmapFromFile(originPath, 720, 1280);
//                ImageContent.createImageContentAsync(bitmap, new ImageContent.CreateImageContentCallback() {
//                    @Override
//                    public void gotResult(int status, String desc, ImageContent imageContent) {
//                        if (status == 0) {
//                            Message msg = conv.createSendMessage(imageContent);
//                            Intent intent = new Intent();
//                            intent.putExtra(MsgIDs, new int[]{msg.getId()});
//                            handleImgRefresh(intent);
//                        }
//                    }
//                });
//            }  catch (NullPointerException e) {
//                SqLog.i(TAG, "onActivityResult unexpected result");
//            }
//        } else if (resultCode == RESULT_CODE_SELECT_PICTURE) {
//            handleImgRefresh(data);
//            //如果作为UIKit使用,去掉以下几段代码
//        } else if (resultCode == RESULT_CODE_CHAT_DETAIL) {
//            if (!mIsSingle) {
//                GroupInfo groupInfo = (GroupInfo) mConv.getTargetInfo();
//                UserInfo userInfo = groupInfo.getGroupMemberInfo(JMessageClient.getMyInfo().getUserName());
//                //如果自己在群聊中，同时显示群人数
//                if (userInfo != null) {
//                    if (TextUtils.isEmpty(data.getStringExtra(NAME))) {
//                        mChatView.setChatTitle(IdHelper.getString(mContext, "group"),
//                                data.getIntExtra(MEMBERS_COUNT, 0));
//                    } else {
//                        mChatView.setChatTitle(data.getStringExtra(NAME),
//                                data.getIntExtra(MEMBERS_COUNT, 0));
//                    }
//                } else {
//                    if (TextUtils.isEmpty(data.getStringExtra(NAME))) {
//                        mChatView.setChatTitle(IdHelper.getString(mContext, "group"));
//                        mChatView.dismissGroupNum();
//                    } else {
//                        mChatView.setChatTitle(data.getStringExtra(NAME));
//                        mChatView.dismissGroupNum();
//                    }
//                }
//
//            } else mChatView.setChatTitle(data.getStringExtra(NAME));
//            if (data.getBooleanExtra("deleteMsg", false)) {
//                mChatAdapter.clearMsgList();
//            }
//        } else if (resultCode == RESULT_CODE_FRIEND_INFO) {
//            if (mIsSingle) {
//                String nickname = data.getStringExtra(NICKNAME);
//                if (!TextUtils.isEmpty(nickname)) {
//                    mChatView.setChatTitle(nickname);
//                }
//            }
//        }
//    }



//    private void dismissSoftInput() {
//        if (mShowSoftInput) {
//            if (mImm != null) {
//                mImm.hideSoftInputFromWindow(mChatView.getInputView().getWindowToken(), 0);
//                mShowSoftInput = false;
//            }
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void showSoftInputAndDismissMenu() {
//        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        mChatView.invisibleMoreMenu();
//        mChatView.getInputView().requestFocus();
//        if (mImm != null) {
//            mImm.showSoftInput(mChatView.getInputView(),
//                    InputMethodManager.SHOW_FORCED);//强制显示键盘
//        }
//        mShowSoftInput = true;
//        mChatView.setMoreMenuHeight();
//    }
//
//    public void dismissSoftInputAndShowMenu() {
//        //隐藏软键盘
//        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        mChatView.showMoreMenu();
//        if (mImm != null) {
//            mImm.hideSoftInputFromWindow(mChatView.getInputView().getWindowToken(), 0); //强制隐藏键盘
//        }
//        mChatView.setMoreMenuHeight();
//        mShowSoftInput = false;
//    }



    private class UIHandler extends Handler {

        private final WeakReference<ConversationDetailController> mController;
        public UIHandler(final ConversationDetailController controller) {
            mController = new WeakReference<ConversationDetailController>(controller);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            ConversationDetailController controller = mController.get();
            if (controller != null) {
                switch (msg.what) {
                    case REFRESH_LAST_PAGE: // 刷新最后一页

                        // 下拉加载更多数据
                        if(controller.mChatAdapter != null){
                          controller.mChatAdapter.dropDownToRefresh(new InterfaceFactory.OnConversatioDetailNotifyCallback(){
                              @Override
                              public void callback(String type, ResultObject obj) {

                              }
                          });
                        }


                        loadMoreConverDetailMsgList(currentStart, currentOffset, loadMoreConverMsgListCallback);



//                        controller.mChatView.getListView().onDropDownComplete();
//                        if (controller.mChatAdapter.isHasLastPage()) {
//                            if (Build.VERSION.SDK_INT >= 21) {
//                                controller.mChatView.getListView()
//                                        .setSelectionFromTop(controller.mChatAdapter.getOffset(),
//                                                controller.mChatView.getListView().getHeaderHeight());
//                            } else {
//                                controller.mChatView.getListView().setSelection(controller.mChatAdapter
//                                        .getOffset());
//                            }
//                            controller.mChatAdapter.refreshStartPosition();
//                        } else {
//                            controller.mChatView.getListView().setSelection(0);
//                        }
//                        controller.mChatView.getListView()
//                                .setOffset(controller.mChatAdapter.getOffset());

                        break;



                    case REFRESH_GROUP_NAME: // 刷新群组名称
                        if (controller.mConv != null) {
                            int num = msg.getData().getInt(MEMBERS_COUNT);
                            String groupName = msg.getData().getString(GROUP_NAME);
                            // 更新会话名称
                            // controller.mChatView.setChatTitle(groupName, num);
                            if(controller.notifyCallback != null) {
                                String title = JMessageJSONUtils.formatTitleToJSONString(groupName, num);
                                controller.notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                            }
                        }
                        break;


                    case REFRESH_GROUP_NUM: // 刷新群组人员数量
                        int num = msg.getData().getInt(MEMBERS_COUNT);
                        // 更新会话名称
                        //controller.mChatView.setChatTitle(IdHelper.getString(activity, "group"), num);
                        if(controller.notifyCallback != null) {
                            String groupName = controller.activity.getString(IdHelper.getString(controller.activity, "group"));
                            String title = JMessageJSONUtils.formatTitleToJSONString(groupName, num);
                            controller.notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                        }
                        break;


                    case REFRESH_CHAT_TITLE: // 刷新会话标题
                        if (controller.mGroupInfo != null) {
                            //检查自己是否在群组中
                            UserInfo info = controller.mGroupInfo.getGroupMemberInfo(JMessageClient
                                    .getMyInfo().getUserName());
                            if (!TextUtils.isEmpty(controller.mGroupInfo.getGroupName())){
                                controller.mGroupName = controller.mGroupInfo.getGroupName();
                                if (info != null){
                                    // 更新会话名称
                                    if(controller.notifyCallback != null) {
                                        String title = JMessageJSONUtils.formatTitleToJSONString(controller.mGroupName,
                                                controller.mGroupInfo.getGroupMembers().size());
                                        controller.notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                                    }

//                                    controller.mChatView.setChatTitle(controller.mGroupName,
//                                            controller.mGroupInfo.getGroupMembers().size());
//                                    controller.mChatView.showRightBtn();
                                }else {
                                    // 更新会话名称
                                    if(controller.notifyCallback != null) {
                                        String title = JMessageJSONUtils.formatTitleToJSONString(controller.mGroupName, 0);
                                        controller.notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                                    }
//                                    controller.mChatView.setChatTitle(controller.mGroupName);
//                                    controller.mChatView.dismissRightBtn();
                                }
                            }
                        }
                        break;
                }
            }
        }
    }

//    public void onKeyBoardStateChange(int state) {
//        switch (state) {
//            case ChatView.KEYBOARD_STATE_INIT:
//                if (mImm != null) {
//                    mImm.isActive();
//                }
//                if (mChatView.getMoreMenu().getVisibility() == View.INVISIBLE
//                        || (!mShowSoftInput && mChatView.getMoreMenu().getVisibility() == View.GONE)) {
//
//                    mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    mChatView.getMoreMenu().setVisibility(View.GONE);
//                }
//                break;
//            default:
//                break;
//        }
//    }

    /**
     * 接收消息类事件
     *
     * @param event 消息事件
     */
    public void onEvent(MessageEvent event) {
        SqLog.i(TAG, "会话详情接收事件：");

        final Message msg = event.getMessage();
        SqLog.i(TAG, "详情—接收消息：" + msg);


        //若为群聊相关事件，如添加、删除群成员
        if (msg.getContentType() == ContentType.eventNotification) {
            GroupInfo groupInfo = (GroupInfo) msg.getTargetInfo();
            long groupId = groupInfo.getGroupID();
            UserInfo myInfo = JMessageClient.getMyInfo();
            EventNotificationContent.EventNotificationType type = ((EventNotificationContent) msg
                    .getContent()).getEventNotificationType();
            if (groupId == mGroupId) {
                switch (type) {
                    case group_member_added:
                        //添加群成员事件
                        List<String> userNames = ((EventNotificationContent) msg.getContent()).getUserNames();
                        //群主把当前用户添加到群聊，则显示聊天详情按钮
                        refreshGroupNum();
                        if (userNames.contains(myInfo.getNickname()) || userNames.contains(myInfo.getUserName())) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mChatView.showRightBtn();
                                }
                            });
                        }

                        break;

                    case group_member_removed:
                        //删除群成员事件
                        userNames = ((EventNotificationContent) msg.getContent()).getUserNames();
                        //群主删除了当前用户，则隐藏聊天详情按钮
                        if (userNames.contains(myInfo.getNickname()) || userNames.contains(myInfo.getUserName())) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    mChatView.dismissRightBtn();
                                    GroupInfo groupInfo = (GroupInfo) mConv.getTargetInfo();
                                    if (TextUtils.isEmpty(groupInfo.getGroupName())) {
                                        //mChatView.setChatTitle(IdHelper.getString(mContext, "group"));
                                        if(notifyCallback != null) {
                                            String groupName = activity.getString(IdHelper.getString(activity, "group"));
                                            String title = JMessageJSONUtils.formatTitleToJSONString(groupName, 0);
                                            notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                                        }
                                    } else {
                                        //mChatView.setChatTitle(groupInfo.getGroupName());
                                        if(notifyCallback != null) {
                                            String title = JMessageJSONUtils.formatTitleToJSONString(groupInfo.getGroupName(), 0);
                                            notifyCallback.callback(DicConstants.REFRESH_CONVERSATION_TITLE, new ResultObject(title));
                                        }
                                    }
                                    //mChatView.dismissGroupNum();
                                }
                            });
                        } else {
                            refreshGroupNum();
                        }

                        break;
                    case group_member_exit:
                        refreshGroupNum();
                        break;
                }
            }
        }

        //刷新消息
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //收到消息的类型为单聊
                if (msg.getTargetType() == ConversationType.single) {
                    UserInfo userInfo = (UserInfo) msg.getTargetInfo();
                    String targetId = userInfo.getUserName();
                    String appKey = userInfo.getAppKey();
                    //判断消息是否在当前会话中
                    if (mIsSingle && targetId.equals(mTargetId) && appKey.equals(mTargetAppKey)) {
                      if(mChatAdapter != null){
                        Message lastMsg = mChatAdapter.getLastMsg();
                        //收到的消息和Adapter中最后一条消息比较，如果最后一条为空或者不相同，则加入到MsgList
                        if (lastMsg == null || msg.getId() != lastMsg.getId()) {
                            mChatAdapter.addMsgToList(msg);
                        } else {
                            mChatAdapter.notifyDataSetChanged();
                        }
                      }
                    }
                } else {
                    long groupId = ((GroupInfo) msg.getTargetInfo()).getGroupID();
                    if (groupId == mGroupId && mChatAdapter != null) {
                        Message lastMsg = mChatAdapter.getLastMsg();
                        if (lastMsg == null || msg.getId() != lastMsg.getId()) {
                            mChatAdapter.addMsgToList(msg);
                        } else {
                            mChatAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void refreshGroupNum() {
        Conversation conv = JMessageClient.getGroupConversation(mGroupId);
        GroupInfo groupInfo = (GroupInfo) conv.getTargetInfo();
        if (!TextUtils.isEmpty(groupInfo.getGroupName())) {
            android.os.Message handleMessage = mUIHandler.obtainMessage();
            handleMessage.what = REFRESH_GROUP_NAME;
            Bundle bundle = new Bundle();
            bundle.putString(GROUP_NAME, groupInfo.getGroupName());
            bundle.putInt(MEMBERS_COUNT, groupInfo.getGroupMembers().size());
            handleMessage.setData(bundle);
            handleMessage.sendToTarget();
        } else {
            android.os.Message handleMessage = mUIHandler.obtainMessage();
            handleMessage.what = REFRESH_GROUP_NUM;
            Bundle bundle = new Bundle();
            bundle.putInt(MEMBERS_COUNT, groupInfo.getGroupMembers().size());
            handleMessage.setData(bundle);
            handleMessage.sendToTarget();
        }
    }

    private MsgDataAdapter.ContentLongClickListener longClickListener = new MsgDataAdapter.ContentLongClickListener() {
        @Override
        public void onContentLongClick(final int position, View view) {
            SqLog.i(TAG, "long click position" + position);

//            final Message msg = mChatAdapter.getMessage(position);
//            UserInfo userInfo = msg.getFromUser();
//            if (msg.getContentType() != ContentType.image) {
//                // 长按文本弹出菜单
//                String name = userInfo.getNickname();
//                View.OnClickListener listener = new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        if (v.getId() == IdHelper.getViewID(mContext, "jmui_copy_msg_btn")) {
//                            if (msg.getContentType() == ContentType.text) {
//                                final String content = ((TextContent) msg.getContent()).getText();
//                                if (Build.VERSION.SDK_INT > 11) {
//                                    ClipboardManager clipboard = (ClipboardManager) mContext
//                                            .getSystemService(Context.CLIPBOARD_SERVICE);
//                                    ClipData clip = ClipData.newPlainText("Simple text", content);
//                                    clipboard.setPrimaryClip(clip);
//                                } else {
//                                    ClipboardManager clipboard = (ClipboardManager) mContext
//                                            .getSystemService(Context.CLIPBOARD_SERVICE);
//                                    clipboard.getText();// 设置Clipboard 的内容
//                                    if (clipboard.hasText()) {
//                                        clipboard.getText();
//                                    }
//                                }
//
//                                Toast.makeText(mContext, IdHelper.getString(mContext, "jmui_copy_toast"),
//                                        Toast.LENGTH_SHORT).show();
//                                mDialog.dismiss();
//                            }
//                        } else if (v.getId() == IdHelper.getViewID(mContext, "jmui_forward_msg_btn")) {
//                            mDialog.dismiss();
//                        } else {
//                            mConv.deleteMessage(msg.getId());
//                            mChatAdapter.removeMessage(position);
//                            mDialog.dismiss();
//                        }
//                    }
//                };
//                boolean hide = msg.getContentType() == ContentType.voice;
//                mDialog = DialogCreator.createLongPressMessageDialog(mContext, name, hide, listener);
//                mDialog.show();
//                mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
//            } else {
//                String name = msg.getFromUser().getNickname();
//                View.OnClickListener listener = new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        if (v.getId() == IdHelper.getViewID(mContext, "jmui_delete_msg_btn")) {
//                            mConv.deleteMessage(msg.getId());
//                            mChatAdapter.removeMessage(position);
//                            mDialog.dismiss();
//                        } else if (v.getId() == IdHelper.getViewID(mContext, "jmui_forward_msg_btn")) {
//                            mDialog.dismiss();
//                        }
//                    }
//                };
//                mDialog = DialogCreator.createLongPressMessageDialog(mContext, name, true, listener);
//                mDialog.show();
//                mDialog.getWindow().setLayout((int) (0.8 * mWidth), WindowManager.LayoutParams.WRAP_CONTENT);
//            }

        }
    };


//    public void onSizeChanged(int w, int h, int oldw, int oldh) {
//        if (oldh - h > 300) {
//            mShowSoftInput = true;
//            if (SharePreferenceManager.getCachedWritableFlag()) {
//                SharePreferenceManager.setCachedKeyboardHeight(oldh - h);
//                SharePreferenceManager.setCachedWritableFlag(false);
//            }
//
//            mChatView.setMoreMenuHeight();
//        } else {
//            mShowSoftInput = false;
//        }
//    }
//
//    @Override
//    public boolean onTouch(View view, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (view.getId() == IdHelper.getViewID(mContext, "jmui_chat_input_et")) {
//                    if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE && !mShowSoftInput) {
//                        showSoftInputAndDismissMenu();
//                        return false;
//                    } else {
//                        return false;
//                    }
//                }
//                if (mChatView.getMoreMenu().getVisibility() == View.VISIBLE) {
//                    mChatView.dismissMoreMenu();
//                } else if (mShowSoftInput) {
//                    View v = getCurrentFocus();
//                    if (mImm != null && v != null) {
//                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//                        mShowSoftInput = false;
//                    }
//                }
//                break;
//        }
//        return false;
//    }





    /**
     * 退出会话界面
     */
    public void exitConversationView(){
        JMessageClient.exitConversation();

        // 停止播放器
        //mChatAdapter.stopMediaPlayer();

        if (mConv != null) {
            mConv.resetUnreadCount();
        }

        activity.unregisterReceiver(mReceiver);
        if(mChatAdapter != null) mChatAdapter.releaseMediaPlayer();
        //mChatView.releaseRecorder();
        mUIHandler.removeCallbacksAndMessages(null);

        if(mChatAdapter != null) mChatAdapter.destroy();
    }

    /**
     * 退出会话
     */
    public void exitConversation(){
        JMessageClient.exitConversation();
    }
}
