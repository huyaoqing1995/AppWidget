package com.osapp.appwidget.wg.wgAppShortcut;

import android.app.WallpaperManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.osapp.appwidget.R;
import com.osapp.appwidget.main.BasicConfigFragment;
import java.util.ArrayList;

public class AppShortcutListFragment extends BasicConfigFragment {

    private AppData appData;
    private ArrayList<App> apps;

    public AppShortcutListFragment() {
    }

    public AppShortcutListFragment set(AppData appData, ArrayList<App> apps) {
        this.appData = appData;
        this.apps = apps;
        return this;
    }

    public static AppShortcutListFragment newInstance() {
        Bundle args = new Bundle();
        AppShortcutListFragment fragment = new AppShortcutListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initBg();

        RecyclerView rcvApplication = view.findViewById(R.id.rcv_app_shortcut);
        rcvApplication.setLayoutManager(new GridLayoutManager(activity,5));
        rcvApplication.setAdapter(new ShortcutListAdapter(activity,apps,appData){
            @Override
            public void OnItemClick(App app) {
                super.OnItemClick(app);
                BasicConfigFragment.addFragment(activity, AppShortcutConfigFragment.newInstance()
                        .setApp(app,appData));
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
        return R.layout.fragment_app_shortcut_list;
    }
}
