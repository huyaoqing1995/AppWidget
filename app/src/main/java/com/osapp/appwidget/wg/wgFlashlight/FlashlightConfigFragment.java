package com.osapp.appwidget.wg.wgFlashlight;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.osapp.appwidget.R;
import com.osapp.appwidget.main.BasicConfigFragment;

public class FlashlightConfigFragment extends BasicConfigFragment {

    public FlashlightConfigFragment() {
    }

    public static FlashlightConfigFragment newInstance() {
        Bundle args = new Bundle();
        FlashlightConfigFragment fragment = new FlashlightConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBg();

        ImageView imgFlashOn = view.findViewById(R.id.img_flash_on);
        ImageView imgFlashOff = view.findViewById(R.id.img_flash_off);

        TextView tvAlpha = view.findViewById(R.id.tv_background_alpha);
        SeekBar sbAlpha = view.findViewById(R.id.sb_background_alpha);
        tvAlpha.setText("0%");
        sbAlpha.setProgress(0);
        sbAlpha.setMax(100);
        sbAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvAlpha.setText(i+"%");
                imgFlashOn.setAlpha(1-(float)i/(float)100);
                imgFlashOff.setAlpha(1-(float)i/(float)100);
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
                FlashlightWidget flashlightWidget = new FlashlightWidget();
                flashlightWidget.fake();
                flashlightWidget.setAlpha(imgFlashOff.getAlpha());
                activity.setResultFlashlightWidget(flashlightWidget);
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


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_config_flashlight_widget;
    }
}
