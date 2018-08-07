package com.example.cookit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

public class CustomImageView extends android.support.v7.widget.AppCompatImageView {

    private int bitmapWidth;
    private int bitmapHeight;
    public  int measuredWidth;

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (bm != null) {
            this.bitmapWidth = bm.getWidth();
            this.bitmapHeight = bm.getHeight();
        }
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();
        int width = getMeasuredWidth();

        if (this.getBitmapWidth() != 0) {
            this.setMeasuredDimension(width, (int) ((double) (this.getBitmapHeight() * width / this.getBitmapWidth())));

            // Stretching the picture if it is too small.
            if (this.getBitmapWidth() < width) {
                this.setScaleType(ScaleType.FIT_XY);
                this.setMeasuredDimension(width, this.getBitmapHeight() * width / this.getBitmapWidth());
            }
        }
        // If the picture is tall and thin, blocking it into the square
        // thats has the screen's width as it's length and width.
        /*if (this.getBitmapWidth() <= this.getBitmapHeight()) {
            setMeasuredDimension(width, width);
        }
        // The picture is fat and short
        else
        {

        }*/
    }
}