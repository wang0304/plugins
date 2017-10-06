//
//  JMessageJSONUtils.m
//  JMessageDemo
//
//  Created by BaiMei on 16/8/29.
//
//

#import "JMessageJSONUtils.h"
#import <Foundation/Foundation.h>
#import <JMessage/JMSGMessage.h>
#import <JMessage/JMSGAbstractContent.h>
#import <JMessage/JMSGTextContent.h>
#import <JMessage/JMSGImageContent.h>
#import <JMessage/JMSGVoiceContent.h>
#import <JMessage/JMSGUser.h>
#import "PrintObjectUtils.h"
#import "LogUtils.h"

@implementation JMessageJSONUtils

/**
 * 群组消息对象转JSON
 *
 */
+ (NSString*) formatJMSGGroupToJson:(JMSGGroup*)group{

    if([self isDataNilOrNull:group]){
        return @"";
    }

    NSString *memberArrayJson = [self formatJMSGUserArrayToJSONArray:group.memberArray];

    NSArray *groupJsonKeys = [[NSArray alloc] initWithObjects:
                                @"membersString",nil];
    NSDictionary *groupDic= [NSDictionary dictionaryWithObjectsAndKeys:
                               [self formatDataNilToNullString:group.gid], @"gid",
                               [self formatDataNilToNullString:group.name], @"name",
                               [self formatDataNilToNull:group.level], @"level",
                               [self formatDataNilToNull:group.flag], @"flag",
                               [self formatDataNilToNull:group.desc], @"desc",
                               [self formatDataNilToNullString:group.owner], @"owner",
                               [self formatDataNilToNullString:group.ownerAppKey], @"ownerAppKey",
                               [self formatDataNilToNullString:memberArrayJson], @"membersString",
                               [self formatDataNilToNull:group.maxMemberCount], @"maxMemberCount", nil];

    return [self UIUtilsFomateJsonWithDictionary: groupDic jsonKeys:groupJsonKeys];
}

/**
 * 群组数组转换JSON
 * @param groupArray 群组对象数组
 */
+ (NSString*)formatJMSGGroupArrayToJson:(NSArray*)groupArray{
    if(groupArray == nil || groupArray == NULL){
        return @"";
    }

    NSUInteger count = [groupArray count];
    NSMutableArray *newArray = [NSMutableArray arrayWithCapacity:count];
    for(int i=0; i<count; i++){
        JMSGGroup *group = [groupArray objectAtIndex:i];
        NSString *json = [self formatJMSGGroupToJson:group];
        [newArray addObject:[self filterJsonSpecialChar:json]];
    }

    return [self formatJsonArray2Json:newArray];
}

/**
 * 用户信息对象转JSON
 *
 */
+ (NSString*) formatJMSGUserToJson:(JMSGUser*)user{

    if([self isDataNilOrNull:user]){
        return @"";
    }

    NSArray *fileds = [[NSArray alloc] initWithObjects:
                       @"uid", @"username", @"nickname", @"birthday", @"address", @"password",
                       @"createTime", @"noteText", @"noteName", @"avatar", @"blackList", @"star",
                       @"thumbAvatarPath", @"originAvatarPath", @"noDisturb",
                       @"isNoDisturb", @"region", @"isInBlacklist", @"token", @"gender",
                       @"resourceID", @"appKey", @"signature", nil];

                    // @"thumbAvatarData",

    NSArray *userJonsKeys = [[NSArray alloc] initWithObjects:
                             @"myInfo", nil];
    NSDictionary *userDic = [PrintObjectUtils getObjectData:user objFiledArray:fileds];

    return [self UIUtilsFomateJsonWithDictionary: userDic jsonKeys:userJonsKeys];
}

/**
 * 用户数组转换JSON
 * @param userArray 用户对象数组
 */
