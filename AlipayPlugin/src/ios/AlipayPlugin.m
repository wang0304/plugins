#import "AlipayPlugin.h"
#import <AlipaySDK/AlipaySDK.h>

@implementation AlipayPlugin

static NSMutableDictionary* alipayPluginObjects;


#pragma mark - Cordova interface methods
- (void) doAlipay:(CDVInvokedUrlCommand*) command{
    
    [AlipayPlugin initDictionary];
    [AlipayPlugin setPlugin:self className:@"AlipayPlugin"];
    
    [self payOrder:command];
}


- (void) payOrder: (CDVInvokedUrlCommand*) command{
    
    CDVPluginResult* pluginResult = nil;
    //NSString* javaScript = nil;
    NSString* singOrderOParam = nil;
    
    @try {
        singOrderOParam = [command.arguments objectAtIndex:0];
        
        if (singOrderOParam != nil) {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
            //javaScript = [pluginResult toSuccessCallbackString:command.callbackId];
            
            [[AlipaySDK defaultService] payOrder:singOrderOParam fromScheme:@"atme1122334455667788" callback:^(NSDictionary *resultDic) {
                
                // 支付结果
                NSString *resultStatus = [resultDic objectForKey: @"resultStatus"];
                dispatch_async(dispatch_get_main_queue(), ^{
                    
                    // 回调JS函数
                    [self callbackJs: resultStatus];
                    
                });
                
            }];
        }
        
    } @catch (id exception) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_JSON_EXCEPTION messageAsString:[exception reason]];
        //javaScript = [pluginResult toErrorCallbackString:command.callbackId];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    //[self writeJavascript:javaScript];
    
}

/**
 * 回调JS函数
 */
- (void) callbackJs:(NSString*) resultStatus{
    // JS回调
//    [self.commandDelegate evalJs:[NSString stringWithFormat:@"navigator.alipayPlugin.receiveMessageIniOSCallback('%@')", resultStatus]];
    
    [self.commandDelegate evalJs:[NSString stringWithFormat:@"window.plugins.alipayPlugin.receiveMessageIniOSCallback('%@')", resultStatus]];
}


/**
 * 支付回调
 */
- (void) alipayForResult:(NSURL *)resultUrl{
    //跳转支付宝钱包进行支付，处理支付结果
    [[AlipaySDK defaultService] processOrderWithPaymentResult:resultUrl standbyCallback:^(NSDictionary *resultDic){
        
        // 支付结果
        NSString* resultStatus = [resultDic objectForKey: @"resultStatus"];
        dispatch_async(dispatch_get_main_queue(), ^{
            
            if(resultStatus != nil){
                
                // JS回调
                [self callbackJs: resultStatus];
                
            }
            
        });
        
    }];
}

+(void) initDictionary{
    alipayPluginObjects = [[NSMutableDictionary alloc] initWithCapacity:10];
}

+ (AlipayPlugin *) getAlipayPlugin:(NSString*) className{
    AlipayPlugin* obj = [alipayPluginObjects objectForKey:className];
    return obj;
}

+ (AlipayPlugin *) setPlugin:(AlipayPlugin *) plugin className:(NSString*) className{
    [alipayPluginObjects setObject:plugin forKey:className];
    return plugin;
}

@end
