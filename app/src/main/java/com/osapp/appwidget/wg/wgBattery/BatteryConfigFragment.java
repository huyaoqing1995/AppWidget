package com.osapp.appwidget.wg.wgBattery;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.osapp.appwidget.R;
import com.osapp.appwidget.helpers.CustomTypeFaceSpan;
import com.osapp.appwidget.main.BasicConfigFragment;
import com.osapp.appwidget.main.Utils;
import com.osapp.appwidget.shapeView.ShapeOfView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.BATTERY_SERVICE;

public class BatteryConfigFragment extends BasicConfigFragment {

    private int font = 0;
    private int color = Color.WHITE;
    private ImageView imgBackground;
    private BatteryReceiver batteryReceiver;

    public static BatteryConfigFragment newInstance() {
        Bundle args = new Bundle();
        BatteryConfigFragment fragment = new BatteryConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBg();

        ShapeOfView shapeBackground = view.findViewById(R.id.shape_background);
        imgBackground = view.findViewById(R.id.img_background);
        TextView tvBattery = view.findViewById(R.id.tv_battery);
        ImageView imgBattery = view.findViewById(R.id.img_type_battery);
        ProgressBar pbBattery = view.findViewById(R.id.pb_battery);

        BatteryManager bm = (BatteryManager) activity.getSystemService(BATTERY_SERVICE);
        int battery = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        if(bm.isCharging()){
            imgBattery.setImageResource(R.drawable.ic_flashlight);
        }else {
            imgBattery.setImageResource(R.drawable.ic_phone);
        }

        pbBattery.setMax(100);
        pbBattery.setProgress(battery);
        tvBattery.setText(battery+"%");

        batteryReceiver = new BatteryReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if(isAdded()){
                        BatteryManager bm1 = (BatteryManager) activity.getSystemService(BATTERY_SERVICE);
                        int battery1 = bm1.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                        tvBattery.setText(battery1 +"%");
                        if(bm1.isCharging()){
                            imgBattery.setImageResource(R.drawable.ic_flashlight);
                        }else {
                            imgBattery.setImageResource(R.drawable.ic_phone);
                        }
                    }
                    Toast.makeText(context, intent.getAction()+"/"+bm.isCharging()+"/"+battery, Toast.LENGTH_SHORT).show();
                },500);


            }
        };
        batteryReceiver.start(activity);


        view.findViewById(R.id.bt_change_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(activity)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                                tvBattery.setTextColor(lastSelectedColor);
                                imgBattery.setColorFilter(lastSelectedColor);
                                color = lastSelectedColor;
                            }
                        })
                        .density(12).build().show();
            }
        });


        view.findViewById(R.id.bt_change_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity, view);
                popupMenu.getMenu().add("Color");
                popupMenu.getMenu().add("Image");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    if(item.getTitle().toString().equals("Color")){
                        ColorPickerDialogBuilder.with(activity)
                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                .setPositiveButton("OK", new ColorPickerClickListener() {
                                    @Override
                                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                                        imgBackground.setColorFilter(lastSelectedColor);
                                    }
                                })
                                .density(12).build().show();
                    }else {
                        selectBackgroundImage();
                    }
                    return false;
                });
            }
        });


        TextView tvAlpha = view.findViewById(R.id.tv_background_alpha);
        SeekBar sbAlpha = view.findViewById(R.id.sb_background_alpha);
        tvAlpha.setText("0%");
        sbAlpha.setProgress(0);
        sbAlpha.setMax(100);
        sbAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvAlpha.setText(i+"%");
                imgBackground.setAlpha(1-(float)i/(float)100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        TextView tvFont = view.findViewById(R.id.tv_font);
        tvFont.setText(Utils.getFontNames()[font]);
        if(font==0){
            tvFont.setTypeface(Typeface.DEFAULT);
            tvBattery.setTypeface(Typeface.DEFAULT);
        }else {
            tvFont.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
            tvBattery.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
        }
        tvFont.setOnClickListener(view12 -> {

            PopupMenu popupMenu = new PopupMenu(activity, view12);
            for (int i = 0; i< Utils.getFontNames().length; i++){
                popupMenu.getMenu().add(i,i,i,Utils.getFontNames()[i]);
            }

            Menu menu = popupMenu.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem mi = menu.getItem(i);
                applyFontToMenuItem(mi,i);
            }

            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                font = item.getItemId();
                tvFont.setText(Utils.getFontNames()[font]);
                if(font==0){
                    tvFont.setTypeface(Typeface.DEFAULT);
                    tvBattery.setTypeface(Typeface.DEFAULT);
                }else {
                    tvFont.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
                    tvBattery.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
                }
                return false;
            });

        });



    }

    private void applyFontToMenuItem(MenuItem mi, int i) {
        if(i==0) return;
        Typeface font = ResourcesCompat.getFont(activity,Utils.getFonts()[i]);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font,Color.BLACK), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private Uri outputFileUriBackgroundImage;
    private void selectBackgroundImage() {
        final String scan = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), scan);
        outputFileUriBackgroundImage = Uri.fromFile(sdImageMainDirectory);
        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = activity.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUriBackgroundImage);
            cameraIntents.add(intent);
        }
        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);
        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[0]));
        startActivityForResult(chooserIntent, 112);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        batteryReceiver.stop(activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 112&&resultCode==RESULT_OK) {
            final boolean isCamera;
            if (data == null || data.getData() == null) {
                isCamera = true;
            } else {
                final String action = data.getAction();
                if (action == null) {
                    isCamera = false;
                } else {
                    isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }
            Uri selectedImageUri;
            if (isCamera) {
                selectedImageUri = outputFileUriBackgroundImage;
            } else {
                selectedImageUri = data.getData();
            }

            imgBackground.setColorFilter(0);
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .load(selectedImageUri)
                    .into(imgBackground);

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_config_battery;
    }

    private void initBg() {
        ImageView imgBg = view.findViewById(R.id.bg);
        try {
            Glide.with(activity)
                    .load(WallpaperManager.getInstance(getActivity()).getDrawable())
                    .into(imgBg);
        }catch (Exception ignored){ }
    }

}
