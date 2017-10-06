//
//  JCHATSelectFriendsCtl.h
//  JPush IM
//
//  Created by Apple on 15/2/6.
//  Copyright (c) 2015年 Apple. All rights reserved.
//

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>
#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
//#import "JCHATChatTable.h"

@interface JCHATSelectFriendsCtl : CDVPlugin //UIViewController<UITableViewDataSource,UITableViewDelegate,TouchTableViewDelegate>

//@property (nonatomic,strong) JCHATChatTable *selectFriendTab;

@property (nonatomic,strong) NSMutableArray *arrayOfCharacters;

-(void)initCtrl:(CDVInvokedUrlCommand*)command;

+(JCHATSelectFriendsCtl*) getInstance;

// 调用自定义JS回调
- (void) callbackJs:(NSString*) resultStatus;
// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult;

@end
