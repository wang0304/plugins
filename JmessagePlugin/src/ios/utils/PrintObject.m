//
//  PrintObject.m
//  JMessageDemo
//
//  Created by 王兆飞 on 16/8/29.
//
//

#import "PrintObject.h"
#import <Foundation/Foundation.h>
#import <objc/runtime.h>
#import "JCHATStringUtils.h"

@implementation PrintObject

+ (NSDictionary*)getObjData:(id)obj
{
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    unsigned int propsCount;
    objc_property_t *props = class_copyPropertyList([obj class], &propsCount);
    for(int i = 0;i < propsCount; i++)
    {
        objc_property_t prop = props[i];


        const char *propertyName = property_getName(prop);
        if(propertyName == nil || propertyName == NULL){
            //NSLog(@"****************** propertyName 属性为nil ******************");
            break;
        } else {
            //NSLog(@"((((((((((((((( propertyName 属性不为nil ))))))))))))))");
        }

        NSString *propName = [NSString stringWithUTF8String:propertyName]; //
        NSLog(@">>>>>>>>>>>>> propName : %@", propName);


        id value = [obj valueForKey:propName];
        NSLog(@"------------> value : %@", value);

        if([value isKindOfClass:[NSString class]] && [JCHATStringUtils isBlankString:value])
        {
            value = @"";
        }
        else if(value == nil || value == NULL)
        {
            value = [NSNull null];
        }
        else if([obj isKindOfClass:[NSArray class]]
                || [obj isKindOfClass:[NSDictionary class]]
                || [obj isKindOfClass:[NSString class]]
                || [obj isKindOfClass:[NSNumber class]]
                || [obj isKindOfClass:[NSNull class]])
        {
            value = [self getObjectInternal:value];
        }
        else;

        [dic setObject:value forKey:propName];
    }
    return dic;
}

+ (void)printMsg:(id)obj
{
    NSLog(@"%@", [self getObjData:obj]);
}


+ (NSData*)getJSONData:(id)obj options:(NSJSONWritingOptions)options error:(NSError**)error
{
    return [NSJSONSerialization dataWithJSONObject:[self getObjData:obj] options:options error:error];
}

+ (id)getObjectInternal:(id)obj
{
    if([obj isKindOfClass:[NSString class]]
       || [obj isKindOfClass:[NSNumber class]]
       || [obj isKindOfClass:[NSNull class]])
    {
        NSLog(@"@@@ -------->>>> is NSString/NSNumber/NSNull <<<<-----------");
        return obj;
    }

    if([obj isKindOfClass:[NSArray class]])
    {
        NSLog(@"@@@ -------->>>> is NSArray <<<<-----------");
        NSArray *objarr = obj;
        NSMutableArray *arr = [NSMutableArray arrayWithCapacity:objarr.count];
        for(int i = 0;i < objarr.count; i++)
        {
            [arr setObject:[self getObjectInternal:[objarr objectAtIndex:i]] atIndexedSubscript:i];
        }
        return arr;
    }

    if([obj isKindOfClass:[NSDictionary class]])
    {
        NSLog(@"@@@ -------->>>> is NSDictionary <<<<-----------");
        NSDictionary *objdic = obj;
        NSMutableDictionary *dic = [NSMutableDictionary dictionaryWithCapacity:[objdic count]];
        for(NSString *key in objdic.allKeys)
        {
            [dic setObject:[self getObjectInternal:[objdic objectForKey:key]] forKey:key];
        }
        return dic;
    }

//    if([obj isKindOfClass:[NSObject class]])
//    {
//        NSLog(@"@@@ -------->>>> is NSObject <<<<-----------");
//        return [self getObjData:obj];
//    }

    NSLog(@"@@@ -------->>>> is Other <<<<-----------");

    return obj;
}

@end
