package com.osapp.appwidget.wg.wgBattery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BatteryReceiver extends BroadcastReceiver {

    public void start(Context context){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        context.registerReceiver(this,intentFilter);
    }

    public void stop(Context context){
        try {
            context.unregisterReceiver(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
