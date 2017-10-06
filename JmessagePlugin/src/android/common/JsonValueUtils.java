package com.baimei.jmessage.common;
/**
 * Created by baimei on 16/9/14.
 */

import com.baimei.jmessage.common.DicConstants;
import com.baimei.jmessage.model.MyEventMessage;
import com.baimei.jmessage.model.MyImageMessage;
import com.baimei.jmessage.model.MyTextMessage;
import com.baimei.jmessage.model.MyVoiceMessage;

/**
 * author:
 * create: 16/9/14
 */
public class JsonValueUtils {


    /**
     * 将会话类型 “文本”转换为对应的“数字”
     * @param converType
     * @return
     */
    public static String changeConversationType(String converType){

        if(DicConstants.CONVERSATION_TYPE_SIGLE.equals(converType)){
            return "1";
        }

        else if(DicConstants.CONVERSATION_TYPE_GROUP.equals(converType)) {
            return "2";
        }

        else {
            return "";
        }
    }

    /**
     * 将内容类型 “文本”转换为对应的“数字”
     * @param contentType
     * @return
     */
    public static String changeContentType(String contentType){

        if(DicConstants.CONTENT_TYPE_IMAGE.equals(contentType)){
            return "2";
        }

        else if(DicConstants.CONTENT_TYPE_VOICE.equals(contentType)) {
            return "3";
        }

        else if(DicConstants.CONTENT_TYPE_EVENT.equals(contentType)) {
            return "4";
        }

        else {
            return "1";
        }
    }

    /**
     * 将消息状态 “文本”转换为对应的“数字”
     * @param status
     * @return
     */
    public static String changeMessageStatus(String status){

        if(DicConstants.MSG_STATUS_1.equals(status)){
            return "1";
        }

        else if(DicConstants.MSG_STATUS_2.equals(status)){
            return "2";
        }

        else if(DicConstants.MSG_STATUS_3.equals(status)){
            return "3";
        }

        else if(DicConstants.MSG_STATUS_4.equals(status)){
            return "4";
        }

        else if(DicConstants.MSG_STATUS_5.equals(status)){
            return "5";
        }

        else if(DicConstants.MSG_STATUS_6.equals(status)){
            return "6";
        }

        else if(DicConstants.MSG_STATUS_7.equals(status)){
            return "7";
        }

        else if(DicConstants.MSG_STATUS_8.equals(status)){
            return "8";
        }

        else {
            return "";
        }
    }

    /**
     * 解析消息内容
     * @param contentType
     * @param messageStr
     * @return
     */
    public static Object resolveMessage(String contentType, String messageStr){
        // 图片消息对象
        if(DicConstants.CONTENT_TYPE_IMAGE.equals(contentType)){
            return MyImageMessage.getInstance().setMessage(messageStr);
        }
        // 语音消息对象
        else if(DicConstants.CONTENT_TYPE_VOICE.equals(contentType)) {
            return MyVoiceMessage.getInstance().setMessage(messageStr);
        }
        // 事件消息对象
        else if(DicConstants.CONTENT_TYPE_EVENT.equals(contentType)) {
            return MyEventMessage.getInstance().setMessage(messageStr);
        }
        // 文本消息对象
        else {
            return MyTextMessage.getInstance().setMessage(messageStr);
        }
    }
}
