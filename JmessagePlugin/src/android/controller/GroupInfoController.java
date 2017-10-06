package com.baimei.jmessage.controller;
/**
 * Created by baimei on 16/9/20.
 */

import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baimei.jmessage.common.InterfaceFactory;
import com.baimei.jmessage.common.ResultObject;
import com.baimei.jmessage.model.Event;
import com.baimei.jmessage.model.MyConversation;
import com.baimei.jmessage.model.MyGroupInfo;
import com.baimei.jmessage.model.MyUserInfo;
import com.baimei.jmessage.utils.JMessageJSONUtils;
import com.baimei.jmessage.utils.ResponseCode;
import com.baimei.jmessage.utils.SqLog;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.android.eventbus.EventBus;
import cn.jpush.im.api.BasicCallback;

/**
 * 群组信息控制器
 * author:
 * create: 16/9/20
 */
public class GroupInfoController {

    private final String TAG = "jchat";

    public static GroupInfoController instatnce;
    public static GroupInfoController getInstatnce(){
        if(instatnce == null){
            instatnce = new GroupInfoController();
        }
        return instatnce;
    }

    /**
     * 创建群组
     * @param context
     * @param groupName 自定义群组名称
     * @param groupDesc 自定义群组描述
     * @param callback
     */
    public void createGroup(final Context context, String groupName, String groupDesc,
                            final InterfaceFactory.RequestDataCallback callback){

        JMessageClient.createGroup(groupName, groupDesc, new CreateGroupCallback() {

            @Override
            public void gotResult(final int status, String msg, final long groupId) {
                SqLog.i(TAG, "## -------- createGroup gotResult status: " + status);
                if (status == 0) { // 创建群组成功

                    // 获取会话对象
                    Conversation conv = Conversation.createGroupConversation(groupId);
                    MyConversation myConv = MyConversation.getInstance().setConversation(conv);

                    // 组装JSON串
                    //JSONObject myConvJSONObj = MyConversation.getInstance().getJSONObject(myConv);
                    //String successJson = JMessageJSONUtils.formatCreateGroupResutJSONString(status, msg, myConvJSONObj);

                    String myConvJsonStr = JSONObject.toJSONString(myConv);
                    SqLog.i(TAG, "## -------- createGroup myconvJson: " + myConvJsonStr);
                    if (callback != null) callback.success(status, new ResultObject(myConvJsonStr));

                } else { // 创建群组失败
                    String error = ResponseCode.onHandle(context, status);
                    SqLog.i(TAG, "## -------- createGroup error: " + error);
                    if (callback != null) callback.error(status, error);
                }
            }

        });
    }

