package com.baimei.jmessage.model;
/**
 * Created by baimei on 16/9/13.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baimei.jmessage.common.JsonKeys;
import com.baimei.jmessage.common.JsonValueUtils;

import java.io.Serializable;

/**
 * 事件信息对象
 * author:
 * create: 16/9/13
 */
public class MyEventMessage implements Serializable {

    private String msgId;
    private String targetAppKey;
    private String fromAppKey;
    private String isReceived;
    private String contentType;
    private String otherSide;
    private MyMsgContent content;
    private String serverMessageId;
    private String status;


    public static MyEventMessage instance;
    public static MyEventMessage getInstance(){
        if(instance == null){
            instance = new MyEventMessage();
        }
        return instance;
    }

    public MyEventMessage setMessage(String msgStr) {

        if (msgStr == null || "".equals(msgStr)) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(msgStr);

        // AppKey
        setFromAppKey(jsonObject.getString(JsonKeys.MSG_FROM_APP_KEY));
        setTargetAppKey(jsonObject.getString(JsonKeys.MSG_TARGET_APP_KEY));

        // 1-接收消息／0-发送消息
        String direct = jsonObject.getString(JsonKeys.MSG_DIRECT);
        if("receive".equals(direct)){
            setIsReceived("1");
        } else {
            setIsReceived("0");
        }

        // 消息ID
        setMsgId("");   // 等待完善

        setOtherSide(jsonObject.getString(JsonKeys.MSG_FROME_NAME));

        setServerMessageId(jsonObject.getString(JsonKeys.MSG_SERVER_MSG_ID));

        // 消息状态
        String status = jsonObject.getString(JsonKeys.MSG_STATUS);
        setStatus(JsonValueUtils.changeMessageStatus(status));

        // 内容类型
        String contentType = jsonObject.getString(JsonKeys.MSG_CONTENT_TYPE);
        setContentType(JsonValueUtils.changeContentType(contentType));

        // 内容
        String contentStr = jsonObject.getString(JsonKeys.MSG_CONTENT);
        MyMsgContent content = MyMsgContent.getInstance().setContent(contentType, contentStr);
        setContent(content);

        return this;
    }

    // =========================
    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTargetAppKey() {
        return targetAppKey;
    }

    public void setTargetAppKey(String targetAppKey) {
        this.targetAppKey = targetAppKey;
    }

    public String getFromAppKey() {
        return fromAppKey;
    }

    public void setFromAppKey(String fromAppKey) {
        this.fromAppKey = fromAppKey;
    }

    public String getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(String isReceived) {
        this.isReceived = isReceived;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOtherSide() {
        return otherSide;
    }

    public void setOtherSide(String otherSide) {
        this.otherSide = otherSide;
    }

    public MyMsgContent getContent() {
        return content;
    }

    public void setContent(MyMsgContent content) {
        this.content = content;
    }

    public String getServerMessageId() {
        return serverMessageId;
    }

    public void setServerMessageId(String serverMessageId) {
        this.serverMessageId = serverMessageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
