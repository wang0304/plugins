var exec = require('cordova/exec');

var JmessagePlugin = function(){

};


// 会话列表JS结果回调
JmessagePlugin.prototype.converListCallback;

// iOS回调函数
JmessagePlugin.prototype.receiveIOSCallback;
// iOS通知回调函数
JmessagePlugin.prototype.receiveIOSConverListNotifyCallback;
JmessagePlugin.prototype.receiveIOSConverDetailNotifyCallback;

JmessagePlugin.prototype.receiveMessage = {}

/**
 * 显示日志
 * @param msg
 */
var showLog = function (msg) {
  // var isShow = 1; // 0-false; 1-true;
  // if(isShow == 1){
    console.log(msg);
  // }
}

// 判断是否为iOS平台
JmessagePlugin.prototype.isPlatformIOS = function() {
  var isPlatformIOS = device.platform == "iPhone"
    || device.platform == "iPad"
    || device.platform == "iPod touch"
    || device.platform == "iOS";
  return isPlatformIOS;
}

JmessagePlugin.prototype.error_callback = function(msg) {
  showLog("Javascript Callback Error: " + msg);
}

// JmessagePlugin.prototype.call_native = function(name, args, callback) {
//   ret = exec(callback, this.error_callback, 'JmessagePlugin', name, args);
//   return ret;
// }

JmessagePlugin.prototype.call_native = function(name, args, callback, error_callback) {
  ret = exec(callback, error_callback, 'JmessagePlugin', name, args);
  return ret;
}

// public methods
JmessagePlugin.prototype.init = function() {
  showLog("-----------  jmessage_init ----------");
  data = [];
  this.call_native("init", data, null, null);
}

