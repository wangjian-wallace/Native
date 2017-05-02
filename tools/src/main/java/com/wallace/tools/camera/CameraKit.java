package com.wallace.tools.camera;

import android.content.res.Resources;

public class CameraKit {

    public static class Internal {

        public static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        public static final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    }

    public static class Constants {

        public static final int PERMISSION_REQUEST_CAMERA = 16;

        public static final int FACING_BACK = 0;
        public static final int FACING_FRONT = 1;

        public static final int FLASH_OFF = 0;
        public static final int FLASH_ON = 1;
        public static final int FLASH_AUTO = 2;

        public static final int METHOD_STANDARD = 0;
        public static final int METHOD_STILL = 1;
        public static final int METHOD_SPEED = 2;

        public static final int FOCUS_OFF = 0;
        public static final int FOCUS_CONTINUOUS = 1;
        public static final int FOCUS_TAP = 2;

        public static final int ZOOM_OFF = 0;
        public static final int ZOOM_PINCH = 1;

    }

    static class Defaults {

        static final int DEFAULT_FACING = Constants.FACING_BACK;
        static final int DEFAULT_FLASH = Constants.FLASH_OFF;
        static final int DEFAULT_FOCUS = Constants.FOCUS_OFF;
        static final int DEFAULT_METHOD = Constants.METHOD_STANDARD;
        static final int DEFAULT_ZOOM = Constants.ZOOM_OFF;

        static final int DEFAULT_JPEG_QUALITY = 100;
        static final boolean DEFAULT_CROP_OUTPUT = false;
        static final boolean DEFAULT_ADJUST_VIEW_BOUNDS = false;

    }

}
