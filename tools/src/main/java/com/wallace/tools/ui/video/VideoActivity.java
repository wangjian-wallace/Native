package com.wallace.tools.ui.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.wallace.tools.R;

public class VideoActivity extends AppCompatActivity {
    private VideoFragment fragment;
    private String TAG = "VideoActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (savedInstanceState != null) {
            fragment = (VideoFragment) getSupportFragmentManager().getFragment(savedInstanceState, "VideoFragment");
        } else {
            fragment = VideoFragment.newInstance();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        new VideoPresenter(fragment);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "VideoFragment", fragment);
    }
    @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }
}
