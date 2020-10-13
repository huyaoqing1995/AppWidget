package com.osapp.appwidget.shapeView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;

import com.osapp.appwidget.R;

public class ShapeOfView extends FrameLayout {
    /* access modifiers changed from: private */
    public ClipManager clipManager = null;
    private final Paint clipPaint = new Paint(1);
    protected Bitmap mask;
    protected PorterDuffXfermode pdMode;

    public ShapeOfView(@NonNull Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public ShapeOfView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ShapeOfView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        int resourceId;
        this.clipPaint.setAntiAlias(true);
        this.clipPaint.setColor(-1);
        setDrawingCacheEnabled(true);
        setLayerType(1, (Paint) null);
        this.pdMode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
        setWillNotDraw(false);
        this.clipPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.clipPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.clipPaint.setStrokeWidth(1.0f);

        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeOfView);
            if (obtainStyledAttributes.hasValue(0) && -1 != (resourceId = obtainStyledAttributes.getResourceId(0, -1))) {
                setDrawable(resourceId);
            }
            obtainStyledAttributes.recycle();
        }


    }

    /* access modifiers changed from: protected */
    public int dpToPx(float f) {
        return (int) TypedValue.applyDimension(1, f, Resources.getSystem().getDisplayMetrics());
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            Log.e("Onlayout:", z + "");
            calculateLayout();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        this.clipPaint.setXfermode(this.pdMode);
        canvas.drawBitmap(this.mask, 0.0f, 0.0f, this.clipPaint);
        this.clipPaint.setXfermode((Xfermode) null);
    }

    private void calculateLayout() {
        if (this.clipManager != null) {
            int measuredHeight = getMeasuredHeight();
            int measuredWidth = getMeasuredWidth();
            if (measuredWidth > 0 && measuredHeight > 0) {
                Bitmap bitmap = this.mask;
                if (bitmap != null && !bitmap.isRecycled()) {
                    this.mask.recycle();
                }
                ClipManager clipManager2 = this.clipManager;
                if (clipManager2 != null) {
                    clipManager2.setupClipLayout(measuredWidth, measuredHeight);
                    this.mask = this.clipManager.createMask(measuredWidth, measuredHeight);
                }
                if (Build.VERSION.SDK_INT >= 21 && ViewCompat.getElevation(this) > 0.0f) {
                    try {
                        setOutlineProvider(getOutlineProvider());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        postInvalidate();
    }

    @TargetApi(21)
    public ViewOutlineProvider getOutlineProvider() {
        return new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                Path shadowConvexPath;
                if (ShapeOfView.this.clipManager != null && (shadowConvexPath = ShapeOfView.this.clipManager.getShadowConvexPath()) != null) {
                    try {
                        outline.setConvexPath(shadowConvexPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void setClipPathCreator(ClipPathManager.ClipPathCreator clipPathCreator) {
        if (!(this.clipManager instanceof ClipPathManager)) {
            this.clipManager = new ClipPathManager();
        }
        ((ClipPathManager) this.clipManager).setClipPathCreator(clipPathCreator);
    }

    public void setDrawable(Drawable drawable) {
        if (!(this.clipManager instanceof ClipDrawableManager)) {
            this.clipManager = new ClipDrawableManager();
        }
        ((ClipDrawableManager) this.clipManager).setDrawable(drawable);
        calculateLayout();
    }

    public void setDrawable(int i) {
        setDrawable(AppCompatResources.getDrawable(getContext(), i));
    }
}