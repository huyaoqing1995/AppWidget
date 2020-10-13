package com.osapp.appwidget.wg.wgDate;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class DateConfigFragment extends BasicConfigFragment {

    private ImageView imgBackground;
    private TextClock tvClock;
    private int textSize = 25;
    private int textColor = Color.WHITE;
    private int font = 0;
    private String textFormat = "d MMM yyyy";

    public static DateConfigFragment newInstance() {
        Bundle args = new Bundle();
        DateConfigFragment fragment = new DateConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBg();

        ShapeOfView shapeOfView = view.findViewById(R.id.shape_view);

        imgBackground = view.findViewById(R.id.img_background);
        tvClock = view.findViewById(R.id.text_clock);
        imgBackground.setColorFilter(Color.BLACK);

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

        view.findViewById(R.id.bt_change_text_color).setOnClickListener(view13 -> {
            selectTextColor();
        });


        SeekBar sbTextSize = view.findViewById(R.id.sb_text_size);
        TextView tvTextSize = view.findViewById(R.id.tv_text_size);
        tvTextSize.setText(textSize+"sp");
        sbTextSize.setProgress(textSize-10);
        tvClock.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvTextSize.setText((i+10)+"sp");
                textSize = i+10;
                tvClock.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
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

        TextView tvFormat = view.findViewById(R.id.tv_format);
        tvClock.setFormat12Hour(textFormat);
        tvClock.setFormat24Hour(textFormat);
        tvFormat.setText(textFormat);
        tvFormat.setOnClickListener(view1 -> {
            PopupMenu popupMenu = new PopupMenu(activity, view1);
            popupMenu.getMenu().add("d MMM yyyy");
            popupMenu.getMenu().add("dd MMM yyyy");
            popupMenu.getMenu().add("EEE, dd MMM yyyy");
            popupMenu.getMenu().add("Input...");
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                if(item.getTitle().equals("Input...")){
                    showDialogInputFormat(tvFormat);
                }else {
                    textFormat = item.getTitle().toString();
                    tvClock.setFormat12Hour(textFormat);
                    tvClock.setFormat24Hour(textFormat);
                    tvFormat.setText(textFormat);
                }
                return false;
            });

        });

        TextView tvFont = view.findViewById(R.id.tv_font);
        tvFont.setText(Utils.getFontNames()[font]);
        if(font==0){
            tvFont.setTypeface(Typeface.DEFAULT);
            tvClock.setTypeface(Typeface.DEFAULT);
        }else {
            tvFont.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
            tvClock.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
        }

        tvFont.setOnClickListener(view12 -> {
            PopupMenu popupMenu = new PopupMenu(activity, view12);
            for (int i=0;i<Utils.getFontNames().length;i++){
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
                    tvClock.setTypeface(Typeface.DEFAULT);
                }else {
                    tvFont.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
                    tvClock.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
                }
                return false;
            });

        });


        view.findViewById(R.id.bt_add_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvClock.setVisibility(View.INVISIBLE);
                shapeOfView.setDrawingCacheEnabled(true);
                shapeOfView.buildDrawingCache();
                Bitmap bm = shapeOfView.getDrawingCache();
                String path = Utils.savePath(activity,bm);

                DateWidget dateWidget = new DateWidget(path,textFormat,font,textSize,textColor);
                dateWidget.fake();
                activity.setResultDateWidget(dateWidget);

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDialogInputFormat(TextView tvFormat) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final EditText edittext = new EditText(activity);
        alert.setTitle("Input date format");
        StringBuilder builder = new StringBuilder();
        Date date = new Date();
        builder.append("E :").append(new SimpleDateFormat("E", Locale.getDefault()).format(date)).append("\n");
        builder.append("EE :").append(new SimpleDateFormat("EE", Locale.getDefault()).format(date)).append("\n");
        builder.append("EEE :").append(new SimpleDateFormat("EEE", Locale.getDefault()).format(date)).append("\n");
        builder.append("EEEE :").append(new SimpleDateFormat("EEEE", Locale.getDefault()).format(date)).append("\n");
        builder.append("d :").append(new SimpleDateFormat("d", Locale.getDefault()).format(date)).append("\n");
        builder.append("dd :").append(new SimpleDateFormat("dd", Locale.getDefault()).format(date)).append("\n");
        builder.append("M:").append(new SimpleDateFormat("M", Locale.getDefault()).format(date)).append("\n");
        builder.append("MM:").append(new SimpleDateFormat("MM", Locale.getDefault()).format(date)).append("\n");
        builder.append("MMM:").append(new SimpleDateFormat("MMM", Locale.getDefault()).format(date)).append("\n");
        builder.append("MMMM:").append(new SimpleDateFormat("MMMM", Locale.getDefault()).format(date)).append("\n");
        builder.append("y:").append(new SimpleDateFormat("y", Locale.getDefault()).format(date)).append("\n");
        builder.append("yy:").append(new SimpleDateFormat("yy", Locale.getDefault()).format(date)).append("\n");
        builder.append("yyy:").append(new SimpleDateFormat("yyy", Locale.getDefault()).format(date)).append("\n");
        builder.append("yyyy:").append(new SimpleDateFormat("yyyy", Locale.getDefault()).format(date)).append("\n");
        alert.setMessage(builder.toString());
        alert.setView(edittext);
        edittext.setText(textFormat);
        alert.setPositiveButton("Ok", (dialogInterface, i) -> {
            textFormat = edittext.getText().toString();
            tvClock.setFormat12Hour(textFormat);
            tvClock.setFormat24Hour(textFormat);
            tvFormat.setText(textFormat);
        });
        alert.setNegativeButton("Cancel",null);
        alert.show();
    }


    private void applyFontToMenuItem(MenuItem mi,int i) {
        if(i==0) return;
        Typeface font = ResourcesCompat.getFont(activity,Utils.getFonts()[i]);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font,Color.BLACK), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
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

    private void selectTextColor() {
        ColorPickerDialogBuilder.with(activity)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                        tvClock.setTextColor(lastSelectedColor);
                        textColor = lastSelectedColor;
                    }
                })
                .density(12).build().show();
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
        return R.layout.fragment_config_text_clock_widget;
    }
}