    /**
     * 获取群组成员信息
     * @param aGroupId
     * @param callback
     */
    public void getGroupInfo(String aGroupId, final InterfaceFactory.RequestDataCallback callback){
        long groupId = Long.parseLong(aGroupId);
        JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {

            @Override
            public void gotResult(int status, String msg, GroupInfo groupInfo) {
                if (status == 0) {
                    String json = "";
                    if (groupInfo != null) {
                        SqLog.i(TAG, "## -------- 群组详情 groupInfo: " + groupInfo);

                        MyGroupInfo myGroupInfo = MyGroupInfo.getInstance().setGroupInfo(groupInfo);
                        json = JSONObject.toJSONString(myGroupInfo);
                    }

                    if (callback != null) callback.success(status, new ResultObject(json));
                } else {
                    if (callback != null) callback.error(status, msg);
                }
            }

        });
    }

    /**
     * 根据关键词获取成员信息
     * @param aGroupId
     * @param userName
     * @param callback
     */
    public void getGroupMemberByUserName(String aGroupId, final String userName, final InterfaceFactory.RequestDataCallback callback){
        long groupId = Long.parseLong(aGroupId);
        JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {

            @Override
            public void gotResult(int status, String msg, GroupInfo groupInfo) {
                if (status == 0) {
                    String json = "";
                    if (groupInfo != null) {

                        UserInfo userInfo = groupInfo.getGroupMemberInfo(userName);
                        SqLog.i(TAG, "## -------- 查询用户 userInfo: " + userInfo);

                        MyUserInfo myUserInfo = MyUserInfo.getInstance().setUserInfo(userInfo);
                        json = JSONObject.toJSONString(myUserInfo);
                    }

                    if (callback != null) callback.success(status, new ResultObject(json));
                } else {
                    if (callback != null) callback.error(status, msg);
                }
            }

        });
    }

    /**
     * 获取群组成员信息
     * @param aGroupId
     * @param callback
     */
    public void getGroupMembers(String aGroupId, final InterfaceFactory.RequestDataCallback callback){
        final long groupId = Long.parseLong(aGroupId);
        JMessageClient.getGroupInfo(groupId, new GetGroupInfoCallback() {

            @Override
            public void gotResult(int status, String msg, GroupInfo groupInfo) {
                if (status == 0) {
                    if (groupInfo != null) {
                        List<UserInfo> members = groupInfo.getGroupMembers();
                    }
                } else {

                }
            }

        });
    }


    /**
     * 添加好友到群组
     * @param aGroupId
     * @param userName
     * @param callback
     */
    public void addMemberToGroup(String aGroupId, String userName, final InterfaceFactory.RequestDataCallback callback){

        long groupId = Long.parseLong(aGroupId);

        List<String> userList = new ArrayList<String>();
        userList.add(userName);

        JMessageClient.addGroupMembers(groupId, userList, new BasicCallback() {
            @Override
            public void gotResult(int status, String msg) {
                if (status == 0) {
                    if (callback != null) callback.success(status, new ResultObject(msg));
                } else {
                    if (callback != null) callback.error(status, msg);
                }
            }
        });

    }

    /**
     * 批量添加好友到群组
     * @param aGroupId
     * @param userArrayJson 好友名称列表JSON字符串
     * @param callback
     */
    public void addMembersToGroup(String aGroupId, String userArrayJson, final InterfaceFactory.RequestDataCallback callback){

        if(userArrayJson == null || "".equals(userArrayJson)){
            if (callback != null) callback.error(0, "请选择好友！");
            return;
        }

        long groupId = Long.parseLong(aGroupId);

        List<String> userList = JSONArray.parseArray(userArrayJson, String.class);

        JMessageClient.addGroupMembers(groupId, userList, new BasicCallback() {
            @Override
            public void gotResult(int status, String msg) {
                if (status == 0) {
                    if (callback != null) callback.success(status, new ResultObject(msg));
                } else {
                    if (callback != null) callback.error(status, msg);
                }
            }
        });

    }

    /**
     * 从群组中移除好友
     * @param aGroupId
     * @param userName
     * @param callback
     */
    public void removeMemberFromGroup(String aGroupId, String userName, final InterfaceFactory.RequestDataCallback callback){

        long groupId = Long.parseLong(aGroupId);

        List<String> userList = new ArrayList<String>();
        userList.add(userName);

        JMessageClient.removeGroupMembers(groupId, userList, new BasicCallback() {
            @Override
            public void gotResult(int status, String msg) {
                if (status == 0) {
                    if (callback != null) callback.success(status, new ResultObject(msg));
                } else {
                    if (callback != null) callback.error(status, msg);
                }
            }
        });

    }

    /**
     * 批量删除群组成员
     * @param aGroupId
     * @param userArrayJson 好友名称列表JSON字符串
     * @param callback
     */
    public void removeMembersFromGroup(String aGroupId, String userArrayJson, final InterfaceFactory.RequestDataCallback callback){

        if(userArrayJson == null || "".equals(userArrayJson)){
            if (callback != null) callback.error(0, "请选择你要移除的群组成员！");
            return;
        }

        long groupId = Long.parseLong(aGroupId);

        List<String> userList = JSONArray.parseArray(userArrayJson, String.class);

        JMessageClient.removeGroupMembers(groupId, userList, new BasicCallback() {
            @Override
            public void gotResult(int status, String msg) {
                if (status == 0) {
                    if (callback != null) callback.success(status, new ResultObject(msg));
                } else {
                    if (callback != null) callback.error(status, msg);
                }
            }
        });

    }

    /**
     * 更新群组名称
     * @param aGroupId
     * @param newName
     * @param callback
     */
    public void updateGroupName(String aGroupId, String newName,
                                final InterfaceFactory.RequestDataCallback callback){

        final long groupId = Long.parseLong(aGroupId);

        JMessageClient.updateGroupName(groupId, newName, new BasicCallback() {
            @Override
            public void gotResult(final int status, final String desc) {
                if (status == 0) {
                    if(callback != null) callback.success(status, new ResultObject(desc));
                } else {
                    if(callback != null) callback.error(status, desc);
                }
            }
        });
    }


    /**
     * 退出群组
     * @param aGroupId
     * @param callback
     */
    public void exitGroup(String aGroupId,
                                final InterfaceFactory.RequestDataCallback callback){

        final long groupId = Long.parseLong(aGroupId);

        JMessageClient.exitGroup(groupId, new BasicCallback() {
            @Override
            public void gotResult(final int status, final String desc) {
                if (status == 0) {
                    if(callback != null) callback.success(status, new ResultObject(desc));
                } else {
                    if(callback != null) callback.error(status, desc);
                }
            }
        });
    }

    /**
     * 删除群组会话信息
     * @param aGroupId
     * @param callback
     */
    public void deleteGroupConversation(String aGroupId,
                                final InterfaceFactory.RequestDataCallback callback){

        final long groupId = Long.parseLong(aGroupId);

        boolean isDel = JMessageClient.deleteGroupConversation(groupId);
        if (isDel) {
          EventBus.getDefault().post(new Event.LongEvent(false, groupId));
          if(callback != null) callback.success(0, new ResultObject("已成功删除群聊会话信息!"));
        } else {
          if(callback != null) callback.error(0, "未能删除群聊会话信息!");
        }
    }
}
