package com.example.cordova.plugin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;


public class NetrixFcmPlugin extends CordovaPlugin {
    private CallbackContext callbackContext = null;
    public static final int REQUEST_SYSTEM_ALERT_WINDOW = 1;



    private static final String TAG = "[^^^AUTOSTART_PLUGIN^^^]";
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        System.out.println(TAG + "==> FCMPlugin initialize");
    }
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
        Context applicationContext = this.cordova.getActivity().getApplicationContext();
        FirebaseApp.initializeApp(applicationContext);
        if(hasPermission())
        {
            Toast toast = Toast.makeText(applicationContext, "Jest pozwolenie na uruchamianie activity z tla",  Toast.LENGTH_LONG);
            toast.show();
        }
        else
        {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    NetrixFcmPlugin.this.callbackContext = callbackContext;
                    requestPermission();
                }
            });
        }
        //GLOWNA LOGIKA KOMEND
        try{
            // READY // //zwraca info ze jest gotowy plugin
            if (action.equals("ready")) {
                //
                callbackContext.success();
            }
            // GET TOKEN //
            else if (action.equals("getToken")) {
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            System.out.println(TAG+ " START ODBIERANIA TOKENU");
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                System.out.println(TAG+" NIE UDALO SIE POBRAC TOKENU");
                                                return;
                                            }
                                            // Get new FCM registration token
                                            System.out.println(TAG+ " UDALO SIE POBRAC TOKEN");
                                            String token = task.getResult();
                                            System.out.println(token);
                                            callbackContext.success(token);
                                        }
                                    });
                        } catch (Exception e) {
                            System.out.println(TAG+ "\tError retrieving token"+ e);

                        }
                    }
                });
                return true;

            }else{
                callbackContext.error("Method not found");
                return false;
            }
        }catch(Exception e){
            System.out.println(TAG+ " ERROR: onPluginAction: " + e.getMessage());
            callbackContext.error(e.getMessage());
            return false;
        }

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