+ (NSString*)formatJMSGUserArrayToJson:(NSArray*)userArray{
    if(userArray == nil || userArray == NULL){
        return @"";
    }

    NSUInteger count = [userArray count];

//    NSMutableArray *newArray = [NSMutableArray arrayWithCapacity:count];
//    for(int i=0; i<count; i++){
//        JMSGUser *user = [userArray objectAtIndex:i];
//        NSString *json = [self formatJMSGUserToJson:user];
//        [newArray addObject:json];
//    }
//    return [self formatJsonArray2Json:newArray];

    NSMutableArray *keys = [NSMutableArray arrayWithCapacity:count];
    NSMutableDictionary *newDict = [NSMutableDictionary dictionary];
    for(int i=0; i<count; i++){
        JMSGUser *user = [userArray objectAtIndex:i];
        NSString *json = [self formatJMSGUserToJson:user];

        [keys addObject:user.username];
        [newDict setObject:json forKey:user.username];
    }

    return [self UIUtilsFomateJsonWithDictionary:newDict jsonKeys:keys];
}


/**
 * 用户数组转换JSON
 * @param userArray 用户对象数组
 */
+ (NSString*)formatJMSGUserArrayToJSONArray:(NSArray*)userArray{
    if(userArray == nil || userArray == NULL){
        return @"";
    }

    NSUInteger count = [userArray count];

        NSMutableArray *newArray = [NSMutableArray arrayWithCapacity:count];
        for(int i=0; i<count; i++){
            JMSGUser *user = [userArray objectAtIndex:i];
            NSString *json = [self formatJMSGUserToJson:user];
            [newArray addObject:json];
        }
        return [self formatJsonArray2Json:newArray];

//    NSMutableArray *keys = [NSMutableArray arrayWithCapacity:count];
//    NSMutableDictionary *newDict = [NSMutableDictionary dictionary];
//    for(int i=0; i<count; i++){
//        JMSGUser *user = [userArray objectAtIndex:i];
//        NSString *json = [self formatJMSGUserToJson:user];
//
//        [keys addObject:user.username];
//        [newDict setObject:json forKey:user.username];
//    }
//
//    return [self UIUtilsFomateJsonWithDictionary:newDict jsonKeys:keys];
}

/**
 * 用户数组转换成对象NSDictionary
 * @param userArray 用户对象数组
 */
+ (NSDictionary*)formatJMSGUserArrayToUserDic:(NSArray*)userArray{

    if(userArray == nil || userArray == NULL || [userArray isKindOfClass:[NSNull class]]){
        return nil;
    }

    NSUInteger count = [userArray count];
//    NSMutableDictionary *dict = [NSMutableDictionary dictionaryWithCapacity:count];
    NSMutableDictionary *dict = [NSMutableDictionary dictionary];
    for(int i=0; i<count; i++){
        JMSGUser *user = [userArray objectAtIndex:i];
        if(![self isDataNilOrNull:user]){
            [dict setObject:user forKey:user.username];
        }
    }
    return dict;
}

