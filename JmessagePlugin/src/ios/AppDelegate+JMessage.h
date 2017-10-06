
#import <UIKit/UIKit.h>
#import "AppDelegate.h"

#import "DDLegacyMacros.h"
#import "JChatConstants.h"
#import <JMessage/JMessageDelegate.h>

@class CDVPlugin;
@class CDVPluginResult;
@class JmessagePlugin;

@interface AppDelegate (JMessage)<UIApplicationDelegate,JMessageDelegate>

/**
 * 交换同类、父子类、类别中函数指针
 * 定义该方法，主要为了解决类别（Category）中同名函数被覆盖问题
 */
+(void)jmsg_method_exchangeImpl:clazz fromMethodSel:(SEL)fromMethodSel toMethodSel:(SEL)toMethodSel;

// *************************************************************************
// *************************************************************************
// *************     JPushIM插件在AppDelegate中的配置   start    *************
// *************************************************************************
// *************************************************************************

// application：未完成启动项
- (void)jmsgApplication:(UIApplication *)application
didFinishLaunchingWithOptions:(NSDictionary *)launchOptions;

// application：应用程序退出激活
- (void)jmsgApplicationWillResignActive:(UIApplication *)application;

// application：应用程序未进入后台
- (void)jmsgApplicationDidEnterBackground:(UIApplication *)application;

// application：应用程序将要回到前台
- (void)jmsgApplicationWillEnterForeground:(UIApplication *)application;

// application：应用程序有未被激活
- (void)jmsgApplicationDidBecomeActive:(UIApplication *)application;

// application：应用程序终止
- (void)jmsgApplicationWillTerminate:(UIApplication *)application;


// ---------------------- JPUSH
// 通常会调用 JPUSHService 方法去完成 Push 相关的功能

- (void)jmsgApplication:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken;

- (void)jmsgApplication:(UIApplication *)application
didFailToRegisterForRemoteNotificationsWithError:(NSError *)error;

#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_7_1

- (void)jmsgApplication:(UIApplication *)application
didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings;

- (void)jmsgApplication:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forLocalNotification:(UILocalNotification *)notification
  completionHandler:(void (^)())completionHandler;

- (void)jmsgApplication:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forRemoteNotification:(NSDictionary *)userInfo
  completionHandler:(void (^)())completionHandler;

#endif // end of - > __IPHONE_7_1

- (void)jmsgApplication:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo;

- (void)jmsgApplication:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo
fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler;

- (void)jmsgApplication:(UIApplication *)application
didReceiveLocalNotification:(UILocalNotification *)notification;

// ---------- end of JPUSH

// *************************************************************************
// *************************************************************************
// *************      JPushIM插件在AppDelegate中的配置   end     *************
// *************************************************************************
// *************************************************************************

@end
