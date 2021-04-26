
'use strict';
var exec = require('cordova/exec');



var SmartRequestPlugin = (function () {
    function SmartRequestPlugin() {
        var _this = this;
        

        console.log('SmartRequestPlugin: has been created');
    }
    SmartRequestPlugin.prototype.ready = function ( successCallback, errorCallback) {
            return cordova.exec(successCallback, errorCallback, "SmartRequestPlugin", "ready");
    };
    SmartRequestPlugin.prototype.getRequest = function (url, successCallback, errorCallback) {
                return cordova.exec(successCallback, errorCallback, "SmartRequestPlugin", "getRequest", [url]);
        };
   
    

    return SmartRequestPlugin;
}());

var NRQ = new SmartRequestPlugin();

module.exports = NRQ;



