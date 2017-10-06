package com.baimei.jmessage.model;/**
 * Created by wzf on 16/9/13.
 */

import java.io.Serializable;

/**
 * 图片信息对象
 * author:
 * create: 16/9/13
 */
public class MyImageMessage implements Serializable {

    public static MyImageMessage instance;
    public static MyImageMessage getInstance(){
        if(instance == null){
            instance = new MyImageMessage();
        }
        return instance;
    }

    public MyImageMessage setMessage(String msgStr) {

        if (msgStr == null || "".equals(msgStr)) {
            return null;
        }

        return this;
    }
}
