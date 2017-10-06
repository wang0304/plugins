/**
 * JmessagePlugin.m Cordova Plugin Implementation
 * Author: BaiMei 2016-07-06 qq:2227476834
 */

#import "JmessagePlugin.h"
#import <JMessage/JMessage.h>

#import "JChatConstants.h"
#import "JCHATTimeOutManager.h"
#import "JCHATStringUtils.h"
#import "PrintObjectUtils.h"
#import "BaseJMessage.h"

#import "JCHATConversationListViewController.h"
#import "JCHATConversationViewController.h"
#import "JCHATDetailsInfoViewController.h"
#import "JCHATFriendDetailViewController.h"
#import "JCHATGroupDetailViewController.h"
#import "JMessageConstants.h"
#import "LogUtils.h"


@implementation JmessagePlugin

// 单例模式：只被GCD初始化一次
+(JmessagePlugin*) getInstance{
    static JmessagePlugin *jmessagePlugin = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        jmessagePlugin = [[self alloc] init];
    });
    return jmessagePlugin;
}

// 初始化
- (void)init:(CDVInvokedUrlCommand*)command
{
    [LogUtils log:@"--------------- init --------------"];
}


// 初始化会话列表控制器
- (void) initConverListCtrl:(CDVInvokedUrlCommand*)command{

  [LogUtils log:@"--------------- initConverListCtrl --------------"];

    JCHATConversationListViewController *controller = [JCHATConversationListViewController getInstance];
    [controller initCtrl];

    // 通知回调
    [controller setNotifyMessageCallback:^(NSString *type, NSString *resultData){
        [self notifyConverListMsgCallbackJs:type resultData:resultData];
    }];
}

// 初始化会话详情控制器
- (void)initConverViewCtrl:(CDVInvokedUrlCommand*)command{

    [LogUtils log:@"--------------- initConverViewCtrl --------------"];
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* targetId = [command.arguments objectAtIndex:0];
    NSString* conversationType = [command.arguments objectAtIndex:1];

    [[JCHATConversationListViewController getInstance] skipToConversationController:targetId converType:conversationType callback:^(JMSGConversation *conversation) {

        JCHATConversationViewController *controller = [JCHATConversationViewController getInstance];
        controller.conversation = conversation;
        [controller initCtrl:^(NSString *type, NSString *resultData){
            if([NotifyConverDetailMsgCallback isEqualToString:type]){
                [self notifyConverDetailCallbackJs:type resultData:resultData];
            }
        }];

    }];

}

// 注册
- (void)doRegister:(CDVInvokedUrlCommand*)command
{
    NSLog(@"--------------- doRegister --------------");
    if(command == nil || command.arguments == nil) {
      return;
    }

    __block CDVPluginResult* pluginResult = nil;
    NSString* username = [command.arguments objectAtIndex:0];
    NSString* password = [command.arguments objectAtIndex:1];

    if ([self checkValidUsername:username AndPassword:password]) {
        [JMSGUser registerWithUsername:username
                              password:password
                     completionHandler:^(id resultObject, NSError *error) {

                     NSLog([NSString stringWithFormat:@"register_result: resultObject: %@",resultObject], nil);
                     NSLog([NSString stringWithFormat:@"register_result: error: %@",error], nil);

                     if (error == nil) {
                        NSLog(@"error: --------- register success --------");
                         // 注册成功
                         pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"注册成功"];
                         [self resultCallback:command pluginResult:pluginResult];

                     } else {
                         NSString *alert = [JCHATStringUtils errorAlert:error];
                         DDLogError(@"%@",alert);

                         NSString *errorJson = [JMessageJSONUtils formatErrorToJsonString:error.code desc:alert];
                         [LogUtils error:[NSString stringWithFormat:@"error: --------- register error: %@", errorJson]];

                         pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorJson];
                         [self resultCallback:command pluginResult:pluginResult];
                     }
                 }];
    }
}

