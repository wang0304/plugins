package com.baimei.jmessage.model;
/**
 * Created by baimei on 16/9/13.
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 群组对象
 * author:
 * create: 16/9/13
 */
public class MyGroupInfo implements Serializable {

//    private long groupID;
//    private String groupOwnerID;
//    private String groupName;
//    private String groupDescription;
//    private int groupLevel;
//    private int groupFlag;
//    private int maxMemberCount;
//    private int noDisturb;
//
//    private JSONArray groupMembers;

    private long gid;
    private String name;
    private int level;
    private int flag;
    private int maxMemberCount;
    private int noDisturb;
    private String owner;
    private int ownerAppKey;
    private String desc;

    private JSONArray membersString;

    public static MyGroupInfo instance;
    public static MyGroupInfo getInstance(){
        if(instance == null){
            instance = new MyGroupInfo();
        }
        return instance;
    }

    // 转换数据
    public MyGroupInfo setGroupInfo(GroupInfo groupInfo){

        if(groupInfo == null) return null;

        setGid(groupInfo.getGroupID());

        setOwner(groupInfo.getGroupOwner());
        setName(groupInfo.getGroupName());

        setDesc(groupInfo.getGroupDescription());

        setFlag(groupInfo.getGroupFlag());
        setLevel(groupInfo.getGroupLevel());

        setMaxMemberCount(groupInfo.getMaxMemberCount());
        setNoDisturb(groupInfo.getNoDisturb());


        JSONArray memberArray = null;
        List<UserInfo> userList = groupInfo.getGroupMembers();
        if(userList != null && !userList.isEmpty()){
            List<JSONObject> newUserList = new ArrayList<JSONObject>();
            for(UserInfo info : userList){
                MyUserInfo myUserInfo = MyUserInfo.getInstance().setUserInfo(info);
                String json = JSONObject.toJSONString(myUserInfo);
                JSONObject jsonObj = JSONObject.parseObject(json);
                newUserList.add(jsonObj);
            }
            String jsonArrayString = JSONArray.toJSONString(newUserList);
            memberArray = JSONArray.parseArray(jsonArrayString);
        }
        setMembersString(memberArray);

        return this;
    }

    /**
     * 获取JSON对象
     * @param groupInfo
     * @return
     */
    public JSONObject getJSONObject(MyGroupInfo groupInfo){
        if(groupInfo == null) return null;

        String json = JSONObject.toJSONString(groupInfo);
        return JSONObject.parseObject(json);
    }

    // =========================== setter and getter =============================
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getMaxMemberCount() {
        return maxMemberCount;
    }

    public void setMaxMemberCount(int maxMemberCount) {
        this.maxMemberCount = maxMemberCount;
    }

    public int getNoDisturb() {
        return noDisturb;
    }

    public void setNoDisturb(int noDisturb) {
        this.noDisturb = noDisturb;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getOwnerAppKey() {
        return ownerAppKey;
    }

    public void setOwnerAppKey(int ownerAppKey) {
        this.ownerAppKey = ownerAppKey;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public JSONArray getMembersString() {
        return membersString;
    }

    public void setMembersString(JSONArray membersString) {
        this.membersString = membersString;
    }

}
