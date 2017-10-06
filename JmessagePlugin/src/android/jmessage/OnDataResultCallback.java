package com.baimei.jmessage;
/**
 * OnDataResultCallback
 * 数据返回回调
 * Created by wzf on 16/7/4.
 */
public interface OnDataResultCallback {

  // 返回值
  public void gotResult(int respCode, String respMsg, String eventFuc, String jsonData);

}
