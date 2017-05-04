package com.wallace.tools.ui.camera;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wallace.tools.R;

/**
 * 图像处理
 */
public class ImageActivity extends AppCompatActivity {


    private ImageFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (savedInstanceState != null) {
            fragment = (ImageFragment) getSupportFragmentManager().getFragment(savedInstanceState, "ImageFragment");
        } else {
            fragment = ImageFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        new ImagePresenter(fragment);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "ImageFragment", fragment);
    }
}
