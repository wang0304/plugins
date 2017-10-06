//
//  LogUtils.h
//  JMessageDemo
//
//  Created by 王兆飞 on 16/8/29.
//
//

#ifndef LogUtils_h
#define LogUtils_h


#import <Foundation/Foundation.h>

@interface LogUtils : NSObject

//直接通过NSLog输出
+ (void)log:(NSString*)msg;

//直接通过NSLog输出
+ (void)debug:(NSString*)msg;

//直接通过NSLog输出
+ (void)error:(NSString*)msg;

@end

#endif /* LogUtils_h */
