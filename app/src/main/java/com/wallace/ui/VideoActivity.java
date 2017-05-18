package com.wallace.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;

import com.wallace.R;
import com.wallace.Settings;
import com.wallace.widget.media.AndroidMediaController;
import com.wallace.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoActivity extends AppCompatActivity {

    private Settings mSettings;
    private IjkVideoView mVideoView;
    private AndroidMediaController mMediaController;
    private TableLayout mHudView;
    private boolean mBackPressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mSettings = new Settings(this);
        mVideoView = (IjkVideoView) findViewById(R.id.videoview);
        mHudView = (TableLayout) findViewById(R.id.hud_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mMediaController = new AndroidMediaController(this, false);
        mMediaController.setSupportActionBar(actionBar);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);

        mVideoView.setVideoPath("http://qthttp.apple.com.edgesuite.net/1010qwoeiuryfg/sl.m3u8");

//        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(IMediaPlayer mp) {
//                mVideoView.start();
//            }
//        });
        mVideoView.start();
    }
    @Override
    public void onBackPressed() {
        mBackPressed = true;
        super.onBackPressed();
    }
    @Override
    protected void onStop() {
        super.onStop();
        //点击返回或不允许后台播放时 释放资源
        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }
}
