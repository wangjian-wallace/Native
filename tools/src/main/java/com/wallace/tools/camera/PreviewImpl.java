package com.wallace.tools.camera;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;

import com.wallace.tools.camera.utils.AspectRatio;


public abstract class PreviewImpl {

    public interface Callback {
        void onSurfaceChanged();
    }

    private Callback mCallback;

    private int mWidth;
    private int mHeight;

    protected int mTrueWidth;
    protected int mTrueHeight;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public abstract Surface getSurface();

    public abstract View getView();

    public abstract Class getOutputClass();

    public abstract void setDisplayOrientation(int displayOrientation);

    public abstract boolean isReady();

    protected void dispatchSurfaceChanged() {
        mCallback.onSurfaceChanged();
    }

    public SurfaceHolder getSurfaceHolder() {
        return null;
    }

    public SurfaceTexture getSurfaceTexture() {
        return null;
    }

    public void setSize(int width, int height) {
        mWidth = width;
        mHeight = height;

        // Refresh true preview size to adjust scaling
        setTruePreviewSize(mTrueWidth, mTrueHeight);
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setTruePreviewSize(final int width, final int height) {
        this.mTrueWidth = width;
        this.mTrueHeight = height;
        getView().post(new Runnable() {
            @Override
            public void run() {
                if (width != 0 && height != 0) {
                    AspectRatio aspectRatio = AspectRatio.of(width, height);
                    int targetHeight = (int) (getView().getWidth() * aspectRatio.toFloat());
                    float scaleY;
                    if (getView().getHeight() > 0) {
                        scaleY = (float) targetHeight / (float) getView().getHeight();
                    } else {
                        scaleY = 1;
                    }

                    if (scaleY > 1) {
                        getView().setScaleX(1);
                        getView().setScaleY(scaleY);
                    } else {
                        getView().setScaleX(1 / scaleY);
                        getView().setScaleY(1);
                    }
                }
            }
        });
    }

    public int getTrueWidth() {
        return mTrueWidth;
    }

    public int getTrueHeight() {
        return mTrueHeight;
    }

}