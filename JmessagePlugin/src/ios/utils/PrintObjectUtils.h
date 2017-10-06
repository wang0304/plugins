//
// Created by BaiMei on 2016-08-15.
//

#import <Foundation/Foundation.h>

@interface PrintObjectUtils : NSObject

// BaiMei 2016-08-15 新增对象与字典、JSON之间的转换函数
// 对象转Dictionary
+(NSMutableDictionary*)getObjectData:(id)obj;

// 对象转Dictionary
+(NSMutableDictionary*)getObjectData:(id)obj objFiledArray:(NSArray*) fileds;

/**
 * Object转NSDictionary(遍历对象属性，获取对象值，将属性及值放入字典中)
 * @param objFiledArray 需要显示的属性，nil表示全部显示所有属性值
 * @param subClassArray 需要解析的（类类型的）属性，nil表示不需要解析成JSON，直接返回字符串
 */
+ (NSMutableDictionary*)getObjectData:(id)obj objFiledArray:(NSArray*) fileds
                        subClassArray:(NSArray*) subClassArray;

//将getObjectData方法返回的NSDictionary转化成JSON
+(NSData*)getJSON:(id)obj options:(NSJSONWritingOptions)options error:(NSError**)error;

// 将字典NSDictionary转换成JSON
+ (NSData *)toJSONData:(id)theData;

//直接通过NSLog输出getObjectData方法返回的NSDictionary
+(void)print:(id)obj;

@end

