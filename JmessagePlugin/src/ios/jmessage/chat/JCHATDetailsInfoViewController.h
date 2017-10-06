//
//  JCHATDetailsInfoViewController.h
//
//
//  Created by Apple on 15/1/21.
//
//

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>
#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
//#import <UIKit/UIViewController.h>
//#import "JCHATDetailTableViewCell.h"
#import "JCHATChatModel.h"
#import "JCHATConversationViewController.h"

typedef NS_ENUM(NSInteger, AlertViewTagInDetailVC) {
  //清除聊天记录
  kAlertViewTagClearSingleChatRecord = 100,
  //创建群聊
  kAlertViewTagCreateGroup = 200,
};

@interface JCHATDetailsInfoViewController : CDVPlugin //UIViewController<UIAlertViewDelegate>

@property (nonatomic,strong) JMSGConversation *conversation;
@property (nonatomic,strong) JCHATConversationViewController *sendMessageCtl;

// 初始化
-(void)initCtrl:(CDVInvokedUrlCommand*)command;

+(JCHATDetailsInfoViewController*) getInstance;

// 获取用户头像数据（BaiMei add）
-(void)thumbAvatarData;

// 清除所有消息记录（BaiMei add）
-(void)deleteAllMessage;

// 添加好友到群组（BaiMei add）
-(void)addFriendsToGrouop:(NSString*)friendName;

// 调用自定义JS回调
- (void) callbackJs:(NSString*) resultStatus;
// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult;

@end
