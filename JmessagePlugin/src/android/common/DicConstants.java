package com.baimei.jmessage.common;
/**
 * Created by baimei on 16/9/14.
 */

/**
 * author:
 * create: 16/9/14
 */
public class DicConstants {

    // 会话类型：single－单体；group－群组；
    public static final String CONVERSATION_TYPE_SIGLE = "single";
    public static final String CONVERSATION_TYPE_GROUP = "group";


    // 内容类型
    public static final String CONTENT_TYPE_TEXT = "text";
    public static final String CONTENT_TYPE_IMAGE = "image";
    public static final String CONTENT_TYPE_VOICE = "voice";
    public static final String CONTENT_TYPE_EVENT = "event";


    // 消息状态
    /// 发送息创建时的初始状态
    public static final String MSG_STATUS_0 = "";
    /// 消息正在发送过程中. UI 一般显示进度条
    public static final String MSG_STATUS_1 = "";
    /// 媒体类消息文件上传失败
    public static final String MSG_STATUS_2 = "";
    /// 媒体类消息文件上传成功
    public static final String MSG_STATUS_3 = "";
    /// 消息发送失败
    public static final String MSG_STATUS_4 = "";
    /// 消息发送成功
    public static final String MSG_STATUS_5 = "receive_success";
    /// 接收中的消息(还在处理)
    public static final String MSG_STATUS_6 = "";
    /// 接收消息时自动下载媒体失败
    public static final String MSG_STATUS_7 = "";
    /// 接收消息成功
    public static final String MSG_STATUS_8 = "";




    /*!
     * 通知事件类型
     */
    /// 事件类型: 登录被踢
    public static final String EVENT_TYPE_1 = "1";
    /// 事件类型: 群组被创建
    public static final String EVENT_TYPE_8 = "8";
    /// 事件类型: 退出群组
    public static final String EVENT_TYPE_9 = "9";
    /// 事件类型: 添加新成员
    public static final String EVENT_TYPE_10 = "10";
    /// 事件类型: 成员被踢出
    public static final String EVENT_TYPE_11 = "11";
    /// 事件类型: 群信息更新
    public static final String EVENT_TYPE_12 = "12";
    /// 事件类型: 免打扰变更
    public static final String EVENT_TYPE_37 = "37";
    /// 事件类型: 黑名单变更
    public static final String EVENT_TYPE_38 = "38";


    // 通知消息事件类型
    // 通知异常回调消息
    public static final String NOTIFY_ERROR_CALLBACK = "10101100";
    // 通知返回会话列表消息
    public static final String NOTIFY_CONVER_LIST = "10101105";
    // 通知返回会话列表消息
    public static final String NOTIFY_CONVER_JMSGMESSAGE = "10101101";
    // 会话详情消息信息刷新回调
    public static final String NOTIFY_CONVER_DETAIL_MSG_CALLBACK = "10101102";
    // 会话详情消息信息发送消息回调
    public static final String SEND_MSG_RESPONSE_CALLBACK = "10101103";
    // 加载头像回调
    public static final String LOAD_AVATAR_CALLBACK = "10101104";
    // 刷新会话标题
    public static final String REFRESH_CONVERSATION_TITLE = "10101106";

    // 网络状态通知：网络正常
    public static final String NOTIFY_NETWORK_OK = "10101201";
    // 网络状态通知：网络异常
    public static final String NOTIFY_NETWORK_ERROR = "10101202";


}
