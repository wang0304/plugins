package com.baimei.jmessage.common;
/**
 * Created by baimei on 16/9/14.
 */

/**
 * author:
 * create: 16/9/14
 */
public class ResultObject {

    private Object obj;

    public ResultObject(Object obj){
        this.obj = obj;
    }

    public void setObj(Object obj){
        this.obj = obj;
    }

    public <T> T getObj() {
        return (T) obj;
    }

}
