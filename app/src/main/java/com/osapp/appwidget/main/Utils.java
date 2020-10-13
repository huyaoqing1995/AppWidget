package com.osapp.appwidget.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.osapp.appwidget.R;

import java.io.File;
import java.io.FileOutputStream;

public class Utils {

    public static Bitmap getBitmapFromPath(String path) {
        File file = new File(path+"");
        if(file.exists()){
            return BitmapFactory.decodeFile(path);
        }else {
            return Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_8888);
        }
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static String savePath(Context context,Bitmap bitmap) {
        File file = new File(context.getFilesDir(),System.currentTimeMillis()+".png");
        Log.e("hoang","savePath "+file.getAbsolutePath());
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getFontNames(){
        return new String[]{"Default","Aga","Dancing","Fabrizio","Garamond","Latype","Nunito", "Pamega",
                "Playlist","Quick Sand","Rukola","Script","Soupof","Wallows"};
    }

    public static int[] getFonts(){
        return new int[]{0,R.font.aga,R.font.dancing,R.font.fabrizio,R.font.garamond,R.font.latype,R.font.nunito,R.font.pamega
                ,R.font.playlist,R.font.quicksand,R.font.rukola,R.font.script,R.font.soupof,R.font.wallows};
    }

    public static int[] getIdTextClock(){
        return new int[]{R.id.tv_clock_0,R.id.tv_clock_1,R.id.tv_clock_2,R.id.tv_clock_3,R.id.tv_clock_4,R.id.tv_clock_5,R.id.tv_clock_6,R.id.tv_clock_7
                ,R.id.tv_clock_8,R.id.tv_clock_9,R.id.tv_clock_10,R.id.tv_clock_11,R.id.tv_clock_12,R.id.tv_clock_13};
    }

    public static int[] getIdContactName(){
        return new int[]{R.id.tv_name_0,R.id.tv_name_1,R.id.tv_name_2,R.id.tv_name_3,R.id.tv_name_4,R.id.tv_name_5,R.id.tv_name_6,R.id.tv_name_7
                ,R.id.tv_name_8,R.id.tv_name_9,R.id.tv_name_10,R.id.tv_name_11,R.id.tv_name_12,R.id.tv_name_13};
    }

    public static int[] getIdContactPhone(){
        return new int[]{R.id.tv_phone_0,R.id.tv_phone_1,R.id.tv_phone_2,R.id.tv_phone_3,R.id.tv_phone_4,R.id.tv_phone_5,R.id.tv_phone_6,R.id.tv_phone_7
                ,R.id.tv_phone_8,R.id.tv_phone_9,R.id.tv_phone_10,R.id.tv_phone_11,R.id.tv_phone_12,R.id.tv_phone_13};
    }

    public static int[] getIdContactView(){
        return new int[]{R.id.view_font0,R.id.view_font1,R.id.view_font2,R.id.view_font3,R.id.view_font4,R.id.view_font5,R.id.view_font6,R.id.view_font7
                ,R.id.view_font8,R.id.view_font9,R.id.view_font10,R.id.view_font11,R.id.view_font12,R.id.view_font13};
    }

    public static ImageView.ScaleType[] getScaleTypes(){
        return new ImageView.ScaleType[]{
                ImageView.ScaleType.CENTER_CROP,ImageView.ScaleType.CENTER_INSIDE,
                ImageView.ScaleType.CENTER,ImageView.ScaleType.FIT_START,
                ImageView.ScaleType.FIT_CENTER,ImageView.ScaleType.FIT_END,
                ImageView.ScaleType.FIT_XY,ImageView.ScaleType.MATRIX,
        };
    }

    public static String[] getScaleTypeNames(){
        return new String[]{
                "Center Crop","Center Inside",
                "Center","Fit Start",
                "Fit Center","Fit End",
                "Fit xy","Matrix",
        };
    }

    public static int[] getShapes(){
        return new int[]{
                R.drawable.ava_shape_1,R.drawable.ava_shape_2,
                R.drawable.ava_shape_3,R.drawable.ava_shape_4,
                R.drawable.ava_shape_5,R.drawable.ava_shape_6,
                R.drawable.ava_shape_7,R.drawable.ava_shape_8,
                R.drawable.ava_shape_9,R.drawable.ava_shape_10,
                R.drawable.ava_shape_11,R.drawable.ava_shape_12,
                R.drawable.ava_shape_13,R.drawable.ava_shape_14,
                R.drawable.ava_shape_15,R.drawable.ava_shape_16,
                R.drawable.ava_shape_17,R.drawable.ava_shape_18,
                R.drawable.ava_shape_19,R.drawable.ava_shape_20
        };
    }
    public static int[] getBorderShapes(){
        return new int[]{
                R.drawable.ava_border_1,R.drawable.ava_border_2,
                R.drawable.ava_border_3,R.drawable.ava_border_4,
                R.drawable.ava_border_5,R.drawable.ava_border_6,
                R.drawable.ava_border_7,R.drawable.ava_border_8,
                R.drawable.ava_border_9,R.drawable.ava_border_10,
                R.drawable.ava_border_11,R.drawable.ava_border_12,
                R.drawable.ava_border_13,R.drawable.ava_border_14,
                R.drawable.ava_border_15,R.drawable.ava_border_16,
                R.drawable.ava_border_17,R.drawable.ava_border_18,
                R.drawable.ava_border_19,R.drawable.ava_border_20
        };
    }
}
