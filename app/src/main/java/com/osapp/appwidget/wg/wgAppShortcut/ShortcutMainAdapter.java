package com.osapp.appwidget.wg.wgAppShortcut;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.osapp.appwidget.R;
import java.util.ArrayList;

public class ShortcutMainAdapter extends RecyclerView.Adapter<ShortcutMainAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<App> apps;
    private AppData appData;

    public ShortcutMainAdapter(Activity context, ArrayList<App> apps, AppData appData) {
        this.context = context;
        this.apps = apps;
        this.appData = appData;
    }

    @NonNull
    @Override
    public ShortcutMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_app_shortcut_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutMainAdapter.ViewHolder holder, final int position) {
        App app = apps.get(position);
        if(appData.getIcon(app.getResolveInfo())!=null){
            Glide.with(context).load(appData.getIcon(app.getResolveInfo()))
                    .into(holder.imgIcon);
        }
        app.setLoadAppCallback(new LoadAppCallback() {
            @Override
            public void OnLoaded() {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemChanged(position);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_app);
            itemView.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels/5.4);
            itemView.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels/5.4);
            itemView.setOnClickListener(view -> OnItemClick(apps.get(getAdapterPosition())));
        }
    }

    public void OnItemClick(App app){};
}
