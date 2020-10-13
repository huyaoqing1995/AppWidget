package com.osapp.appwidget.shapeView;

import android.graphics.Bitmap;
import android.graphics.Path;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface ClipManager {
    @NonNull
    Bitmap createMask(int i, int i2);
    @Nullable
    Path getShadowConvexPath();
    void setupClipLayout(int i, int i2);
}