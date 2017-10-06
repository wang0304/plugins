package com.baimei.jmessage.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class JchatStringUtils{

  public static final String ENCODE_HTTP = "UTF-8";

  /**
   * 对文本进行编码
   */
  public static String paramEncode(String str){
    if(str == null) return "";
    try {
      str = URLEncoder.encode(str, ENCODE_HTTP);
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
     return str;
   }

}