// TODO javen 还没有加基本的有效性校验
- (BOOL)checkValidUsername:username AndPassword:password {
    if (![JMessageJSONUtils isDataNilOrNullOrNullString:password]
        && ![JMessageJSONUtils isDataNilOrNullOrNullString:username]) {
        return YES;
    }

    NSString *alert = @"用户名或者密码不合法.";
    if ([password isEqualToString:@""]) {
        alert = @"密码不能为空";
    } else if ([username isEqualToString:@""]) {
        alert =  @"用户名不能为空";
    }

    DDLogError(@"%@",alert);

    //NSLog(@"username: %@, password: %@", username, password);

    return NO;
}

// 登录
- (void)login:(CDVInvokedUrlCommand*)command
{
    NSLog(@"--------------- login --------------");
    if(command == nil || command.arguments == nil) {
      return;
    }

    NSString* username = [command.arguments objectAtIndex:0];
    NSString* password = [command.arguments objectAtIndex:1];

    if ([self checkValidUsername:username AndPassword:password]) {
        // 请求计时
       [JMSGUser loginWithUsername:username
                          password:password
                 completionHandler:^(id resultObject, NSError *error) {

                    [LogUtils debug:[NSString stringWithFormat:@"login_result: resultObject: %@",resultObject]];
                    [LogUtils debug:[NSString stringWithFormat:@"login_result: error: %@",error]];

                     // 请求超时
                     if (error == nil) {
                         CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"登录成功"];
                         [self resultCallback:command pluginResult:pluginResult];

                         [[NSUserDefaults standardUserDefaults] setObject:username forKey:kuserName];
                         [[NSUserDefaults standardUserDefaults] setObject:username forKey:klastLoginUserName];
                     } else {

                         DDLogDebug(@"login fail error  %@",error);
                         NSString *alert = [JCHATStringUtils errorAlert:error];
                         DDLogDebug(@"login_error  %@",alert);

                         NSString *errorJson = [JMessageJSONUtils formatErrorToJsonString:error.code desc:alert];
                         [LogUtils error:[NSString stringWithFormat:@"error: --------- login error: %@", errorJson]];

                         CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:errorJson];
                         [self resultCallback:command pluginResult:pluginResult];
                     }
                 }];
    }

}

// 退出登录
- (void)exitLogin:(CDVInvokedUrlCommand*)command
{
  NSLog(@"--------------- exitLogin --------------");
  [JMSGUser logout:^(id resultObject, NSError *error) {
    if (error == nil) {
      DDLogDebug(@"Action logout success");
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"用户已退出登录"];
        [self resultCallback:command pluginResult:pluginResult];
    } else {
        NSString *alert = [JCHATStringUtils errorAlert:error];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                        messageAsString:alert];
        [self resultCallback:command pluginResult:pluginResult];
    }
  }];
}

/**
 * 获取登录用户信息
 *
 */
- (void)getMyInfo:(CDVInvokedUrlCommand*)command
{
  NSLog(@"--------------- getMyInfo --------------");
  JMSGUser *userInfo = [JMSGUser myInfo];
    [LogUtils debug:[NSString stringWithFormat:@"@--- getMyInfo : %@", userInfo]];

  if(userInfo != nil && userInfo != NULL && ![userInfo isKindOfClass:[NSNull class]]){
      // 用户已登录
      NSString *userJson = [JMessageJSONUtils formatJMSGUserToJson:userInfo];
      [LogUtils debug:[NSString stringWithFormat:@"@--- userJson : %@", userJson]];

      CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:userJson];
      [self resultCallback:command pluginResult:pluginResult];
   } else {
      // 用户未登录
      CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                              messageAsString:@"用户还未登录!"];
            [self resultCallback:command pluginResult:pluginResult];
   }
}


//
- (void)getMineInfo:(CDVInvokedUrlCommand*)command
{
  NSLog(@"### baimei ---------- getMineInfo --------------");



}

// 根据用户名称,获取用户信息
- (void)getUserInfo:(CDVInvokedUrlCommand*)command
{
}

