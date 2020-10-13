package com.osapp.appwidget.wg.wgFlashlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.camera2.CameraManager;

public class FlashlightHelper {

    public static boolean isFlashlightOn(Context context){
        SharedPreferences preferences = context.getSharedPreferences(FlashlightHelper.class.getSimpleName(),Context.MODE_PRIVATE);
        return preferences.getBoolean("flashlight_type",false);
    }

    public static void setFlashlight(Context context,boolean isOn){
        SharedPreferences preferences = context.getSharedPreferences(FlashlightHelper.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putBoolean("flashlight_type",isOn).apply();
    }

    public static void off(Context context){
        try {
            CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            camManager.setTorchMode(camManager.getCameraIdList()[0], false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void on(Context context){
        try {
            CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            camManager.setTorchMode(camManager.getCameraIdList()[0], true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
