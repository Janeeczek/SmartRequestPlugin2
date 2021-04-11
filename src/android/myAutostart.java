package com.example.cordova.plugin;

import android.content.Context;
import android.widget.Toast;
import com.example.cordova.plugin.services.MyFirebaseMessagingService;
import com.google.firebase.FirebaseApp;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class myAutostart extends CordovaPlugin {
  private static final String DURATION_LONG = "long";



  @Override
  public boolean execute(String action, JSONArray args,
    final CallbackContext callbackContext) {
      Context  applicationContext = this.cordova.getActivity().getApplicationContext();
      // Verify that the user sent a 'show' action
      if (!action.equals("show")) {
        callbackContext.error("\"" + action + "\" is not a recognized action.");
        return false;
      }

      FirebaseApp.initializeApp(applicationContext);
      Toast toast = Toast.makeText(cordova.getActivity(), "TEST", DURATION_LONG.equals(duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
      // Create the toast
      toast.show();
      message = MyFirebaseMessagingService.getMyToken();
      toast = Toast.makeText(cordova.getActivity(), message, DURATION_LONG.equals(duration) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
      // Display toast
      toast.show();


      // Send a positive result to the callbackContext
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
      callbackContext.sendPluginResult(pluginResult);
      return true;
  }
}