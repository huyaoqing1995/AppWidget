package com.osapp.appwidget.wg.wgContact;

import java.io.File;

public class ContactWidget {
    private String contactPhotoPath;
    private String backgroundPath;
    private int font;
    private String name;
    private String phone;
    private int color;

    private String  ContactWidgetABCXYZ;
    public static final String KEY = "ContactWidgetABCXYZ";

    public ContactWidget(String contactPhotoPath, String backgroundPath, int font, String name, String phone, int color) {
        this.contactPhotoPath = contactPhotoPath;
        this.backgroundPath = backgroundPath;
        this.font = font;
        this.name = name;
        this.phone = phone;
        this.color = color;
    }


    public String getContactPhotoPath() {
        return contactPhotoPath;
    }

    public void setContactPhotoPath(String contactPhotoPath) {
        this.contactPhotoPath = contactPhotoPath;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public int getFont() {
        return font;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getContactWidgetABCXYZ() {
        return ContactWidgetABCXYZ;
    }

    public void setContactWidgetABCXYZ(String contactWidgetABCXYZ) {
        ContactWidgetABCXYZ = contactWidgetABCXYZ;
    }

    public static String getKEY() {
        return KEY;
    }

    public void delete(){
        new File(contactPhotoPath).delete();
        new File(backgroundPath).delete();
    }


    public void fake(){
        ContactWidgetABCXYZ = "key";
    }

}
