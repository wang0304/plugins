//
//  JCHATSendMessageViewController.h
//  JPush IM
//
//  Created by Apple on 14/12/26.
//  Copyright (c) 2014年 Apple. All rights reserved.
//

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>
//#import "JCHATToolBar.h"
//#import "JCHATMoreView.h"
//#import "JCHATRecordAnimationView.h"
#import "JCHATChatModel.h"
//#import "XHVoiceRecordHUD.h"
//#import "XHVoiceRecordHelper.h"
//#import "JCHATVoiceTableViewCell.h"
//#import "JCHATMessageTableView.h"
//#import "JCHATMessageTableViewCell.h"
//#import "JCHATPhotoPickerViewController.h"
#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
#import "JMessageConstants.h"

#define interval 60*2 //static =const
#define navigationRightButtonRect CGRectMake(0, 0, 14, 17)
#define messageTableColor [UIColor colorWithRed:236/255.0 green:237/255.0 blue:240/255.0 alpha:1]


/**
 * 定义会话详情信息回调函数指针
 * @param type 回调类别（会话消息、其他消息等等）
 * @param resultData 返回数据（会话消息JSON串等）
 */
typedef void(^NotifyConverDetailBlock)(NSString * type, NSString *resultData);


/**
 * 定义会话详情信息回调函数指针
 * @param type 回调类别（会话消息、其他消息等等）
 * @param resultData 返回数据（会话消息JSON串等）
 */
typedef void(^NotifySendMessageResponseBlock)(NSString * type, NSString *resultData);


/**
 * 定义会话详情操作回调
 * @param error 错误信息
 * @param resultData 返回数据
 */
typedef void(^NotifyCallbackBlock)(NSString *error, NSString *resultData);


static NSInteger const messagePageNumber = 25;
static NSInteger const messagefristPageNumber = 20;

@interface JCHATConversationViewController : CDVPlugin <
//UITableViewDataSource,
//UITableViewDelegate,
//SendMessageDelegate,
//AddBtnDelegate,
//UIImagePickerControllerDelegate,
//UINavigationControllerDelegate,
//PictureDelegate,
//playVoiceDelegate,
//UIGestureRecognizerDelegate,
//UIAlertViewDelegate,
JMessageDelegate
//UIScrollViewDelegate,
//JCHATPhotoPickerViewControllerDelegate,
//UITextViewDelegate
>

//@property (weak, nonatomic) IBOutlet JCHATMessageTableView *messageTableView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *toolBarHeightConstrait;
//@property (weak, nonatomic) IBOutlet JCHATToolBarContainer *toolBarContainer;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *toolBarToBottomConstrait;
//@property (weak, nonatomic) IBOutlet JCHATMoreViewContainer *moreViewContainer;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *moreViewHeight;
//@property(nonatomic, assign) JPIMInputViewType textViewInputViewType;
@property(assign, nonatomic) BOOL barBottomFlag;
//@property(nonatomic, strong, readwrite) XHVoiceRecordHUD *voiceRecordHUD;
@property(strong, nonatomic) JMSGConversation *conversation;
@property(strong, nonatomic) NSString *targetName;
@property(assign, nonatomic) BOOL isConversationChange;
@property(weak,nonatomic)id superViewController;

/**
 *  管理录音工具对象
 */
//@property(nonatomic, strong) XHVoiceRecordHelper *voiceRecordHelper;

/**
 *  记录旧的textView contentSize Heigth
 */
@property(nonatomic, assign) CGFloat previousTextViewContentHeight;

-(void)initCtrl:(NotifyConverDetailBlock)notifyBlock;

+(JCHATConversationViewController*) getInstance;

- (void)setupView;
- (void)prepareImageMessage:(UIImage *)img;

// 从服务器更新用户信息
-(void) refreshTargetInfoFromServer;

/**
 * 动态加载更多会话信息
 *
 */
- (void)flashToLoadMessage:(NSString*) start offset:(NSString*)offset callback:(NotifyCallbackBlock)notifyCallbackBlock;

/**
 * 获取请求回调数据
 *
 */
-(NSString *) getResultData;

#pragma mark --发送文本
- (void)sendText:(NSString *)text callback:(NotifySendMessageResponseBlock)callback;

// 清除未读信息
-(void) clearUnreadCount;

/**
 * 加载用户头像
 */
- (void)loadConverDetailAvatar:username callback:(NotifyCallbackBlock)callback;

@end
