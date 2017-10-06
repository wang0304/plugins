//
//  JCHATFriendDetailViewController.h
//  极光IM
//
//  Created by Apple on 15/4/7.
//  Copyright (c) 2015年 Apple. All rights reserved.
//

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>
#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
//#import "JCHATFrIendDetailMessgeCell.h"
#import "JCHATChatModel.h"

@interface JCHATFriendDetailViewController : CDVPlugin // UIViewController<UITableViewDataSource,UITableViewDelegate,SkipToSendMessageDelegate>

//@property (weak, nonatomic) IBOutlet UITableView *detailTableView;
@property (strong, nonatomic) JMSGUser *userInfo;
@property (assign, nonatomic) BOOL isGroupFlag;

-(void)initCtrl:(CDVInvokedUrlCommand*)command;

+(JCHATFriendDetailViewController*) getInstance;

- (void)switchDisturb;
- (void)switchBlack;

// 调用自定义JS回调
- (void) callbackJs:(NSString*) resultStatus;
// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult;

@end
