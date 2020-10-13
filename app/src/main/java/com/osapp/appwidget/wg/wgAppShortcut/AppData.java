package com.osapp.appwidget.wg.wgAppShortcut;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppData {

    private Map<String, Drawable> iconMap = new HashMap<>();
    private Map<String, String> nameMap = new HashMap<>();

    public ArrayList<App> getApps(Context context) {
        ArrayList<App> apps = new ArrayList<>();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packageList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo info:packageList){
            String pkg = info.activityInfo.packageName;
            App app = new App();
            app.setResolveInfo(info);
            app.setAppId(pkg);
            apps.add(app);
        }
        startLoadApp(context,apps);
        return apps;
    }

    private void startLoadApp(final Context context, final ArrayList<App> apps) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                PackageManager pm = context.getPackageManager();
                for (App app:apps){
                    String name = app.getResolveInfo().activityInfo.loadLabel(pm).toString();
                    Drawable icon = app.getResolveInfo().activityInfo.loadIcon(pm);
                    nameMap.put(app.getResolveInfo().activityInfo.packageName,name);
                    iconMap.put(app.getResolveInfo().activityInfo.packageName,icon);
                    if(app.getLoadAppCallback()!=null){
                        app.getLoadAppCallback().OnLoaded();
                    }
                    if(app.getLoadAppCallback2()!=null){
                        app.getLoadAppCallback2().OnLoaded();
                    }
                    if(app.getLoadAppCallback3()!=null){
                        app.getLoadAppCallback3().OnLoaded();
                    }
                }
            }
        }.start();
    }

    public String getName(ResolveInfo resolveInfo){
        return nameMap.get(resolveInfo.activityInfo.packageName);
    }

    public Drawable getIcon(ResolveInfo resolveInfo){
        return iconMap.get(resolveInfo.activityInfo.packageName);
    }

}
