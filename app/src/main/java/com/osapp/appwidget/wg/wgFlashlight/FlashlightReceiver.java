package com.osapp.appwidget.wg.wgFlashlight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.osapp.appwidget.main.AppWidget;

public class FlashlightReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            return;
        }

        Log.e("hoang3",appWidgetId+"");

        if(FlashlightHelper.isFlashlightOn(context)){
            FlashlightHelper.off(context);
            FlashlightHelper.setFlashlight(context,false);
        }else {
            FlashlightHelper.on(context);
            FlashlightHelper.setFlashlight(context,true);
        }

        AppWidget.updateAppWidget(context, AppWidgetManager.getInstance(context),appWidgetId);
    }
}
