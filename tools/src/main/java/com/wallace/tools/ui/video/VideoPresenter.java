package com.wallace.tools.ui.video;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.video
 * Created by Wallace.
 * on 2017/6/6.
 */

public class VideoPresenter implements VideoContract.Presenter {
    private VideoContract.VideoView view;
    private ArrayList<HashMap<String,String>> list;
    public VideoPresenter(VideoContract.VideoView view){
        this.view = view;
        view.setPresenter(this);
    }
    @Override
    public void getData() {
        list = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        view.toNext(list);
        map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        view.toNext(list);
        map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        view.toNext(list);
        map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        view.toNext(list);
        map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        view.toNext(list);
        map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        view.toNext(list);
        map = new HashMap<>();
        map.put("video","http://baobab.wdjcdn.com/14564977406580.mp4");
        list.add(map);
        view.toNext(list);
    }

    @Override
    public void toStart() {

    }

    @Override
    public void toStop() {

    }
}
