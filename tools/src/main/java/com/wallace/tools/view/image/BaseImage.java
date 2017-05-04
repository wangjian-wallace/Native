package com.wallace.tools.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

/**
 * Package com.wallace.tools.view.image
 * Created by Wallace.
 * on 2017/5/4.
 */

public abstract class BaseImage extends View {
    public BaseImage(Context context) {
        super(context);
    }

    public BaseImage(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImage(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract Bitmap getImage();
    public abstract void setOnSeekListener(OnSeekListener onSeekListener);

    public interface OnSeekListener{
        void onSeek(Bitmap bitmap);
    }
}
