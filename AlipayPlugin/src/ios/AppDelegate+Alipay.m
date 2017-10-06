//
//  APAppDelegate+Alipay.m
//
//  Created by baimei 2016/05/31
//  Copyright (c) 2013 atme.com. All rights reserved.
//

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>

#import "AppDelegate+Alipay.h"
#import "AlipayPlugin.h"

#import <AlipaySDK/AlipaySDK.h>

@implementation AppDelegate (Alipay)

//- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
//{
//    // Override point for customization after application launch.
//    return YES;
//}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
    
    //NSLog(@"******************* 支付钱包回调 ******************** 1");
    
    if ([url.host isEqualToString:@"safepay"]) {
        
        //跳转支付宝钱包进行支付，处理支付结果
        AlipayPlugin* apPlugin = [AlipayPlugin getAlipayPlugin:@"AlipayPlugin"];
        //NSLog(@"^~^ ............... 插件字典：%@", apPlugin);
        
        SEL plugSelector = @selector(callbackJs:);
        
        if([apPlugin respondsToSelector:plugSelector])
        {
            [apPlugin alipayForResult:url];
        }
    }
    return YES;
}

// NOTE: 9.0以后使用新API接口
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<NSString*, id> *)options
{
    //NSLog(@"************** 支付钱包回调 ************** 2");
    
    if ([url.host isEqualToString:@"safepay"]) {
        
        //跳转支付宝钱包进行支付，处理支付结果
        AlipayPlugin* apPlugin = [AlipayPlugin getAlipayPlugin:@"AlipayPlugin"];
        //NSLog(@"^~^ ............... 插件字典：%@", apPlugin);
        
        SEL plugSelector = @selector(callbackJs:);
        
        if([apPlugin respondsToSelector:plugSelector])
        {
            [apPlugin alipayForResult:url];
        }
        
    }
    return YES;
}

@end
