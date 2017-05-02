package com.wallace.tools.camera.api21;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Log;

import com.wallace.tools.camera.CameraImpl;
import com.wallace.tools.camera.CameraListener;
import com.wallace.tools.camera.PreviewImpl;
import com.wallace.tools.camera.api16.ConstantMapper;
import com.wallace.tools.camera.type.Facing;
import com.wallace.tools.camera.type.Flash;
import com.wallace.tools.camera.type.Focus;
import com.wallace.tools.camera.type.Method;
import com.wallace.tools.camera.type.Zoom;
import com.wallace.tools.camera.utils.AspectRatio;
import com.wallace.tools.camera.utils.CommonAspectRatioFilter;
import com.wallace.tools.camera.utils.Size;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

@TargetApi(21)
public class Camera2 extends CameraImpl {

    private CameraDevice mCamera;
    private CameraCharacteristics mCameraCharacteristics;
    private CameraManager mCameraManager;

    private String mCameraId;
    private int mFacing;

    private Size mCaptureSize;
    private Size mPreviewSize;

    public Camera2(CameraListener callback, PreviewImpl preview, Context context) {
        super(callback, preview);
        preview.setCallback(new PreviewImpl.Callback() {
            @Override
            public void onSurfaceChanged() {

            }
        });

        mCameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }

    // CameraImpl:

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setDisplayOrientation(int displayOrientation) {

    }

    @Override
    public void setFacing(@Facing int facing) {
        int internalFacing = new ConstantMapper.Facing(facing).map();
        if (internalFacing == -1) {
            return;
        }

        final String[] ids;
        try {
            ids = mCameraManager.getCameraIdList();
        } catch (CameraAccessException e) {
            Log.e("CameraKit", e.toString());
            return;
        }

        if (ids.length == 0) {
            throw new RuntimeException("No camera available.");
        }
//
//        for (String id : ids) {
//            CameraCharacteristics cameraCharacteristics = mCameraManager.getCameraCharacteristics(id);
//            Integer level = cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
//            if (level == null || level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
//                continue;
//            }
//            Integer internal = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
//            if (internal == null) {
//                throw new NullPointerException("Unexpected state: LENS_FACING null");
//            }
//            if (internal == internalFacing) {
//                mCameraId = id;
//                mCameraCharacteristics = cameraCharacteristics;
//                return true;
//            }
//        }

        if (mFacing == facing && isCameraOpened()) {
            stop();
            start();
        }
    }

    @Override
    public void setFlash(@Flash int flash) {

    }

    @Override
    public void setFocus(@Focus int focus) {

    }

    @Override
    public void setMethod(@Method int method) {

    }

    @Override
    public void setZoom(@Zoom int zoom) {

    }

    @Override
    public void captureImage() {

    }

    @Override
    public void startVideo() {

    }

    @Override
    public void endVideo() {

    }

    @Override
    public Size getCaptureResolution() {
        if (mCaptureSize == null && mCameraCharacteristics != null) {
            TreeSet<Size> sizes = new TreeSet<>();
            sizes.addAll(getAvailableCaptureResolutions());

            TreeSet<AspectRatio> aspectRatios = new CommonAspectRatioFilter(
                    getAvailablePreviewResolutions(),
                    getAvailableCaptureResolutions()
            ).filter();
            AspectRatio targetRatio = aspectRatios.size() > 0 ? aspectRatios.last() : null;

            Iterator<Size> descendingSizes = sizes.descendingIterator();
            Size size;
            while (descendingSizes.hasNext() && mCaptureSize == null) {
                size = descendingSizes.next();
                if (targetRatio == null || targetRatio.matches(size)) {
                    mCaptureSize = size;
                    break;
                }
            }
        }

        return mCaptureSize;
    }

    @Override
    public Size getPreviewResolution() {
        if (mPreviewSize == null && mCameraCharacteristics != null) {
            TreeSet<Size> sizes = new TreeSet<>();
            sizes.addAll(getAvailablePreviewResolutions());

            TreeSet<AspectRatio> aspectRatios = new CommonAspectRatioFilter(
                    getAvailablePreviewResolutions(),
                    getAvailableCaptureResolutions()
            ).filter();
            AspectRatio targetRatio = aspectRatios.size() > 0 ? aspectRatios.last() : null;

            Iterator<Size> descendingSizes = sizes.descendingIterator();
            Size size;
            while (descendingSizes.hasNext() && mPreviewSize == null) {
                size = descendingSizes.next();
                if (targetRatio == null || targetRatio.matches(size)) {
                    mPreviewSize = size;
                    break;
                }
            }
        }

        return mPreviewSize;
    }

    @Override
    public boolean isCameraOpened() {
        return mCamera != null;
    }

    // Internal

    private List<Size> getAvailableCaptureResolutions() {
        List<Size> output = new ArrayList<>();

        if (mCameraCharacteristics != null) {
            StreamConfigurationMap map = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                throw new IllegalStateException("Failed to get configuration map: " + mCameraId);
            }

            for (android.util.Size size : map.getOutputSizes(ImageFormat.JPEG)) {
                output.add(new Size(size.getWidth(), size.getHeight()));
            }
        }

        return output;
    }

    private List<Size> getAvailablePreviewResolutions() {
        List<Size> output = new ArrayList<>();

        if (mCameraCharacteristics != null) {
            StreamConfigurationMap map = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (map == null) {
                throw new IllegalStateException("Failed to get configuration map: " + mCameraId);
            }

            for (android.util.Size size : map.getOutputSizes(mPreview.getOutputClass())) {
                output.add(new Size(size.getWidth(), size.getHeight()));
            }
        }

        return output;
    }

}
