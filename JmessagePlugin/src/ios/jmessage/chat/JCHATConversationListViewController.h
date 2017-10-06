//
//  JCHATChatViewController.h
//  JPush IM
//

//#import "JCHATCommon.h"
#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>

#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
#import <JMessage/JMSGMessage.h>
#import <JMessage/JMSGUser.h>
#import "JMessageConstants.h"
#import "JMessageJSONUtils.h"

/**
 * 定义JMessage通知信息回调函数指针
 * @param type 通知类别（会话消息、其他消息等等）
 * @param resultData 返回数据（会话消息JSON串等）
 */
typedef void(^NotifyBlock)(NSString * type, NSString *resultData);

/**
 * 定义JMessage会话列表查询结果回调函数
 * @param unreadCount 未读消息条数
 * @param resultData 返回JOSN数据
 */
typedef void(^ConverListBlock)(int unreadCount, NSString *resultData);


/**
 * 请求结果回调函数
 * @param respStatus 请求结果状态
 * @param resultData 返回数据
 */
typedef void(^RequestResultBlock)(NSInteger respStatus, id resultData);

/**
 * 跳转到会话信息列表主页时回调函数
 * 初始化会话信息列表控制器
 * @param type 会话类型（单体／群组）
 * @param conversation 返回会话对象
 */
typedef void(^ConversationViewBlock)(JMSGConversation *conversation);


@interface JCHATConversationListViewController : UIViewController<JMessageDelegate,JMSGConversationDelegate> // UIGestureRecognizerDelegate,

//UISearchBarDelegate,UISearchControllerDelegate,UISearchControllerDelegate,UISearchResultsUpdating,UISearchDisplayDelegate,UITableViewDataSource,UITableViewDelegate,

@property (nonatomic, strong) UIImageView *addBgView;

//@property (weak, nonatomic) IBOutlet JCHATChatTable *chatTableView;

- (void)initCtrl;

+ (JCHATConversationListViewController*) getInstance;

/**
 * 获取会话列表
 * @param callback 数据回调
 */
- (void)getConversationList:(ConverListBlock)callback;

/*
 * 跳转到会话消息列表控制器
 * @param targetId 会话ID
 * @param callback 回调函数
 */
- (void)skipToConversationController:(NSString *)targetId converType:(NSString*)type callback:(ConversationViewBlock)callback;

// 添加好友
- (void) addFriend:(NSString*) username callback:(RequestResultBlock) callback;

// 调用自定义JS回调
- (void) callbackJs:(NSString*) resultStatus;
- (void) callbackJs:(NSString*)type resultData:(NSString*) resultData;

/**
 *  通知信息回调
 */
- (void)setNotifyMessageCallback:(NotifyBlock)notifyBlock;


/**
 * 加载会话头像
 */
- (void)loadConverListAvatar:targetId callback:(NotifyBlock)callback;

- (NSString *)conversationIdWithConversation:(JMSGConversation *)conversation;


/**
 * 获取完整的头像地址
 *
 */
- (NSString*)getFullConverListAvatarPath:(NSString*)avatarPath;

@end
