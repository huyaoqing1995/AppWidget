package com.osapp.appwidget.wg.wgAppShortcut;

import android.content.Context;

import com.osapp.appwidget.main.Utils;

import java.io.File;

public class AppShortcutWidget {
    private String iconPath;
    private String name;
    private String appId;
    private int iconPadding;
    private int nameLine;
    private int textSize;
    private int openType;
    private String openLink;
    private String AppShortcutABCXYZ;
    public static final String KEY = "AppShortcutABCXYZ";

    public AppShortcutWidget(String iconPath, String name, String appId, int iconPadding, int nameLine, int textSize, int openType, String openLink) {
        this.iconPath = iconPath;
        this.name = name;
        this.appId = appId;
        this.iconPadding = iconPadding;
        this.nameLine = nameLine;
        this.textSize = textSize;
        this.openType = openType;
        this.openLink = openLink;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public String getOpenLink() {
        return openLink;
    }

    public void setOpenLink(String openLink) {
        this.openLink = openLink;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getNameLine() {
        return nameLine;
    }

    public void setNameLine(int nameLine) {
        this.nameLine = nameLine;
    }

    public int getIconPadding() {
        return iconPadding;
    }

    public int getIconPaddingPx(Context context) {
        return (int) Utils.convertDpToPixel(iconPadding,context);
    }

    public void setIconPadding(int iconPadding) {
        this.iconPadding = iconPadding;
    }

    public String getAppShortcutABCXYZ() {
        return AppShortcutABCXYZ;
    }

    public void setAppShortcutABCXYZ(String appShortcutABCXYZ) {
        AppShortcutABCXYZ = appShortcutABCXYZ;
    }

    public static String getKEY() {
        return KEY;
    }

    public void fake(){
        AppShortcutABCXYZ = "key";
    }

    public void delete(){
        new File(iconPath).delete();
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
