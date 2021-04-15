
'use strict';
var exec = require('cordova/exec');

module.exports = {
    getToken: function ( successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "NetrixFcmPlugin", "getToken", []);
    }
};