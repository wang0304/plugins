//
//  JMSGApiKey.m
//  JPushIMDemo
//
//  Created by BaiMei on 16/8/26.
//
//

#import <Foundation/Foundation.h>
#import "JMSGApiKey.h"

static NSString *const JMSG_APP_KEY = @"APP_KEY";
static NSString *const JMSG_APP_CHANNEL = @"CHANNEL";
static NSString *const JPushConfigFileName = @"JMessageConfig";

@implementation JMSGApiKey

+(NSString*)getAppKey{

    //read appkey and channel from JMessageConfig.plist
    NSString *plistPath = [[NSBundle mainBundle] pathForResource:JPushConfigFileName ofType:@"plist"];
    if (plistPath == nil) {
        //alert('JMessage缺少配置文件!');
        NSLog(@"error: JMessageConfig.plist not found");
        assert(0);
    }

    NSMutableDictionary *plistData = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    NSString *appkey    = [plistData valueForKey:JMSG_APP_KEY];

    if (!appkey || appkey.length == 0) {
        NSLog(@"JMessage AppKey配置错误!");
        assert(0);
    }

    return appkey;
}


+(NSString*)getChannel{

    //read appkey and channel from JMessageConfig.plist
    NSString *plistPath = [[NSBundle mainBundle] pathForResource:JPushConfigFileName ofType:@"plist"];
    if (plistPath == nil) {
        NSLog(@"error: JMessageConfig.plist not found");
        //alert('JMessage缺少配置文件!');
        assert(0);
    }

    NSMutableDictionary *plistData = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    NSString *channel            = [plistData valueForKey:JMSG_APP_CHANNEL];
    if (!channel || channel.length == 0) {
        NSLog(@"JMessage channel配置错误!");
        assert(0);
    }

    return channel;
}

@end
