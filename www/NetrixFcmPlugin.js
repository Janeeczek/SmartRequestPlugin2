
'use strict';
var exec = require('cordova/exec');
var asDisposableListener = function (eventTarget, eventName, callback, options) {
    if (options === void 0) { options = {}; }
    var once = options.once;
    var handler = function (event) { return callback(event.detail); };
    eventTarget.addEventListener(eventName, handler, { passive: true, once: once });
    return {
        dispose: function () { return eventTarget.removeEventListener(eventName, handler); },
    };
};
var bridgeNativeEvents = function (eventTarget) {
    var onError = function (error) { return console.log('NetrixFcmPlugin: Error listening to native events', error); };
    var onEvent = function (data) {
        try {
            var _a = JSON.parse(data), eventName = _a[0], eventData = _a[1];
            eventTarget.dispatchEvent(new CustomEvent(eventName, { detail: eventData }));
        }
        catch (error) {
            console.log('NetrixFcmPlugin: Error parsing native event data', error);
        }
    };
    window.cordova.exec(onEvent, onError, 'NetrixFcmPlugin', 'startJsEventBridge', []);
};


var NetrixFcmPlugin = (function () {
    function NetrixFcmPlugin() {
        var _this = this;
        this.eventTarget = document.createElement('div');
        bridgeNativeEvents(_this.eventTarget);
        var test =  function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "NetrixFcmPlugin", "ready", []);
        };
        console.log('NetrixFcmPlugin: has been created');
    }

    NetrixFcmPlugin.prototype.getInitialPushPayload = function (successCallback, errorCallback) {
        return cordova.exec(successCallback, errorCallback, "NetrixFcmPlugin", "getInitialPushPayload", []);
    };
    NetrixFcmPlugin.prototype.getToken = function (successCallback, errorCallback) {
        return cordova.exec(successCallback, errorCallback, "NetrixFcmPlugin", "getToken", []);
    };
    NetrixFcmPlugin.prototype.onNotification = function (callback, options) {
        return asDisposableListener(this.eventTarget, 'notification', callback, options);
    };

    return NetrixFcmPlugin;
}());

var NCM = new NetrixFcmPlugin();

module.exports = NCM;



