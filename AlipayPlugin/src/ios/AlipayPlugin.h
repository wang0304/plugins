/**
 * 定义变量和函数
 */

#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <UIKit/UIKit.h>

@interface AlipayPlugin : CDVPlugin {
    // Member variables go here.
}

// 定义对象方法(支付宝支付函数)
- (void)doAlipay: (CDVInvokedUrlCommand*)command;

- (void) payOrder:(CDVInvokedUrlCommand*) command;

- (void) callbackJs:(NSString*) resultStatus;

- (void) alipayForResult:(NSURL *)resultUrl;

+ (void) initDictionary;

+ (AlipayPlugin *) getAlipayPlugin:(NSString*) className;

+ (AlipayPlugin *) setPlugin:(AlipayPlugin *) plugin className:(NSString*) className;

@end