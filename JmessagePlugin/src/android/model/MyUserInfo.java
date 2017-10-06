package com.baimei.jmessage.model;
/**
 * Created by baimei on 16/9/13.
 */

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.baimei.jmessage.utils.SqLog;

import java.io.File;
import java.io.Serializable;

import cn.jmessage.android.uikit.chatting.utils.FileHelper;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 用户信息
 * author:
 * create: 16/9/13
 */
public class MyUserInfo implements Serializable{

    private long uid;
    private String username;
    private String address;
    private String password;
    private String createTime;
    private String noteText;
    private String noteName;
    private String avatar;
    private int blackList;
    private int star;
    private String isInBlacklist;
    private String thumbAvatarPath;
    private String originAvatarPath;
    private String thumbAvatarData;
    private String token;
    private UserInfo.Gender gender;
    private String resourceID;
    private String appKey;
    private int noDisturb;
    private String signature;
    private String isNoDisturb;
    private String region;
    private String nickname;
    private long birthday;

    public static MyUserInfo instance;
    public static MyUserInfo getInstance(){
        if(instance == null){
            instance = new MyUserInfo();
        }
        return instance;
    }

    // 转换数据
    public MyUserInfo setUserInfo(UserInfo user){

        if(user == null) return null;

        setUid(user.getUserID());

        setUsername(user.getUserName());
        setAddress(user.getAddress());
        setAppKey(user.getAppKey());
        setAvatar(user.getAvatar());
        setBirthday(user.getBirthday());
        setBlackList(user.getBlacklist());
        setGender(user.getGender());
        setNickname(user.getNickname());
        setNoDisturb(user.getNoDisturb());
        setNoteName(user.getNotename());
        setNoteText(user.getNoteText());
        setRegion(user.getRegion());
        setSignature(user.getSignature());
        setStar(user.getStar());

        SqLog.i("jchat", " # 用户头像small地址avatar: " + user.getAvatar());
        if(user.getAvatar() == null){
            setThumbAvatarPath("");
            setOriginAvatarPath("");
        } else {
            File avatarFile = user.getAvatarFile();
//            SqLog.i("jchat", " # 用户头像small地址avatarFile-String: " + avatarFile.toString());
//            SqLog.i("jchat", " # 用户头像small地址avatarFile-url: " + avatarFile.toURI());
//            SqLog.i("jchat", " # 用户头像small地址avatarFile-path: " + avatarFile.getPath());
//            SqLog.i("jchat", " # 用户头像small地址avatarFile-absolute: " + avatarFile.getAbsolutePath());
//            try {
//                SqLog.i("jchat", " @ 用户头像large地址avatarFile-canonical: " + avatarFile.getCanonicalPath());
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }

            String path;
            if (avatarFile != null && avatarFile.exists()) {
                path = avatarFile.getAbsolutePath();
            } else {
                path = FileHelper.getUserAvatarPath(user.getUserName());
            }

            SqLog.i("jchat", " # 用户头像avatar: " + path);
            setThumbAvatarPath(path);
            setOriginAvatarPath(path);
        }

        return this;
    }

    public JSONObject obj2JSONObject(Object obj){
        String json = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(json);
    }


    // =========================   setter() and getter() =================================
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBlackList() {
        return blackList;
    }

    public void setBlackList(int blackList) {
        this.blackList = blackList;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getIsInBlacklist() {
        return isInBlacklist;
    }

    public void setIsInBlacklist(String isInBlacklist) {
        this.isInBlacklist = isInBlacklist;
    }

    public String getThumbAvatarPath() {
        return thumbAvatarPath;
    }

    public void setThumbAvatarPath(String thumbAvatarPath) {
        this.thumbAvatarPath = thumbAvatarPath;
    }

    public String getOriginAvatarPath() {
        return originAvatarPath;
    }

    public void setOriginAvatarPath(String originAvatarPath) {
        this.originAvatarPath = originAvatarPath;
    }

    public String getThumbAvatarData() {
        return thumbAvatarData;
    }

    public void setThumbAvatarData(String thumbAvatarData) {
        this.thumbAvatarData = thumbAvatarData;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserInfo.Gender getGender() {
        return gender;
    }

    public void setGender(UserInfo.Gender gender) {
        this.gender = gender;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public int getNoDisturb() {
        return noDisturb;
    }

    public void setNoDisturb(int noDisturb) {
        this.noDisturb = noDisturb;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIsNoDisturb() {
        return isNoDisturb;
    }

    public void setIsNoDisturb(String isNoDisturb) {
        this.isNoDisturb = isNoDisturb;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }
}
