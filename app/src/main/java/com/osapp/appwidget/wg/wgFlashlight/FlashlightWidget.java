package com.osapp.appwidget.wg.wgFlashlight;

public class FlashlightWidget {

    private float alpha;
    private String FlashlightWidgetABCXYZ;
    public static final String KEY = "FlashlightWidgetABCXYZ";

    public FlashlightWidget() {

    }

    public float getAlpha() {
        return alpha;
    }

    public String getFlashlightWidgetABCXYZ() {
        return FlashlightWidgetABCXYZ;
    }

    public void setFlashlightWidgetABCXYZ(String flashlightWidgetABCXYZ) {
        FlashlightWidgetABCXYZ = flashlightWidgetABCXYZ;
    }

    public static String getKEY() {
        return KEY;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }


    public void fake(){
        FlashlightWidgetABCXYZ = "key";
    }

}
