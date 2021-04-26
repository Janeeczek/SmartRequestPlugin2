package com.example.cordova.plugin;


import android.content.Context;

import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;


public class SmartRequestPlugin extends CordovaPlugin {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url;
    public static CordovaWebView gWebView;
    protected Context context;

    private static final String TAG = "[^^SmartRequestPlugin^^]";

    public SmartRequestPlugin() {}
    public SmartRequestPlugin(Context context) {
        this.context = context;
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        gWebView = webView;
        System.out.println(TAG + "==> SmartRequestPlugin initialize");
        mRequestQueue = Volley.newRequestQueue(cordova.getContext());
    }

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext)throws JSONException {
        //GLOWNA LOGIKA KOMEND
        try{
            // READY // //zwraca info ze jest gotowy plugin
            if (action.equals("ready")) {
                callbackContext.success();
            } else if( action.equals("getRequest")) {
                url = args.getString(0);
                if(url.isEmpty() || url.length() < 4) {
                    callbackContext.error(TAG + " Błąd! NIE PODANO URL");
                    return false;
                }

                cordova.getThreadPool().execute(new Runnable() {
                    public void run() {

                        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                System.out.println(TAG + "Odebrano response " + response.toString());
                                Toast.makeText(cordova.getContext(),"Response :" + response.toString(), Toast.LENGTH_LONG).show();//display the response on screen

                                callbackContext.success(response.toString());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println(TAG + "Błąd requesta " + error.toString());
                                Toast.makeText(cordova.getContext(),"Response :" + error.toString(), Toast.LENGTH_LONG).show();
                                callbackContext.error(error.toString());
                            }
                        });
                        mRequestQueue.add(mStringRequest);
                    }
                });
            }
            else{
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
}
