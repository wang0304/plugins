/**
 * 定义变量和函数
 * Author: BaiMei 2016-07-06 qq:2227476834
 */

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>


#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>

@interface JmessagePlugin : CDVPlugin {
    // Member variables go here.
}

// 单例模式：只被实例化一次
+(JmessagePlugin*) getInstance;

// 初始化
- (void)init:(CDVInvokedUrlCommand*)command;

// 初始化会话列表
- (void)initConverListCtrl:(CDVInvokedUrlCommand*)command;
// 初始化会话详细信息列表
- (void)initConverViewCtrl:(CDVInvokedUrlCommand*)command;

// 注册
- (void)doRegister:(CDVInvokedUrlCommand*)command;
// 登录
- (void)login:(CDVInvokedUrlCommand*)command;
// 退出登录
- (void)exitLogin:(CDVInvokedUrlCommand*)command;
// 获取我的信息(登录信息)
- (void)getMyInfo:(CDVInvokedUrlCommand*)command;
// 获取我的信息
- (void)getMineInfo:(CDVInvokedUrlCommand*)command;
// 根据用户名称,获取用户信息
- (void)getUserInfo:(CDVInvokedUrlCommand*)command;
// 设置昵称
- (void)setNickname:(CDVInvokedUrlCommand*)command;
// 更新头像
- (void)updateUserAvatar:(CDVInvokedUrlCommand*)command;

/**
 * 加载用户头像
 */
- (void)loadConverDetailAvatar:(CDVInvokedUrlCommand*)command;

/**
 * 加载会话头像
 */
- (void)loadConverListAvatar:(CDVInvokedUrlCommand*)command;


// 添加好友
- (void)addFriend:(CDVInvokedUrlCommand*)command;
// 判断好友是否已经存在
- (void)isExistConv:(CDVInvokedUrlCommand*)command;
// 获取会话列表
- (void)getConversationList:(CDVInvokedUrlCommand*)command;
// 创建单人会话对象
- (void)createSingleConversation:(CDVInvokedUrlCommand*)command;
// 获取单人会话对象
- (void)getSingleConversation:(CDVInvokedUrlCommand*)command;
// 获取群组信息
- (void)getGroupInfo:(CDVInvokedUrlCommand*)command;





// 根据用户名获取用户信息
- (void)getGroupMemberByUserName:(CDVInvokedUrlCommand*)command;
// 创建群聊
- (void)createGroup:(CDVInvokedUrlCommand*)command;
// 群聊插入好友
- (void)addMemberToGroup:(CDVInvokedUrlCommand*)command;
// 群聊批量插入好友
- (void)addMembersToGroup:(CDVInvokedUrlCommand*)command;
// 移除群聊中好友
- (void)removeMemberFromGroup:(CDVInvokedUrlCommand*)command;
// 批量移除群聊好友
- (void)removeMembersFromGroup:(CDVInvokedUrlCommand*)command;
// 更新群聊名称
- (void)updateGroupName:(CDVInvokedUrlCommand*)command;
// 退出群聊
- (void)exitGroup:(CDVInvokedUrlCommand*)command;
// 删除群聊会话信息
- (void)deleteGroupConversation:(CDVInvokedUrlCommand*)command;
/**
 * 清空消息
 */
- (void)deleteAllMessages:(CDVInvokedUrlCommand*)command;






// 创建群组会话
- (void)createGroupConversation:(CDVInvokedUrlCommand*)command;
// 获取群组会话
- (void)getGroupConversation:(CDVInvokedUrlCommand*)command;
// 获取会话消息列表
- (void)getConvMsgList:(CDVInvokedUrlCommand*)command;




/**
 * 加载更多会话消息列表
 * @param aStart 页码
 * @param aOffset 增量(每页条数)
 */
- (void)loadMoreConverDetailMsgList:(CDVInvokedUrlCommand*)command;
// 推出会话
- (void)exitConversation:(CDVInvokedUrlCommand*)command;
// 发送会话消息
- (void)sendMessage:(CDVInvokedUrlCommand*)command;





// 调用自定义JS回调
- (void)callbackJs:(CDVInvokedUrlCommand*)command;
//- (void)callbackJs:(NSString*) resultStatus;
/**
 * 回调JS函数
 * 会话详情消息列表通知回调函数（专用语ConversationViewController通知回调）
 */
- (void)notifyConverDetailCallbackJs:(NSString*) type resultData:(NSString*) resultData;
/**
 * 回调JS函数
 * 会话列表页面消息通知回调函数（专用语ConversationListController通知回调）
 */
- (void)notifyConverListMsgCallbackJs:(NSString*) type resultData:(NSString*) resultData;

// 请求会话列表JS回调接口
- (void)requestConverListCallbackJs:(int) count resultData:(id) resultData;

// 请求成功回调
- (void)CommandStatusOk:(CDVInvokedUrlCommand*)command message:(NSString*)string;
// 请求失败回调
- (void)CommandStatusError:(CDVInvokedUrlCommand*)command message:(NSString*)string;
// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult;

@end
