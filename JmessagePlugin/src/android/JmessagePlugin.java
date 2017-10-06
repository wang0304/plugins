package com.baimei.jmessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSONObject;
import com.baimei.jmessage.common.InterfaceFactory;
import com.baimei.jmessage.common.JchatApplication;
import com.baimei.jmessage.common.ResultObject;
import com.baimei.jmessage.controller.ConversationDetailController;
import com.baimei.jmessage.controller.ConversationListController;
import com.baimei.jmessage.controller.GroupInfoController;
import com.baimei.jmessage.model.MyConversation;
import com.baimei.jmessage.utils.JMessageJSONUtils;
import com.baimei.jmessage.utils.ResponseCode;
import com.baimei.jmessage.utils.SqLog;
import com.baimei.jmessage.utils.JchatStringUtils;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jmessage.android.uikit.utils.StringUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;

/**
 * author: baimei create 2016-07-01 qq:2227476834
 * This class echoes a string called from JavaScript.
 */
public class JmessagePlugin extends CordovaPlugin {

  private final static List<String> methodList =
          Arrays.asList(
                  "addLocalNotification",
                  "clearAllNotification",
                  "clearLocalNotifications",
                  "clearNotificationById",
                  "getNotification",
                  "requestPermission",
                  "removeLocalNotification",
                  "reportNotificationOpened",
                  "setLatestNotificationNum"
          );

  private ExecutorService threadPool = Executors.newFixedThreadPool(1);

  private final String TAG = "jchat";
  private static Activity cordovaActivity;
  private JmessagePlugin instance;

  // 会话详情对象
  private Conversation chatConv;

  // 消息
  public static String notificationTitle;
  public static String notificationAlert;
  public static Map<String, Object> notificationExtras = new HashMap<String, Object>();

  // 打开消息
  public static String openNotificationTitle;
  public static String openNotificationAlert;
  public static Map<String, Object> openNotificationExtras = new HashMap<String, Object>();

//  public JmessagePlugin(){
//    instance = this;
//  }

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    this.cordovaActivity = cordova.getActivity();
    this.webView = webView;
    this.instance = this;

