package com.wallace.tools.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.wallace.tools.R;
import com.wallace.tools.bean.MusicInfo;
import com.wallace.tools.ui.music.MusicActivity;
import com.wallace.tools.ui.music.MusicProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Package com.wallace.tools
 * Created by Wallace.
 * on 2017/4/1.
 */

public class MusicService extends Service{

    private static final String TAG = "MusicService";
    
//    public static final String MY_SERVICE_NAME = "com.wallace.music.MusicService";
    public static final String ACTION_STOP_SERVICE = "com.wallace.music.MusicService.stop";
    public static final String ACTION_PLAY_LAST = "com.wallace.music.MusicService.last";
    public static final String ACTION_PLAY_NEXT = "com.wallace.music.MusicService.next";
    public static final String ACTION_PLAY_TOGGLE = "com.wallace.music.MusicService.toggle";
    public static final String ACTION_PLAY_START = "com.wallace.music.MusicService.start";
    public static final String ACTION_PLAY_PAUSE = "com.wallace.music.MusicService.pause";
    public static final int NOTIFICATION_ID = 1201;
    public static final int NOTIFICATION_FLAG = 990929;

    private MusicProvider musicProvider;
    private MusicInfo musicInfo;

    private MyBinder mBinder = new MyBinder();
    /**
     * 播放列表
     */
    private ArrayList<MusicInfo> musicList;
    private int index = 0;

    private CompositeDisposable compositeDisposable;
    @Override
    public void onCreate() {
        super.onCreate();
        musicProvider = new MusicProvider(this);
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            Log.d(TAG, "onStartCommand: " + index);
//            Log.d(TAG, "onStartCommand:(flag) " + intent.getFlags());
            String action = intent.getAction();
            if (ACTION_PLAY_TOGGLE.equals(action)) {
                if (isPlaying()) {
                    pause();
                } else {
                    if (musicInfo != null){
                        start();
                    }else {
                        musicInfo = musicList.get(index);
                        play(musicInfo.getUrl());
                    }

                }
            } else if (ACTION_PLAY_START.equals(action)) {
                index = intent.getIntExtra("index",0);
                musicInfo = musicList.get(index);
                play(musicInfo.getUrl());
            } else if (ACTION_PLAY_NEXT.equals(action)) {
                playNext();
            } else if (ACTION_PLAY_LAST.equals(action)) {
                playLast();
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (isPlaying()) {
                    pause();
                }
                stop();
                release();
                stopForeground(true);
                stopSelf();
            }
        }
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        if (intent != null){
            index = intent.getIntExtra("index",0);
            musicList = intent.getParcelableArrayListExtra("name");
        }
        
