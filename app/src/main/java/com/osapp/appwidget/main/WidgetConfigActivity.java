package com.osapp.appwidget.main;

import android.Manifest;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.osapp.appwidget.R;
import com.osapp.appwidget.wg.wgAppShortcut.App;
import com.osapp.appwidget.wg.wgAppShortcut.AppData;
import com.osapp.appwidget.wg.wgAppShortcut.AppShortcutWidget;
import com.osapp.appwidget.wg.wgAppShortcut.AppShortcutChooseFragment;
import com.osapp.appwidget.wg.wgAppShortcut.AppShortcutConfigFragment;
import com.osapp.appwidget.wg.wgAppShortcut.AppShortcutListFragment;
import com.osapp.appwidget.wg.wgAppShortcut.ChooseAppCallback;
import com.osapp.appwidget.wg.wgAppShortcut.ShortcutMainAdapter;
import com.osapp.appwidget.wg.wgAnalogClock.AnalogClockConfigFragment;
import com.osapp.appwidget.wg.wgAnalogClock.AnalogClockWidget;
import com.osapp.appwidget.wg.wgBattery.BatteryConfigFragment;
import com.osapp.appwidget.wg.wgContact.ContactConfigFragment;
import com.osapp.appwidget.wg.wgContact.ContactWidget;
import com.osapp.appwidget.wg.wgDate.DateConfigFragment;
import com.osapp.appwidget.wg.wgDate.DateWidget;
import com.osapp.appwidget.wg.wgFlashlight.FlashlightConfigFragment;
import com.osapp.appwidget.wg.wgFlashlight.FlashlightWidget;
import com.osapp.appwidget.wg.wgPhoto.PhotoConfigFragment;
import com.osapp.appwidget.wg.wgPhoto.PhotoWidget;
import com.osapp.appwidget.wg.wgTextClock.TextClockConfigFragment;
import com.osapp.appwidget.wg.wgTextClock.TextClockWidget;
import java.util.ArrayList;

public class WidgetConfigActivity extends AppCompatActivity {

    private AppData appData;
    private ArrayList<App> apps;
    private int appWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        appWidgetId = getIntent().getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        loadBg();

        initAppShortcut();

        initFlashlight();

        initClock();

        initTextClock();

        initDate();

        initPhoto();

        initContact();

        initBattery();

    }

    private void initBattery() {
        findViewById(R.id.bt_battery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, BatteryConfigFragment.newInstance());
            }
        });
    }

    private void initContact() {
        findViewById(R.id.bt_contact_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, ContactConfigFragment.newInstance());
            }
        });
    }

    private void initPhoto() {
        findViewById(R.id.bt_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, PhotoConfigFragment.newInstance());
            }
        });
    }

    private void initDate() {
        findViewById(R.id.bt_date_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, DateConfigFragment.newInstance());
            }
        });
    }

    private void initTextClock() {
        findViewById(R.id.bt_text_clock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, TextClockConfigFragment.newInstance());
            }
        });
    }

    private void initClock() {
        findViewById(R.id.bt_analog_clock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, AnalogClockConfigFragment.newInstance());
            }
        });
    }

    private void initFlashlight() {
        findViewById(R.id.bt_flashlight_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, FlashlightConfigFragment.newInstance());
            }
        });

    }

    private void initAppShortcut() {
        appData = new AppData();
        RecyclerView rcvApplication = findViewById(R.id.rcv_application);
        rcvApplication.setLayoutManager(new GridLayoutManager(this,5));
        apps = appData.getApps(this);
        rcvApplication.setAdapter(new ShortcutMainAdapter(this,apps,appData){
            @Override
            public void OnItemClick(App app) {
                super.OnItemClick(app);
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, AppShortcutConfigFragment.newInstance()
                        .setApp(app,appData));
            }
        });
        findViewById(R.id.bt_see_all_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicConfigFragment.addFragment(WidgetConfigActivity.this, AppShortcutListFragment.newInstance()
                .set(appData,apps));
            }
        });
    }

    public void chooseApp(ChooseAppCallback chooseAppCallback){
        BasicConfigFragment.addFragment(WidgetConfigActivity.this, AppShortcutChooseFragment.newInstance()
        .set(appData,apps,chooseAppCallback));
    }

    public void setResultAppShortcut(AppShortcutWidget appShortcutWidget) {
        appShortcutWidget.fake();
        AppWidget.setAppShortcutWidget(this,appWidgetId, appShortcutWidget);
        AppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this),appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void setResultFlashlightWidget(FlashlightWidget flashlightWidget) {
        AppWidget.setFlashlightWidget(this,appWidgetId, flashlightWidget);
        AppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this),appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void setResultAnalogClockWidget(AnalogClockWidget analogClockWidget) {
        AppWidget.setAnalogClockWidget(this,appWidgetId, analogClockWidget);
        AppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this),appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void setResultTextClockWidget(TextClockWidget textClockWidget) {
        AppWidget.setTextClockWidget(this,appWidgetId, textClockWidget);
        AppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this),appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void setResultDateWidget(DateWidget dateWidget) {
        AppWidget.setDateWidget(this,appWidgetId, dateWidget);
        AppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this),appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void setPhotoWidgetResult(PhotoWidget photoWidget) {
        AppWidget.setPhotoWidget(this,appWidgetId, photoWidget);
        AppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this),appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    public void setResultContactWidget(ContactWidget contactWidget) {
        AppWidget.setContactWidget(this,appWidgetId, contactWidget);
        AppWidget.updateAppWidget(this, AppWidgetManager.getInstance(this),appWidgetId);
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void loadBg() {
        ImageView imgBg = findViewById(R.id.bg);
        mRequestObject = PermissionUtil.with(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onAllGranted(new Func() {
                    @Override
                    protected void call() {
                        try {
                            Glide.with(WidgetConfigActivity.this)
                                    .load(WallpaperManager.getInstance(WidgetConfigActivity.this).getDrawable())
                                    .into(imgBg);
                        }catch (Exception ignored){ }
                    }
                }).ask(12);
    }

    public PermissionUtil.PermissionRequestObject mRequestObject;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mRequestObject!=null){
            mRequestObject.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }



}