var exec = require('cordova/exec');

var AlipayPlugin = function(){

};

// iOS支付回调函数
AlipayPlugin.prototype.receiveIOSAlipayCallback;

// 判断是否为iOS平台
AlipayPlugin.prototype.isPlatformIOS = function() {
    var isPlatformIOS = device.platform == "iPhone"
        || device.platform == "iPad"
        || device.platform == "iPod touch"
        || device.platform == "iOS";
    return isPlatformIOS;
}

AlipayPlugin.prototype.error_callback = function(msg) {
    console.log("Javascript Callback Error: " + msg);
}

AlipayPlugin.prototype.call_native = function(name, args, callback) {
    ret = exec(callback, this.error_callback, 'AlipayPlugin', name, args);
    return ret;
}

// public methods
AlipayPlugin.prototype.init = function() {
    if(this.isPlatformIOS()) {
        var data = [];
        this.call_native("initial", data, null);
    } else {
        data = [];
        this.call_native("init", data, null);
    }
}

/**
 * 调用支付宝支付
 */
AlipayPlugin.prototype.doAlipay = function(signStr, callback) {
    try {
        var data = [signStr];
        this.receiveIOSAlipayCallback = callback;
        this.call_native("doAlipay", data, callback);
    } catch(exception) {
        console.log(exception);
    }
}

// /**
//  * 调用支付宝支付
//  */
// AlipayPlugin.prototype.doAlipay = function(signStr, scheme, callback) {
//     try {
//         var data = [signStr, scheme];
//         this.receiveIOSAlipayCallback = callback;
//         this.call_native("doAlipay", data, callback);
//     } catch(exception) {
//         console.log(exception);
//     }
// }

/**
 * iOS回调函数
 * @param data
 */
AlipayPlugin.prototype.receiveMessageIniOSCallback = function(signStr) {
    try {
        if(this.receiveIOSAlipayCallback){
            this.receiveIOSAlipayCallback(signStr);
        }
    } catch(exception) {
        console.log("AlipayPlugin:receiveMessageIniOSCallback: " + exception);
    }
}

/**
 * 用于在 Android 6.0 及以上系统，申请一些权限
 */
AlipayPlugin.prototype.requestPermission = function() {
    if(device.platform == "Android") {
        this.call_native("requestPermission", [], null);
    }
}

if(!window.plugins) {
    window.plugins = {};
}

if(!window.plugins.alipayPlugin) {
    window.plugins.alipayPlugin = new AlipayPlugin();
}

module.exports = new AlipayPlugin();
