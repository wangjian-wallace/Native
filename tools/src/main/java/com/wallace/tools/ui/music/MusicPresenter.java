package com.wallace.tools.ui.music;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.wallace.tools.bean.MusicInfo;
import com.wallace.tools.utils.FileUtils2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.os.Build.UNKNOWN;
import static com.wallace.tools.utils.FileUtils2.getFileLastModified;

/**
 * Package com.wallace.tools.ui.music
 * Created by Wallace.
 * on 2017/4/1.
 */

class MusicPresenter implements MusicContract.Presenter {

    private MusicContract.View view;

    MusicPresenter(MusicContract.View view){
        this.view = view;
        view.setPresenter(this);
    }
    @Override
    public void getData() {
        new Thread(runnable).start();
    }

    @Override
    public void toStart() {
//        handler.post(runnable);
    }

    @Override
    public void toStop() {
        handler.removeCallbacks(runnable2);
    }
    private ArrayList<MusicInfo> list;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            List<File> files = FileUtils2.listFilesInDir(Environment.getExternalStorageDirectory());
            List<File> files2 = FileUtils2.listFilesInDir("/storage/E213-1918");

            list = new ArrayList<>();
            Comparator<File> comparator = new Comparator<File>() {
                @Override
                public int compare(File object1, File object2) {// 实现接口中的方法
                    if (getFileLastModified(object1)>(getFileLastModified(object2))){
                        return -1;
                    }else if (getFileLastModified(object1)==(getFileLastModified(object2))){
                        return 0;
                    }else {
                        return 1;
                    }
                }
            };

            Collections.sort(files,comparator);
            MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

            try {
                for (File file : files){
                    if (isMusic(file)){
                        Log.d("File", "run: " + file.getAbsolutePath());
                        MusicInfo m = fileToMusic(metadataRetriever,file);
                        if (m.getSize() > 300000)list.add(m);
                    }

                }
                if (files2 != null && files2.size() > 0){

                    Collections.sort(files2,comparator);
                    for (File file : files2){
                        if(isMusic(file)){
                            MusicInfo m = fileToMusic(metadataRetriever,file);
                            if (m.getSize() > 300000)list.add(m);

                        }

                    }
                }
            }catch (RuntimeException r){
                r.printStackTrace();
            }finally {
                try {
                    metadataRetriever.release();
                }catch (RuntimeException r){
                    r.printStackTrace();
                }
            }



            handler.post(runnable2);

        }
    };
    private static MusicInfo fileToMusic(MediaMetadataRetriever metadataRetriever, File file) {
        if (file.length() == 0) return null;

        metadataRetriever.setDataSource(file.getAbsolutePath());

        final int duration;

        String keyDuration = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        // ensure the duration is a digit, otherwise return null song
        if (keyDuration == null || !keyDuration.matches("\\d+")) return null;
        duration = Integer.parseInt(keyDuration);

        final String title = extractMetadata(metadataRetriever, MediaMetadataRetriever.METADATA_KEY_TITLE, file.getName());
        final String displayName = extractMetadata(metadataRetriever, MediaMetadataRetriever.METADATA_KEY_TITLE, file.getName());
        final String artist = extractMetadata(metadataRetriever, MediaMetadataRetriever.METADATA_KEY_ARTIST, UNKNOWN);
        final String album = extractMetadata(metadataRetriever, MediaMetadataRetriever.METADATA_KEY_ALBUM, UNKNOWN);

        final MusicInfo song = new MusicInfo();
        song.setTitle(title);
        song.setDisplayName(displayName);
        song.setArtist(artist);
        song.setUrl(file.getAbsolutePath());
        song.setAlbum(album);
        song.setDuration(duration);
        song.setSize((int) file.length());
        return song;
    }
    private static String extractMetadata(MediaMetadataRetriever retriever, int key, String defaultValue) {
        String value = retriever.extractMetadata(key);
        if (TextUtils.isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }
    private static boolean isMusic(File file) {
        final String REGEX = "(.*/)*.+\\.(mp3|m4a|ogg|wav|aac|flac)$";
        return file.getName().matches(REGEX);
    }
    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            view.findList(list);
        }
    };
    private Handler handler = new Handler();

}
