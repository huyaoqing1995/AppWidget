package com.osapp.appwidget.wg.wgDate;

import java.io.File;

public class DateWidget {

    private String backgroundPath;
    private String format;
    private int font;
    private int textSize;
    private int textColor;
    private String DateWidgetABCXYZ;
    public static final String KEY = "DateWidgetABCXYZ";


    public DateWidget(String backgroundPath, String format, int font, int textSize, int textColor) {
        this.backgroundPath = backgroundPath;
        this.format = format;
        this.font = font;
        this.textSize = textSize;
        this.textColor = textColor;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public String getDateWidgetABCXYZ() {
        return DateWidgetABCXYZ;
    }

    public void setDateWidgetABCXYZ(String textClockWidgetABCXYZ) {
        DateWidgetABCXYZ = textClockWidgetABCXYZ;
    }

    public static String getKEY() {
        return KEY;
    }

    public void delete(){
        new File(backgroundPath).delete();
    }


    public void fake(){
        DateWidgetABCXYZ = "key";
    }

}
