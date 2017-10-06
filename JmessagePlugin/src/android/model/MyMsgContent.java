package com.baimei.jmessage.model;/**
 * Created by wzf on 16/9/14.
 */

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.baimei.jmessage.common.DicConstants;
import com.baimei.jmessage.common.JsonKeys;

import java.io.Serializable;

/**
 * author:
 * create: 16/9/14
 */
public class MyMsgContent implements Serializable {

    public String text;

    public long gid;
    public long fromUid;
    public long cTime;
    public long eventId;
    public int eventType;
    // 事件
    public JSONArray toUidList;


    public static MyMsgContent instance;
    public static MyMsgContent getInstance(){
        if(instance == null){
            instance = new MyMsgContent();
        }
        return instance;
    }

    public MyMsgContent setContent(String type, String contentStr){

        if(contentStr == null || contentStr.equals("")){
            return null;
        }

        JSONObject jsonObj = JSONObject.parseObject(contentStr);
        // 图片
        if(DicConstants.CONTENT_TYPE_IMAGE.equals(type)){

        }
        // 语音
        else if(DicConstants.CONTENT_TYPE_VOICE.equals(type)) {

        }
        // 事件
        else if(DicConstants.CONTENT_TYPE_EVENT.equals(type)) {
            setGid(jsonObj.getLong(JsonKeys.EVENT_CONTENT_GID));
            setEventId(jsonObj.getLong(JsonKeys.EVENT_CONTENT_ID));
            setEventType(jsonObj.getInteger(JsonKeys.EVENT_CONTENT_TYPE));
            setFromUid(jsonObj.getLong(JsonKeys.EVENT_CONTENT_FROM_UID));
            setcTime(jsonObj.getLong(JsonKeys.EVENT_CONTENT_CTIME));
            setToUidList(jsonObj.getJSONArray(JsonKeys.EVENT_CONTENT_UIDLIST));
        }
        // 文本
        else {
            setText(jsonObj.getString(JsonKeys.CONTENT_TEXT));
        }

        return this;
    }




    // ===========================  setter and getter ==========================
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JSONArray getToUidList() {
        return toUidList;
    }

    public void setToUidList(JSONArray toUidList) {
        this.toUidList = toUidList;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public long getFromUid() {
        return fromUid;
    }

    public void setFromUid(long fromUid) {
        this.fromUid = fromUid;
    }

    public long getcTime() {
        return cTime;
    }

    public void setcTime(long cTime) {
        this.cTime = cTime;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }
}
