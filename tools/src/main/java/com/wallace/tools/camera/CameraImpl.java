package com.wallace.tools.camera;


import com.wallace.tools.camera.type.Facing;
import com.wallace.tools.camera.type.Flash;
import com.wallace.tools.camera.type.Focus;
import com.wallace.tools.camera.type.Method;
import com.wallace.tools.camera.type.Zoom;
import com.wallace.tools.camera.utils.Size;

public abstract class CameraImpl {

    protected final CameraListener mCameraListener;
    protected final PreviewImpl mPreview;

    public CameraImpl(CameraListener callback, PreviewImpl preview) {
        mCameraListener = callback;
        mPreview = preview;
    }

    public abstract void start();
    public abstract void stop();

    public abstract void setDisplayOrientation(int displayOrientation);

    public abstract void setFacing(@Facing int facing);
    public abstract void setFlash(@Flash int flash);
    public abstract void setFocus(@Focus int focus);
    public abstract void setMethod(@Method int method);
    public abstract void setZoom(@Zoom int zoom);

    public abstract void captureImage();
    public abstract void startVideo();
    public abstract void endVideo();

    public abstract Size getCaptureResolution();
    public abstract Size getPreviewResolution();
    public abstract boolean isCameraOpened();

}