    //如果同时缓存了打开事件 openNotificationAlert 和 消息事件 notificationAlert，只向 UI 发打开事件。
    //这样做是为了和 iOS 统一。
//    if (openNotificationAlert != null) {
//      notificationAlert = null;
//      transmitNotificationOpen(openNotificationTitle, openNotificationAlert,
//        openNotificationExtras);
//    }
//    if (notificationAlert != null) {
//      transmitNotificationReceive(notificationTitle, notificationAlert,
//        notificationExtras);
//    }
  }

  @JavascriptInterface
  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

    // 初始化
    if (action.equals("init")) {
      this.init();
      return true;
    }

    // 初始化会话列表
    if (action.equals("initConverListCtrl")) {
      this.initConverListCtrl();
      return true;
    }

    // 获取会话列表
    if (action.equals("getConversationList")) {
      this.getConversationList();
      return true;
    }

    // 退出会话列表
    if (action.equals("exitConversationList")) {
      this.exitConversationList();
      return true;
    }

    // 初始化会话详情
    if (action.equals("initConverViewCtrl")) {
      String targetId = args.getString(0);
      String conversationType = args.getString(1);
      this.initConverViewCtrl(targetId, conversationType);
      return true;
    }

    // 加载更多消息
    if (action.equals("loadMoreConverDetailMsgList")) {
      String aStart = args.getString(0);
      String aOffset = args.getString(1);
      this.loadMoreConverDetailMsgList(aStart, aOffset, callbackContext);
      return true;
    }

    // 退出会话详情界面
    if (action.equals("exitConversationView")) {
      this.exitConversationView();
      return true;
    }

    // 退出会话
    if (action.equals("exitConversation")) {
      this.exitConversation();
      return true;
    }

    // 注册
    if (action.equals("doRegister")) {
      String userId = args.getString(0);
      String password = args.getString(1);
      this.doRegister(userId, password, callbackContext);
      return true;
    }

    // 登录
    if (action.equals("login")) {
      String userId = args.getString(0);
      String password = args.getString(1);
      this.login(userId, password, callbackContext);
      return true;
    }

    // 退出登录
    if (action.equals("exitLogin")) {
      this.exitLogin(callbackContext);
      return true;
    }

    // 获取我的信息
    if (action.equals("getMyInfo")) {
      this.getMyInfo(callbackContext);
      return true;
    }
    if (action.equals("getMineInfo")) {
      this.getMineInfo(callbackContext);
      return true;
    }

    // 获取用户信息
    if (action.equals("getUserInfo")) {
      String username = args.getString(0);
      this.getUserInfo(username, callbackContext);
      return true;
    }

    // 设置用户昵称
    if (action.equals("setNickname")) {
      String nickname = args.getString(0);
      this.setNickname(nickname, callbackContext);
      return true;
    }

    // 上传用户头像
    if (action.equals("updateUserAvatar")) {
      String path = args.getString(0);
      this.updateUserAvatar(path, callbackContext);
      return true;
    }

    // 添加好友
    if (action.equals("addFriend")) {
      String targetId = args.getString(0);
      this.addFriend(targetId, callbackContext);
      return true;
    }

    // 判断好友是否已存在好友
    if (action.equals("isExistConv")) {
      String targetId = args.getString(0);
      this.isExistConv(targetId, callbackContext);
      return true;
    }

    // 创建单例会话
    if (action.equals("createSingleConversation")) {
      String targetId = args.getString(0);
      String targetAppKey = args.getString(1);
      this.createSingleConversation(targetId, targetAppKey, callbackContext);
      return true;
    }

    // 获取单例会话
    if (action.equals("getSingleConversation")) {
      String targetId = args.getString(0);
      String targetAppKey = args.getString(1);
      this.getSingleConversation(targetId, targetAppKey, callbackContext);
      return true;
    }

    // 获取群组信息
    if (action.equals("getGroupInfo")) {
      String groupId = args.getString(0);
      this.getGroupInfo(groupId, callbackContext);
      return true;
    }

    // 获取群组信息
    if (action.equals("getGroupMemberByUserName")) {
      String groupId = args.getString(0);
      String userName = args.getString(1);
      this.getGroupMemberByUserName(groupId, userName, callbackContext);
      return true;
    }

    // 创建群组信息
    if (action.equals("createGroup")) {
      String groupName = args.getString(0);
      String groupDesc = args.getString(1);
      this.createGroup(groupName, groupDesc, callbackContext);
      return true;
    }

    // 添加好友到群组
    if (action.equals("addMemberToGroup")) {
      String groupId = args.getString(0);
      String userName = args.getString(1);
      this.addMemberToGroup(groupId, userName, callbackContext);
      return true;
    }

    // 批量添加好友到群组
    if (action.equals("addMembersToGroup")) {
      String groupId = args.getString(0);
      String userArrayJson = args.getString(1);
      this.addMembersToGroup(groupId, userArrayJson, callbackContext);
      return true;
    }

    // 从群组中移除好友
    if (action.equals("removeMemberFromGroup")) {
      String groupId = args.getString(0);
      String userName = args.getString(1);
      this.removeMemberFromGroup(groupId, userName, callbackContext);
      return true;
    }

    // 批量从群组中移除好友
    if (action.equals("removeMembersFromGroup")) {
      String groupId = args.getString(0);
      String userArrayJson = args.getString(1);
      this.removeMembersFromGroup(groupId, userArrayJson, callbackContext);
      return true;
    }

    // 修改群组名称
    if (action.equals("updateGroupName")) {
      String groupId = args.getString(0);
      String newName = args.getString(1);
      this.updateGroupName(groupId, newName, callbackContext);
      return true;
    }

    // 退出群聊
    if (action.equals("exitGroup")) {
      String groupId = args.getString(0);
      this.exitGroup(groupId, callbackContext);
      return true;
    }

    // 删除会话信息
    if (action.equals("deleteGroupConversation")) {
      String groupId = args.getString(0);
      this.deleteGroupConversation(groupId, callbackContext);
      return true;
    }

    // 创建群组会话
    if (action.equals("createGroupConversation")) {
      String groupId = args.getString(0);
      this.createGroupConversation(groupId, callbackContext);
      return true;
    }

    // 获取群组会话
    if (action.equals("getGroupConversation")) {
      String groupId = args.getString(0);
      this.getGroupConversation(groupId, callbackContext);
      return true;
    }

    // 获取会话消息列表
    if (action.equals("getConvMsgList")) {
      int aStart = args.getInt(0);
      int aOffset = args.getInt(1);
      this.getConvMsgList(aStart, aOffset, callbackContext);
      return true;
    }

    // 发送会话消息
    if (action.equals("sendMessage")) {
      String msgContent = args.getString(0);
      this.sendMessage(msgContent, callbackContext);
      return true;
    }




    // 打开会话界面
    if (action.equals("openConvListActivity")) {
      this.openConvListActivity(callbackContext);
      return true;
    }

    // 接收通知
    if (methodList.contains(action)) {
      return true;
    }

    return false;
  }



  // ==================================================================================================

  /**
   * 初始化
   */
  @JavascriptInterface
  public void init(){
    SqLog.i(TAG, TAG + "_plugin -------- init ---------");
    Context context = this.cordova.getActivity().getApplicationContext();
    //初始化JMessage-sdk
    JMessageClient.init(context);
    //设置Notification的模式
    JMessageClient.setNotificationMode(JMessageClient.NOTI_MODE_DEFAULT);
  }

  /**
   * 初始化会话列表
   */
  @JavascriptInterface
  public void initConverListCtrl(){
    ConversationListController.getInstance().initCtrl(cordovaActivity, new InterfaceFactory.OnConversatioListNotifyCallback() {
      @Override
      public void callback(String type, ResultObject obj) {
        String json = "";
        if (obj != null) {
          json = obj.getObj();
        }
        OnConverListNotifyCallback(type, json != null ? json : "");
      }
    });
  }

  /**
   * 获取会话列表数据
   */
  @JavascriptInterface
  public void getConversationList(){
    ConversationListController.getInstance().getConversationList(new InterfaceFactory.OnConversatioListDataCallback() {
      @Override
      public void callback(int unReadCount, ResultObject obj) {

        String json = "";
        if (obj != null && obj.getObj() != null) {
          List<com.alibaba.fastjson.JSONObject> datas = obj.getObj();
          json = com.alibaba.fastjson.JSONArray.toJSONString(datas);
        }

        SqLog.i(TAG, TAG + "_初始化会话列表返回数据：" + json);

        requestConverListCallback(unReadCount, json);
      }
    });
  }

  /**
   * 退出会话
   */
  @JavascriptInterface
  public void exitConversationList(){
    ConversationListController.getInstance().exitConversationList();
  }

  /**
   * 初始化会话详情
   * @param targetId
   */
  @JavascriptInterface
  public void initConverViewCtrl(String targetId, String conversationType){
    ConversationDetailController.getInstance().initCtrl(cordovaActivity, targetId, conversationType, new InterfaceFactory.OnConversatioListNotifyCallback() {
      @Override
      public void callback(String type, ResultObject obj) {
        String json = "";
        if (obj != null) {
          json = obj.getObj();
        }
        OnConverDetailNotifyCallback(type, json != null ? json : "");
      }
    });
  }

  /**
   * 加载更多消息
   * @param aStart 起始页码
   * @param aOffset 每页增量
   */
  @JavascriptInterface
  public void loadMoreConverDetailMsgList(String aStart, String aOffset, final CallbackContext callbackContext){
    ConversationDetailController.getInstance().loadMoreConverDetailMsgList(
            aStart, aOffset, new InterfaceFactory.OnConversatioDetailNotifyCallback() {

              @Override
              public void callback(String type, ResultObject obj) {
                String json = "";
                if (obj != null) {
                  json = obj.getObj();
                }
                SqLog.i("jchat", "$$$ 加载更多消息： " + json != null ? json : "");
                onSuccessCallback(json != null ? json : "", callbackContext);
              }

            }
    );
  }

  /**
   * 离开会话详情
   */
  @JavascriptInterface
  public void exitConversationView(){
    ConversationDetailController.getInstance().exitConversationView();
  }

  /**
   * 注册
   */
  @JavascriptInterface
  public void doRegister(String userId, String password, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "#_plugin -------- doRegister ---------");
    JMessageClient.register(userId, password, new BasicCallback() {
      @Override
      public void gotResult(final int status, final String desc) {

        if (status == 0) {
          String json = JMessageJSONUtils.formatErrorToJSONString(status, desc);
          onSuccessCallback(json, callbackContext);
        } else {
          onErrorCallback(status, desc, callbackContext);
        }

      }
    });
  }

  /**
   * 登录
   * @param userId
   * @param password
   * @param callbackContext
   */
  @JavascriptInterface
  public void login(String userId, String password, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- login ---------");
    JMessageClient.login(userId, password, new BasicCallback() {
      @Override
      public void gotResult(final int status, String desc) {
        SqLog.i(TAG, TAG + "_plugin @ login ---status: " + status + " - desc: " + desc);

        if (status == 0) {
          String json = JMessageJSONUtils.formatErrorToJSONString(status, desc);
          onSuccessCallback(json, callbackContext);
        } else {
          onErrorCallback(status, desc, callbackContext);
        }

      }
    });
  }

  /**
   * 退出登录
   * @param callbackContext
   */
  @JavascriptInterface
  public void exitLogin(final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- exit_login ---------");
    JMessageClient.logout();

    UserInfo myInfo = JMessageClient.getMyInfo();
    if(myInfo == null){
      onSuccessCallback("已退出登录！", callbackContext);
    } else {
      onErrorCallback(0, "用户没有登录！", callbackContext);
    }
  }

  /**
   * 获取我的信息
   * @param callbackContext
   */
  @JavascriptInterface
  public void getMyInfo(final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getMyInfo ---------");
    UserInfo userInfo = JMessageClient.getMyInfo();
    if(null != userInfo){
      String str = StringUtil.format2JSONStr(userInfo.toString(), "MyUserInfo");
      String json = JMessageJSONUtils.formatMyInfoToJSONString(userInfo);
      SqLog.i(TAG, TAG + "_getMyinfo: " + json);
      onSuccessCallback(json, callbackContext);
    } else {
      onErrorCallback(0, "", callbackContext);
    }
  }

  /**
   * 获取我的信息
   * @param callbackContext
   */
  @JavascriptInterface
  public void getMineInfo(final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getMineInfo ---------");
    String userName = JMessageClient.getMyInfo().getUserName();
    JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
      @Override
      public void gotResult(int status, String desc, UserInfo userInfo) {
        if (status == 0) {
          final String str = StringUtil.format2JSONStr(userInfo.toString(), "MyUserInfo");
          SqLog.i(TAG, TAG + "_get_mineinfo: " + str);
          onSuccessCallback(str, callbackContext);

        } else {
          onErrorCallback(status, desc, callbackContext);
        }
      }
    });
  }

  /**
   * 根据用户名称,获取用户信息
   * @param callbackContext
   */
  @JavascriptInterface
  public void getUserInfo(final String userName, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getUserInfo ----userName: " + userName);
    JMessageClient.getUserInfo(userName, new GetUserInfoCallback() {
      @Override
      public void gotResult(int status, String desc, UserInfo userInfo) {
        if (status == 0) {
          String str = StringUtil.format2JSONStr(userInfo.toString(), "MyUserInfo");
          SqLog.i(TAG, TAG + "_get_userinfo: " + str);
          onSuccessCallback(str, callbackContext);
        } else {
          onErrorCallback(status, desc, callbackContext);
        }
      }
    });
  }

  /**
   * 设置用户昵称
   * @param nickName
   * @param callbackContext
   */
  @JavascriptInterface
  public void setNickname(String nickName, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- setNickname ---------");
    nickName = JchatStringUtils.paramEncode(nickName); // 编码

    UserInfo myUserInfo = JMessageClient.getMyInfo();
    myUserInfo.setNickname(nickName);
    JMessageClient.updateMyInfo(UserInfo.Field.nickname, myUserInfo, new BasicCallback() {
      @Override
      public void gotResult(final int status, String desc) {
        if (status == 0) {
          onSuccessCallback(desc, callbackContext);
        } else {
          onErrorCallback(status, desc, callbackContext);
        }
      }
    });
  }

  /**
   * 上传头像
   * @param path 图片路径
   * @param callbackContext
   */
  @JavascriptInterface
  public void updateUserAvatar(final String path, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- updateUserAvatar ---------");
    JMessageClient.updateUserAvatar(new File(path), new BasicCallback() {
      @Override
      public void gotResult(int status, final String desc) {
        if (status == 0) {
          onSuccessCallback(path, callbackContext);
        } else {
          onErrorCallback(status, desc, callbackContext);
        }
      }
    });
  }

  /**
   * 添加好友
   * @param targetId 用户名
   * @param callbackContext
   */
  @JavascriptInterface
  public void addFriend(final String targetId, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- addFriend ---------");

    ConversationListController.getInstance().addFriend(cordovaActivity, targetId,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {

                String json = "";
                MyConversation conversation = obj.getObj();
                if (conversation != null) {
                  json = JSONObject.toJSONString(conversation);
                }

                SqLog.i(TAG, TAG + "_单聊会话Conversation: " + json);

                onSuccessCallback(json, callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 判断用户是否存在
   * @param targetId
   * @return
   */
  @JavascriptInterface
  private boolean isExistConv(String targetId, final CallbackContext callbackContext) {
    SqLog.i(TAG, TAG + "_plugin -------- isExistConv ---------");
    Conversation conv = JMessageClient.getSingleConversation(targetId);
    if(conv == null){
      onErrorCallback(0, "false", callbackContext);
    } else {
      onSuccessCallback("true", callbackContext);
    }
    return conv != null;
  }

  /**
   * 创建单例会话
   * @param aTargetId
   * @param aTargetAppKey
   * @param callbackContext
   */
  @JavascriptInterface
  public void createSingleConversation(String aTargetId, String aTargetAppKey, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin ----- createSingleConver aTargetId: " + aTargetId + "  -- aTargetAppKey: " + aTargetAppKey);
    //System.out.println(TAG + "_plugin ----- createSingleConver aTargetId: " + aTargetId + "  -- aTargetAppKey: " + aTargetAppKey);
    chatConv = Conversation.createSingleConversation(aTargetId, aTargetAppKey);
    if(null != chatConv){
      String jsonStr = StringUtil.format2JSONStr(chatConv.toString(), "Conversation");
      SqLog.i(TAG, TAG + "_single_conver_jsonStr: " + jsonStr);
      onSuccessCallback(jsonStr, callbackContext);
    } else {
      onErrorCallback(0, "", callbackContext);
    }
  }

  /**
   * 获取单例会话
   * @param aTargetId
   * @param aTargetAppKey
   * @param callbackContext
   */
  @JavascriptInterface
  public void getSingleConversation(String aTargetId, String aTargetAppKey, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getSingleConver aTargetId: " + aTargetId + "  -- aTargetAppKey: " + aTargetAppKey);
    //System.out.println(TAG + "_plugin -------- getSingleConver aTargetId: " + aTargetId + "  -- aTargetAppKey: " + aTargetAppKey);
    if(null != aTargetAppKey){
      chatConv = JMessageClient.getSingleConversation(aTargetId, aTargetAppKey);
    } else {
      chatConv = JMessageClient.getSingleConversation(aTargetId);
    }
    if(null != chatConv){
      String jsonStr = StringUtil.format2JSONStr(chatConv.toString(), "Conversation");
      SqLog.i(TAG, TAG + "_single_conver_jsonStr: " + jsonStr);
      onSuccessCallback(jsonStr, callbackContext);
    } else {
      onErrorCallback(0, "", callbackContext);
    }
  }







  /**
   * 获取群组信息
   * @param aGroupId
   * @param callbackContext
   */
  @JavascriptInterface
  public void getGroupInfo(String aGroupId, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getGroupInfo aGroupId: " + aGroupId);

    GroupInfoController.getInstatnce().getGroupInfo(aGroupId, new InterfaceFactory.RequestDataCallback(){

      @Override
      public void success(int respStatus, ResultObject obj) {
        String groupInfoJson = "";
        if(obj != null){
          groupInfoJson = obj.getObj();
        }

        SqLog.i(TAG, "＃＃＃ 群组信息json： " + groupInfoJson);
        onSuccessCallback(groupInfoJson != null ? groupInfoJson : "", callbackContext);
      }

      @Override
      public void error(int errorCode, String error) {
        error = ResponseCode.onHandle(cordovaActivity, errorCode);
        SqLog.i(TAG, "＃＃＃ 群组error： " + error);
        onErrorCallback(errorCode, error, callbackContext);
      }
    });
  }

  /**
   * 根据用户名获取群组信息
   * @param aGroupId
   * @param keywords
   * @param callbackContext
   */
  @JavascriptInterface
  public void getGroupMemberByUserName(String aGroupId, String keywords, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getGroupInfo aGroupId: " + aGroupId);

    GroupInfoController.getInstatnce().getGroupMemberByUserName(aGroupId, keywords,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String userInfoJson = "";
                if (obj != null) {
                  userInfoJson = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 用户信息json： " + userInfoJson);
                onSuccessCallback(userInfoJson != null ? userInfoJson : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 用户error： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 获取群组信息
   * @param groupName 群组名称，默认为空
   * @param callbackContext
   */
  @JavascriptInterface
  public void createGroup(String groupName, String groupDesc, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- createGroup groupName: " + groupName);
    GroupInfoController.getInstatnce().createGroup(cordovaActivity, groupName, groupDesc,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String conversationJson = "";
                if (obj != null) {
                  conversationJson = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 创建群组会话json： " + conversationJson);
                onSuccessCallback(conversationJson != null ? conversationJson : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 创建群组error： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 添加好友到群组
   * @param groupId
   * @param userName
   * @param callbackContext
   */
  @JavascriptInterface
  public void addMemberToGroup(String groupId, String userName, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- addMemberToGroup userName: " + userName);
    GroupInfoController.getInstatnce().addMemberToGroup(groupId, userName,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String msg = "";
                if (obj != null) {
                  msg = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 添加好友结果msg： " + msg);
                onSuccessCallback(msg != null ? msg : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 添加好友结果error： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 批量添加好友到群组
   * @param groupId
   * @param userArrayJson
   * @param callbackContext
   */
  @JavascriptInterface
  public void addMembersToGroup(String groupId, String userArrayJson, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- addMemberToGroup userArrayJson: " + userArrayJson);
    GroupInfoController.getInstatnce().addMembersToGroup(groupId, userArrayJson,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String msg = "";
                if (obj != null) {
                  msg = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 添加好友结果msg： " + msg);
                onSuccessCallback(msg != null ? msg : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 添加好友结果error： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 从群组中移除好友
   * @param groupId
   * @param userName
   * @param callbackContext
   */
  @JavascriptInterface
  public void removeMemberFromGroup(String groupId, String userName, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- removeMemberFromGroup userName: " + userName);
    GroupInfoController.getInstatnce().removeMemberFromGroup(groupId, userName,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String msg = "";
                if (obj != null) {
                  msg = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 移除好友结果msg： " + msg);
                onSuccessCallback(msg != null ? msg : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 移除好友结果error： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 批量从群组中移除好友
   * @param groupId
   * @param userArrayJson
   * @param callbackContext
   */
  @JavascriptInterface
  public void removeMembersFromGroup(String groupId, String userArrayJson, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- removeMembersFromGroup userArrayJson: " + userArrayJson);
    GroupInfoController.getInstatnce().removeMembersFromGroup(groupId, userArrayJson,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String msg = "";
                if (obj != null) {
                  msg = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 移除好友结果msg： " + msg);
                onSuccessCallback(msg != null ? msg : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 移除好友结果error： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 修改群组名称
   * @param groupId
   * @param newName
   * @param callbackContext
   */
  @JavascriptInterface
  public void updateGroupName(String groupId, String newName, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- updateGroupName newName: " + newName);
    GroupInfoController.getInstatnce().updateGroupName(groupId, newName,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String msg = "";
                if (obj != null) {
                  msg = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 修改群组名称msg： " + msg);
                onSuccessCallback(msg != null ? msg : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 修改群组名称error： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 退出群聊
   * @param groupId
   * @param callbackContext
   */
  @JavascriptInterface
  public void exitGroup(String groupId, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- exitGroup groupId: " + groupId);
    GroupInfoController.getInstatnce().exitGroup(groupId,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String msg = "";
                if (obj != null) {
                  msg = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 退出群聊： " + msg);
                onSuccessCallback(msg != null ? msg : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 退出群聊： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }

  /**
   * 删除群聊会话
   * @param groupId
   * @param callbackContext
   */
  @JavascriptInterface
  public void deleteGroupConversation(String groupId, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- deleteGroupConversation groupId: " + groupId);
    GroupInfoController.getInstatnce().deleteGroupConversation(groupId,
            new InterfaceFactory.RequestDataCallback() {

              @Override
              public void success(int respStatus, ResultObject obj) {
                String msg = "";
                if (obj != null) {
                  msg = obj.getObj();
                }

                SqLog.i(TAG, "＃＃＃ 删除群聊会话： " + msg);
                onSuccessCallback(msg != null ? msg : "", callbackContext);
              }

              @Override
              public void error(int errorCode, String error) {
                error = ResponseCode.onHandle(cordovaActivity, errorCode);
                SqLog.i(TAG, "＃＃＃ 删除群聊会话： " + error);
                onErrorCallback(errorCode, error, callbackContext);
              }
            });
  }









  /**
   * 创建群组会话
   * @param aGroupId
   * @param callbackContext
   */
  @JavascriptInterface
  public void createGroupConversation(String aGroupId, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- createGroupConver aGroupId: " + aGroupId);
    long group_id = StringUtil.parseLong(aGroupId);
    chatConv = Conversation.createGroupConversation(group_id);
    if(null != chatConv){
      String jsonStr = StringUtil.format2JSONStr(chatConv.toString(), "Conversation");
      SqLog.i(TAG, TAG + "_group_conver_jsonStr: " + jsonStr);
      onSuccessCallback(jsonStr, callbackContext);
    } else {
      onErrorCallback(0, "", callbackContext);
    }
  }

  /**
   * 获取群组会话
   * @param aGroupId
   * @param callbackContext
   */
  @JavascriptInterface
  public void getGroupConversation(String aGroupId, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getGroupConver aGroupId: " + aGroupId);
    long group_id = StringUtil.parseLong(aGroupId);
    chatConv = JMessageClient.getGroupConversation(group_id);
    //SqLog.i(TAG, TAG + "_Conversation: " + chatConv);
    if(null != chatConv){
      String jsonStr = StringUtil.format2JSONStr(chatConv.toString(), "Conversation");
      SqLog.i(TAG, TAG + "_group_conver_jsonStr: " + jsonStr);
      onSuccessCallback(jsonStr, callbackContext);
    } else {
      onErrorCallback(0, "", callbackContext);
    }
  }

  /**
   * 获取会话消息列表
   * @param aStart 开始索引
   * @param aOffset 增量(每次家在条数)
   * @param callbackContext
   */
  @JavascriptInterface
  public void getConvMsgList(int aStart, int aOffset, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- getConvMsgList ---------");
    if(null != chatConv){
      List<Message> mMsgList = chatConv.getMessagesFromNewest(aStart, aOffset);
      List<String> msgJsonList = new ArrayList<String>();
      String jsonStr = "";
      for(Message msg: mMsgList){
        jsonStr = StringUtil.format2JSONStr(msg.toString(), "Message");
        msgJsonList.add(jsonStr);
      }
      jsonStr = msgJsonList.toString();
      SqLog.i(TAG, TAG + "_get_conv_msg_jsonStr: " + jsonStr);
      onSuccessCallback(jsonStr, callbackContext);
    } else {
      onErrorCallback(0, "", callbackContext);
    }
  }






  /**
   * 退出会话
   */
  @JavascriptInterface
  public void exitConversation(){
    SqLog.i(TAG, TAG + "_plugin -------- exitConversation ------");
    ConversationDetailController.getInstance().exitConversation();
  }

  /**
   * 发送会话消息
   */
  @JavascriptInterface
  public void sendMessage(String msgContent, final CallbackContext callbackContext){
    SqLog.i(TAG, TAG + "_plugin -------- sendMessage : " + msgContent);
    msgContent = JchatStringUtils.paramEncode(msgContent); // 编码

//    if(null != chatConv){
//      TextContent content = new TextContent(msgContent);
//      final Message msg = chatConv.createSendMessage(content);
//      SqLog.i(TAG, TAG + "_plugin -------- Message : " + msg.toString());
//      JMessageClient.sendMessage(msg);
//        onSuccessCallback("success", callbackContext);
//    } else {
//        onErrorCallback(0, "error", callbackContext);
//    }

    ConversationDetailController.getInstance().sendTextMessage(msgContent);
  }



  @JavascriptInterface
  public void resetUnreadCount(){
    if(null != chatConv){

    }
  }

  @JavascriptInterface
  public void openConvListActivity(final CallbackContext callbackContext){
    try {
      Intent intent = new Intent();
      intent.setClass(cordovaActivity, ConvListMainActivity.class);
      cordovaActivity.startActivity(intent);

//      //下面三句为cordova插件回调页面的逻辑代码
//      PluginResult mPlugin = new PluginResult(PluginResult.Status.NO_RESULT);
//      mPlugin.setKeepCallback(true);
//
//      callbackContext.sendPluginResult(mPlugin);
//      onSuccessCallback("success", callbackContext);

    } catch (Exception e) {
      SqLog.i(TAG, TAG + "_plugin -------- openConv ------ error");
      e.printStackTrace();
    }
  }






  /**
   * 数据返回回调
   */
  private final OnDataResultCallback onDataResultCallback = new OnDataResultCallback(){

    @JavascriptInterface
    public void gotResult(int respCode, String respMsg, String eventFuc, String jsonData){
      if (instance == null) {
        return;
      }

      try {

        final String jsEvent = String.format("cordova.fireDocumentEvent('" + eventFuc + "', '%s')", jsonData);

        cordova.getActivity().runOnUiThread(new Runnable() {
          @Override
          public void run() {
            instance.webView.loadUrl("javascript:" + jsEvent);
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  };

  @JavascriptInterface
  @Override
  public void onDestroy() {
    super.onDestroy();
    cordovaActivity = null;
    instance = null;
    chatConv = null;
  }

  /**
   * 通知消息点击事件
   * @param message
   */
  @JavascriptInterface
  public void transmitMessageReceive(String message) {
    if (instance == null) {
      return;
    }
    String format = "window.plugins.jmessagePlugin.receiveMessageInAndroidCallback('%s');";
    final String js = String.format(format, message);
    cordovaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        instance.webView.loadUrl("javascript:" + js);
      }
    });
  }


  /**
   * 请求会话列表数据返回回调
   * @param count
   * @param data
   */
  @JavascriptInterface
  public void requestConverListCallback(int count, String data){
    String format = "window.plugins.jmessagePlugin.requestConverListCallback('%d','%s');";
    final String js = String.format(format, count, data);
    cordovaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        instance.webView.loadUrl("javascript:" + js);
      }
    });
  }

  /**
   * 会话列表通知回调
   * @param type
   * @param data
   */
  @JavascriptInterface
  public void OnConverListNotifyCallback(String type, String data){
    SqLog.i(TAG, " -------------- OnConverListNotifyCallback --------------");
    String format = "window.plugins.jmessagePlugin.receiveMsgInIOSConverListNotifyCallback('%s','%s');";
    final String js = String.format(format, type, data);
    cordovaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        instance.webView.loadUrl("javascript:" + js);
      }
    });
  }

  /**
   * 会话详情消息列表通知回调
   * @param type
   * @param data
   */
  @JavascriptInterface
  public void OnConverDetailNotifyCallback(String type, String data){
    SqLog.i(TAG, " -------------- OnConverDetailNotifyCallback --------------");
    String format = "window.plugins.jmessagePlugin.receiveMsgInIOSConverDetailNotifyCallback('%s','%s');"; // window.plugins.jmessagePlugin.
    final String js = String.format(format, type, data);
    cordovaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        instance.webView.loadUrl("javascript:" + js);
      }
    });
  }





  /**
   * 用于 Android 6.0 以上系统申请权限，具体可参考：
   * http://docs.Push.io/client/android_api/#android-60
   */
  public void requestPermission(JSONArray data, CallbackContext callbackContext) {
    JPushInterface.requestPermission(this.cordova.getActivity());
  }

  /**
   * 成功回调
   * @param resultData 回调信息
   * @param callbackContext
   */
  private void onSuccessCallback(final String resultData, final CallbackContext callbackContext){
    cordovaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (callbackContext != null) callbackContext.success(resultData);
      }
    });
  }

  /**
   * 异常回调
   * @param status 异常码
   * @param desc 描述
   * @param callbackContext
   */
  private void onErrorCallback(final int status, final String desc, final CallbackContext callbackContext){
    final String json = JMessageJSONUtils.formatErrorToJSONString(status, desc);
    cordovaActivity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if(callbackContext != null) callbackContext.error(json);
      }
    });
  }
}
