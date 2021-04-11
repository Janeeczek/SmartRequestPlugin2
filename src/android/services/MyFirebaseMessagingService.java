package com.example.cordova.plugin.services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


import com.example.cordova.plugin.foregroundServices.ForeService;
import com.example.test.MainActivity;
import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private static  String myToken;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().containsKey("START")) {
                if(!isMyServiceRunning(ForeService.class)) {
                    //Intent intent = new Intent(getApplicationContext(), ForeService.class);
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getApplicationContext().startForegroundService(intent);
                    } else {
                        getApplicationContext().startService(intent);
                    }

                     */
                    System.out.println("SKFNASKFJASKFJSKFJKSAJFKLSAJFKLSFJKLSFJKLSFJKSLFJSKFJSKFJKFSKJFKFJk START");
                    //Toast.makeText(this, "STARTUJE ACTUVUT", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);

                }
            } else if (remoteMessage.getData().containsKey("STOP")) {
                Intent intent = new Intent(getApplicationContext(), ForeService.class);
                getApplicationContext().stopService(intent);
            } else {

                handleNow();
            }
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
        }
    }
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        myToken = token;
        sendRegistrationToServer(token);
    }
    public static String getMyToken(){
        System.out.println( "Fetching FCM registration token failedaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasdSDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA11111111111111111111111111111");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println( "Fetching FCM registration token failedaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasdSDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA2222222222222222222222222");
                            return;
                        }
                        // Get new FCM registration token
                        System.out.println( "Fetching FCM registration token failedaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaasdSDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA333333333333333333333333333333333333");
                        String token = task.getResult();
                        System.out.println(token);
                        myToken = token;
                        // Log and toast

                    }
                });

        return myToken;
    }
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String messageBody) {
        Class mainActivity = null;
        Context context = getApplicationContext();
        String  packageName = context.getPackageName();
        Intent  launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        String  className = launchIntent.getComponent().getClassName();

        try {
            //loading the Main Activity to not import it in the plugin
            mainActivity = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context, mainActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId =  getStringResource("default_notification_channel_id");//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                        .setContentTitle(getStringResource("fcm_message"))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0 , notificationBuilder.build());
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private String getStringResource(String name) {
        Context applicationContext = getApplicationContext();
        return applicationContext.getString(
                applicationContext.getResources().getIdentifier(
                        name, "string", applicationContext.getPackageName()
                )
        );
    }
}