// 会话消息对象转JSON
+ (NSString*) formatJMSGMessageToJson:(JMSGMessage *)message{
    NSString *jmessageMsg = nil;

    if([self isDataNilOrNull:message]){
        jmessageMsg = nil;
    } else {

        NSString *contentJson = nil;
        switch (message.contentType) {
            case kJMSGContentTypeUnknown: { // 未知类型消息
                if (message.content == nil) {
                    contentJson = @"";
                }
            }
                break;
            case kJMSGContentTypeText:{ // 文本消息
                contentJson = [((JMSGTextContent *)message.content) toJsonString];
            }
                break;

            case kJMSGContentTypeImage:{ // 图片消息
                contentJson = ((JMSGImageContent *)message.content).toJsonString;
            }
                break;

            case kJMSGContentTypeVoice:{ // 语音消息
                contentJson = ((JMSGVoiceContent *)message.content).toJsonString;
            }
                break;

            case kJMSGContentTypeEventNotification:{ // 事件通知消息
                JMSGEventContent *eventContent = (JMSGEventContent *)message.content;
                contentJson = eventContent.toJsonString;
            }
                break;

            default:{ // 其他消息
                contentJson = ((JMSGAbstractContent *)message.content).toJsonString;
            }
                break;
        }

        if([self isDataNilOrNullOrNullString:contentJson]){
            contentJson = @"";
        }

        //contentJson = [self filterJsonSpecialChar:((JMSGTextContent *)message.content).toJsonString];

        contentJson = [self filterJsonSpecialChar:contentJson];

        JMSGUser *fromUser = [self formatDataNilToNull:message.fromUser];
        NSString *userName = fromUser.username;
        NSArray *jmsgMsgJsonKeys = [[NSArray alloc] initWithObjects:
                                    @"messageJson", @"content",nil];
        NSDictionary *jmsgMsgDic= [NSDictionary dictionaryWithObjectsAndKeys:
                                   [self formatDataNilToNullString:message.msgId], @"msgId",
                                   [self formatDataNilToNullString:message.serverMessageId], @"serverMessageId",
                                   [self formatDataNilToNullString:userName], @"otherSide",
                                   [self formatDataNilToNull:@(message.isReceived)], @"isReceived",
                                   [self formatDataNilToNull:@(message.contentType)], @"contentType",
                                   [self formatDataNilToNull:@(message.status)], @"status",
                                   [self formatDataNilToNullString:message.toJsonString], @"messageJson",
                                   [self formatDataNilToNullString:contentJson], @"content",
                                   [self formatDataNilToNullString:message.fromAppKey], @"fromAppKey",
                                   [self formatDataNilToNull:message.targetAppKey], @"targetAppKey", nil];

        jmessageMsg = [self UIUtilsFomateJsonWithDictionary:jmsgMsgDic jsonKeys:jmsgMsgJsonKeys];
    }

    return [self formatDataNilToNull:jmessageMsg];
}

/**
 * 返回JSON字符串
 * add by baimei on 2016.09.03
 */
+ (NSString*) formatJCHATModelToJsonString:(JCHATChatModel *)chatModel{

    if([self isDataNilOrNull:chatModel]){
        return @"";
    }

    NSString *messageJson = [self formatJMSGMessageToJson:chatModel.message];

    CGSize cgSize = chatModel.contentSize;
    CGSize imageSize = chatModel.imageSize;

    NSArray *modelDicJsonKeys = [[NSArray alloc] initWithObjects:
                                @"message", @"contentSize", @"imageSize",nil];
    NSDictionary *modelDic= [NSDictionary dictionaryWithObjectsAndKeys:
                             [self formatDataNilToNullString:chatModel.timeId], @"timeId",
                             @(chatModel.isTime), @"isTime",
                             @(chatModel.isErrorMessage), @"isErrorMessage",
                             [self formatDataNilToNull:chatModel.messageError], @"messageError",
                             [self formatCGSizeToJsonString:&(cgSize)], @"contentSize",
                             [self formatCGSizeToJsonString:&(imageSize)], @"imageSize",
                             @(chatModel.contentHeight), @"contentHeight",
                             @(chatModel.photoIndex), @"photoIndex",
                             [self formatDataNilToNull:chatModel.messageTime], @"messageTime",
                             [self formatDataNilToNullString:messageJson], @"message", nil];

    NSString *modelJson = [JMessageJSONUtils UIUtilsFomateJsonWithDictionary: modelDic jsonKeys:modelDicJsonKeys];

    return modelJson;
}

/**
 * 将消息模型列表Dictionary转换成json串
 * @param modelListDic 消息模型字典
 */
+ (NSString*) formatJCHATModelListDicToJsonString:(NSDictionary*)modelListDic{
    NSString *json = nil;

    if(modelListDic){
        NSArray *keys = [modelListDic allKeys];

//        NSUInteger count = [keys count];
//        NSMutableArray *newArray = [NSMutableArray arrayWithCapacity:count];
//        for (NSString *key in modelListDic) {
//            JCHATChatModel *chatModel = modelListDic[key];
//            NSString *modelJson = [self formatJCHATModelToJsonString:chatModel];
//            [newArray addObject:modelJson];
//        }
//        json = [self formatJsonArray2Json:newArray];


        NSMutableDictionary *newDict = [NSMutableDictionary dictionary];
        for (NSString *key in modelListDic) {
            JCHATChatModel *chatModel = modelListDic[key];
            NSString *modelJson = [self formatJCHATModelToJsonString:chatModel];
            [newDict setObject:modelJson forKey:key];
        }

        json = [self UIUtilsFomateJsonWithDictionary: newDict jsonKeys:keys];
    }

    if(json == nil){
        json = @"";
    }

    return json;
}