        return mBinder;
    }

    public class MyBinder extends Binder {

        public MusicService getService(){
            return MusicService.this;
        }

    }

    private RemoteViews mContentViewBig, mContentViewSmall;

    private void play(String n){
        Log.d(TAG, "play: ");
        try {
            musicProvider.play(n);
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(ACTION_PLAY_START);
        showNotification();

        compositeDisposable.clear();
        threadSeek(musicInfo.getDuration());
    }
    public void stop() {

        Log.d(TAG, "stop: ");
        musicProvider.stop();
        send(ACTION_PLAY_PAUSE);
        showNotification();
        compositeDisposable.clear();
    }
    private void start(){
        Log.d(TAG, "start: ");
//        isPause = false;
        musicProvider.start();
        send(ACTION_PLAY_START);
        showNotification();
        compositeDisposable.clear();
        threadSeek(musicInfo.getDuration());
    }
    private void pause(){
        Log.d(TAG, "pause: ");

        musicProvider.pause();
        send(ACTION_PLAY_PAUSE);
        showNotification();
        compositeDisposable.clear();
    }
    public void playNext(){
        Log.d(TAG, "playNext: " + musicList.size());
        Log.d(TAG, "index: " + index);
        int i = index + 2;

        if (musicList.size() >= i){
            index ++;
            musicInfo = musicList.get(index);
            musicProvider.next(musicInfo.getUrl());

        }else {
            musicProvider.stop();
        }
        send(ACTION_PLAY_START);
        showNotification();

        compositeDisposable.clear();
        threadSeek(musicInfo.getDuration());
    }
    private void playLast(){
        Log.d(TAG, "playLast: ");
        if (index != 0){
            index --;
        }

        musicInfo = musicList.get(index);
        try {
            musicProvider.play(musicInfo.getUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(ACTION_PLAY_START);
        showNotification();

        compositeDisposable.clear();
        threadSeek(musicInfo.getDuration());
    }
    private void release(){
        musicProvider.release();
    }
    private void seek(int seek){
        musicProvider.seek(seek);
    }

    private void send(String action){
        if (musicInfo != null){
            Log.d(TAG, "send: ");
            Intent intent = new Intent("MusicReceiver");
            intent.putExtra("music",musicInfo);
            intent.putExtra("action",action);
            intent.putExtra("index",index);
            sendBroadcast(intent);
        }
    }
    @SuppressLint("WrongConstant")
    private void showNotification() {
        Intent intent = new Intent(this,MusicActivity.class);
        intent.putExtra("index",index);
        intent.setFlags(NOTIFICATION_FLAG);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, NOTIFICATION_FLAG);

        // Set the info for the views that show in the notification panel.
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .setCustomContentView(getSmallContentView())
                .setCustomBigContentView(getBigContentView())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .build();

        // Send the notification.
        startForeground(NOTIFICATION_ID, notification);
    }

    private RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }

    private RemoteViews getBigContentView() {
        if (mContentViewBig == null) {
            mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_music_player);
            setUpRemoteView(mContentViewBig);
        }
        updateRemoteViews(mContentViewBig);
        return mContentViewBig;
    }

    private void setUpRemoteView(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_close_black_24dp);
        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_skip_previous_black_24dp);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_skip_next_black_24dp);

        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_PLAY_LAST));
        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_PLAY_TOGGLE));
    }

    private void updateRemoteViews(RemoteViews remoteView) {
//        MusicInfo currentSong = new MusicInfo();
        if (musicInfo != null) {
            remoteView.setTextViewText(R.id.text_view_name, musicInfo.getTitle());
            remoteView.setTextViewText(R.id.text_view_artist, musicInfo.getArtist());

        }
//        remoteView.setProgressBar(R.id.progress_bars, 1000,10,false);
        remoteView.setImageViewResource(R.id.image_view_play_toggle, isPlaying()
                ? R.drawable.ic_pause_black_24dp : R.drawable.ic_play_arrow_black_24dp);
//        Bitmap album = AlbumUtils.parseAlbum(getPlayingSong());
//        if (album == null) {
            remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
//        } else {
//            remoteView.setImageViewBitmap(R.id.image_view_album, album);
//
//        }
    }
    private boolean isPlaying(){
        return musicProvider.isPlay();
    }

    @SuppressLint("WrongConstant")
    private PendingIntent getPendingIntent(String action) {
        Intent i = new Intent(action);
        i.setFlags(NOTIFICATION_FLAG);
        i.putExtra("index",index);
        return PendingIntent.getService(this, 0, i, NOTIFICATION_FLAG);
    }

    private void threadSeek(int duration) {
        final int d = duration / 1000;
        Disposable disposable = Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long a) throws Exception {
//                        Log.d(TAG, "apply: " + a);
                        return a;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Long>(){

                    @Override
                    public void onNext(Long o) {
                        Log.d(TAG, "onNext: " + o);

                        if (onProgressListener != null){
                            onProgressListener.onProgress(musicProvider.getMediaPlayer().getCurrentPosition());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }

                    //它会在 subscribe 刚开始，而事件还未发送之前被调用，
                    // 可以用于做一些准备工作，例如数据的清零或重置。
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }
                });
        compositeDisposable.add(disposable);
    }
    /**
     * 更新进度的回调接口
     */
    private OnProgressListener onProgressListener;


    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }
    public interface OnProgressListener{
        void onProgress(int progress);
    }
}
