package com.osapp.appwidget.shapeView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ClipDrawableManager implements ClipManager {
    protected Drawable drawable = null;

    @Nullable
    public Path getShadowConvexPath() {
        return null;
    }

    public void setupClipLayout(int i, int i2) {
    }

    public void setDrawable(Drawable drawable2) {
        this.drawable = drawable2;
    }

    @NonNull
    public Bitmap createMask(int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Drawable drawable2 = this.drawable;
        if (drawable2 != null) {
            drawable2.setBounds(0, 0, i, i2);
            this.drawable.draw(canvas);
        }
        return createBitmap;
    }
}