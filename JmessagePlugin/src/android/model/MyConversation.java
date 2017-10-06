package com.baimei.jmessage.model;
/**
 * Created by baimei on 16/9/13.
 */

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.baimei.jmessage.common.JsonKeys;
import com.baimei.jmessage.common.JsonValueUtils;

import java.io.Serializable;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 会话对象
 * author:
 * create: 16/9/13
 */
public class MyConversation implements Serializable {

    private String avatarPath;
    private String unreadCount;
    private String title;
    private String targetId;
    private String conversationType;
    private Object latestMessage;

    private JSONObject targetJson;

    // 实例化
    private static MyConversation instance;
    public static MyConversation getInstance(){
        if(instance == null){
            instance = new MyConversation();
        }
        return instance;
    }

    public MyConversation setConversation(Conversation conver){

        if(conver == null) return null;

        String jsonStr = JSONObject.toJSONString(conver);
        JSONObject jsonObj = JSONObject.parseObject(jsonStr);

        // 会话类型
        String conversationType = jsonObj.getString(JsonKeys.CONVER_TYPE);
        Log.i("jchat", "<<<<<<<<<<< conversationType: " + conversationType);
        String type = JsonValueUtils.changeConversationType(conversationType);
        setConversationType(type);

        if("1".equals(type)){

            MyUserInfo myUserInfo = MyUserInfo.getInstance().setUserInfo((UserInfo) conver.getTargetInfo());
            JSONObject userJson = MyUserInfo.getInstance().obj2JSONObject(myUserInfo);
            setTargetJson(userJson);

        } else if("2".equals(type)) {

            MyGroupInfo myGroupInfo = MyGroupInfo.getInstance().setGroupInfo((GroupInfo) conver.getTargetInfo());
            JSONObject groupJson = MyUserInfo.getInstance().obj2JSONObject(myGroupInfo);
            setTargetJson(groupJson);

        } else;

        // 头像
        setAvatarPath("");

        // 单体／群组ID
        setTargetId(jsonObj.getString(JsonKeys.CONVER_TARGET_ID));

        // 标题
        setTitle(jsonObj.getString(JsonKeys.CONVER_TITLE));

        // 未读消息数
        setUnreadCount(jsonObj.getString(JsonKeys.CONVER_UN_READ_COUNT));

        // 最新消息信息
        String lastestMsgType = jsonObj.getString(JsonKeys.CONVER_LAST_TYPE);
        String lastestMsg = jsonObj.getString(JsonKeys.CONVER_LAST_MSG);
        setLatestMessage(JsonValueUtils.resolveMessage(lastestMsgType, lastestMsg));

        return this;
    }

    /**
     * Java对象转换成JSON对象
     * @param conv
     * @return
     */
    public JSONObject getJSONObject(MyConversation conv){
        String myConvJson = JSONObject.toJSONString(conv);
        return JSONObject.parseObject(myConvJson);
    }


    // ================================  setter and getter ===============================
    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getConversationType() {
        return conversationType;
    }

    public void setConversationType(String conversationType) {
        this.conversationType = conversationType;
    }

    public JSONObject getTargetJson() {
        return targetJson;
    }

    public void setTargetJson(JSONObject targetJson) {
        this.targetJson = targetJson;
    }

    public <T> T getLatestMessage() {
        return (T) latestMessage;
    }

    public void setLatestMessage(Object latestMessage) {
        this.latestMessage = latestMessage;
    }


}
