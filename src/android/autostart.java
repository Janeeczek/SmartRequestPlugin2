package com.example.cordova.plugin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
import com.example.cordova.plugin.FCM.MyFirebaseMessagingService;
import com.google.firebase.FirebaseApp;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class autostart extends CordovaPlugin {
    private CallbackContext callbackContext = null;
    public static final int REQUEST_SYSTEM_ALERT_WINDOW = 1;
  public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 0;
  public static final String DRAW_OVER_OTHER_APPS = Manifest.permission.SYSTEM_ALERT_WINDOW;

  @Override
  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
      Context  applicationContext = this.cordova.getActivity().getApplicationContext();
      if(hasPermission())
      {
          Toast toast = Toast.makeText(applicationContext, "MAM PERMISJE",  Toast.LENGTH_LONG);
          toast.show();
      }
      else
      {
          cordova.getThreadPool().execute(new Runnable() {
              public void run() {
                  autostart.this.callbackContext = callbackContext;
                  requestPermission();
              }
          });
      }

      // enable Cordova apps to be started in the background
      /*Bundle extras = getIntent().getExtras();
      if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
          moveTaskToBack(true);
      }

       */
      // Verify that the user sent a 'show' action
      if (!action.equals("show")) {
        callbackContext.error("\"" + action + "\" is not a recognized action.");
        return false;
      }

      FirebaseApp.initializeApp(applicationContext);
      Toast toast = Toast.makeText(applicationContext, "TEST",  Toast.LENGTH_LONG);
      // Create the toast
      toast.show();
      String message = MyFirebaseMessagingService.getMyToken();
      toast = Toast.makeText(applicationContext, message, Toast.LENGTH_LONG);
      // Display toast
      toast.show();


      // Send a positive result to the callbackContext
      PluginResult pluginResult = new PluginResult(PluginResult.Status.OK);
      callbackContext.sendPluginResult(pluginResult);
      return true;
  }
    protected void requestPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + cordova.getActivity().getPackageName()));
            cordova.startActivityForResult((CordovaPlugin) this, intent, REQUEST_SYSTEM_ALERT_WINDOW);
        }
    }
    protected boolean hasPermission() {

        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            return Settings.canDrawOverlays(cordova.getActivity()) ? true : false;
        }
    }



}
