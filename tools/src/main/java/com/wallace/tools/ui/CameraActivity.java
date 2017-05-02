package com.wallace.tools.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wallace.tools.BaseActivity;
import com.wallace.tools.R;
import com.wallace.tools.camera.CameraView;


public class CameraActivity extends BaseActivity {

    private CameraView camera;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        camera = (CameraView) findViewById(R.id.camera);
        toolbar = (Toolbar) findViewById(R.id.toolbarCamera);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
    }

    public void onFlash(View view){
//        camera.setFlash(CameraKit.Constants.FLASH_ON);
        camera.toggleFlash();
    }
    public void onFacing(View view){
        camera.toggleFacing();
//        camera.start();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_camera;
    }
}