/**
 * 将消息模型Array转换成json串
 * @param modelArray 消息模型数组
 */
+ (NSString*) formatJCHATModelArrayToJsonString:(NSArray*)modelArray{
    NSString *json = nil;

    if(modelArray){

        NSUInteger count = [modelArray count];
        NSMutableArray *newArray = [NSMutableArray arrayWithCapacity:count];

        for (int i=0; i<count; i++) {
            JCHATChatModel *chatModel = [modelArray objectAtIndex:i];
            NSString *modelJson = [self formatJCHATModelToJsonString:chatModel];
            [newArray addObject:modelJson];
        }

        json = [self formatJsonArray2Json:newArray];
    }

    if(json == nil){
        json = @"";
    }

    return json;
}


// JMSGConversionList转JSON
+ (NSString*) conversationListToJson:(NSArray *)conversationArray{
    NSString *json = nil;

    if(conversationArray){
        NSUInteger count = [conversationArray count];
        NSMutableArray *newArray = [NSMutableArray arrayWithCapacity:count];

        for (NSInteger i=0; i < count; i++) {

            JMSGConversation *conversation = [conversationArray objectAtIndex:i];

            NSString *converJson = [self conversationToJson:conversation];

            [newArray addObject:converJson];
        }

        json = [self formatJsonArray2Json:newArray];

    }

    return json;
}

+ (NSString*) conversationToJson:(JMSGConversation *)conversation{

    NSDictionary *conversationDic = [PrintObjectUtils getObjectData:conversation objFiledArray:nil];
    [LogUtils debug:[NSString stringWithFormat:@"((((((*****************))))))))) 会话信息：%@", conversationDic]];


    // 过滤部分key,如果value本身为json字符串，则拼接json时，值不加双引号
    NSArray *jsonKeys = [[NSArray alloc] initWithObjects:
                         @"latestMessage", @"targetJson",nil];
    // 消息JSON
    JMSGMessage *jmsgMsg = conversation.latestMessage;
    NSString *msgJson = @"";
    if([self isDataNilOrNull:jmsgMsg]){
        msgJson = @"\"\"";
    } else {
        msgJson = [self formatJMSGMessageToJson: jmsgMsg];
    }

    // Target信息（User/Group）
    NSString *targetId = [conversationDic objectForKey:@"targetId"];
    NSString *targetJson = @"";
    NSString *avatarPath = [conversationDic objectForKey:@"avatarPath"];
    if (conversation.conversationType == kJMSGConversationTypeSingle) {
        //targetId = ((JMSGUser *)conversation.target).username;
        //targetJson = [self formatJMSGUserToJson:((JMSGUser *)conversation.target)];
        //avatar = ((JMSGUser *)conversation.target).avatar;

        JMSGUser *user = [conversationDic objectForKey:@"target"];
        targetJson = [self formatJMSGUserToJson:user];

    } else {
        //targetId = ((JMSGGroup *)conversation.target).gid;
        //targetJson = [self formatJMSGGroupToJson:((JMSGGroup *)conversation.target)];

        JMSGGroup *group = [conversationDic objectForKey:@"target"];
        targetJson = [self formatJMSGGroupToJson:group];
    }

    if([self isDataNilOrNull:targetJson]){
        targetJson = @"\"\"";
    }

    if([self isDataNilOrNullOrNullString:avatarPath]){
        avatarPath = @"";
    } else {
       avatarPath = [self getFullConverListAvatarPath:avatarPath];
    }

    // 会话字典
    NSDictionary *dic= [NSDictionary dictionaryWithObjectsAndKeys:
                        [self formatDataNilToNull:@(conversation.conversationType)], @"conversationType",
                        [self formatDataNilToNullString:targetId], @"targetId",
                        [self formatDataNilToNullString:targetJson], @"targetJson",
                        [self formatDataNilToNullString:conversation.title], @"title",
                        [self formatDataNilToNullString:avatarPath], @"avatarPath",
                        [self formatDataNilToNullString:msgJson], @"latestMessage",
                        [self formatDataNilToNullString:conversation.targetAppKey], @"targetAppKey",
                        conversation.unreadCount, @"unreadCount", nil];
    //conversation.mess, @"message_table", // 暂时不知是什么,没有放到字典中

    NSString *dicJson = [self UIUtilsFomateJsonWithDictionary: dic jsonKeys:jsonKeys];

    return dicJson;
}

