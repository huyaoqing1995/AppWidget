package com.osapp.appwidget.wg.wgTextClock;

import java.io.File;

public class TextClockWidget {

    private String backgroundPath;
    private String format;
    private int font;
    private int textSize;
    private int textColor;
    private String TextClockWidgetABCXYZ;
    public static final String KEY = "TextClockWidgetABCXYZ";


    public TextClockWidget(String backgroundPath, String format, int font, int textSize, int textColor) {
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

    public String getTextClockWidgetABCXYZ() {
        return TextClockWidgetABCXYZ;
    }

    public void setTextClockWidgetABCXYZ(String textClockWidgetABCXYZ) {
        TextClockWidgetABCXYZ = textClockWidgetABCXYZ;
    }

    public static String getKEY() {
        return KEY;
    }

    public void delete(){
        new File(backgroundPath).delete();
    }


    public void fake(){
        TextClockWidgetABCXYZ = "key";
    }

}
