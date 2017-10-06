//
//  JMessageJSONUtils.h
//  JMessageDemo
//
//  Created by BaiMei on 16/8/29.
//
//

#import <JMessage/JMessage.h>
#import <JMessage/JMSGMessage.h>
#import <JMessage/JMSGConversation.h>
#import "JCHATChatModel.h"

#ifndef JMessageJSONUtils_h
#define JMessageJSONUtils_h

@interface JMessageJSONUtils : NSObject{

}

/**
 * 群组消息对象转JSON
 *
 */
+ (NSString*) formatJMSGGroupToJson:(JMSGGroup*)group;

/**
 * 群组数组转换JSON
 * @param groupArray 群组对象数组
 */
+ (NSString*)formatJMSGGroupArrayToJson:(NSArray*)groupArray;

/**
 * 用户信息对象转JSON
 *
 */
+ (NSString*) formatJMSGUserToJson:(JMSGUser*)user;

/**
 * 用户数组转换JSON
 * @param userArray 用户对象数组
 */
+ (NSString*)formatJMSGUserArrayToJson:(NSArray*)userArray;

/**
 * 用户数组转换JSON
 * @param userArray 用户对象数组
 */
+ (NSString*)formatJMSGUserArrayToJSONArray:(NSArray*)userArray;

/**
 * 用户数组转换成对象NSDictionary
 * @param userArray 用户对象数组
 */
+ (NSDictionary*)formatJMSGUserArrayToUserDic:(NSArray*)userArray;

// JMSGMessage转JSON
+ (NSString*) formatJMSGMessageToJson:(JMSGMessage *)message;

/**
 * 返回JSON字符串
 * add by baimei on 2016.09.03
 */
+ (NSString*) formatJCHATModelToJsonString:(JCHATChatModel*)model;

/**
 * 将消息模型列表转换成json串
 * @param modelListDic 消息模型字典
 */
+ (NSString*) formatJCHATModelListDicToJsonString:(NSDictionary*)modelListDic;

/**
 * 将消息模型Array转换成json串
 * @param modelListDic 消息模型字典
 */
+ (NSString*) formatJCHATModelArrayToJsonString:(NSArray*)modelArray;

// JMSGConversionList转JSON
+ (NSString*) conversationListToJson:(NSMutableArray *)conversationArray;

// JMSGConversion转JSON
+ (NSString*) conversationToJson:(JMSGConversation *)conversation;

+ (NSData *) dictionaryToJSON:(NSDictionary*)dic;

// Dic转换成JSON数据(如果是JSON数据，则两侧不加双引号)
+ (NSString *)UIUtilsFomateJsonWithDictionary:(NSDictionary *)dic jsonKeys:(NSArray*) jsonKeys;

/**
 * 将json字符串的Array转化为JSON字符串
 * @param array 子元素为普通字符串
 */
+ (NSString *)formatStringArray2Json:(NSArray *)array;

/**
 * 将json字符串的Array转化为JSON字符串
 * @param array 子元素为json字符串
 */
+ (NSString *)formatJsonArray2Json:(NSArray *)array;

// NSArray转换成JSON数据
+ (NSString *)UIUtilsFomateJsonArrWithArray:(NSArray *)array;

// 去掉字符串两端特殊字符
+ (id)removeEndsSpecialCharacter:(NSString*)tempString special:(NSString*)specialChar;

/**
 * 将矩形宽高数据转换成JSON串
 * @param cgSize 矩形宽高数据变量
 */
+ (NSString*) formatCGSizeToJsonString:(CGSize*)cgSize;

/**
 * 过滤JSON字符串中的两端空格和回车符号
 * @param jsonStr 目标JSON字符串
 */
+ (NSString *) filterJsonSpecialChar:(NSString*) jsonStr;

/**
 * 将nil数据转化成Null
 * @param data 目标数据
 */
+ (id) formatDataNilToNull:(id)data;

/**
 * 将nil数据转化成@""
 * @param data 目标数据
 */
+ (id) formatDataNilToNullString:(id)data;
/**
 * 判断数据是否为空
 * @param data 目标数据
 */
+ (BOOL) isDataNilOrNull:(id)data;
/**
 * 判断数据是否为空
 * @param data 目标数据
 */
+ (BOOL) isDataNilOrNullOrNullString:(id)data;

/**
 * 获取完整的头像地址
 *
 */
+ (NSString*)getFullConverListAvatarPath:(NSString*)avatarPath;

/**
 * 将状态码和状态信息组装成JSON
 */
+ (NSString*)formatErrorToJsonString:(NSInteger)status desc:(NSString*)description;

/**
 * JSON字符串转NSArray或者NSDictionary
 *
 */
+ (id) formatJsonStr2ArrayOrDic:(NSString*)jsonString;

// 将JSON串转化为字典或者数组
+ (id)formatData2ArrayOrDict:(NSData *)jsonData;


@end

#endif /* JMessageJSONUtils_h */
