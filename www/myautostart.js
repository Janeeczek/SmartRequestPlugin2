var exec = require('cordova/exec');
// Empty constructor
function myautostart() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
myautostart.prototype.show = function(message, duration, successCallback, errorCallback) {
  var options = {};
  options.message = message;
  options.duration = duration;
  cordova.exec(successCallback, errorCallback, 'myautostart', 'show', [options]);
}

// Installation constructor that binds ToastyPlugin to window
myautostart.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.myautostart = new myautostart();
  return window.plugins.myautostart;
};
cordova.addConstructor(myautostart.install);

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'myautostart', 'coolMethod', [arg0]);
};