// 设置昵称
- (void)setNickname:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* nickname = [command.arguments objectAtIndex:0];
    // 为群组名称添加编码
    if([JMessageJSONUtils isDataNilOrNullOrNullString:nickname]){
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                              messageAsString:@"昵称不能为空!"];
        [self resultCallback:command pluginResult:pluginResult];
        return;
    } else {
      nickname = [nickname stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    }

    [JMSGUser updateMyInfoWithParameter:nickname userFieldType:kJMSGUserFieldsNickname completionHandler:^(id resultObject, NSError *error) {

        if (error == nil) { // 修改成功
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"修改成功!"];
            [self resultCallback:command pluginResult:pluginResult];
        } else { // 修改失败
            NSString *alert = [JCHATStringUtils errorAlert:error];
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                              messageAsString:alert];
            [self resultCallback:command pluginResult:pluginResult];

        }
    }];
}

// 上传用户头像
- (void)updateUserAvatar:(CDVInvokedUrlCommand*)command
{
}

/**
 * 加载用户头像
 */
- (void)loadConverDetailAvatar:(CDVInvokedUrlCommand*)command
{
  if(command == nil || command.arguments == nil) {
     return;
  }

  NSString* username = [command.arguments objectAtIndex:0];
  if(username == nil || username == NULL || [username isKindOfClass:[NSNull class]]){
    return;
  }

  [[JCHATConversationViewController getInstance] loadConverDetailAvatar:username
              callback:^(NSString *type, NSString *resultData){
                  // 加载头像
                  if([LoadAvatarCallback isEqualToString:type]){
                    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resultData];
                    [self resultCallback:command pluginResult:pluginResult];
                  }
                  // 异常
                  if([NotifyErrorCallback isEqualToString:type]){
                    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                                                messageAsString:resultData];
                    [self resultCallback:command pluginResult:pluginResult];
                  }
              }];
}

/**
 * 加载会话头像
 */
- (void)loadConverListAvatar:(CDVInvokedUrlCommand*)command
{
  if(command == nil || command.arguments == nil) {
     return;
  }

  NSString* targetId = [command.arguments objectAtIndex:0];
  if(targetId == nil || targetId == NULL || [targetId isKindOfClass:[NSNull class]]){
    return;
  }

  [[JCHATConversationListViewController getInstance] loadConverListAvatar:targetId
              callback:^(NSString *type, NSString *resultData){
                  // 加载头像
                  if([LoadAvatarCallback isEqualToString:type]){
                    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resultData];
                    [self resultCallback:command pluginResult:pluginResult];
                  }
                  // 异常
                  if([NotifyErrorCallback isEqualToString:type]){
                    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                                                messageAsString:resultData];
                    [self resultCallback:command pluginResult:pluginResult];
                  }
              }];
}


// 添加好友
- (void)addFriend:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* username = [command.arguments objectAtIndex:0];
    if([JMessageJSONUtils isDataNilOrNullOrNullString:username]){
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                                  messageAsString:@"好友用户名不能为空!"];
        [self resultCallback:command pluginResult:pluginResult];
        return;
    }

    JCHATConversationListViewController *controller = [JCHATConversationListViewController getInstance];
    [controller addFriend:username callback:^(NSInteger respStatus, id resultData){

        if(respStatus == kJMSGRequestStatusSuc){

            JMSGConversation *conversation = resultData;
            NSString *converJson = [JMessageJSONUtils conversationToJson:conversation];

            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:converJson];
            [self resultCallback:command pluginResult:pluginResult];
        } else {
            NSError *error = resultData;
            NSString *alert = [JCHATStringUtils errorAlert:error];
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                              messageAsString:alert];
            [self resultCallback:command pluginResult:pluginResult];
        }
    }];
}
//
- (void)isExistConv:(CDVInvokedUrlCommand*)command
{

}

// 获取会话列表
- (void)getConversationList:(CDVInvokedUrlCommand*)command
{
    JCHATConversationListViewController *controller = [JCHATConversationListViewController getInstance];
    [controller getConversationList:^(int unreadCount, NSString *converJson){
        [LogUtils debug:[NSString stringWithFormat:@"¥¥¥¥¥¥¥¥¥ converList: %d - %@", unreadCount, converJson]];
        [self requestConverListCallbackJs:unreadCount resultData:converJson];
    }];
}

//
- (void)createSingleConversation:(CDVInvokedUrlCommand*)command
{
}
//
- (void)getSingleConversation:(CDVInvokedUrlCommand*)command
{
}




