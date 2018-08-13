package com.gvjay.schedulemanager;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DayViewDrawable extends Drawable {

    private float left, top, right, bottom;
    private Paint paint;

    public DayViewDrawable(float left, float top, float right, float bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.paint = new Paint();
        this.paint.setARGB(255, 255, 0, 0);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