// 初始化会话列表
JmessagePlugin.prototype.initConverListCtrl = function(callback, error_callback, notify_callback) {
  showLog("-----------  jmessage_init_conver_list ----------");
  try {
    var data = [];
    this.receiveIOSConverListNotifyCallback = notify_callback;
    this.call_native("initConverListCtrl", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

// 初始化会话详细信息列表
JmessagePlugin.prototype.initConverViewCtrl = function(targetId, converType, callback, error_callback, notify_callback) {
  showLog("-----------  jmessage_init_conver_detail ----------");
  try {
    var data = [targetId, converType];
    this.receiveIOSConverDetailNotifyCallback = notify_callback;
    this.call_native("initConverViewCtrl", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 调用注册
 */
JmessagePlugin.prototype.doRegister = function(userId, password, callback, error_callback) {
  showLog("-----------  jmessage_register ----------");
  try {
    var data = [userId, password];
    this.receiveIOSCallback = callback;
    this.call_native("doRegister", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 调用登录
 */
JmessagePlugin.prototype.login = function(userId, password, callback, error_callback) {
  showLog("-----------  jmessage_login ----------");
  try {
    var data = [userId, password];
    this.receiveIOSCallback = callback;
    this.call_native("login", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 退出登录
 */
JmessagePlugin.prototype.exitLogin = function(callback, error_callback) {
  showLog("-----------  jmessage_exit_login ----------");
  try {
    var data = [];
    this.receiveIOSCallback = callback;
    this.call_native("exitLogin", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 获取用户信息
 */
JmessagePlugin.prototype.getMyInfo = function(callback, error_callback) {
  showLog("-----------  jmessage_getMyinfo ----------");
  try {
    var data = [];
    this.receiveIOSCallback = callback;
    this.call_native("getMyInfo", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 获取用户信息
 */
JmessagePlugin.prototype.getMineInfo = function(callback, error_callback) {
  showLog("-----------  jmessage_getMineinfo ----------");
  try {
    var data = [];
    this.receiveIOSCallback = callback;
    this.call_native("getMineInfo", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 新增群成员
 */
JmessagePlugin.prototype.getUserInfo = function(username, callback, error_callback) {
  showLog("-----------  jmessage_getUserinfo ----------");
  try {
    var data = [username];
    this.receiveIOSCallback = callback;
    this.call_native("getUserInfo", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 设置用户昵称
 */
JmessagePlugin.prototype.setNickname = function(nickname, callback, error_callback) {
  showLog("-----------  jmessage_setNickname ----------");
  try {
    var data = [nickname];
    this.receiveIOSCallback = callback;
    this.call_native("setNickname", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 设置用户头像
 */
JmessagePlugin.prototype.updateUserAvatar = function(path, callback, error_callback) {
  showLog("-----------  jmessage_update_user_avatar ----------");
  try {
    var data = [path];
    this.receiveIOSCallback = callback;
    this.call_native("updateUserAvatar", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 获取头像
 * @param username 用户名称
 */
JmessagePlugin.prototype.loadConverDetailAvatar = function(username, callback, error_callback) {
  showLog("-----------  jmessage_loadConverDetailAvatar ----------");
  try {
    var data = [username];
    this.receiveIOSCallback = callback;
    this.call_native("loadConverDetailAvatar", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}


/**
 * 获取会话列表头像
 * @param username 用户名称
 */
JmessagePlugin.prototype.loadConverListAvatar = function(targetId, callback, error_callback) {
  showLog("-----------  jmessage_loadConverListAvatar ----------");
  try {
    var data = [targetId];
    this.receiveIOSCallback = callback;
    this.call_native("loadConverListAvatar", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 创建群组
 */
JmessagePlugin.prototype.createGroup = function(groupName, groupDesc, callback, error_callback) {
  showLog("-----------  jmessage_createGroup ----------");
  try {
    var data = [groupName, groupDesc];
    this.receiveIOSCallback = callback;
    this.call_native("createGroup", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 添加好友
 */
JmessagePlugin.prototype.addFriend = function(targetId, callback, error_callback) {
  showLog("-----------  jmessage_add_friend ----------");
  try {
    var data = [targetId];
    this.receiveIOSCallback = callback;
    this.call_native("addFriend", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 判断好友是否存在
 */
JmessagePlugin.prototype.isExistConv = function(targetId, callback, error_callback) {
  showLog("-----------  jmessage_is_friend_exist ----------");
  try {
    var data = [targetId];
    this.receiveIOSCallback = callback;
    this.call_native("isExistConv", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 获取会话列表[会话]
 */
JmessagePlugin.prototype.getConversationList = function(callback, error_callback) {
  showLog("-----------  jmessage_get_conversation_list ----------");
  try {
    var data = [];
    this.receiveIOSCallback = callback;
    this.call_native("getConversationList", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 获取会话列表[会话]
 */
JmessagePlugin.prototype.getConversationList = function(callback, error_callback, result_callback) {
  showLog("-----------  jmessage_get_conversation_list ----------");
  try {
    var data = [];
    this.converListCallback = result_callback;
    this.call_native("getConversationList", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 创建单例会话信息
 */
JmessagePlugin.prototype.createSingleConversation = function(targetId, targetAppKey, callback, error_callback) {
  showLog("-----------  jmessage_create_single_conversation ----------");
  try {
    var data = [targetId, targetAppKey];
    this.receiveIOSCallback = callback;
    this.call_native("createSingleConversation", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 调用单例会话信息
 */
JmessagePlugin.prototype.getSingleConversation = function(targetId, targetAppKey, callback, error_callback) {
  showLog("-----------  jmessage_get_single_conversation ----------");
  try {
    var data = [targetId, targetAppKey];
    this.receiveIOSCallback = callback;
    this.call_native("getSingleConversation", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 获取群组信息
 */
JmessagePlugin.prototype.getGroupInfo = function(groupId, callback, error_callback) {
  showLog("-----------  jmessage_get_groupinfo ----------");
  try {
    var data = [groupId];
    this.receiveIOSCallback = callback;
    this.call_native("getGroupInfo", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 根据用户名获取群组成员信息
 */
JmessagePlugin.prototype.getGroupMemberByUserName = function(groupId, userName, callback, error_callback) {
  showLog("-----------  jmessage_getGroupMemberByUserName ----------");
  try {
    var data = [groupId, userName];
    this.receiveIOSCallback = callback;
    this.call_native("getGroupInfo", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 获取群组成员信息
 */
JmessagePlugin.prototype.getGroupMembers = function(groupId, callback, error_callback) {
  showLog("-----------  jmessage_get_groupinfo ----------");
  try {
    var data = [groupId];
    this.receiveIOSCallback = callback;
    this.call_native("getGroupMembers", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 添加成员到群组
 */
JmessagePlugin.prototype.addMemberToGroup = function(groupId, userName, callback, error_callback) {
  showLog("-----------  jmessage_addMemberToGroup ----------");
  try {
    var data = [groupId, userName];
    this.receiveIOSCallback = callback;
    this.call_native("addMemberToGroup", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 批量添加成员到群组
 * @param userArrayJson 用户集合JSON串
 */
JmessagePlugin.prototype.addMembersToGroup = function(groupId, userArrayJson, callback, error_callback) {
  showLog("-----------  jmessage_addMembersToGroup ----------");
  try {
    var data = [groupId, userArrayJson];
    this.receiveIOSCallback = callback;
    this.call_native("addMembersToGroup", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 从群组中移除成员
 */
JmessagePlugin.prototype.removeMemberFromGroup = function(groupId, userName, callback, error_callback) {
  showLog("-----------  jmessage_removeMemberFromGroup ----------");
  try {
    var data = [groupId, userName];
    this.receiveIOSCallback = callback;
    this.call_native("removeMemberFromGroup", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 批量移除群组成员
 * @param userArrayJson 用户集合JSON串
 */
JmessagePlugin.prototype.removeMembersFromGroup = function(groupId, userArrayJson, callback, error_callback) {
  showLog("-----------  jmessage_removeMembersFromGroup ----------");
  try {
    var data = [groupId, userArrayJson];
    this.receiveIOSCallback = callback;
    this.call_native("removeMembersFromGroup", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 批量移除群组成员
 * @param groupId
 * @param groupname
 */
JmessagePlugin.prototype.updateGroupName = function(groupId, groupname, callback, error_callback) {
  showLog("-----------  jmessage_updateGroupName ----------");
  try {
    var data = [groupId, groupname];
    this.receiveIOSCallback = callback;
    this.call_native("updateGroupName", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 退出群聊
 * @param groupId
 */
JmessagePlugin.prototype.exitGroup = function(groupId, callback, error_callback) {
  showLog("-----------  jmessage_exitGroup ----------");
  try {
    var data = [groupId];
    this.receiveIOSCallback = callback;
    this.call_native("exitGroup", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 删除群聊会话
 * @param groupId
 */
JmessagePlugin.prototype.deleteGroupConversation = function(groupId, callback, error_callback) {
  showLog("-----------  jmessage_deleteGroupConversation ----------");
  try {
    var data = [groupId];
    this.receiveIOSCallback = callback;
    this.call_native("deleteGroupConversation", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 创建群组会话信息
 */
JmessagePlugin.prototype.createGroupConversation = function(groupId, callback, error_callback) {
  showLog("-----------  jmessage_create_group_conversation ----------");
  try {
    var data = [groupId];
    this.receiveIOSCallback = callback;
    this.call_native("createGroupConversation", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 调用群组会话信息
 */
JmessagePlugin.prototype.getGroupConversation = function(groupId, callback, error_callback) {
  showLog("-----------  jmessage_get_group_conversation ----------");
  try {
    var data = [groupId];
    this.receiveIOSCallback = callback;
    this.call_native("getGroupConversation", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 调用会话消息列表
 * @param aStart 页码
 * @param aOffset 增量(每页条数)
 */
JmessagePlugin.prototype.getConvMsgList = function(aStart, aOffset, callback, error_callback) {
  showLog("-----------  jmessage_get_conv_msg_list ----------");
  try {
    var data = [aStart, aOffset];
    this.receiveIOSCallback = callback;
    this.call_native("getConvMsgList", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 加载更多会话消息列表
 * @param aStart 页码
 * @param aOffset 增量(每页条数)
 */
JmessagePlugin.prototype.loadMoreConverDetailMsgList = function(aStart, aOffset, callback, error_callback) {
  showLog("-----------  jmessage_load_more_conv_msg_list ----------");
  try {
    var data = [aStart, aOffset];
    //this.receiveIOSCallback = callback;
    this.call_native("loadMoreConverDetailMsgList", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 退出会话
 */
JmessagePlugin.prototype.exitConversation = function () {
  showLog("-----------  jmessage_exitConversation ----------");
  try {
    var data = [];
    this.receiveIOSCallback = callback;
    this.call_native("exitConversation", data, null, null);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 退出会话
 */
JmessagePlugin.prototype.sendMessage = function (msgContent, callback, error_callback) {
  showLog("-----------  jmessage_sendMessage ----------");
  try {
    var data = [msgContent];
    this.receiveIOSCallback = callback;
    this.call_native("sendMessage", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}

/**
 * 打开会话页面
 */
JmessagePlugin.prototype.openConvListActivity = function (callback, error_callback) {
  showLog("-----------  jmessage_openConvListActivity ----------");
  try {
    var data = [];
    this.receiveIOSCallback = callback;
    this.call_native("openConvListActivity", data, callback, error_callback);
  } catch(exception) {
    console.log(exception);
  }
}


/**
 * 会话列表回调函数
 * @param data
 */
JmessagePlugin.prototype.requestConverListCallback = function(count, data) {
  try {
    if(this.converListCallback){
      this.converListCallback(count, data);
    }
  } catch(exception) {
    console.log("JmessagePlugin:converListCallback: " + exception);
  }
}

/**
 * iOS回调函数
 * @param data
 */
JmessagePlugin.prototype.receiveMessageIniOSCallback = function(statue) {
  try {
    if(this.receiveIOSCallback){
      this.receiveIOSCallback(statue);
    }
  } catch(exception) {
    console.log("JmessagePlugin:receiveIOSCallback: " + exception);
  }
}
/**
 * iOS通知回调函数
 * @param data
 */
JmessagePlugin.prototype.receiveMessageIniOSNotifyCallback = function(type, data) {
  showLog("----------- receiveMessageIniOSNotifyCallback ----------- ");
  try {
    if(this.receiveIOSConverListNotifyCallback){
      this.receiveIOSConverListNotifyCallback(type, data);
    }
  } catch(exception) {
    console.log("JmessagePlugin:receiveIOSConverListNotifyCallback: " + exception);
  }
}

/**
 * iOS会话列表通知消息回调函数
 * @param data
 */
JmessagePlugin.prototype.receiveMsgInIOSConverListNotifyCallback = function(type, data) {
  showLog("----------- receiveIOSConverListNotifyCallback ----------- ");
  try {
    if(this.receiveIOSConverListNotifyCallback){
      this.receiveIOSConverListNotifyCallback(type, data);
    }
  } catch(exception) {
    console.log("JmessagePlugin:receiveIOSConverListNotifyCallback: " + exception);
  }
}


/**
 * iOS会话详情消息列表页面通知消息回调函数
 * @param data
 */
JmessagePlugin.prototype.receiveMsgInIOSConverDetailNotifyCallback = function(type, data) {
  showLog("----------- receiveIOSConverDetailNotifyCallback ----------- ");
  try {
    if(this.receiveIOSConverDetailNotifyCallback){
      this.receiveIOSConverDetailNotifyCallback(type, data);
    }
  } catch(exception) {
    console.log("JmessagePlugin:receiveIOSConverDetailNotifyCallback: " + exception);
  }
}

JmessagePlugin.prototype.receiveMessageInAndroidCallback = function(data) {
  try {
    showLog("jmessage_:receiveMessageInAndroidCallback");
    this.receiveMessage = data;
    cordova.fireDocumentEvent('jmessage.receiveMessage', null);
  } catch(exception) {
    console.log("JPushPlugin:pushCallback " + exception);
  }
}

/**
 * 用于在 Android 6.0 及以上系统，申请一些权限
 */
JmessagePlugin.prototype.requestPermission = function() {
  if(device.platform == "Android") {
    this.call_native("requestPermission", [], null);
  }
}

if(!window.plugins) {
  window.plugins = {};
}

if(!window.plugins.jmessagePlugin) {
  window.plugins.jmessagePlugin = new JmessagePlugin();
}

module.exports = new JmessagePlugin();
