package com.baimei.jmessage.utils;

import com.alibaba.fastjson.JSONObject;
import com.baimei.jmessage.model.MyUserInfo;

import cn.jpush.im.android.api.model.UserInfo;

/**
 * JSON数据处理
 */
public class JMessageJSONUtils{

  public static String formatMyInfoToJSONString(UserInfo userInfo){
    if(userInfo == null) return "";
    return JSONObject.toJSONString(MyUserInfo.getInstance().setUserInfo(userInfo));
  }

  /**
   * 格式化错误信息为JSON串
   * @param status
   * @param description
   * @return
   */
  public static String formatErrorToJSONString(int status, String description){
    if(description == null) description = "";
    String format = "{\"status\":\"%d\", \"desc\":\"%s\"}";
    return String.format(format, status, description);
  }

  /**
   * 格式化标题数据为JSON串
   * @param title
   * @return
   */
  public static String formatTitleToJSONString(String title, int num){
    if(title == null) title = "";
    String format = "{\"title\":\"%s\", \"num\":\"%d\"}";
    return String.format(format, title, num);
  }

  /**
   * 格式化创建群组返回数据为JSON串
   * @param status
   * @param desc
   * @param conversation
   * @return
   */
  public static String formatCreateGroupResutJSONString(int status, String desc, String conversation){
    if(desc == null) desc = "";
    if(conversation == null) conversation = "";
    String format = "{\"status\":\"%d\", \"desc\":\"%s\", \"conversation\":\"%s\"}";
    return String.format(format, status, desc, conversation);
  }

}
