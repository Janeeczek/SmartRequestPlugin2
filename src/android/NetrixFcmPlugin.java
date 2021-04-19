package com.example.cordova.plugin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class NetrixFcmPlugin extends CordovaPlugin {
    //private CallbackContext callbackContext = null;
    public static CordovaWebView gWebView;
    public static final int REQUEST_SYSTEM_ALERT_WINDOW = 1;
    public static final int MIUI_REQUEST_SYSTEM_ALERT_WINDOW = 2;
    public static String notificationEventName = "notification";
    public static Map<String, Object> initialPushPayload;

    private static NetrixFcmPlugin instance;
    protected Context context;
    protected static CallbackContext jsEventBridgeCallbackContext;

    private static final String TAG = "[^^AUTOSTART_PLUGIN^^]";

    public NetrixFcmPlugin() {}
    public NetrixFcmPlugin(Context context) {
        this.context = context;
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        gWebView = webView;
        System.out.println(TAG + "==> FCMPlugin initialize");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("PLUGIN", "DZIALA PAYLOAD");
        setInitialPushPayload(data);
    }

    public static synchronized NetrixFcmPlugin getInstance(Context context) {
        if (instance == null) {
            instance = new NetrixFcmPlugin(context);
            instance = getPlugin(instance);
        }

        return instance;
    }

    public static synchronized NetrixFcmPlugin getInstance() {
        if (instance == null) {
            instance = new NetrixFcmPlugin();
            instance = getPlugin(instance);
        }

        return instance;
    }
    public static NetrixFcmPlugin getPlugin(NetrixFcmPlugin plugin) {
        if (plugin.webView != null) {
            instance = (NetrixFcmPlugin) plugin.webView.getPluginManager().getPlugin(NetrixFcmPlugin.class.getName());
        } else {
            plugin.initialize(null, null);
            instance = plugin;
        }

        return instance;
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
                   // NetrixFcmPlugin.this.callbackContext = callbackContext;
                    requestPermission();
                    requestPermissionMiui();
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
            else if (action.equals("startJsEventBridge")) {
                this.jsEventBridgeCallbackContext = callbackContext;
            }
            else if (action.equals("getInitialPushPayload")) {
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        getInitialPushPayload(callbackContext);
                    }
                });
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
    protected void requestPermissionMiui() {
        Class<?> c = null;
        try {
            c = Class.forName("android.os.SystemProperties");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method get = null;
        try {
            get = c.getMethod("get", String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        String miui = null;
        try {
            miui = (String) get.invoke(c, "ro.miui.ui.version.name");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (miui != null && miui.contains("11")) {
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", cordova.getActivity().getPackageName());

            cordova.startActivityForResult((CordovaPlugin) this,intent,MIUI_REQUEST_SYSTEM_ALERT_WINDOW);
        }

    }
    protected boolean hasPermission() {

        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else {
            return Settings.canDrawOverlays(cordova.getActivity()) ? true : false;
        }
    }
    public static void setInitialPushPayload(Map<String, Object> payload) {
        if(initialPushPayload == null) {
            initialPushPayload = payload;
        }
    }
    private static void dispatchJSEvent(String eventName, String stringifiedJSONValue) throws Exception {
        String jsEventData = "[\"" + eventName + "\"," + stringifiedJSONValue + "]";
        PluginResult dataResult = new PluginResult(PluginResult.Status.OK, jsEventData);
        dataResult.setKeepCallback(true);
        if(NetrixFcmPlugin.jsEventBridgeCallbackContext == null) {
            Log.d(TAG, "\tUnable to send event due to unreachable bridge context");
            return;
        }
        NetrixFcmPlugin.jsEventBridgeCallbackContext.sendPluginResult(dataResult);
        Log.d(TAG, "\tSent event: " + eventName + " with " + stringifiedJSONValue);
    }

    public void getInitialPushPayload(CallbackContext callback) {
        if(initialPushPayload == null) {
            Log.d(TAG, "getInitialPushPayload: nullme");
            callback.success((String) null);
            return;
        }
        Log.d(TAG, "getInitialPushPayload");
        try {
            JSONObject jo = new JSONObject();
            for (String key : initialPushPayload.keySet()) {
                jo.put(key, initialPushPayload.get(key));
                Log.d(TAG, "\tinitialPushPayload: " + key + " => " + initialPushPayload.get(key));
            }
            callback.success(jo);
        } catch(Exception error) {
            try {
                callback.error(exceptionToJson(error));
            }
            catch (JSONException jsonErr) {
                Log.e(TAG, "Error when parsing json", jsonErr);
            }
        }
    }
    private JSONObject exceptionToJson(final Exception exception) throws JSONException {
        return new JSONObject() {
            {
                put("message", exception.getMessage());
                put("cause", exception.getClass().getName());
                put("stacktrace", exception.getStackTrace().toString());
            }
        };
    }

    public static void sendPushPayload(Map<String, Object> payload) {
        Log.d(TAG, "==> FCMPlugin sendPushPayload");
        try {
            JSONObject jo = new JSONObject();
            for (String key : payload.keySet()) {
                jo.put(key, payload.get(key));
                Log.d(TAG, "\tpayload: " + key + " => " + payload.get(key));
            }
            NetrixFcmPlugin.dispatchJSEvent(notificationEventName, jo.toString());
        } catch (Exception e) {
            Log.d(TAG, "\tERROR sendPushPayload: " + e.getMessage());
        }
    }
}
