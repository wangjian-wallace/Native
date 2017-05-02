package com.wallace.tools.ui.music;

import android.media.MediaPlayer;
import android.util.Log;

import com.wallace.tools.service.MusicService;

import java.io.IOException;

/**
 * Package com.wallace.tools.ui.music
 * Created by Wallace.
 * on 2017/4/5.
 */

public class MusicProvider implements MediaPlayer.OnCompletionListener{

//    private Context mContext;
    private MusicService mService;
    private MediaPlayer mediaPlayer;

    private static final String TAG = "MusicProvider";
    public MusicProvider(MusicService mService){
//        this.mContext = mContext;
        this.mService = mService;
        this.mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
    }

    public void play(String name) throws IOException {
        Log.d(TAG, "play: " +name);
        mediaPlayer.reset();
        mediaPlayer.setDataSource(name);
        mediaPlayer.prepare();
        start();
    }
    public void next(String name){
        Log.d(TAG, "next: "+name);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(name);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        start();
    }
    public void release(){
        mediaPlayer.release();
    }
    public void start(){
        mediaPlayer.start();
    }
    public void stop(){
        mediaPlayer.stop();
//        release();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public int getSeek(){
        return mediaPlayer.getDuration();
    }

    public void seek(int seek) {
        int pro = reviseSeekValue(seek);
        int time = mediaPlayer.getDuration();
        int curTime = (int)((float)pro / 100 * time);
        mediaPlayer.seekTo(curTime);
    }
    public boolean isPlay(){
        return mediaPlayer.isPlaying();
    }

    private int reviseSeekValue(int progress) {
        if(progress < 0) {
            progress = 0;
        } else if(progress > 100) {
            progress = 100;
        }
        return progress;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion: ");
        mService.playNext();
    }
}
