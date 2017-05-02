package com.wallace.tools.camera.type;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wallace.tools.camera.CameraKit.Constants.ZOOM_OFF;
import static com.wallace.tools.camera.CameraKit.Constants.ZOOM_PINCH;

@Retention(RetentionPolicy.SOURCE)
@IntDef({ZOOM_OFF, ZOOM_PINCH})
public @interface Zoom {
}
