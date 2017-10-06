package com.baimei.jmessage.model;
/**
 * Created by baimei on 16/9/13.
 */

import java.io.Serializable;

/**
 * 语音消息对象
 * author:
 * create: 16/9/13
 */
public class MyVoiceMessage implements Serializable {

    public static MyVoiceMessage instance;
    public static MyVoiceMessage getInstance(){
        if(instance == null){
            instance = new MyVoiceMessage();
        }
        return instance;
    }

    public MyVoiceMessage setMessage(String msgStr) {

        if (msgStr == null || "".equals(msgStr)) {
            return null;
        }

        return this;
    }
}
