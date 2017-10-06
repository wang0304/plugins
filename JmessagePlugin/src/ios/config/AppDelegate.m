/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

//
//  AppDelegate.m
//  JPushIMDemo
//
//  Created by ___FULLUSERNAME___ on ___DATE___.
//  Copyright ___ORGANIZATIONNAME___ ___YEAR___. All rights reserved.
//

#import "AppDelegate.h"
#import "AppDelegate+JMessage.h"
#import "MainViewController.h"

#import "JmessagePlugin.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions
{
    self.viewController = [[MainViewController alloc] init];

    NSLog(@">>>>>>>>>>>>1.0 AppDelegate: 初始化JMessage插件");
    // 初始化JMessage插件
//    [[JmessagePlugin getInstance] initApplication:application
//                    didFinishLaunchingWithOptions:launchOptions
//                                         delegate:self
//                                         observer:self];

    [self jmsgApplication:application didFinishLaunchingWithOptions:launchOptions];

    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}



// *************************************************************************
// *************************************************************************
// *************     JPushIM插件在AppDelegate中的配置   start    *************
// *************************************************************************
// *************************************************************************

// application
- (void)applicationWillResignActive:(UIApplication *)application {

    NSLog(@">>>>>>>>>>>>1 AppDelegate: applicationWillResignActive");
    //[self jmsgApplicationWillResignActive:application];

}

// application
- (void)applicationDidEnterBackground:(UIApplication *)application {

    NSLog(@">>>>>>>>>>>>2 AppDelegate: applicationDidEnterBackground");
    [self jmsgApplicationDidEnterBackground:application];

}

// application
- (void)applicationWillEnterForeground:(UIApplication *)application {

    NSLog(@">>>>>>>>>>>>3 AppDelegate: applicationWillEnterForeground");
    [self jmsgApplicationWillEnterForeground:application];

}

// application
- (void)applicationDidBecomeActive:(UIApplication *)application {

    NSLog(@">>>>>>>>>>>>4 AppDelegate: applicationDidBecomeActive");
    [self jmsgApplicationDidBecomeActive:application];

}

// application
- (void)applicationWillTerminate:(UIApplication *)application {

    NSLog(@">>>>>>>>>>>>5 AppDelegate: applicationWillTerminate");
    [self jmsgApplicationWillTerminate:application];

}


// ---------------------- JPUSH
// 通常会调用 JPUSHService 方法去完成 Push 相关的功能

- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {

    NSLog(@">>>>>>>>>>>>6 AppDelegate: didRegisterForRemoteNotificationsWithDeviceToken");
    [self jmsgApplication:application
didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];


}

- (void)application:(UIApplication *)application
didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {

    NSLog(@">>>>>>>>>>>>7 AppDelegate: didFailToRegisterForRemoteNotificationsWithError");
    [self jmsgApplication:application
didFailToRegisterForRemoteNotificationsWithError:error];

}

#if __IPHONE_OS_VERSION_MAX_ALLOWED > __IPHONE_7_1

- (void)application:(UIApplication *)application
didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings {

    NSLog(@">>>>>>>>>>>>8 AppDelegate: didRegisterUserNotificationSettings");
    [self jmsgApplication:application
          didRegisterUserNotificationSettings:notificationSettings];

}

- (void)application:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forLocalNotification:(UILocalNotification *)notification
  completionHandler:(void (^)())completionHandler {

    NSLog(@">>>>>>>>>>>>9 AppDelegate: forLocalNotification");
    [self jmsgApplication:application
                   handleActionWithIdentifier:identifier
                         forLocalNotification:notification
                            completionHandler:completionHandler];

}

- (void)application:(UIApplication *)application
handleActionWithIdentifier:(NSString *)identifier
forRemoteNotification:(NSDictionary *)userInfo
  completionHandler:(void (^)())completionHandler {

    NSLog(@">>>>>>>>>>>>10 AppDelegate: forRemoteNotification");
    [self jmsgApplication:application
                   handleActionWithIdentifier:identifier
                        forRemoteNotification:userInfo
                            completionHandler:completionHandler];
}

#endif // end of - > __IPHONE_7_1

- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo {

    NSLog(@">>>>>>>>>>>>11 AppDelegate: didReceiveRemoteNotification");
    [self jmsgApplication:application
                 didReceiveRemoteNotification:userInfo];

}

- (void)application:(UIApplication *)application
didReceiveRemoteNotification:(NSDictionary *)userInfo
fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {

    NSLog(@">>>>>>>>>>>>12 AppDelegate: didReceiveRemoteNotification");
    [self jmsgApplication:application
                 didReceiveRemoteNotification:userInfo
                       fetchCompletionHandler:completionHandler];

}

- (void)application:(UIApplication *)application
didReceiveLocalNotification:(UILocalNotification *)notification {

    NSLog(@">>>>>>>>>>>>13 AppDelegate: didReceiveLocalNotification");
    [self jmsgApplication:application
                  didReceiveLocalNotification:notification];

}

// ---------- end of JPUSH
// *************************************************************************
// *************************************************************************
// *************      JPushIM插件在AppDelegate中的配置   end     *************
// *************************************************************************
// *************************************************************************


@end
