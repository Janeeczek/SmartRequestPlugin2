
var exec = require('cordova/exec');
// Empty constructor
function autostart() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
autostart.prototype.show = function(message, duration, successCallback, errorCallback) {
  var options = {};
  options.message = message;
  options.duration = duration;
  cordova.exec(successCallback, errorCallback, 'autostart', 'show', [options]);
}

// Installation constructor that binds ToastyPlugin to window
autostart.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.autostart = new autostart();
  return window.plugins.autostart;
};
cordova.addConstructor(autostart.install);

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'autostart', 'coolMethod', [arg0]);
};

