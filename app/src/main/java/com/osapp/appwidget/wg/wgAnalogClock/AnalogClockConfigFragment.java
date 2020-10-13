package com.osapp.appwidget.wg.wgAnalogClock;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.osapp.appwidget.R;
import com.osapp.appwidget.main.BasicConfigFragment;
import com.osapp.appwidget.main.Utils;
import com.osapp.appwidget.shapeView.ShapeOfView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AnalogClockConfigFragment extends BasicConfigFragment {

    private ImageView imgBackground;
    private int typeClock = 1;

    public static AnalogClockConfigFragment newInstance() {
        Bundle args = new Bundle();
        AnalogClockConfigFragment fragment = new AnalogClockConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBg();

        ShapeOfView shapeOfView = view.findViewById(R.id.shape_view);
        View clock1 = view.findViewById(R.id.clock1);
        View clock3 = view.findViewById(R.id.clock3);
        clock3.setVisibility(View.GONE);



        imgBackground = view.findViewById(R.id.img_background);
        imgBackground.setColorFilter(Color.BLACK);

        view.findViewById(R.id.bt_clock_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapeOfView.setDrawable(R.drawable.bg_radius_15dp);
                clock1.setVisibility(View.VISIBLE);
                clock3.setVisibility(View.GONE);
                typeClock = 1;
            }
        });
        view.findViewById(R.id.bt_clock_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapeOfView.setDrawable(R.drawable.bg_radius_max);
                clock1.setVisibility(View.VISIBLE);
                clock3.setVisibility(View.GONE);
                typeClock = 2;
            }
        });
        view.findViewById(R.id.bt_clock_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapeOfView.setDrawable(R.drawable.bg_radius_clock3);
                clock1.setVisibility(View.GONE);
                clock3.setVisibility(View.VISIBLE);
                typeClock = 3;
            }
        });

        view.findViewById(R.id.bt_change_background).setOnClickListener(view13 -> {
            PopupMenu popupMenu = new PopupMenu(activity, view13);
            popupMenu.getMenu().add("Color");
            popupMenu.getMenu().add("Image");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getTitle().toString().equals("Color")){
                    selectColor();
                }else {
                    selectImage();
                }
                return false;
            });
        });

        TextView tvAlpha = view.findViewById(R.id.tv_alpha);
        SeekBar sbAlpha = view.findViewById(R.id.sb_alpha);
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


        view.findViewById(R.id.bt_add_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shapeOfView.setDrawingCacheEnabled(true);
                shapeOfView.buildDrawingCache();
                Bitmap bm = shapeOfView.getDrawingCache();
                String path = Utils.savePath(activity,bm);
                AnalogClockWidget analogClockWidget = new AnalogClockWidget(path, typeClock);
                analogClockWidget.fake();
                activity.setResultAnalogClockWidget(analogClockWidget);
            }
        });

    }

    private Uri outputFileUri;
    private void selectImage() {
        final String scan = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), scan);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
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
        startActivityForResult(chooserIntent, 111);
    }

    private void selectColor() {
        ColorPickerDialogBuilder.with(activity)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                        imgBackground.setColorFilter(lastSelectedColor);
                    }
                })
                .density(12).build().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111&&resultCode==RESULT_OK) {
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
                selectedImageUri = outputFileUri;
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


    private void initBg() {
        ImageView imgBg = view.findViewById(R.id.bg);
        try {
            Glide.with(activity)
                    .load(WallpaperManager.getInstance(getActivity()).getDrawable())
                    .into(imgBg);
        }catch (Exception ignored){ }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_config_analog_clock_widget;
    }
}
