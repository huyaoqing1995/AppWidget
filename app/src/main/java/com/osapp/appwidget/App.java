package com.osapp.appwidget;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import com.osapp.appwidget.services.AppWidgetService;

public class App extends Application {

    private static App app;

    public static App get() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, AppWidgetService.class));
        }else {
            startService(new Intent(this, AppWidgetService.class));
        }
    }
}
