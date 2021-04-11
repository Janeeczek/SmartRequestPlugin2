package com.example.cordova.plugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.example.cordova.plugin.foregroundServices.ForeService;


public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent arg1) {
        Class mainActivity = null;

        String  packageName = context.getPackageName();
        Intent  launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        String  className = launchIntent.getComponent().getClassName();

        try {
            //loading the Main Activity to not import it in the plugin
            mainActivity = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "SERVICE Z AUTOSTARTU", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(context, ForeService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

         */
        Intent i = new Intent(context, mainActivity);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Toast.makeText(context, "SERVICE Z AUTOSTARTU KONIEC", Toast.LENGTH_SHORT).show();
    }
}