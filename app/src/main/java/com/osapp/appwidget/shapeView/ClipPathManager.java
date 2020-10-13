package com.osapp.appwidget.shapeView;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class ClipPathManager implements ClipManager {
    private ClipPathCreator createClipPath = null;
    private final Paint paint = new Paint(1);
    protected final Path path = new Path();

    public interface ClipPathCreator {
        Path createClipPath(int i, int i2);
    }

    public ClipPathManager() {
        this.paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(1.0f);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public final Path createClipPath(int i, int i2) {
        ClipPathCreator clipPathCreator = this.createClipPath;
        if (clipPathCreator != null) {
            return clipPathCreator.createClipPath(i, i2);
        }
        return null;
    }

    public void setClipPathCreator(ClipPathCreator clipPathCreator) {
        this.createClipPath = clipPathCreator;
    }

    public Bitmap createMask(int i, int i2) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawPath(this.path, this.paint);
        return createBitmap;
    }

    @Nullable
    public Path getShadowConvexPath() {
        return this.path;
    }

    public void setupClipLayout(int i, int i2) {
        this.path.reset();
        Path createClipPath2 = createClipPath(i, i2);
        if (createClipPath2 != null) {
            this.path.set(createClipPath2);
        }
    }
}
