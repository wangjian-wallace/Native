package com.wallace.tools.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.wallace.tools.bean.MusicInfo;

import java.util.ArrayList;

public class MusicLoader {
      
    private static final String TAG = "MusicLoader";
      
    private static ArrayList<MusicInfo> musicList = new ArrayList<>();
      
    private static MusicLoader musicLoader;  
      
    private static ContentResolver contentResolver;
    //Uri，指向external的database  
    private Uri contentUri = Media.EXTERNAL_CONTENT_URI;
    //projection：选择的列; where：过滤条件; sortOrder：排序。  
    private String[] projection = {  
            Media._ID,  
            Media.DISPLAY_NAME,  
            Media.DATA,  
            Media.ALBUM,  
            Media.ARTIST,  
            Media.DURATION,           
            Media.SIZE,
            Media.TITLE
    };
    private String where =  "mime_type in ('audio/mpeg','audio/x-ms-wma') and bucket_display_name <> 'audio' and is_music > 0 " ;  
    private String sortOrder = Media.DATA;  
      
    public static MusicLoader instance(ContentResolver pContentResolver){  
        if(musicLoader == null){  
            contentResolver = pContentResolver;  
            musicLoader = new MusicLoader();              
        }  
        return musicLoader;  
    }  
      
    private MusicLoader(){                                                                                                             //利用ContentResolver的query函数来查询数据，然后将得到的结果放到MusicInfo对象中，最后放到数组中  
        Cursor cursor = contentResolver.query(contentUri, projection, where, null, sortOrder);
        if(cursor == null){
            Log.d(TAG, "Line(37  )   Music Loader cursor == null.");
        }else if(!cursor.moveToFirst()){  
            Log.d(TAG,"Line(39  )   Music Loader cursor.moveToFirst() returns false.");
        }else{            
            int displayNameCol = cursor.getColumnIndex(Media.DISPLAY_NAME);  
            int albumCol = cursor.getColumnIndex(Media.ALBUM);  
            int idCol = cursor.getColumnIndex(Media._ID);  
            int durationCol = cursor.getColumnIndex(Media.DURATION);  
            int sizeCol = cursor.getColumnIndex(Media.SIZE);  
            int artistCol = cursor.getColumnIndex(Media.ARTIST);  
            int urlCol = cursor.getColumnIndex(Media.DATA);           
            int ti = cursor.getColumnIndex(Media.TITLE);
            do{
                String title = cursor.getString(ti);
                String d = cursor.getString(displayNameCol);
                String album = cursor.getString(albumCol);
                long id = cursor.getLong(idCol);                  
                int duration = cursor.getInt(durationCol);  
                long size = cursor.getLong(sizeCol);  
                String artist = cursor.getString(artistCol);  
                String url = cursor.getString(urlCol);  
                  
                MusicInfo musicInfo = new MusicInfo(id, title);
                musicInfo.setAlbum(album);
                musicInfo.setDuration(duration);
                musicInfo.setSize(size);
                musicInfo.setArtist(artist);
                musicInfo.setUrl(url);
                musicInfo.setDisplayName(d);
                musicList.add(musicInfo);
                Log.d(TAG, "MusicLoader: "+ musicInfo.toString());
                  
            }while(cursor.moveToNext());  
        }
        cursor.close();
    }  
      
    public ArrayList<MusicInfo> getMusicList(){
        return musicList;  
    }  
      
    public Uri getMusicUriById(long id){  
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        return uri;  
    }     

} 