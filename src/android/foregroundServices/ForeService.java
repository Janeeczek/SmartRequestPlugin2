package com.example.cordova.plugin.foregroundServices;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;


import com.example.test.BuildConfig;
import com.example.test.R;

public class ForeService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public static final String ACTION_STARTED = ForeService.class.getName() + ".STARTED";
    public static final String ACTION_STOP = ForeService.class.getName() + ".STOP";
    public static final String ACTION_IS_STARTED = ForeService.class.getName() + ".ISSTARTED";


    public ForeService() {
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Usługa utworzona", Toast.LENGTH_SHORT).show();
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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

        Toast.makeText(this, "Usługa się uruchamia", Toast.LENGTH_SHORT).show();
        startActivity(mainActivity);
        /*
        //String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, mainActivity);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Aktywna usługa Foreground")
                .setContentText("TEXT")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent,false)
                .build();

        startForeground(1, notification);
        //Intent intents = new Intent(this, mainActivity);
        //intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intents);
        Toast.makeText(context, "Usługa uruchomiona sukcesem", Toast.LENGTH_LONG).show();
*/
        return START_STICKY;
    }
    private void startActivity(Class mainActivity) {

        //Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
            String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
            assert notificationManager != null;

            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                //mChannel.setSound(, attributes);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

            builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_dark_focused)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("TEXT")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setFullScreenIntent(openScreen(2,mainActivity), true)
                    .setAutoCancel(true)
                    .setOngoing(true);

            Notification notification = builder.build();
            notificationManager.notify(2, notification);
        } else {
            startActivity(new Intent(ForeService.this, mainActivity)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    private PendingIntent openScreen(int notificationId, Class mainActivity ) {

        Intent fullScreenIntent = new Intent(this, mainActivity);
        fullScreenIntent.putExtra("2", notificationId);
        return PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onDestroy() {
        Toast.makeText( getApplicationContext(), "Usługa zakończyła działanie", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


}