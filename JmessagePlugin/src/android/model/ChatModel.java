package com.baimei.jmessage.model;
/**
 * Created by baimei on 16/9/18.
 */

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Random;

import cn.jpush.im.android.api.model.Message;

/**
 * 数据模型
 * author:
 * create: 16/9/18
 */
public class ChatModel implements Serializable {

    private String modelId;
    private Long serverMessageId;

    // 图片
    private String photoIndex;
    private ImageSize imageSize;

    // 内容
    private ContentSize contentSize;
    private String contentHeight;
    private JSONObject message;

    // 事件
    private String isErrorMessage;
    private String messageError;

    // 时间
    private String isTime = "0";
    private String timeId;
    private long messageTime;


    public static ChatModel instance;
    public static ChatModel getInstance(){
        if(instance == null){
            instance = new ChatModel();
        }
        return instance;
    }

    /**
     * 转化MyMessage为ChatModel
     * @param msg
     * @return
     */
    public ChatModel setMessageInfo(Message msg){
        if(msg == null) return null;

        String json = JSONObject.toJSONString(MyMessage.getInstance().setMessage(msg));
        setMessage(JSONObject.parseObject(json));

        setContentSize(new ContentSize("", ""));
        setContentHeight("");
        setImageSize(new ImageSize("", ""));

        setIsErrorMessage("0");
        setMessageError("");

        setIsTime("0");
        setPhotoIndex("");
        setTimeId("");
        setMessageTime(msg.getCreateTime());

        setModelId("msg_" + msg.getCreateTime() + randomTimeId());
        setServerMessageId(msg.getServerMessageId());

        return this;
    }

    /**
     * 时间模型
     * @param msg
     * @return
     */
    public ChatModel setTimeMessage(Message msg){

        if(msg == null) return null;

        setMessage(obj2JSONObject(MyMessage.getInstance().setMessage(msg)));

        setPhotoIndex("");

        setIsTime("1");
        setTimeId(randomTimeId());
        setMessageTime(msg.getCreateTime());

        setIsErrorMessage("0");
        setMessageError("");

        setContentHeight("");
        setContentSize(new ContentSize("", ""));
        setImageSize(new ImageSize("", ""));

        setModelId("time_" + msg.getCreateTime() + randomTimeId());
        setServerMessageId(msg.getServerMessageId());

        return this;
    }

    /**
     * 图片模型
     * @param msg
     * @return
     */
    public ChatModel setImageMessage(int index, Message msg){

        if(msg == null) return null;

        setMessage(obj2JSONObject(MyMessage.getInstance().setMessage(msg)));

        setPhotoIndex(index + "");

        setIsTime("0");
        setTimeId("");
        setMessageTime(msg.getCreateTime());

        setIsErrorMessage("0");
        setMessageError("");

        setContentHeight("");
        setContentSize(new ContentSize("", ""));
        setImageSize(new ImageSize("", ""));

        setModelId("img_" + msg.getCreateTime() + randomTimeId());
        setServerMessageId(msg.getServerMessageId());

        return this;
    }

    /**
     * 事件模型
     * @param msg
     * @return
     */
    public ChatModel setEventMessage(Message msg){

        if(msg == null) return null;

        setMessage(obj2JSONObject(MyMessage.getInstance().setMessage(msg)));

        setPhotoIndex("");

        setIsTime("0");
        setTimeId("");
        setMessageTime(msg.getCreateTime());

        setIsErrorMessage("1");
        setMessageError("");

        setContentHeight("");
        setContentSize(new ContentSize("", ""));
        setImageSize(new ImageSize("", ""));

        setModelId("event_" + msg.getCreateTime() + randomTimeId());
        setServerMessageId(msg.getServerMessageId());

        return this;
    }

    /**
     * 随机生成timeID
     * @return
     */
    private String randomTimeId(){
        Random random = new Random();
        int x = random.nextInt(8999999);
        int timeId = x+1000000;
        return timeId + "";
    }

    public JSONObject obj2JSONObject(Object obj){
        String json = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(json);
    }

    // ========================= setter and getter ========================

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public Long getServerMessageId() {
        return serverMessageId;
    }

    public void setServerMessageId(Long serverMessageId) {
        this.serverMessageId = serverMessageId;
    }

    public String getPhotoIndex() {
        return photoIndex;
    }

    public void setPhotoIndex(String photoIndex) {
        this.photoIndex = photoIndex;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public ContentSize getContentSize() {
        return contentSize;
    }

    public void setContentSize(ContentSize contentSize) {
        this.contentSize = contentSize;
    }

    public String getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(String contentHeight) {
        this.contentHeight = contentHeight;
    }

    public JSONObject getMessage() {
        return message;
    }

    public void setMessage(JSONObject message) {
        this.message = message;
    }

    public String getIsErrorMessage() {
        return isErrorMessage;
    }

    public void setIsErrorMessage(String isErrorMessage) {
        this.isErrorMessage = isErrorMessage;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getIsTime() {
        return isTime;
    }

    public void setIsTime(String isTime) {
        this.isTime = isTime;
    }

    /**
     * 内容大小
     */
    public class ContentSize{
        public ContentSize(String width, String height){
            this.width = width;
            this.height = height;
        }
        public String width;
        public String height;
    }

    /**
     * 图片大小
     */
    public class ImageSize{
        public ImageSize(String width, String height){
            this.width = width;
            this.height = height;
        }
        public String width;
        public String height;
    }
}
