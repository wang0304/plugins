/**
 *
 *
 */
#import "AppDelegate+JMessage.h"

#import "JCHATFileManager.h"
#import "JCHATCustomFormatter.h"
#import "JCHATStringUtils.h"

#import "JmessagePlugin.h"

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <JMessage/JMessage.h>
#import <JMessage/JMessageDelegate.h>
#import <objc/runtime.h>

#import "BaseJMessage.h"

@implementation AppDelegate (JMessage)

// 程序运行前执行
+(void)load{

    [super load];

    // 交换函数指针1：未完成启动项
    [self jmsg_method_exchangeImpl:[self class]
                     fromMethodSel:@selector(application:didFinishLaunchingWithOptions:)
                       toMethodSel:@selector(jmsgApplication:didFinishLaunchingWithOptions:)];

//    // 交换函数指针2：应用程序退出激活
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(applicationWillResignActive:)
//                       toMethodSel:@selector(jmsgApplicationWillResignActive:)];
//
//    // 交换函数指针3：应用程序未进入后台
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(applicationDidEnterBackground:)
//                       toMethodSel:@selector(jmsgApplicationDidEnterBackground:)];
//
//    // 交换函数指针4：应用程序将要回到前台
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(applicationWillEnterForeground:)
//                       toMethodSel:@selector(jmsgApplicationWillEnterForeground:)];
//
//    // 交换函数指针5：应用程序有未被激活
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(applicationDidBecomeActive:)
//                       toMethodSel:@selector(jmsgApplicationDidBecomeActive:)];
//
//    // 交换函数指针6：应用程序终止
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(applicationWillTerminate:)
//                       toMethodSel:@selector(jmsgApplicationWillTerminate:)];
//
//    // －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－JPush 调用 start
//    // 交换函数指针7：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             didRegisterForRemoteNotificationsWithDeviceToken:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             didRegisterForRemoteNotificationsWithDeviceToken:)];
//    // 交换函数指针8：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             didFailToRegisterForRemoteNotificationsWithError:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             didFailToRegisterForRemoteNotificationsWithError:)];
//
//
//    #if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_7_1
//    // 交换函数指针9：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             didRegisterUserNotificationSettings:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             didRegisterUserNotificationSettings:)];
//    // 交换函数指针10：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             handleActionWithIdentifier:
//                                             forLocalNotification:
//                                             completionHandler:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             handleActionWithIdentifier:
//                                             forLocalNotification:
//                                             completionHandler:)];
//    // 交换函数指针11：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             handleActionWithIdentifier:
//                                             forRemoteNotification:
//                                             completionHandler:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             handleActionWithIdentifier:
//                                             forRemoteNotification:
//                                             completionHandler:)];
//    #endif // end of - > __IPHONE_7_1
//
//    // 交换函数指针12：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             didReceiveRemoteNotification:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             didReceiveRemoteNotification:)];
//    // 交换函数指针13：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             didReceiveRemoteNotification:
//                                             fetchCompletionHandler:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             didReceiveRemoteNotification:
//                                             fetchCompletionHandler:)];
//    // 交换函数指针14：
//    [self jmsg_method_exchangeImpl:[self class]
//                     fromMethodSel:@selector(application:
//                                             didReceiveLocalNotification:)
//                       toMethodSel:@selector(jmsgApplication:
//                                             didReceiveLocalNotification:)];
//
//    // －－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－JPush 调用 end

}

/**
 * 交换同类、父子类、类别中函数指针
 * 定义该方法，主要为了解决类别（Category）中同名函数被覆盖问题
 */
+(void)jmsg_method_exchangeImpl:clazz fromMethodSel:(SEL)fromMethodSel toMethodSel:(SEL)toMethodSel{
    // 通过class_getInstanceMethod()函数从当前对象中的method list获取method结构体，如果是类方法就使用class_getClassMethod()函数获取。
    Method fromMethod = class_getInstanceMethod([clazz class], fromMethodSel);
    Method toMethod = class_getInstanceMethod([clazz class], toMethodSel);
    /**
     *  我们在这里使用class_addMethod()函数对Method Swizzling做了一层验证，如果clazz没有实现被交换的方法，会导致失败。
     *  而且clazz没有交换的方法实现，但是父类有这个方法，这样就会调用父类的方法，结果就不是我们想要的结果了。
     *  所以我们在这里通过class_addMethod()的验证，如果clazz实现了这个方法，class_addMethod()函数将会返回NO，我们就可以对其进行交换了。
     */
    if (!class_addMethod([clazz class], fromMethodSel, method_getImplementation(toMethod), method_getTypeEncoding(toMethod))) {

        NSLog(@"#------> class_addMethod is success");
        method_exchangeImplementations(fromMethod, toMethod);

    }
}