// Dic转换成JSON数据(如果是JSON数据，则两侧不加双引号)
+ (NSString *)UIUtilsFomateJsonWithDictionary:(NSDictionary *)dic jsonKeys:(NSArray*)jsonKeys{

    if(dic == nil || dic == NULL || ![dic isKindOfClass:[NSDictionary class]]){
        return @"";
    }

    NSArray *keys = [dic allKeys];
    if(![keys count]){
        return @"";
    }

    NSString *string = [NSString string];

    for (NSString *key in keys) {

        NSString *value = [self formatDataNilToNullString:[dic objectForKey:key]];

        // 过滤特殊字符
        value = [self filterJsonSpecialChar:value];

        if(![self isDataNilOrNull:jsonKeys] && [jsonKeys containsObject:key]){
            if([self isDataNilOrNullOrNullString:value]){
                //value = [NSString stringWithFormat:@"\"%@\"",value];
                value = @"\"\"";
            }

            //            else {
            //                value = [NSString stringWithFormat:@"%@",value];
            //            }
        } else {
            value = [NSString stringWithFormat:@"\"%@\"",value];
        }

        NSString *newkey = [NSString stringWithFormat:@"\"%@\"",key];


        if (!string.length) {

            string = [NSString stringWithFormat:@"%@:%@}",newkey,value];

        }else {

            string = [NSString stringWithFormat:@"%@:%@,%@",newkey,value,string];

        }
    }

    string = [NSString stringWithFormat:@"{%@",string];

    return string;
}

+ (NSData *) dictionaryToJSON:(NSDictionary*)dic{
    NSError *parseError = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    return jsonData;
}


/**
 * 将json字符串的Array转化为JSON字符串
 * @param array 子元素为普通字符串
 */
+ (NSString *)formatStringArray2Json:(NSArray *)array{

    NSUInteger count = array.count;
    if(count <= 0){
        return @"";
    }

    NSString *string = @"[";

    for (int j = 0; j<count; j++) {

        NSString *value = array[j];

        // 过滤特殊字符
        value = [self filterJsonSpecialChar:value];

            if(j == 0){
                string = [NSString stringWithFormat:@"%@\"%@\"",string, value];
            } else {
                string = [NSString stringWithFormat:@"%@,\"%@\"",string, value];
            }
    }

    string = [NSString stringWithFormat:@"%@]",string];
    return string;
}


/**
 * 将json字符串的Array转化为JSON字符串
 * @param array 子元素为json字符串
 */
+ (NSString *)formatJsonArray2Json:(NSArray *)array{
    NSUInteger count = array.count;
    if(count <= 0){
        return @"";
    }

    NSString *string = @"[";

    for (int j = 0; j<count; j++) {

            NSString *value = array[j];

            // 过滤特殊字符
            value = [self filterJsonSpecialChar:value];

            if(j == 0){
                string = [NSString stringWithFormat:@"%@%@",string,value];
            } else {
                string = [NSString stringWithFormat:@"%@,%@",string,value];
            }
    }

    string = [NSString stringWithFormat:@"%@]",string];
    return string;
}

