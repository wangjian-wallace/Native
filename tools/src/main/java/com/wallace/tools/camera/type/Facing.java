package com.wallace.tools.camera.type;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.wallace.tools.camera.CameraKit.Constants.FACING_BACK;
import static com.wallace.tools.camera.CameraKit.Constants.FACING_FRONT;

@IntDef({FACING_BACK, FACING_FRONT})
@Retention(RetentionPolicy.SOURCE)
public @interface Facing {
}