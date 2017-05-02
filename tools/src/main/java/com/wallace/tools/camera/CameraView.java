package com.wallace.tools.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.wallace.tools.R;
import com.wallace.tools.camera.api16.Camera1;
import com.wallace.tools.camera.type.Facing;
import com.wallace.tools.camera.type.Flash;
import com.wallace.tools.camera.type.Focus;
import com.wallace.tools.camera.type.Method;
import com.wallace.tools.camera.type.Zoom;
import com.wallace.tools.camera.utils.AspectRatio;
import com.wallace.tools.camera.utils.CenterCrop;
import com.wallace.tools.camera.utils.DisplayOrientationDetector;
import com.wallace.tools.camera.utils.Size;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.wallace.tools.camera.CameraKit.Constants.FACING_BACK;
import static com.wallace.tools.camera.CameraKit.Constants.FACING_FRONT;
import static com.wallace.tools.camera.CameraKit.Constants.FLASH_AUTO;
import static com.wallace.tools.camera.CameraKit.Constants.FLASH_OFF;
import static com.wallace.tools.camera.CameraKit.Constants.FLASH_ON;
import static com.wallace.tools.camera.CameraKit.Constants.METHOD_STANDARD;

public class CameraView extends FrameLayout {

    @Facing
    private int mFacing;

    @Flash
    private int mFlash;

    @Focus
    private int mFocus;

    @Method
    private int mMethod;

    @Zoom
    private int mZoom;

    private int mJpegQuality;
    private boolean mCropOutput;
    private boolean mAdjustViewBounds;

    private CameraListenerMiddleWare mCameraListener;
    private DisplayOrientationDetector mDisplayOrientationDetector;

    private CameraImpl mCameraImpl;
    private PreviewImpl mPreviewImpl;

    public CameraView(@NonNull Context context) {
        super(context, null);
    }

