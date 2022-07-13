package ru.timestop.android;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class CenterCropDrawable extends Drawable {
    private final Drawable drawable;

    public CenterCropDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    @Override
    public void draw(Canvas canvas) {
        drawable.draw(canvas);
    }

    @Override
    public void setAlpha(int alpha) {
        drawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        drawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return drawable.getOpacity();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        final int vwidth = bounds.width();
        final int vheight = bounds.height();
        final int dwidth = drawable.getIntrinsicWidth();
        final int dheight = drawable.getIntrinsicHeight();

        float scale;
        float dx = 0, dy = 0;

        if (dwidth * vheight > vwidth * dheight) {
            scale = (float) vheight / (float) dheight;
            dx = (vwidth - dwidth * scale) * 0.5f;
        } else {
            scale = (float) vwidth / (float) dwidth;
            dy = (vheight - dheight * scale) * 0.5f;
        }

        int l = (int) dx;
        int t = (int) dy;
        int r = (int) (l + (dwidth * scale));
        int b = (int) (t + (dheight * scale));

        drawable.setBounds(l, t, r, b);
    }
}