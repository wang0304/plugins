//
//  LogUtils.m
//  JMessageDemo
//
//  Created by 王兆飞 on 16/8/29.
//
//

#import "LogUtils.h"
#import <Foundation/Foundation.h>
#import <objc/runtime.h>

@implementation LogUtils

+ (void)log:(NSString*)msg
{
    NSLog(@"%@", msg);
}

/**
 * 测试打印Log
 */
+ (void)debug:(NSString*)msg
{
    //NSLog(@"%@", msg);
}

+ (void)error:(NSString*)msg
{
    NSLog(@"%@", msg);
}

@end