// 获取群组信息
- (void)getGroupInfo:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        //
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller getGroupInfo:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                JMSGGroup *groupInfo = (JMSGGroup*)resultData;
                NSString *groupJson = [JMessageJSONUtils formatJMSGGroupToJson:groupInfo];
                [LogUtils debug:[NSString stringWithFormat:@"@ -----> 获取到群组信息groupJson： %@", groupJson]];
                [self CommandStatusOk:command message:groupJson];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}

// 根据用户名获取用户信息
- (void)getGroupMemberByUserName:(CDVInvokedUrlCommand*)command
{
}

// 创建群聊
- (void)createGroup:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupName = [command.arguments objectAtIndex:0];
    NSString* groupDesc = [command.arguments objectAtIndex:1];
        //
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        [controller createGroup:nil callback:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                JMSGConversation *conversation = (JMSGConversation*)resultData;
                NSString *conversationJson = [JMessageJSONUtils conversationToJson:conversation];
                [LogUtils debug:[NSString stringWithFormat:@" ### 创建群组conversationJson: %@", conversationJson]];
                [self CommandStatusOk:command message:conversationJson];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

}

// 群聊插入好友
- (void)addMemberToGroup:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];
    NSString* username = [command.arguments objectAtIndex:1];

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        //
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller addMemberWithUsername:username callback:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                [self CommandStatusOk:command message:resultData];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}

// 群聊批量插入好友
- (void)addMembersToGroup:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];
    NSString* userNameArrayJson = [command.arguments objectAtIndex:1];
    NSArray* userNameArray = [JMessageJSONUtils formatJsonStr2ArrayOrDic:userNameArrayJson];

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        //
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller addMemberWithUsernameArray:userNameArray callback:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                [self CommandStatusOk:command message:resultData];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}

// 移除群聊中好友
- (void)removeMemberFromGroup:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];
    NSString* username = [command.arguments objectAtIndex:1];

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        //
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller deleteMemberWithUserName:username callback:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                [self CommandStatusOk:command message:resultData];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}

// 批量移除群聊好友
- (void)removeMembersFromGroup:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];
    NSString* userNameArrayJson = [command.arguments objectAtIndex:1];
    NSArray* userNameArray = [JMessageJSONUtils formatJsonStr2ArrayOrDic:userNameArrayJson];

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        //
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller deleteMemberWithUserNameArray:userNameArray callback:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                [self CommandStatusOk:command message:resultData];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}
// 更新群聊名称
- (void)updateGroupName:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];
    NSString* groupName = [command.arguments objectAtIndex:1];
    // 为群组名称添加编码
    if([JMessageJSONUtils isDataNilOrNullOrNullString:groupName]){
        groupName = [groupName stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    }

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        // 删除群聊
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller updateGroupName:groupName callback:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                [self CommandStatusOk:command message:resultData];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}

// 退出群聊
- (void)exitGroup:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        // 删除群聊
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller quitGroup:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                [self CommandStatusOk:command message:resultData];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}

// 删除群聊会话信息
- (void)deleteGroupConversation:(CDVInvokedUrlCommand*)command
{
    if(command == nil || command.arguments == nil) {
        return;
    }

    NSString* groupId = [command.arguments objectAtIndex:0];

    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:@"" callback:^(JMSGConversation *conversation) {

        // 删除群聊
        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
        controller.conversation = conversation;
        [controller deleteGroupConversation:groupId callback:^(NSInteger respStatus, id resultData){
            if(respStatus == 0){ // 成功
                [self CommandStatusOk:command message:resultData];
            } else { // 失败
                NSString *error = [JMessageJSONUtils formatErrorToJsonString:-1 desc:resultData];
                [self CommandStatusError:command message:error];
            }
        }];

    }];
}

/**
 * 清空会话消息
 */
- (void)deleteAllMessages:(CDVInvokedUrlCommand*)command{

//    if(command == nil || command.arguments == nil) {
//        return;
//    }
//
//    NSString* groupId = [command.arguments objectAtIndex:0];
//    NSString* conversationType = [command.arguments objectAtIndex:1];
//
//    [[JCHATConversationListViewController getInstance] skipToConversationController:groupId converType:conversationType callback:^(JMSGConversation *conversation) {
//
//        JCHATGroupDetailViewController *controller = [JCHATGroupDetailViewController getInstance];
//        controller.conversation = conversation;
//        [controller deleteAllMessages];
//
//    }];

    [[JCHATGroupDetailViewController getInstance] deleteAllMessages];
}







