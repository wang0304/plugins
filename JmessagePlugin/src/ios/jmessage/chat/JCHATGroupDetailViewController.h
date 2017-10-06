//
//  JCHATGroupDetailViewController.h
//  JChat
//
//  Created by HuminiOS on 15/11/23.
//  Copyright © 2015年 HXHG. All rights reserved.
//

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>
#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
#import "JCHATConversationViewController.h"


/**
 * 请求结果回调函数
 * @param respStatus 请求结果状态
 * @param resultData 返回数据
 */
typedef void(^RequestResultBlock)(NSInteger respStatus, id resultData);

typedef NS_ENUM(NSInteger, AlertViewTag) {
//清除聊天记录
  kAlertViewTagClearChatRecord = 100,
//修改群名
  kAlertViewTagRenameGroup = 200,
//添加成员
  kAlertViewTagAddMember = 300,
//退出群组
  kAlertViewTagQuitGroup = 400
};

@interface JCHATGroupDetailViewController : CDVPlugin // UIViewController

@property (nonatomic,weak) JMSGConversation *conversation;
@property (nonatomic,weak) JCHATConversationViewController *sendMessageCtl;
@property (nonatomic,strong) NSMutableArray *memberArr;

-(void)initCtrl:(CDVInvokedUrlCommand*)command;

+(JCHATGroupDetailViewController*) getInstance;

- (void)switchDisturb;

// 获取用户信息
- (void)getGroupInfo:(RequestResultBlock)callback;

// 清空所有消息（BaiMei add）
-(void)deleteAllMessages;

// 添加群成员
-(void)addMemberWithUsername:(NSString*)username callback:(RequestResultBlock)callback;
// 添加群成员
-(void)addMemberWithUsernameArray:(NSArray*)userNameArray callback:(RequestResultBlock)callback;

// 退出群组
-(void)quitGroup:(RequestResultBlock)callback;

/**
 * 删除群组会话
 * @param groupId 群组ID
 */
- (void)deleteGroupConversation:(NSString*)groupId callback:(RequestResultBlock)callback;

// 更新群组名称
-(void)updateGroupName:(NSString*)newGroupName callback:(RequestResultBlock)callback;

// 加好友进群组(新建两人会话)
-(void)addMemberToGroup:(NSString*)username callback:(RequestResultBlock)callback;
/**
 * 创建群组
 * @param
 */
-(void)createGroup:(NSString*)username callback:(RequestResultBlock)callback;

// 根据索引获取好友信息
-(void)getMemberByIndex:(unsigned int)index callback:(RequestResultBlock)callback;

// 根据索引删除群组成员
-(void)deleteMemberByIndex:(unsigned int)index callback:(RequestResultBlock)callback;
// 根据用户名称删除群组成员
-(void)delMemberWithUsername:(NSString*)username callback:(RequestResultBlock)callback;
-(void)deleteMemberWithUserName:(NSString *)userName callback:(RequestResultBlock)callback;
/**
 * 根据用户名数组删除好友
 * @param userNameArray 用户账号数组
 */
- (void)deleteMemberWithUserNameArray:(NSArray *)userNameArray callback:(RequestResultBlock)callback;




// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult;

@end
