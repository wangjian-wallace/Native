package com.wallace.tools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Package com.wallace
 * Created by Wallace.
 * on 2017/3/13.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected void handleIntent(Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        if (null != getIntent()) {
            handleIntent(getIntent());
        }

        ActivityManager.getInstance().addActivity(this);
    }
    protected abstract int getContentViewId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }
}
