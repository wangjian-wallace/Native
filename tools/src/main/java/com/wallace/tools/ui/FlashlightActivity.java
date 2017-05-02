package com.wallace.tools.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wallace.tools.BaseActivity;
import com.wallace.tools.R;


public class FlashlightActivity extends BaseActivity {

    private Toolbar toolbar;
    private CameraManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
            String [] camerList = manager.getCameraIdList();

            for (String str:camerList) {
                Log.d("List",str);
            }
        } catch (CameraAccessException e) {
            Log.e("error",e.getMessage());
        }
        }
        init();
        pre();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbarFlashlight);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void pre(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(FlashlightActivity.this,
                    Manifest.permission.CAMERA) ){
                Snackbar.make(toolbar, "需要获得相机权限",
                        Snackbar.LENGTH_INDEFINITE).setAction("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            ActivityCompat.requestPermissions(FlashlightActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    1);
                    }
                }).show();
            }else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            1);
            }
        }
    }
    private boolean isFlashlight = true;
    private Camera m_Camera;
    public void onFlashlight(View view){

        if (isFlashlight){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    manager.setTorchMode("0", true);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }else {
                m_Camera = Camera.open();
                Camera.Parameters mParameters;
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                m_Camera.setParameters(mParameters);
            }
            isFlashlight = false;
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    manager.setTorchMode("0", false);

                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }else {
                Camera.Parameters mParameters;
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                m_Camera.setParameters(mParameters);
                m_Camera.release();
            }

            isFlashlight = true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                manager.setTorchMode("0", false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_flashlight;
    }
}
