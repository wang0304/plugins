//
//  JCHATGroupSettingCtl.h
//  JPush IM
//
//  Created by Apple on 15/3/6.
//  Copyright (c) 2015年 Apple. All rights reserved.
// TODO: 换成collectionview

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>
#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
//#import "JCHATChatTable.h"
//#import "JCHATGroupPersonView.h"
#import "JCHATConversationViewController.h"

@class JMSGConversation;

@interface JCHATGroupSettingCtl : CDVPlugin //UIViewController<UITableViewDataSource,UITableViewDelegate,TouchTableViewDelegate,GroupPersonDelegate,UITextFieldDelegate>

//@property (nonatomic,strong) JCHATChatTable *groupTab;
@property (nonatomic,strong) JMSGConversation *conversation;
@property (nonatomic,strong) JCHATConversationViewController *sendMessageCtl;
@property (nonatomic,strong) NSMutableArray *groupData;

-(void)initCtrl:(CDVInvokedUrlCommand*)command;

+(JCHATGroupSettingCtl*) getInstance;

// 调用自定义JS回调
- (void) callbackJs:(NSString*) resultStatus;
// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult;

@end
