package com.osapp.appwidget.wg.wgContact;

import android.Manifest;
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
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.osapp.appwidget.R;
import com.osapp.appwidget.helpers.CustomTypeFaceSpan;
import com.osapp.appwidget.main.BasicConfigFragment;
import com.osapp.appwidget.main.Utils;
import com.osapp.appwidget.shapeView.ShapeOfView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ContactConfigFragment extends BasicConfigFragment {

    private int font = 0;
    private int color = Color.WHITE;
    private ImageView imgContactPhoto;
    private ImageView imgBackground;
    private TextView tvName;
    private TextView tvPhone;


    public static ContactConfigFragment newInstance() {
        Bundle args = new Bundle();
        ContactConfigFragment fragment = new ContactConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBg();

        ShapeOfView shapeOfContactPhoto = view.findViewById(R.id.shape_avatar);
        ShapeOfView shapeBorderContactPhoto = view.findViewById(R.id.shape_border);
        ShapeOfView shapeBackground = view.findViewById(R.id.shape_background);

        imgContactPhoto = view.findViewById(R.id.img_contact_image);
        ImageView imgContactBorderPhoto = view.findViewById(R.id.img_border);
        imgBackground = view.findViewById(R.id.img_background);

        tvName = view.findViewById(R.id.tv_name);
        tvPhone = view.findViewById(R.id.tv_phone);
        ImageView btCall = view.findViewById(R.id.bt_call);
        ImageView btSms = view.findViewById(R.id.bt_sms);


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
                    shapeOfContactPhoto.setDrawable(Utils.getShapes()[position]);
                    shapeBorderContactPhoto.setDrawable(Utils.getBorderShapes()[position]);
                });
            }

            @Override
            public int getItemCount() {
                return Utils.getShapes().length;
            }
        });

        view.findViewById(R.id.bt_change_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPickerDialogBuilder.with(activity)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                                tvName.setTextColor(lastSelectedColor);
                                tvPhone.setTextColor(lastSelectedColor);
                                btCall.setColorFilter(lastSelectedColor);
                                btSms.setColorFilter(lastSelectedColor);
                                imgContactBorderPhoto.setColorFilter(lastSelectedColor);
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

        view.findViewById(R.id.bt_contact_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectContactImage();
            }
        });

        TextView tvFont = view.findViewById(R.id.tv_font);
        tvFont.setText(Utils.getFontNames()[font]);
        if(font==0){
            tvFont.setTypeface(Typeface.DEFAULT);
            tvName.setTypeface(Typeface.DEFAULT);
            tvPhone.setTypeface(Typeface.DEFAULT);
        }else {
            tvFont.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
            tvName.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
            tvPhone.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
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
                    tvName.setTypeface(Typeface.DEFAULT);
                    tvPhone.setTypeface(Typeface.DEFAULT);
                }else {
                    tvFont.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
                    tvName.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
                    tvPhone.setTypeface(ResourcesCompat.getFont(activity,Utils.getFonts()[font]));
                }
                return false;
            });

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


        view.findViewById(R.id.bt_add_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRequestObject = PermissionUtil.with(ContactConfigFragment.this)
                        .request(Manifest.permission.READ_CONTACTS)
                        .onAllGranted(new Func() {
                            @Override
                            protected void call() {
                                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),
                                        11);
                            }
                        }).ask(12);
            }
        });

        view.findViewById(R.id.bt_add_widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mRequestObject = PermissionUtil.with(ContactConfigFragment.this)
                        .request(Manifest.permission.CALL_PHONE)
                        .onAllGranted(new Func() {
                            @Override
                            protected void call() {
                                shapeOfContactPhoto.setDrawingCacheEnabled(true);
                                shapeOfContactPhoto.buildDrawingCache();
                                Bitmap bm = shapeOfContactPhoto.getDrawingCache();
                                String contactPhotoPath = Utils.savePath(activity,bm);

                                shapeBackground.setDrawingCacheEnabled(true);
                                shapeBackground.buildDrawingCache();
                                Bitmap bm2 = shapeBackground.getDrawingCache();
                                String backgroundPath = Utils.savePath(activity,bm2);

                                ContactWidget contactWidget = new ContactWidget(contactPhotoPath,backgroundPath,
                                        font,tvName.getText().toString(),tvPhone.getText().toString(),color);
                                contactWidget.fake();

                                activity.setResultContactWidget(contactWidget);
                            }
                        }).ask(12);


            }
        });

    }


    private void applyFontToMenuItem(MenuItem mi, int i) {
        if(i==0) return;
        Typeface font = ResourcesCompat.getFont(activity,Utils.getFonts()[i]);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font,Color.BLACK), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private Uri outputFileUriContactImage;
    private void selectContactImage() {
        final String scan = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), scan);
        outputFileUriContactImage = Uri.fromFile(sdImageMainDirectory);
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
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUriContactImage);
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
                selectedImageUri = outputFileUriContactImage;
            } else {
                selectedImageUri = data.getData();
            }

            Glide.with(this)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE))
                    .load(selectedImageUri)
                    .into(imgContactPhoto);

        }else if (requestCode == 112&&resultCode==RESULT_OK) {
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

        } if (requestCode == 11&&resultCode == RESULT_OK) {
            if(data!=null&&data.getData()!=null){
                String id = ContactWidgetUtils.getContactId(activity,data.getData());
                if(!TextUtils.isEmpty(id)){
                    String name = ContactWidgetUtils.getContactName(activity,data.getData());
                    String phone = ContactWidgetUtils.getContactNumber(activity,id);
                    tvName.setText(name);
                    tvPhone.setText(phone);
                    Glide.with(this)
                            .applyDefaultRequestOptions(new RequestOptions()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE))
                            .load(ContactWidgetUtils.getContactBitmap(activity,id))
                            .circleCrop()
                            .into(imgContactPhoto);
                }
            }
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

    public PermissionUtil.PermissionRequestObject mRequestObject;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(mRequestObject!=null){
            mRequestObject.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_config_contact;
    }
}
