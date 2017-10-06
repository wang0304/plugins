package com.baimei.jmessage.common;
/**
 * Created by baimei on 16/9/14.
 */

/**
 * 接口工厂
 * author:
 * create: 16/9/14
 */
public class InterfaceFactory {

    /**
     * 会话列表通知回调
     */
    public interface OnConversatioListNotifyCallback{
        /**
         * 通知事件回调方法
         * @param type 事件类型
         * @param obj 回调数据
         */
        public void callback(String type, ResultObject obj);
    }

    /**
     * 会话详情消息列表通知回调
     */
    public interface OnConversatioDetailNotifyCallback{
        /**
         * 通知事件回调方法
         * @param type 事件类型
         * @param obj 回调数据
         */
        public void callback(String type, ResultObject obj);
    }

    /**
     * 会话列表数据回调
     */
    public interface OnConversatioListDataCallback{

        /**
         * 会话列表数据回调方法
         * @param unReadCount 未读消息数
         * @param obj 回调数据
         */
        public void callback(int unReadCount, ResultObject obj);
    }

    /**
     * 请求回调
     */
    public interface RequestDataCallback{

        /**
         * 会话列表数据回调方法
         * @param respStatus 响应状态
         * @param obj 回调数据
         */
        public void success(int respStatus, ResultObject obj);

        /**
         * 会话列表数据回调方法
         * @param errorCode 状态码
         * @param error 提示信息
         */
        public void error(int errorCode, String error);
    }

}