//
- (void)createGroupConversation:(CDVInvokedUrlCommand*)command
{
}
//
- (void)getGroupConversation:(CDVInvokedUrlCommand*)command
{
}
//
- (void)getConvMsgList:(CDVInvokedUrlCommand*)command
{
}




/**
 * 加载更多会话消息列表
 * @param aStart 页码
 * @param aOffset 增量(每页条数)
 */
- (void)loadMoreConverDetailMsgList:(CDVInvokedUrlCommand*)command
{
  if(command == nil || command.arguments == nil) {
        return;
   }

   NSString* aStart = [command.arguments objectAtIndex:0];
   NSString* aOffset = [command.arguments objectAtIndex:1];

  // 加载更多消息
  [[JCHATConversationViewController getInstance] flashToLoadMessage:aStart
      offset:aOffset callback:^(NSString *error, NSString *resultData){

        if(![JMessageJSONUtils isDataNilOrNullOrNullString:error]){

          CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                                      messageAsString:error];
          [self resultCallback:command pluginResult:pluginResult];

        } else {

          CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resultData];
          [self resultCallback:command pluginResult:pluginResult];

        }

      }];
}

// 退出会话
- (void)exitConversation:(CDVInvokedUrlCommand*)command
{

}

/**
 * 发送文本消息
 */
- (void)sendMessage:(CDVInvokedUrlCommand*)command
{

  if(command == nil || command.arguments == nil) {
        return;
   }

   NSString* content = [command.arguments objectAtIndex:0];
    if(![JMessageJSONUtils isDataNilOrNullOrNullString:content]){
        content = [content stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]; // 编码
    }

    [LogUtils debug:[NSString stringWithFormat:@"******* >>>>> sendMsg － Content：%@", content]];

    [[JCHATConversationViewController getInstance] sendText:content callback:^(NSString *type, NSString *resultData){

        // 发送通知
        //[self notifyConverDetailCallbackJs:type resultData:resultData];

        // 发送成功
        if([SendMessageResponseCallback isEqualToString:type]){
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resultData];
            [self resultCallback:command pluginResult:pluginResult];
        }

        // 失败
        else {
            CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR
                                                              messageAsString:resultData];
            [self resultCallback:command pluginResult:pluginResult];
        }
    }];

}











//
- (void)callbackJs:(CDVInvokedUrlCommand*)command
{
}

/**
 * 回调JS函数
 * 会话列表页面消息通知回调函数（专用语ConversationListController通知回调）
 */
- (void) notifyConverListMsgCallbackJs:(NSString*) type resultData:(NSString*) resultData{
    [self.commandDelegate evalJs:[NSString stringWithFormat:@"window.plugins.jmessagePlugin.receiveMsgInIOSConverListNotifyCallback('%@','%@')", type, resultData]];
}


/**
 * 回调JS函数
 * 会话页面消息列表通知回调函数（专用语ConversationViewController通知回调）
 */
- (void)notifyConverDetailCallbackJs:(NSString*) type resultData:(NSString*) resultData{
    [self.commandDelegate evalJs:[NSString stringWithFormat:@"window.plugins.jmessagePlugin.receiveMsgInIOSConverDetailNotifyCallback('%@','%@')", type, resultData]];
}

/**
 * 回调JS函数
 */
- (void) requestConverListCallbackJs:(int) count resultData:(id) resultData{
    [self.commandDelegate evalJs:[NSString stringWithFormat:@"window.plugins.jmessagePlugin.requestConverListCallback('%d','%@')", count, resultData]];
}



// 请求成功回调
- (void)CommandStatusOk:(CDVInvokedUrlCommand*)command message:(NSString*)string {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:string];
    [self resultCallback:command pluginResult:pluginResult];
}


// 请求失败回调
- (void)CommandStatusError:(CDVInvokedUrlCommand*)command message:(NSString*)string {
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:string];
    [self resultCallback:command pluginResult:pluginResult];
}


// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult
{
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
