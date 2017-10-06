package com.baimei.jmessage.model;/**
 * Created by wzf on 16/9/18.
 */

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.model.Message;

/**
 * author:
 * create: 16/9/18
 */
public class MyMessage implements Serializable{

    public static MyMessage instance;
    public static MyMessage getInstance(){
        if(instance == null){
            instance = new MyMessage();
        }
        return instance;
    }

    public <T> T setMessage(Message message){

        if(message == null) return null;

        String json = JSONObject.toJSONString(message);
        ContentType contentType = message.getContentType();
        // 文本
        if(contentType.equals(ContentType.text)){
            return (T) MyTextMessage.getInstance().setMessage(json);
        }
        // 图片
        else if(contentType.equals(ContentType.image)) {
            return (T) MyImageMessage.getInstance().setMessage(json);
        }
        // 语音
        else if(contentType.equals(ContentType.voice)) {
            return (T) MyVoiceMessage.getInstance().setMessage(json);
        }
        // 事件
        else if(contentType.equals(ContentType.eventNotification)) {
            return (T) MyEventMessage.getInstance().setMessage(json);
        }

        else {
            return (T) this;
        }
    }

    // ======================== setter and getter ============================

}
