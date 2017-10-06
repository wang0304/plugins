//
//  PrintObject.h
//  JMessageDemo
//
//  Created by 王兆飞 on 16/8/29.
//
//

#ifndef PrintObject_h
#define PrintObject_h


#import <Foundation/Foundation.h>

@interface PrintObject : NSObject
//通过对象返回一个NSDictionary，键是属性名称，值是属性值。
+ (NSDictionary*)getObjData:(id)obj;

//将getObjectData方法返回的NSDictionary转化成JSON
+ (NSData*)getJSONData:(id)obj options:(NSJSONWritingOptions)options error:(NSError**)error;

//直接通过NSLog输出getObjectData方法返回的NSDictionary
+ (void)printMsg:(id)obj;

@end




#endif /* PrintObject_h */
