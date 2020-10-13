package com.osapp.appwidget.wg.wgAppShortcut;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.osapp.appwidget.main.BasicConfigFragment;
import com.osapp.appwidget.R;
import com.osapp.appwidget.main.Utils;
import com.osapp.appwidget.shapeView.ShapeOfView;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static android.app.Activity.RESULT_OK;

public class AppShortcutConfigFragment extends BasicConfigFragment {

    private App app;
    private AppData appData;
    private ImageView imgApp;
    private TextView tvAppName;
    private TextView tvAction;
    private int iconPadding;
    private int textSize;
    private int openType = 1;
    private String openLink;

    public AppShortcutConfigFragment setApp(App app, AppData appData) {
        this.app = app;
        this.appData = appData;
        return this;
    }

    public AppShortcutConfigFragment() {
    }

    public static AppShortcutConfigFragment newInstance() {
        Bundle args = new Bundle();
        AppShortcutConfigFragment fragment = new AppShortcutConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        iconPadding = getLastIconPadding(activity);
        textSize = getLastTextSize(activity);

        initBg();

        ShapeOfView shapeOfView = view.findViewById(R.id.shape_view);
        imgApp = view.findViewById(R.id.img_app);
        tvAppName = view.findViewById(R.id.tv_app_name);

        shapeOfView.setDrawable(appData.getIcon(app.getResolveInfo()));
        Glide.with(activity)
                .load(appData.getIcon(app.getResolveInfo()))
                .into(imgApp);

        tvAppName.setText(appData.getName(app.getResolveInfo()));

        view.findViewById(R.id.bt_change_icon).setOnClickListener(view1 -> selectImage());
        view.findViewById(R.id.bt_change_name).setOnClickListener(view12 -> showDialogChangeName());

        openLink = app.getAppId();
        tvAction = view.findViewById(R.id.tv_action);
        tvAction.setText("Open "+appData.getName(app.getResolveInfo()));
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity,view);
                popupMenu.getMenu().add(1,1,1,"Open "+appData.getName(app.getResolveInfo()));
                popupMenu.getMenu().add(2,2,2,"Open app...");
                popupMenu.getMenu().add(3,3,3,"Open link...");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==1){
                            tvAction.setText("Open "+appData.getName(app.getResolveInfo()));
                            openLink = app.getAppId();
                            openType = 1;
                        }else if(item.getItemId()==2){
                            activity.chooseApp(app -> {
                                tvAction.setText("Open "+appData.getName(app.getResolveInfo()));
                                openLink = app.getAppId();
                                openType = 1;
                            });
                        }else if(item.getItemId()==3){
                            showDialogOpenLink();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        SeekBar sbPadding = view.findViewById(R.id.sb_padding);
        TextView tvPadding = view.findViewById(R.id.tv_padding);
        tvPadding.setText(iconPadding+"dp");
        sbPadding.setProgress(iconPadding);
        sbPadding.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPadding.setText(i+"dp");
                iconPadding = i;
                setLastIconPadding(activity,iconPadding);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SeekBar sbTextSize = view.findViewById(R.id.sb_text_size);
        TextView tvTextSize = view.findViewById(R.id.tv_text_size);
        tvTextSize.setText(textSize+"sp");
        sbTextSize.setProgress(textSize-10);
        tvAppName.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvTextSize.setText((i+10)+"sp");
                textSize = i+10;
                setLastTextSize(activity,textSize);
                tvAppName.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        RadioButton rdSingleLine = view.findViewById(R.id.cb_single_line);
        RadioButton rdDoubleLine = view.findViewById(R.id.cb_double_line);
        if(getLastLine(activity)==1){
            rdSingleLine.setChecked(true);
        }else {
            rdDoubleLine.setChecked(true);
        }
        tvAppName.setLines(rdSingleLine.isChecked()?1:2);
        rdSingleLine.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                setLastLine(activity,1);
                tvAppName.setLines(1);
            }
        });
        rdDoubleLine.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b){
                setLastLine(activity,2);
                tvAppName.setLines(2);
            }
        });






        view.findViewById(R.id.bt_add_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tvAppName.getText().toString()+"";
                shapeOfView.setDrawingCacheEnabled(true);
                shapeOfView.buildDrawingCache();
                Bitmap bm = shapeOfView.getDrawingCache();
                String path = Utils.savePath(activity,bm);
                activity.setResultAppShortcut(new AppShortcutWidget(path,name,app.getAppId(),iconPadding,
                        rdSingleLine.isChecked()?1:2,textSize,openType,openLink));
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void showDialogOpenLink() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final EditText edittext = new EditText(activity);
        alert.setMessage("Input url: ");
        alert.setTitle("Open link");
        alert.setView(edittext);
        edittext.setText("https://");
        alert.setPositiveButton("Ok", (dialogInterface, i) -> {
            if(isValidURL(edittext.getText().toString())){
                tvAction.setText("Open "+edittext.getText().toString());
                openLink = edittext.getText().toString();
                openType = 2;
            }else {
                Toast.makeText(activity, "Invalid url!", Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancel",null);
        alert.show();
    }

    public boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
        return true;
    }


    private void showDialogChangeName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final EditText edittext = new EditText(activity);
        edittext.setText(tvAppName.getText().toString());
        alert.setMessage("Enter name: ");
        alert.setTitle("Change App Name");
        alert.setView(edittext);
        alert.setPositiveButton("Ok", (dialog, whichButton) -> tvAppName.setText(edittext.getText().toString()));
        alert.setNegativeButton("Cancel",null);
        alert.show();
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


    private void initBg() {
        ImageView imgBg = view.findViewById(R.id.bg);
        try {
            Glide.with(activity)
                    .load(WallpaperManager.getInstance(getActivity()).getDrawable())
                    .into(imgBg);
        }catch (Exception ignored){ }
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
            imgApp.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .load(selectedImageUri)
                    .into(imgApp);

        }
    }

    private int getLastTextSize(Context context){
        SharedPreferences preferences = context.getSharedPreferences(AppShortcutConfigFragment.class.getSimpleName(),Context.MODE_PRIVATE);
        return preferences.getInt("text_size",16);
    }
    private void setLastTextSize(Context context,int textSize){
        SharedPreferences preferences = context.getSharedPreferences(AppShortcutConfigFragment.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putInt("text_size",textSize).apply();
    }

    private int getLastIconPadding(Context context){
        SharedPreferences preferences = context.getSharedPreferences(AppShortcutConfigFragment.class.getSimpleName(),Context.MODE_PRIVATE);
        return preferences.getInt("icon_padding",0);
    }

    private void setLastIconPadding(Context context,int padding){
        SharedPreferences preferences = context.getSharedPreferences(AppShortcutConfigFragment.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putInt("icon_padding",padding).apply();
    }

    private int getLastLine(Context context){
        SharedPreferences preferences = context.getSharedPreferences(AppShortcutConfigFragment.class.getSimpleName(),Context.MODE_PRIVATE);
        return preferences.getInt("name_line",1);
    }

    private void setLastLine(Context context,int line){
        SharedPreferences preferences = context.getSharedPreferences(AppShortcutConfigFragment.class.getSimpleName(),Context.MODE_PRIVATE);
        preferences.edit().putInt("name_line",line).apply();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_custom_app_shortcut;
    }
}
