
//#import <UIKit/UIKit.h>

#import "DDLegacyMacros.h"
#import "JChatConstants.h"
#import <JMessage/JMessage.h>
#import "JMSGApiKey.h"

#define JMESSAGE_APPKEY [JMSGApiKey getAppKey]
#define CHANNEL [JMSGApiKey getChannel]

#define kBADGE @"badge"

/*!
 * 请求API返回结果状态
 */
typedef NS_ENUM(NSInteger, JMSGResultStatus) {
    // false
    kJMSGRequestStatusFail = 0,
    // true
    kJMSGRequestStatusSuc = 1,
};

@interface BaseJMessage : NSObject //UIResponder<UIApplicationDelegate>


/*!
 * @abstract 消息的内容类型
 */
@property(nonatomic, assign, readonly) JMSGResultStatus resultStatus;
@property (strong, nonatomic) UIWindow *window;
@property (assign, nonatomic)BOOL isDBMigrating;

// 单例模式：只被实例化一次
+ (BaseJMessage*) getInstance;

// 初始化基础配置
- (void)initBase:(UIApplication *)application
   launchOptions:(NSDictionary *)launchOptions
        delegate:(id <JMessageDelegate>)delegate
        observer:(id)observer;

- (void)onLoginUserKicked;
- (void)onDBMigrateStart;
- (void)onDBMigrateFinishedWithError:(NSError *)error;
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex;

- (void)registerJPushStatusNotification:(id)observer;
- (void)initLogger;


- (void)networkDidSetup:(NSNotification *)notification;
- (void)networkIsConnecting:(NSNotification *)notification;
- (void)networkDidClose:(NSNotification *)notification;
- (void)networkDidRegister:(NSNotification *)notification;
- (void)networkDidLogin:(NSNotification *)notification;
- (void)receivePushMessage:(NSNotification *)notification;



- (void)jpushApplicationWillResignActive:(UIApplication *)application;
- (void)jpushApplicationDidEnterBackground:(UIApplication *)application;
- (void)jpushApplicationWillEnterForeground:(UIApplication *)application;
- (void)jpushApplicationDidBecomeActive:(UIApplication *)application;
- (void)jpushApplicationWillTerminate:(UIApplication *)application;

- (void)jpushApplication:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;

- (void)jpushApplication:(UIApplication *)application
didFailToRegisterForRemoteNotificationsWithError:(NSError *)error;

- (void)jpushApplication:(UIApplication *)application
didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings;

- (void)jpushApplication:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forLocalNotification:(UILocalNotification *)notification
  completionHandler:(void (^)())completionHandler;

- (void)jpushApplication:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forRemoteNotification:(NSDictionary *)userInfo
  completionHandler:(void (^)())completionHandler;

- (void)jpushApplication:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo;

- (void)jpushApplication:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo
fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler;

- (void)jpushApplication:(UIApplication *)application
didReceiveLocalNotification:(UILocalNotification *)notification;




- (void)resetApplicationBadge;

@end
