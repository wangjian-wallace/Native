package com.wallace.tools.camera.type;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wallace.tools.camera.CameraKit.Constants.FOCUS_CONTINUOUS;
import static com.wallace.tools.camera.CameraKit.Constants.FOCUS_OFF;
import static com.wallace.tools.camera.CameraKit.Constants.FOCUS_TAP;

@Retention(RetentionPolicy.SOURCE)
@IntDef({FOCUS_CONTINUOUS, FOCUS_TAP, FOCUS_OFF})
public @interface Focus {
}