// *************************************************************************
// *************************************************************************
// *******************      JMessage自定义函数   start     *******************
// *************************************************************************
// *************************************************************************

// 初始化,启动应用时,调用该方法
- (void)jmsgApplication:(UIApplication *)application
didFinishLaunchingWithOptions:(NSDictionary *)launchOptions{

    NSLog(@"--------------- JMessage init --------------");

//    if([self respondsToSelector:@selector(applicationDidEnterBackground:)]){
//        NSLog(@"### 存在某个函数的实现");
//    } else {
//        NSLog(@"XXX 不存在某个函数的实现");
//    }

    [[BaseJMessage getInstance] initBase:application
                           launchOptions:launchOptions
                                delegate:self
                                observer:self];

//    AppDelegate *appDelegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
//    if([appDelegate respondsToSelector:@selector(applicationWillResignActive:)]){
//        NSLog(@"********************************* 1");
//        [self jmsgApplication:application didFinishLaunchingWithOptions:launchOptions];
//    }

    [self jmsgApplication:application didFinishLaunchingWithOptions:launchOptions];
}



// application
- (void)jmsgApplicationWillResignActive:(UIApplication *)application {

    //[[BaseJMessage getInstance] jpushApplicationWillResignActive:application];

    //[self jmsgApplicationWillResignActive:application];
}

// application
- (void)jmsgApplicationDidEnterBackground:(UIApplication *)application {

    [[BaseJMessage getInstance] jpushApplicationDidEnterBackground:application];

    //[self jmsgApplicationDidEnterBackground:application];
}

// application
- (void)jmsgApplicationWillEnterForeground:(UIApplication *)application {

    [[BaseJMessage getInstance] jpushApplicationWillEnterForeground:application];

    //[self jmsgApplicationWillEnterForeground:application];

}

// application
- (void)jmsgApplicationDidBecomeActive:(UIApplication *)application {

    [[BaseJMessage getInstance] jpushApplicationDidBecomeActive:application];

    //[self jmsgApplicationDidBecomeActive:application];

}

// application
- (void)jmsgApplicationWillTerminate:(UIApplication *)application {

    [[BaseJMessage getInstance] jpushApplicationWillTerminate:application];

    //[self jmsgApplicationWillTerminate:application];

}


// ---------------------- JPUSH
// 通常会调用 JPUSHService 方法去完成 Push 相关的功能

- (void)jmsgApplication:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {

    [[BaseJMessage getInstance] jpushApplication:application
didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];

//    [self jmsgApplication:application
//didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];

}

- (void)jmsgApplication:(UIApplication *)application
didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {

    [[BaseJMessage getInstance] jpushApplication:application
didFailToRegisterForRemoteNotificationsWithError:error];

//    [self jmsgApplication:application
//didFailToRegisterForRemoteNotificationsWithError:error];

}

#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_7_1

- (void)jmsgApplication:(UIApplication *)application
didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings {

    [[BaseJMessage getInstance] jpushApplication:application
        didRegisterUserNotificationSettings:notificationSettings];

//    [self jmsgApplication:application
//        didRegisterUserNotificationSettings:notificationSettings];
}

- (void)jmsgApplication:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forLocalNotification:(UILocalNotification *)notification
  completionHandler:(void (^)())completionHandler {

    [[BaseJMessage getInstance] jpushApplication:application
                 handleActionWithIdentifier:identifier
                       forLocalNotification:notification
                          completionHandler:completionHandler];

//    [self jmsgApplication:application
//                 handleActionWithIdentifier:identifier
//                       forLocalNotification:notification
//                          completionHandler:completionHandler];

}

- (void)jmsgApplication:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forRemoteNotification:(NSDictionary *)userInfo
  completionHandler:(void (^)())completionHandler {

    [[BaseJMessage getInstance] jpushApplication:application
                 handleActionWithIdentifier:identifier
                      forRemoteNotification:userInfo
                          completionHandler:completionHandler];

//    [self jmsgApplication:application
//                 handleActionWithIdentifier:identifier
//                      forRemoteNotification:userInfo
//                          completionHandler:completionHandler];
}

#endif // end of - > __IPHONE_7_1

- (void)jmsgApplication:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo {

    [[BaseJMessage getInstance] jpushApplication:application
               didReceiveRemoteNotification:userInfo];

//    [self jmsgApplication:application
//               didReceiveRemoteNotification:userInfo];
}

