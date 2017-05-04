package com.wallace.tools.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable {
    private long id;
    private String title;
    private String album;
    private int duration;
    private long size;
    private String artist;
    private String url;
    private String displayName;

    public MusicInfo() {
    }

    public MusicInfo(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    protected MusicInfo(Parcel in) {
        id = in.readLong();
        title = in.readString();
        album = in.readString();
        duration = in.readInt();
        size = in.readLong();
        artist = in.readString();
        url = in.readString();
        displayName = in.readString();
    }

    public static final Creator<MusicInfo> CREATOR = new Creator<MusicInfo>() {
        @Override
        public MusicInfo createFromParcel(Parcel in) {
            return new MusicInfo(in);
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeInt(duration);
        dest.writeLong(size);
        dest.writeString(artist);
        dest.writeString(url);
        dest.writeString(displayName);
    }
}