package com.osapp.appwidget.wg.wgPhoto;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class PhotoConfigFragment extends BasicConfigFragment {

    private ImageView imgBackground;

    public static PhotoConfigFragment newInstance() {
        Bundle args = new Bundle();
        PhotoConfigFragment fragment = new PhotoConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBg();

        ShapeOfView shapeOfView = view.findViewById(R.id.shape_view);
        ShapeOfView shapeBorder = view.findViewById(R.id.shape_border);
        ImageView imgBorder = view.findViewById(R.id.img_border);
        imgBackground = view.findViewById(R.id.img_background);
        shapeOfView.setDrawable(Utils.getShapes()[0]);
        shapeBorder.setDrawable(Utils.getBorderShapes()[0]);



        RecyclerView rcvShape = view.findViewById(R.id.rcv_shape);
        rcvShape.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
        rcvShape.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_shape,parent,false)) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((ImageView)holder.itemView.findViewById(R.id.img_shape)).setImageResource(Utils.getShapes()[position]);
                holder.itemView.setOnClickListener(view1 -> {
                    shapeOfView.setDrawable(Utils.getShapes()[position]);
                    shapeBorder.setDrawable(Utils.getBorderShapes()[position]);
                });
            }

            @Override
            public int getItemCount() {
                return Utils.getShapes().length;
            }
        });

        view.findViewById(R.id.bt_add_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        TextView tvScaleType = view.findViewById(R.id.tv_scale_type);
        imgBackground.setScaleType(Utils.getScaleTypes()[0]);
        tvScaleType.setText(Utils.getScaleTypeNames()[0]);
        view.findViewById(R.id.tv_scale_type).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(activity,view);
                for (int i=0;i<Utils.getScaleTypeNames().length;i++){
                    popupMenu.getMenu().add(i,i,i,Utils.getScaleTypeNames()[i]);
                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        tvScaleType.setText(Utils.getScaleTypeNames()[item.getItemId()]);
                        imgBackground.setScaleType(Utils.getScaleTypes()[item.getItemId()]);
                        tvScaleType.setText(Utils.getScaleTypeNames()[item.getItemId()]);

                        if(outputFileUri!=null){
                            Glide.with(activity)
                                    .applyDefaultRequestOptions(new RequestOptions()
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                                    .load(outputFileUri)
                                    .into(imgBackground);
                        }
                        return false;
                    }
                });
            }
        });

        view.findViewById(R.id.bt_change_border_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(activity)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                                imgBorder.setImageDrawable(new ColorDrawable(lastSelectedColor));
                            }
                        })
                        .density(12).build().show();
            }
        });

        view.findViewById(R.id.bt_add_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shapeOfView.setDrawingCacheEnabled(true);
                shapeOfView.buildDrawingCache();
                Bitmap bm = shapeOfView.getDrawingCache();
                String path = Utils.savePath(activity,bm);

                PhotoWidget photoWidget = new PhotoWidget(path,outputFileUri!=null?outputFileUri.toString():"");
                photoWidget.fake();
                activity.setPhotoWidgetResult(photoWidget);
            }
        });


    }

    private void initBg() {
        ImageView imgBg = view.findViewById(R.id.bg);
        try {
            Glide.with(activity)
                    .load(WallpaperManager.getInstance(getActivity()).getDrawable())
                    .into(imgBg);
        }catch (Exception ignored){ }
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

            if (!isCamera) {
                outputFileUri = data.getData();
            }

            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .load(outputFileUri)
                    .into(imgBackground);

        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_config_photo;
    }
}
