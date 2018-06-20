package com.example.cookit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends android.support.v7.widget.AppCompatImageView {

    private int bitmapWidth;
    private int bitmapHeight;

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bitmapWidth = bm.getWidth();
        this.bitmapHeight = bm.getHeight();
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        // If the picture is tall and thin, blocking it into the square
        // thats has the screen's width as it's length and width.
        if (this.getBitmapWidth() <= this.getBitmapHeight()) {
            setMeasuredDimension(width, width);
        }
        // The picture is fat and short
        else
        {
            this.setMeasuredDimension(width, (int)((double)(this.getBitmapHeight() * width / this.getBitmapWidth())));

            // Smaller than the screen - neeed to stretch it.
            if (this.getBitmapWidth() < width) {
                this.setScaleType(ScaleType.FIT_XY);
            }
        }




    }
}