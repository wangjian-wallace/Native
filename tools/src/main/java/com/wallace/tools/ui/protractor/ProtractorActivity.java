package com.wallace.tools.ui.protractor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.wallace.tools.BaseActivity;
import com.wallace.tools.R;
import com.wallace.tools.camera.CameraView;


public class ProtractorActivity extends BaseActivity {

    private CameraView cameraView;
    private FrameLayout mContentView;
    private Toolbar toolbar;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraView = (CameraView) findViewById(R.id.protractor_camera);
        mContentView = (FrameLayout) findViewById(R.id.flProtractor);
        toolbar = (Toolbar) findViewById(R.id.toolbarProtractor);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_camera:

                        if (cameraView.getVisibility() == View.VISIBLE){
                            cameraView.setVisibility(View.GONE);
                            if (fixed != null){
                                fixed.setVisibility(View.GONE);
                            }
                        }else {
                            cameraView.setVisibility(View.VISIBLE);
                            if (fixed != null){
                                fixed.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case R.id.action_fixed:

//                        if (cameraView.getVisibility() == View.VISIBLE){
//                            cameraView.setVisibility(View.GONE);
//                        }else {
//                            cameraView.setVisibility(View.VISIBLE);
//                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
//        cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
        cameraView.setVisibility(View.GONE);
    }
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }
    View fixed;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.protractor, menu);
        fixed = findViewById(R.id.action_fixed);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(1000);
    }
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            toolbar.setVisibility(View.GONE);
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        mHideHandler.postDelayed(mHidePart2Runnable, 100);
    }
    @SuppressLint("InlinedApi")
    private void show() {
//        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;
        toolbar.setVisibility(View.VISIBLE);


        // Schedule a runnable to display UI elements after a delay
//        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, 5000);
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_protractor;
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