- (void)jmsgApplication:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo
fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {

    [[BaseJMessage getInstance] jpushApplication:application
               didReceiveRemoteNotification:userInfo
                     fetchCompletionHandler:completionHandler];

//    [self jmsgApplication:application
//               didReceiveRemoteNotification:userInfo
//                     fetchCompletionHandler:completionHandler];
}

- (void)jmsgApplication:(UIApplication *)application
didReceiveLocalNotification:(UILocalNotification *)notification {

    [[BaseJMessage getInstance] jpushApplication:application
                didReceiveLocalNotification:notification];

//    [self jmsgApplication:application
//                didReceiveLocalNotification:notification];
}

// ---------- end of JPUSH
// *************************************************************************
// *************************************************************************
// *********************      JMessage自定义函数   end     *******************
// *************************************************************************
// *************************************************************************






























// *************************************************************************
// *************************************************************************
// *************     JPushIM插件在AppDelegate中的配置   start    *************
// *************************************************************************
// *************************************************************************

// application
- (void)applicationWillResignActive:(UIApplication *)application {

    NSLog(@"-------->1 AppDelegate: applicationWillResignActive");

}

// application
- (void)applicationDidEnterBackground:(UIApplication *)application {

    NSLog(@"-------->2 AppDelegate: applicationDidEnterBackground");
//    [self jmsgApplicationDidEnterBackground:application];

}

// application
- (void)applicationWillEnterForeground:(UIApplication *)application {

    NSLog(@"-------->3 AppDelegate: applicationWillEnterForeground");
//    [self jmsgApplicationWillEnterForeground:application];

}

// application
- (void)applicationDidBecomeActive:(UIApplication *)application {

    NSLog(@"-------->4 AppDelegate: applicationDidBecomeActive");
//    [self jmsgApplicationDidBecomeActive:application];

}

// application
- (void)applicationWillTerminate:(UIApplication *)application {

    NSLog(@"-------->5 AppDelegate: applicationWillTerminate");
//    [self jmsgApplicationWillTerminate:application];

}


// ---------------------- JPUSH
// 通常会调用 JPUSHService 方法去完成 Push 相关的功能

- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {

    NSLog(@"-------->6 AppDelegate: didRegisterForRemoteNotificationsWithDeviceToken");
//    [self jmsgApplication:application
//didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];


}

- (void)application:(UIApplication *)application
didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {

    NSLog(@"-------->7 AppDelegate: didFailToRegisterForRemoteNotificationsWithError");
//    [self jmsgApplication:application
//didFailToRegisterForRemoteNotificationsWithError:error];

}

#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_7_1

- (void)application:(UIApplication *)application
didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings {

    NSLog(@"-------->8 AppDelegate: didRegisterUserNotificationSettings");
//    [self jmsgApplication:application
//          didRegisterUserNotificationSettings:notificationSettings];

}

- (void)application:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forLocalNotification:(UILocalNotification *)notification
  completionHandler:(void (^)())completionHandler {

    NSLog(@"-------->9 AppDelegate: forLocalNotification");
//    [self jmsgApplication:application
//                   handleActionWithIdentifier:identifier
//                         forLocalNotification:notification
//                            completionHandler:completionHandler];

}

- (void)application:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forRemoteNotification:(NSDictionary *)userInfo
  completionHandler:(void (^)())completionHandler {

    NSLog(@"-------->10 AppDelegate: forRemoteNotification");
//    [self jmsgApplication:application
//                   handleActionWithIdentifier:identifier
//                        forRemoteNotification:userInfo
//                            completionHandler:completionHandler];
}

#endif // end of - > __IPHONE_7_1

- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo {

    NSLog(@"-------->11 AppDelegate: didReceiveRemoteNotification");
//    [self jmsgApplication:application
//                 didReceiveRemoteNotification:userInfo];

}

- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo
fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {

    NSLog(@"-------->12 AppDelegate: didReceiveRemoteNotification");
//    [self jmsgApplication:application
//                 didReceiveRemoteNotification:userInfo
//                       fetchCompletionHandler:completionHandler];

}

- (void)application:(UIApplication *)application
didReceiveLocalNotification:(UILocalNotification *)notification {

    NSLog(@"-------->13 AppDelegate: didReceiveLocalNotification");
//    [self jmsgApplication:application
//                  didReceiveLocalNotification:notification];

}

// ---------- end of JPUSH
// *************************************************************************
// *************************************************************************
// *************      JPushIM插件在AppDelegate中的配置   end     *************
// *************************************************************************
// ******

@end