// NSArray转换成JSON数据
+ (NSString *)UIUtilsFomateJsonArrWithArray:(NSArray *)array {

    if([self isDataNilOrNull:array]){
        return @"";
    }

    NSString *string = [NSString string];

    NSUInteger count = [array count];

    for (int j = 0; j<count; j++) {

        id indexObj = array[j];
        if([indexObj isKindOfClass:[NSDictionary class]]){

            NSDictionary *dic = indexObj;

            NSArray *keys = [dic allKeys];

            for (int i = 0; i<keys.count; i++) {

                NSString *key = keys[i];

                NSString *value = [dic objectForKey:key];

                // 过滤特殊字符
                value = [self filterJsonSpecialChar:value];

                value = [NSString stringWithFormat:@"\"%@\"",value];

                key = [NSString stringWithFormat:@"\"%@\"",key];

                if (!string.length)
                {
                    string = [NSString stringWithFormat:@"%@:%@}",key,value];
                }
                else if(i == 0)
                {
                    string = [NSString stringWithFormat:@"%@:%@}%@",key,value,string];
                }
                else
                {
                    string = [NSString stringWithFormat:@"%@:%@,%@",key,value,string];
                }
            }

            if (j != array.count-1) {
                string = [NSString stringWithFormat:@",%@",string];
            }else {
                string = [NSString stringWithFormat:@"[{%@]",string];
            }
        }

        else if([indexObj isKindOfClass:[NSNumber class]])
        {
            if (!string.length)
            {
                string = [NSString stringWithFormat:@"\"%@\"",indexObj];
            }
            else
            {
                string = [NSString stringWithFormat:@"\"%@\",%@", indexObj, string];
            }

            if (j == array.count-1) {
                string = [NSString stringWithFormat:@"[%@]",string];
            }
        }


        else if([indexObj isKindOfClass:[NSString class]]
                || [indexObj isKindOfClass:[NSArray class]])
        {
            // 过滤特殊字符
            indexObj = [self filterJsonSpecialChar:indexObj];

            if (!string.length)
            {
                string = [NSString stringWithFormat:@"%@",indexObj];
            }
            else
            {
                string = [NSString stringWithFormat:@"%@,%@", indexObj, string];
            }

            if (j == array.count-1) {
                string = [NSString stringWithFormat:@"[%@]",string];
            }
        }

        else if([indexObj isKindOfClass:[NSNull class]])
        {
            NSString *newObj = [self UIUtilsFomateJsonArrWithArray: indexObj];

            if (!string.length)
            {
                string = [NSString stringWithFormat:@"%@",newObj];
            }
            else
            {
                string = [NSString stringWithFormat:@"%@,%@", newObj, string];
            }

            if (j == array.count-1) {
                string = [NSString stringWithFormat:@"[%@]",string];
            }
        }
    }

    return string;
}

/**
 * 去掉字符串两端特殊字符
 * @param tempString 目标字符串
 * @param specialChar 过滤条件（特殊字符）
 */
+ (id)removeEndsSpecialCharacter:(NSString*)tempString special:(NSString*)specialChar{
    //NSString *defaultChar = @"[]{}（#%-*+=_）\\|~(＜＞$%^&*)_+ ";
    NSCharacterSet *doNotWant = [NSCharacterSet characterSetWithCharactersInString:specialChar];
    return [[tempString componentsSeparatedByCharactersInSet: doNotWant]componentsJoinedByString: @""];
}

/**
 * 将矩形宽高数据转换成JSON串
 * @param cgSize 矩形宽高数据变量
 */
+ (NSString*) formatCGSizeToJsonString:(CGSize *)cgSize{
    if(cgSize == nil || cgSize == NULL){
        return @"";
    }
    return [NSString stringWithFormat:@"{\"width\":\"%f\",\"height\":\"%f\"}", cgSize->width, cgSize->height];
}

/**
 * 过滤JSON字符串中的两端空格、回车、制表等特殊符号
 * @param jsonStr 目标JSON字符串
 */
