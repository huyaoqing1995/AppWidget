package com.osapp.appwidget.wg.wgAnalogClock;

import java.io.File;

public class AnalogClockWidget {

    private String backgroundPath;
    private int type;
    private String AnalogClockWidgetABCXYZ;
    public static final String KEY = "AnalogClockWidgetABCXYZ";

    public AnalogClockWidget(String backgroundPath, int type) {
        this.backgroundPath = backgroundPath;
        this.type = type;
    }

    public void delete(){
        new File(backgroundPath).delete();
    }


    public static String getKEY() {
        return KEY;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public int getType() {
        return type;
    }

    public String getAnalogClockWidgetABCXYZ() {
        return AnalogClockWidgetABCXYZ;
    }

    public void setAnalogClockWidgetABCXYZ(String analogClockWidgetABCXYZ) {
        AnalogClockWidgetABCXYZ = analogClockWidgetABCXYZ;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void fake(){
        AnalogClockWidgetABCXYZ = "key";
    }

}
