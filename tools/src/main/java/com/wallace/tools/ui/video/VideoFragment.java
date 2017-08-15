package com.wallace.tools.ui.video;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.wallace.tools.R;
import com.wallace.tools.listener.SampleListener;

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
    private int lastVisibleItem;
    private int firstVisibleItem;
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = manager.findFirstVisibleItemPosition();
                lastVisibleItem = manager.findLastVisibleItemPosition();
                Debuger.printfLog("firstVisibleItem " + firstVisibleItem +" lastVisibleItem " + lastVisibleItem);
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals("VideoAdapter")) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!listVideoUtil.isSmall() && !listVideoUtil.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(getActivity(), 150);
                            //actionbar为true才不会掉下面去
                            listVideoUtil.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (listVideoUtil.isSmall()) {
                            listVideoUtil.smallVideoToNormal();
                        }
                    }
                }
            }
        });
        //小窗口关闭被点击的时候回调处理回复页面
        listVideoUtil.setVideoAllCallBack(new SampleListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("Duration " + listVideoUtil.getDuration() + " CurrentPosition " + listVideoUtil.getCurrentPositionWhenPlaying());
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                super.onQuitSmallWidget(url, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals("VideoAdapter")) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        listVideoUtil.releaseVideoPlayer();
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
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
