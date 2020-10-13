package com.osapp.appwidget.services;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.osapp.appwidget.R;
import com.osapp.appwidget.main.MainActivity;
import com.osapp.appwidget.wg.wgBattery.BatteryReceiver;

public class AppWidgetService extends Service {

    private BatteryReceiver batteryReceiver = new BatteryReceiver();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotification();
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Intent restartServiceIntent = new Intent(getApplicationContext(),
                this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(
                getApplicationContext(), 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        if (alarmService != null) {
            alarmService.set(AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime() + 1000,
                    restartServicePendingIntent);
        }

        super.onTaskRemoved(rootIntent);

    }
    private void createNotification(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Object systemService = getSystemService(NOTIFICATION_SERVICE);
        if (systemService != null) {
            NotificationManager notificationManager = (NotificationManager) systemService;
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel("10001",getString(R.string.app_name), NotificationManager.IMPORTANCE_MIN);
                notificationChannel.setSound(null, null);
                notificationChannel.setShowBadge(false);
                notificationChannel.setLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
                notificationManager.createNotificationChannel(notificationChannel);

            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"10001");
            builder.setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(false).setOngoing(true).setChannelId("10001").setPriority(2).setContentIntent(activity);
            if(Build.VERSION.SDK_INT >= 26){
                builder.setContentTitle("Keep app running!");
            }else {
                builder.setContentText("Keep app running!");
                builder.setContentTitle(getString(R.string.app_name));
            }
            startForeground(1,builder.build());
        }
    }

}
