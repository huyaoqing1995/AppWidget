package com.osapp.appwidget.main;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.google.gson.Gson;
import com.osapp.appwidget.R;
import com.osapp.appwidget.wg.wgAppShortcut.AppShortcutWidget;
import com.osapp.appwidget.wg.wgAnalogClock.AnalogClockWidget;
import com.osapp.appwidget.wg.wgContact.ContactReceiver;
import com.osapp.appwidget.wg.wgContact.ContactWidget;
import com.osapp.appwidget.wg.wgDate.DateWidget;
import com.osapp.appwidget.wg.wgFlashlight.FlashlightHelper;
import com.osapp.appwidget.wg.wgFlashlight.FlashlightReceiver;
import com.osapp.appwidget.wg.wgFlashlight.FlashlightWidget;
import com.osapp.appwidget.wg.wgPhoto.PhotoWidget;
import com.osapp.appwidget.wg.wgTextClock.TextClockWidget;

public class AppWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views  = new RemoteViews(context.getPackageName(), R.layout.app_shortcut_widget);
        String widget = getWidget(context,appWidgetId);

        if(widget.contains(AppShortcutWidget.KEY)){

            // App Shortcut

            AppShortcutWidget appShortcutWidget =  new Gson().fromJson(widget, AppShortcutWidget.class);
            views = new RemoteViews(context.getPackageName(), R.layout.app_shortcut_widget);
            views.setTextViewText(R.id.tv_app_name, appShortcutWidget.getName());
            views.setImageViewBitmap(R.id.img_app,Utils.getBitmapFromPath(appShortcutWidget.getIconPath()));
            views.setViewPadding(R.id.img_app,
                    appShortcutWidget.getIconPaddingPx(context),0, appShortcutWidget.getIconPaddingPx(context),0);
            if(appShortcutWidget.getNameLine()>-1)
            views.setInt(R.id.tv_app_name,"setLines", appShortcutWidget.getNameLine());
            if(appShortcutWidget.getTextSize()>-1)
            views.setTextViewTextSize(R.id.tv_app_name, TypedValue.COMPLEX_UNIT_SP, appShortcutWidget.getTextSize());

            if(appShortcutWidget.getOpenType()==2){
                Intent launchIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(appShortcutWidget.getOpenLink()));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.bt_open_app, pendingIntent);
            }else {
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appShortcutWidget.getOpenLink());
                PendingIntent pendingIntent = PendingIntent.getActivity(context, appWidgetId, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.bt_open_app, pendingIntent);
            }


        }else if(widget.contains(FlashlightWidget.KEY)){

            /// Flashlight

            FlashlightWidget flashlightWidget = new Gson().fromJson(widget,FlashlightWidget.class);

            if(FlashlightHelper.isFlashlightOn(context)){
                views = new RemoteViews(context.getPackageName(), R.layout.flashlight_widget_on);
            }else {
                views = new RemoteViews(context.getPackageName(), R.layout.flashlight_widget_off);
            }

            if(flashlightWidget.getAlpha()>-1){
                views.setInt(R.id.img_flashlight,"setAlpha", (int) ((flashlightWidget.getAlpha()*255)));
            }

            Intent flashIntent = new Intent(context, FlashlightReceiver.class);
            flashIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, flashIntent, 0);
            views.setOnClickPendingIntent(R.id.img_flashlight,pendingIntent);

        } if(widget.contains(AnalogClockWidget.KEY)){

            // Analog Clock

            AnalogClockWidget analogClockWidget = new Gson().fromJson(widget,AnalogClockWidget.class);
            if(analogClockWidget.getType()==1){
                views = new RemoteViews(context.getPackageName(), R.layout.analog_clock_1);
            }else if(analogClockWidget.getType()==2){
                views = new RemoteViews(context.getPackageName(), R.layout.analog_clock_2);
            }else {
                views = new RemoteViews(context.getPackageName(), R.layout.analog_clock_3);
            }
            views.setImageViewBitmap(R.id.bg_clock,Utils.getBitmapFromPath(analogClockWidget.getBackgroundPath()));


        }if(widget.contains(TextClockWidget.KEY)){

            // Text Clock

            TextClockWidget textClockWidget = new Gson().fromJson(widget,TextClockWidget.class);
            views = new RemoteViews(context.getPackageName(), R.layout.text_clock);

            views.setImageViewBitmap(R.id.bg_clock,Utils.getBitmapFromPath(textClockWidget.getBackgroundPath()));
            views.setTextColor(Utils.getIdTextClock()[textClockWidget.getFont()],textClockWidget.getTextColor());
            views.setTextViewTextSize(Utils.getIdTextClock()[textClockWidget.getFont()],TypedValue.COMPLEX_UNIT_SP,textClockWidget.getTextSize());
            views.setInt(Utils.getIdTextClock()[textClockWidget.getFont()],"setVisibility", View.VISIBLE);
            views.setCharSequence(Utils.getIdTextClock()[textClockWidget.getFont()], "setFormat12Hour", textClockWidget.getFormat());
            views.setCharSequence(Utils.getIdTextClock()[textClockWidget.getFont()], "setFormat24Hour", textClockWidget.getFormat());
        }if(widget.contains(DateWidget.KEY)){

            // Date Widget

            DateWidget dateWidget = new Gson().fromJson(widget,DateWidget.class);
            views = new RemoteViews(context.getPackageName(), R.layout.text_clock);

            views.setImageViewBitmap(R.id.bg_clock,Utils.getBitmapFromPath(dateWidget.getBackgroundPath()));
            views.setTextColor(Utils.getIdTextClock()[dateWidget.getFont()],dateWidget.getTextColor());
            views.setTextViewTextSize(Utils.getIdTextClock()[dateWidget.getFont()],TypedValue.COMPLEX_UNIT_SP,dateWidget.getTextSize());
            views.setInt(Utils.getIdTextClock()[dateWidget.getFont()],"setVisibility", View.VISIBLE);
            views.setCharSequence(Utils.getIdTextClock()[dateWidget.getFont()], "setFormat12Hour", dateWidget.getFormat());
            views.setCharSequence(Utils.getIdTextClock()[dateWidget.getFont()], "setFormat24Hour", dateWidget.getFormat());
        }else if(widget.contains(PhotoWidget.KEY)){

            // Photo Widget
            PhotoWidget photoWidget = new Gson().fromJson(widget,PhotoWidget.class);
            views = new RemoteViews(context.getPackageName(), R.layout.photo_widget);
            views.setImageViewBitmap(R.id.img_photo,Utils.getBitmapFromPath(photoWidget.getPath()));

        }else if(widget.contains(ContactWidget.KEY)){

            // Contact Widget
            ContactWidget contactWidget = new Gson().fromJson(widget,ContactWidget.class);
            views = new RemoteViews(context.getPackageName(), R.layout.contact_widget);
            views.setImageViewBitmap(R.id.img_background,Utils.getBitmapFromPath(contactWidget.getBackgroundPath()));
            views.setImageViewBitmap(R.id.img_contact_image,Utils.getBitmapFromPath(contactWidget.getContactPhotoPath()));
            views.setInt(Utils.getIdContactView()[contactWidget.getFont()],"setVisibility", View.VISIBLE);

            views.setTextColor(Utils.getIdContactName()[contactWidget.getFont()],contactWidget.getColor());
            views.setTextColor(Utils.getIdContactPhone()[contactWidget.getFont()],contactWidget.getColor());

            views.setTextViewText(Utils.getIdContactName()[contactWidget.getFont()],contactWidget.getName());
            views.setTextViewText(Utils.getIdContactPhone()[contactWidget.getFont()],contactWidget.getPhone());

            views.setInt(R.id.bt_call,"setColorFilter",contactWidget.getColor());
            views.setInt(R.id.bt_sms,"setColorFilter",contactWidget.getColor());


            Intent smsIntent = new Intent(context, ContactReceiver.class);
            smsIntent.setAction("SMS");
            smsIntent.putExtra("number",contactWidget.getPhone());
            PendingIntent smsPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, smsIntent, 0);
            views.setOnClickPendingIntent(R.id.bt_sms,smsPendingIntent);

            Intent callIntent = new Intent(context, ContactReceiver.class);
            callIntent.setAction("CALL");
            callIntent.putExtra("number",contactWidget.getPhone());
            PendingIntent callPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, callIntent, 0);
            views.setOnClickPendingIntent(R.id.bt_call,callPendingIntent);

        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int i:appWidgetIds){
            deleteWidgetObject(context,i);
        }
    }

    private void deleteWidgetObject(Context context,int appWidgetId) {
        deleteWidget(context,appWidgetId);
        String widget = getWidget(context,appWidgetId);
        if(widget.contains(AppShortcutWidget.KEY)){
            AppShortcutWidget appShortcutWidget =  new Gson().fromJson(widget, AppShortcutWidget.class);
            appShortcutWidget.delete();
        }else if(widget.contains(FlashlightWidget.KEY)){

        }else if(widget.contains(AnalogClockWidget.KEY)){
            AnalogClockWidget analogClockWidget = new Gson().fromJson(widget,AnalogClockWidget.class);
            analogClockWidget.delete();
        } if(widget.contains(TextClockWidget.KEY)){
            TextClockWidget textClockWidget = new Gson().fromJson(widget,TextClockWidget.class);
            textClockWidget.delete();
        }if(widget.contains(DateWidget.KEY)){
            DateWidget dateWidget = new Gson().fromJson(widget,DateWidget.class);
            dateWidget.delete();
        }if(widget.contains(PhotoWidget.KEY)){
            PhotoWidget photoWidget = new Gson().fromJson(widget,PhotoWidget.class);
            photoWidget.delete();
        }if(widget.contains(ContactWidget.KEY)){
            ContactWidget contactWidget = new Gson().fromJson(widget,ContactWidget.class);
            contactWidget.delete();
        }
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }


    public static void setContactWidget(Context context, int idWidget, ContactWidget contactWidget) {
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putString(idWidget+"",new Gson().toJson(contactWidget)).apply();
    }

    public static void setPhotoWidget(Context context, int idWidget, PhotoWidget photoWidget) {
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putString(idWidget+"",new Gson().toJson(photoWidget)).apply();
    }

    public static void setDateWidget(Context context, int idWidget, DateWidget dateWidget) {
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putString(idWidget+"",new Gson().toJson(dateWidget)).apply();
    }

    public static void setTextClockWidget(Context context, int idWidget, TextClockWidget textClockWidget) {
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putString(idWidget+"",new Gson().toJson(textClockWidget)).apply();
    }

    public static void setAnalogClockWidget(Context context, int idWidget, AnalogClockWidget analogClockWidget) {
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putString(idWidget+"",new Gson().toJson(analogClockWidget)).apply();
    }

    public static void setFlashlightWidget(Context context,int idWidget, FlashlightWidget flashlightWidget){
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putString(idWidget+"",new Gson().toJson(flashlightWidget)).apply();
    }

    public static void setAppShortcutWidget(Context context,int idWidget, AppShortcutWidget appShortcutWidget){
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putString(idWidget+"",new Gson().toJson(appShortcutWidget)).apply();
    }

    public static void deleteWidget(Context context,int idWidget){
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().remove(idWidget+"").apply();
    }

    public static String getWidget(Context context,int idWidget){
        SharedPreferences preferences = context.getSharedPreferences(AppWidget.class.getSimpleName(),Context.MODE_PRIVATE);
        return preferences.getString(idWidget+"","")+"";
    }
}

