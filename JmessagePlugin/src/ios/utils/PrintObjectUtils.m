//
// Created by BaiMei on 2016-08-15.
//
//

#import "PrintObjectUtils.h"
#import <objc/runtime.h>

@implementation PrintObjectUtils {

}


// BaiMei 2016-08-15 新增对象与字典、JSON之间的转换函数
+ (NSMutableDictionary*)getObjectData:(id)obj
{
    return [self getObjectData:obj objFiledArray:nil subClassArray:nil];
}

// 对象转Dictionary
+(NSMutableDictionary*)getObjectData:(id)obj objFiledArray:(NSArray*) fileds
{
    return [self getObjectData:obj objFiledArray:fileds subClassArray:nil];
}

/**
 * 对象转Dictionary
 * @param objFiledArray 过滤自断（控制哪些字段可以显示，nil表示全部显示）
 * @param subClassArray 需要解析的（类类型的）属性，nil表示不需要解析成JSON，直接返回字符串
 */
+ (NSMutableDictionary*)getObjectData:(id)obj objFiledArray:(NSArray*)fileds subClassArray:(NSArray*) subClassArray
{
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];

    unsigned int propsCount;

    objc_property_t *props = class_copyPropertyList([obj class], &propsCount);

    for(int i = 0; propsCount > 0 && i < propsCount; i++)
    {

        objc_property_t prop = props[i];

        NSString *propName = [NSString stringWithUTF8String:property_getName(prop)];

        id value = [obj valueForKey:propName];

        if(value == nil || value == NULL || [value isKindOfClass:[NSNull class]]
           || [value isEqual:[NSNull null]])
        {
            value = [NSNull null];
        }

        else
        {
            value = [self getObjectInternal:value subClassArray:subClassArray];
        }

        // 控制显示的字段
        if(fileds != nil && fileds != NULL && ![fileds isKindOfClass:[NSNull class]]){
            if([fileds containsObject:propName]){
                [dic setObject:value forKey:propName];
            }
        } else {
            [dic setObject:value forKey:propName];
        }

    }

    return dic;
}

/**
 * 解析值
 *
 */
+ (id)getObjectInternal:(id)obj subClassArray:(NSArray*) subClassArray
{
    if(obj == nil || obj == NULL || [obj isKindOfClass:[NSNull class]]){
        return [NSNull null];
    }

    if([obj isKindOfClass:[NSString class]]
       || [obj isKindOfClass:[NSNumber class]]
       ||[obj isKindOfClass:[NSNull class]])
    {
        return obj;
    }

    if([obj isKindOfClass:[NSArray class]] || [obj isKindOfClass:[NSMutableArray class]])
    {
        NSArray *objarr = obj;

        NSMutableArray *arr = [NSMutableArray arrayWithCapacity:objarr.count];

        for(int i = 0;i < objarr.count; i++)
        {
            [arr setObject:[self getObjectInternal:[objarr objectAtIndex:i] subClassArray: subClassArray] atIndexedSubscript:i];
        }

        return arr;
    }

    if([obj isKindOfClass:[NSDictionary class]])
    {
        NSDictionary *objdic = obj;

        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:[objdic count]];

        for(NSString *key in objdic.allKeys)
        {
            [dic setObject:[self getObjectInternal:[objdic objectForKey:key] subClassArray: subClassArray] forKey:key];
        }

        return dic;
    }

    //
    if(subClassArray != nil)
    {
        NSArray *subClassAarr = subClassArray;
        NSUInteger count = subClassAarr.count;
        for(int i=0; i < count; i++)
        {
            if([obj isKindOfClass:[[subClassAarr objectAtIndex:i] class]])
            {
                return [self getObjectData:obj];
            }
        }
    }

    return obj;
    // return [self getObjectData:obj];
}

+ (void)print:(id)obj
{
    NSLog(@"%@", [self getObjectData:obj]);
}



+ (NSData*)getJSON:(id)obj options:(NSJSONWritingOptions)options error:(NSError**)error
{
    return [NSJSONSerialization dataWithJSONObject:[self getObjectData:obj] options:options error:error];
}


+ (NSData *)toJSONData:(id)theData{

    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:theData
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];

    if ([jsonData length] > 0 && error == nil){
        return jsonData;
    }else{
        return nil;
    }
}

@end