+ (NSString *) filterJsonSpecialChar:(NSString*) jsonStr{

    if(jsonStr != nil && [jsonStr isKindOfClass:[NSString class]] && jsonStr.length){

        // 过滤回车转义符
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"(\r)"
                                                   withString:@"" options:NSRegularExpressionSearch
                                                        range:NSMakeRange(0, [jsonStr length])];
        // 过滤换行转义符
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"(\n)"
                                                   withString:@"" options:NSRegularExpressionSearch
                                                        range:NSMakeRange(0, [jsonStr length])];
        // 过滤后退转义符
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"(\b)"
                                                 withString:@"" options:NSRegularExpressionSearch
                                                      range:NSMakeRange(0, [jsonStr length])];
        // 过滤水平制表符
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"(\t)"
                                                 withString:@"" options:NSRegularExpressionSearch
                                                      range:NSMakeRange(0, [jsonStr length])];
        // 过滤垂直制表符
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"(\v)"
                                                 withString:@"" options:NSRegularExpressionSearch
                                                      range:NSMakeRange(0, [jsonStr length])];
        // 过滤空转义符
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"(\0)"
                                                 withString:@"" options:NSRegularExpressionSearch
                                                      range:NSMakeRange(0, [jsonStr length])];

        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"\\r" withString:@""];
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"\\n" withString:@""];
        jsonStr = [jsonStr stringByReplacingOccurrencesOfString:@"\\t" withString:@""];

        jsonStr = [jsonStr stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    }

    return jsonStr;
}

/**
 * 将nil数据转化成Null
 * @param data 目标数据
 */
+ (id) formatDataNilToNull:(id)data{
    if(data == nil){
        return [NSNull null];
    }
    return data;
}


/**
 * 将nil数据转化成@""
 * @param data 目标数据
 */
+ (id) formatDataNilToNullString:(id)data{
    if(data == nil || data == NULL
       || [data isKindOfClass:[NSNull class]]){
        return @"";
    }
    return data;
}

/**
 * 判断数据是否为空
 * @param data 目标数据
 */
+ (BOOL) isDataNilOrNull:(id)data{
    if(data == nil || data == NULL
       || [data isKindOfClass:[NSNull class]]
       || [data isEqual:[NSNull null]]){
        return true;
    }
    return false;
}

/**
 * 判断数据是否为空
 * @param data 目标数据
 */
+ (BOOL) isDataNilOrNullOrNullString:(id)data{
    if([self isDataNilOrNull:data] || [data isEqualToString:@""]){
        return true;
    }
    return false;
}


/**
 * 获取完整的头像地址
 *
 */
+ (NSString*)getFullConverListAvatarPath:(NSString*)avatarPath{
    // 测试
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    NSString *filePath = paths[0];

    JMSGUser *user = [JMSGUser myInfo];
    NSString *userPath = [NSString stringWithFormat:@"/%@_%@/", user.username, user.appKey];

    NSString *fullAvatarPath = [NSString stringWithFormat:@"%@%@%@", filePath, userPath, avatarPath];
    [LogUtils debug:[NSString stringWithFormat:@"&& ------------------> fullAvatarPath: %@", fullAvatarPath]];
    return fullAvatarPath;
}

/**
 * 将状态码和状态信息组装成JSON
 */
+ (NSString*)formatErrorToJsonString:(NSInteger)status desc:(NSString*)description{
    if(description == nil || description == NULL){
      description = @"";
    }
    NSString *errorStr = @"{\"status\":\"%d\",\"desc\":\"%@\"}";
    return [NSString stringWithFormat:errorStr, status, description];
}

/**
 * JSON字符串转NSArray或者NSDictionary
 *
 */
+ (id) formatJsonStr2ArrayOrDic:(NSString*)jsonString{
    NSData *nsData = [jsonString dataUsingEncoding:NSASCIIStringEncoding];
    return [self formatData2ArrayOrDict:nsData];
}

// 将JSON串转化为字典或者数组
+ (id)formatData2ArrayOrDict:(NSData *)jsonData{
    NSError *error = nil;
    id jsonObject = [NSJSONSerialization JSONObjectWithData:jsonData
                                                    options:NSJSONReadingAllowFragments
                                                      error:&error];
    if (jsonObject != nil && error == nil){
        return jsonObject;
    }else{
        // 解析错误
        return nil;
    }

}

@end
