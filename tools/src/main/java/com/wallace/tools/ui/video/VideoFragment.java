package com.wallace.tools.ui.video;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.wallace.tools.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.video
 * Created by Wallace.
 * on 2017/6/6.
 */

public class VideoFragment extends Fragment implements VideoContract.VideoView {

    private static final String TAG = "VideoFragment";

    private VideoContract.Presenter presenter;
    //    private SimpleExoPlayerView simpleExoPlayerView;
    private RecyclerView recyclerView;
    private VideoAdapter mAdapter;
    private FrameLayout videoFullContainer;

//    private ArrayList<HashMap<String,String>> list;
    private ListVideoUtil listVideoUtil;
    private LinearLayoutManager manager;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        initViews(view);
        return view;
    }


    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvVideo);
        videoFullContainer = (FrameLayout) view.findViewById(R.id.video_full_container);

        manager = new LinearLayoutManager(getActivity());
//        manager.setSmoothScrollbarEnabled(true);
//        manager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(manager);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);
        listVideoUtil = new ListVideoUtil(getActivity());
        listVideoUtil.setFullViewContainer(videoFullContainer);
        listVideoUtil.setHideStatusBar(true);
        listVideoUtil.setHideActionBar(true);
//        list = new ArrayList<>();

        presenter.getData();
    }


    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void toNext(ArrayList<HashMap<String, String>> text) {
        mAdapter = new VideoAdapter(getActivity(),text);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void toComplete() {

    }

    @Override
    public void showError() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        listVideoUtil.releaseVideoPlayer();
        GSYVideoPlayer.releaseAllVideos();
    }

}
