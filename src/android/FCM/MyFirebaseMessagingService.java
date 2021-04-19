package com.example.cordova.plugin.FCM;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.cordova.plugin.NetrixFcmPlugin;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.HashMap;
import java.util.Map;
/*
TUTAJ NALEŻY DODAC MainActivity.class
np. import com.example.test.MainActivity;
 */


public class MyFirebaseMessagingService  extends FirebaseMessagingService {
    private static final String TAG = " [^^^MyFirebaseMsgService^^^]";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, Object> data = new HashMap<String, Object>();


        System.out.println(TAG+  " From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            if (remoteMessage.getData().containsKey("START")) {
                    System.out.println(TAG+" ODEBRANO KOMENDE START ");
                    //Toast.makeText(this, "STARTUJE ACTUVUT", Toast.LENGTH_SHORT).show();
                    data.put("rozkaz", "START");
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    NetrixFcmPlugin.sendPushPayload(data);

            } else if (remoteMessage.getData().containsKey("STOP")) {
                //do rozwiniecia opcja komendy STOP
            } else {

                handleNow();
            }
        }
        if (remoteMessage.getNotification() != null) {
            System.out.println(TAG+ " Message Notification Body: " + remoteMessage.getNotification().getBody());
            //sendNotification(remoteMessage.getNotification().getBody());
        }
    }
    @Override
    public void onNewToken(String token) {
        System.out.println(TAG+ " Refreshed token: " + token);
        sendRegistrationToServer(token);
    }
    private void handleNow() {
        System.out.println(TAG+ " Short lived task is done.");
    }


    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(String messageBody) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId =  getStringResource("default_notification_channel_id");//getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(getApplicationInfo().icon)
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
    private String getStringResource(String name) {
        Context applicationContext = getApplicationContext();
        return applicationContext.getString(
                applicationContext.getResources().getIdentifier(
                        name, "string", applicationContext.getPackageName()
                )
        );
    }
}