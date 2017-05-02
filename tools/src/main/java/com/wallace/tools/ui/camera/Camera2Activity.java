package com.wallace.tools.ui.camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wallace.tools.R;
import com.wallace.tools.camera2.Camera2BasicFragment;

public class Camera2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }
}