    @SuppressWarnings("all")
    public CameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CameraView,
                    0, 0);

            try {
                mFacing = a.getInteger(R.styleable.CameraView_ckFacing, CameraKit.Defaults.DEFAULT_FACING);
                mFlash = a.getInteger(R.styleable.CameraView_ckFlash, CameraKit.Defaults.DEFAULT_FLASH);
                mFocus = a.getInteger(R.styleable.CameraView_ckFocus, CameraKit.Defaults.DEFAULT_FOCUS);
                mMethod = a.getInteger(R.styleable.CameraView_ckMethod, CameraKit.Defaults.DEFAULT_METHOD);
                mZoom = a.getInteger(R.styleable.CameraView_ckZoom, CameraKit.Defaults.DEFAULT_ZOOM);
                mJpegQuality = a.getInteger(R.styleable.CameraView_ckJpegQuality, CameraKit.Defaults.DEFAULT_JPEG_QUALITY);
                mCropOutput = a.getBoolean(R.styleable.CameraView_ckCropOutput, CameraKit.Defaults.DEFAULT_CROP_OUTPUT);
                mAdjustViewBounds = a.getBoolean(R.styleable.CameraView_android_adjustViewBounds, CameraKit.Defaults.DEFAULT_ADJUST_VIEW_BOUNDS);
            } finally {
                a.recycle();
            }
        }

        mCameraListener = new CameraListenerMiddleWare();

        mPreviewImpl = new TextureViewPreview(context, this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            mCameraImpl = new Camera2(mCameraListener, mPreviewImpl,context);
//        }else {
            mCameraImpl = new Camera1(mCameraListener, mPreviewImpl);
//        }


        setFacing(mFacing);
        setFlash(mFlash);
        setFocus(mFocus);
        setMethod(mMethod);
        setZoom(mZoom);

        mDisplayOrientationDetector = new DisplayOrientationDetector(context) {
            @Override
            public void onDisplayOrientationChanged(int displayOrientation) {
                mCameraImpl.setDisplayOrientation(displayOrientation);
                mPreviewImpl.setDisplayOrientation(displayOrientation);
            }
        };
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mDisplayOrientationDetector.enable(
                ViewCompat.isAttachedToWindow(this)
                        ? DisplayManagerCompat.getInstance(getContext()).getDisplay(Display.DEFAULT_DISPLAY)
                        : null
        );
    }

    @Override
    protected void onDetachedFromWindow() {
        mDisplayOrientationDetector.disable();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mAdjustViewBounds) {
            Size previewSize = getPreviewSize();
            if(previewSize != null) {
                if (getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
                    int height = MeasureSpec.getSize(heightMeasureSpec);
                    float ratio = (float) height / (float) previewSize.getWidth();
                    int width = (int) (previewSize.getHeight() * ratio);
                    super.onMeasure(
                            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                            heightMeasureSpec
                    );
                    return;
                } else if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
                    int width = MeasureSpec.getSize(widthMeasureSpec);
                    float ratio = (float) width / (float) previewSize.getHeight();
                    int height = (int) (previewSize.getWidth() * ratio);
                    super.onMeasure(
                            widthMeasureSpec,
                            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
                    );
                    return;
                }
            }else{
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                return;
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void start() {
        int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mCameraImpl.start();
                }
            }).start();
        } else {
            requestCameraPermission();
        }
    }

    public void stop() {
        mCameraImpl.stop();
    }

    public void setFacing(@Facing final int facing) {
        this.mFacing = facing;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mCameraImpl.setFacing(facing);
            }
        }).start();
    }

    public void setFlash(@Flash int flash) {
        this.mFlash = flash;
        mCameraImpl.setFlash(flash);
    }

    public void setFocus(@Focus int focus) {
        this.mFocus = focus;
        mCameraImpl.setFocus(mFocus);
    }

    public void setMethod(@Method int method) {
        this.mMethod = method;
        mCameraImpl.setMethod(mMethod);
    }

    public void setZoom(@Zoom int zoom) {
        this.mZoom = zoom;
        mCameraImpl.setZoom(mZoom);
    }

    public void setJpegQuality(int jpegQuality) {
        this.mJpegQuality = jpegQuality;
    }

    public void setCropOutput(boolean cropOutput) {
        this.mCropOutput = cropOutput;
    }

    @Facing
    public int toggleFacing() {
        switch (mFacing) {
            case FACING_BACK:
                setFacing(FACING_FRONT);
                break;

            case FACING_FRONT:
                setFacing(FACING_BACK);
                break;
        }

        return mFacing;
    }

    @Flash
    public int toggleFlash() {
        switch (mFlash) {
            case FLASH_OFF:
                setFlash(FLASH_ON);
                break;

            case FLASH_ON:
                setFlash(FLASH_AUTO);
                break;

            case FLASH_AUTO:
                setFlash(FLASH_OFF);
                break;
        }

        return mFlash;
    }

    public void setCameraListener(CameraListener cameraListener) {
        this.mCameraListener.setCameraListener(cameraListener);
    }

    public void captureImage() {
        mCameraImpl.captureImage();
    }

    public void startRecordingVideo() {
        mCameraImpl.startVideo();
    }

    public void stopRecordingVideo() {
        mCameraImpl.endVideo();
    }

    public Size getPreviewSize() {
        return mCameraImpl != null ? mCameraImpl.getPreviewResolution() : null;
    }

    public Size getCaptureSize() {
        return mCameraImpl != null ? mCameraImpl.getCaptureResolution() : null;
    }

    private void requestCameraPermission() {
        Activity activity = null;
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                activity = (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }

        if (activity != null) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    CameraKit.Constants.PERMISSION_REQUEST_CAMERA);
        }
    }

    private class CameraListenerMiddleWare extends CameraListener {

        private CameraListener mCameraListener;

        @Override
        public void onCameraOpened() {
            super.onCameraOpened();
            getCameraListener().onCameraOpened();
        }

        @Override
        public void onCameraClosed() {
            super.onCameraClosed();
            getCameraListener().onCameraClosed();
        }

        @Override
        public void onPictureTaken(byte[] jpeg) {
            super.onPictureTaken(jpeg);
            if (mCropOutput) {
                int width = mMethod == METHOD_STANDARD ? mCameraImpl.getCaptureResolution().getWidth() : mCameraImpl.getPreviewResolution().getWidth();
                int height = mMethod == METHOD_STANDARD ? mCameraImpl.getCaptureResolution().getHeight() : mCameraImpl.getPreviewResolution().getHeight();
                AspectRatio outputRatio = AspectRatio.of(getWidth(), getHeight());
                getCameraListener().onPictureTaken(new CenterCrop(jpeg, outputRatio, mJpegQuality).getJpeg());
            } else {
                getCameraListener().onPictureTaken(jpeg);
            }
        }

        @Override
        public void onPictureTaken(YuvImage yuv) {
            super.onPictureTaken(yuv);
            if (mCropOutput) {
                AspectRatio outputRatio = AspectRatio.of(getWidth(), getHeight());
                getCameraListener().onPictureTaken(new CenterCrop(yuv, outputRatio, mJpegQuality).getJpeg());
            } else {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                yuv.compressToJpeg(new Rect(0, 0, yuv.getWidth(), yuv.getHeight()), mJpegQuality, out);
                getCameraListener().onPictureTaken(out.toByteArray());
            }
        }

        @Override
        public void onVideoTaken(File video) {
            super.onVideoTaken(video);
            getCameraListener().onVideoTaken(video);
        }

        public void setCameraListener(@Nullable CameraListener cameraListener) {
            this.mCameraListener = cameraListener;
        }

        @NonNull
        public CameraListener getCameraListener() {
            return mCameraListener != null ? mCameraListener : new CameraListener() {
            };
        }

    }

}
