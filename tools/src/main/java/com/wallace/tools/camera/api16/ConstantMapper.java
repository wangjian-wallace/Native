package com.wallace.tools.camera.api16;

import android.annotation.TargetApi;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.support.v4.util.SparseArrayCompat;
import android.util.SparseIntArray;

import com.wallace.tools.camera.CameraKit;


public class ConstantMapper {

    private abstract static class BaseMapper<T> {

        protected int mCameraKitConstant;

        protected BaseMapper(int cameraKitConstant) {
            this.mCameraKitConstant = cameraKitConstant;
        }

        abstract T map();

    }

    public static class Flash extends BaseMapper<String> {

        private static final SparseArrayCompat<String> FLASH_MODES = new SparseArrayCompat<>();

        static {
            FLASH_MODES.put(CameraKit.Constants.FLASH_OFF, Camera.Parameters.FLASH_MODE_OFF);
            FLASH_MODES.put(CameraKit.Constants.FLASH_ON, Camera.Parameters.FLASH_MODE_ON);
            FLASH_MODES.put(CameraKit.Constants.FLASH_AUTO, Camera.Parameters.FLASH_MODE_AUTO);
        }

        protected Flash(int cameraKitConstant) {
            super(cameraKitConstant);
        }

        @Override
        public String map() {
            return FLASH_MODES.get(mCameraKitConstant, FLASH_MODES.get(CameraKit.Constants.FLASH_OFF));
        }

    }

    @TargetApi(21)
    public static class Flash2 extends BaseMapper<String> {

        protected Flash2(int cameraKitConstant) {
            super(cameraKitConstant);
        }

        @Override
        String map() {
            return null;
        }

    }

    public static class Facing extends BaseMapper<Integer> {

        private static final SparseArrayCompat<Integer> FACING_MODES = new SparseArrayCompat<>();

        static {
            FACING_MODES.put(CameraKit.Constants.FACING_BACK, Camera.CameraInfo.CAMERA_FACING_BACK);
            FACING_MODES.put(CameraKit.Constants.FACING_FRONT, Camera.CameraInfo.CAMERA_FACING_FRONT);
        }

        public  Facing(int cameraKitConstant) {
            super(cameraKitConstant);
        }

        @Override
        public Integer map() {
            return FACING_MODES.get(mCameraKitConstant, FACING_MODES.get(CameraKit.Constants.FACING_BACK));
        }

    }

    @TargetApi(21)
    static class Facing2 extends BaseMapper<Integer> {

        private static final SparseIntArray FACING_MODES = new SparseIntArray();

        static {
            FACING_MODES.put(CameraKit.Constants.FACING_BACK, CameraCharacteristics.LENS_FACING_BACK);
            FACING_MODES.put(CameraKit.Constants.FACING_FRONT, CameraCharacteristics.LENS_FACING_FRONT);
        }

        protected Facing2(int cameraKitConstant) {
            super(cameraKitConstant);
        }

        @Override
        Integer map() {
            return FACING_MODES.get(mCameraKitConstant, FACING_MODES.get(CameraKit.Constants.FACING_BACK));
        }

    }


}
