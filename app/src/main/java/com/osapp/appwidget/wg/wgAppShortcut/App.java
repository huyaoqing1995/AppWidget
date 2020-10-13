package com.osapp.appwidget.wg.wgAppShortcut;

import android.content.pm.ResolveInfo;

public class App {

    private String appId;
    private ResolveInfo resolveInfo;
    private LoadAppCallback loadAppCallback;
    private LoadAppCallback loadAppCallback2;
    private LoadAppCallback loadAppCallback3;

    public LoadAppCallback getLoadAppCallback() {
        return loadAppCallback;
    }

    public void setLoadAppCallback(LoadAppCallback loadAppCallback) {
        this.loadAppCallback = loadAppCallback;
    }

    public LoadAppCallback getLoadAppCallback2() {
        return loadAppCallback2;
    }

    public LoadAppCallback getLoadAppCallback3() {
        return loadAppCallback3;
    }

    public void setLoadAppCallback3(LoadAppCallback loadAppCallback3) {
        this.loadAppCallback3 = loadAppCallback3;
    }

    public void setLoadAppCallback2(LoadAppCallback loadAppCallback2) {
        this.loadAppCallback2 = loadAppCallback2;
    }

    public ResolveInfo getResolveInfo() {
        return resolveInfo;
    }

    public void setResolveInfo(ResolveInfo resolveInfo) {
        this.resolveInfo = resolveInfo;
    }

    public App() {

    }

    public App(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
