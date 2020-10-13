package com.osapp.appwidget.wg.wgPhoto;

import java.io.File;

public class PhotoWidget {

    private String path;
    private String uri;
    private String PhotoWidgetABCXYZ;
    public static final String KEY = "PhotoWidgetABCXYZ";

    public PhotoWidget(String path, String uri) {
        this.path = path;
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getPhotoWidgetABCXYZ() {
        return PhotoWidgetABCXYZ;
    }

    public void setPhotoWidgetABCXYZ(String photoWidgetABCXYZ) {
        PhotoWidgetABCXYZ = photoWidgetABCXYZ;
    }

    public static String getKEY() {
        return KEY;
    }

    public void delete(){
        new File(path).delete();
    }


    public void fake(){
        PhotoWidgetABCXYZ = "key";
    }

}
