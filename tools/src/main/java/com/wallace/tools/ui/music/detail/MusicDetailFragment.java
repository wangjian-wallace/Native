package com.wallace.tools.ui.music.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wallace.tools.R;
import com.wallace.tools.bean.MusicInfo;
import com.wallace.tools.ui.music.MusicActivity;
import com.wallace.tools.utils.TimeUtils;
import com.wallace.tools.view.ENPlayView;


public class MusicDetailFragment extends Fragment implements View.OnClickListener {

    //    private MusicDetailContract.Presenter presenter;

    private static final String TAG = "MusicDetailFragment";
    private static final String ARG_INFO = "MUSIC_INFO";
    private static final String ARG_PLAY = "MUSIC_PLAY";

    private Toolbar toolbar;
    private ImageView ivLast, ivNext;
    private ENPlayView ivToggle;
    private TextView tvTime, tvDuration;
    private ProgressBar progressBar;

    private MusicInfo musicInfo;

    private boolean isPlay = false;


    public static MusicDetailFragment newInstance() {
        return new MusicDetailFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_detail, container, false);

        initViews(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    public void initViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbarMusicDetail);
        ivLast = (ImageView) view.findViewById(R.id.ivMusic_last);
        ivNext = (ImageView) view.findViewById(R.id.ivMusic_next);
        ivToggle = (ENPlayView) view.findViewById(R.id.ivMusic_toggle);
        tvTime = (TextView) view.findViewById(R.id.tvMusic_time);
        tvDuration = (TextView) view.findViewById(R.id.tvMusic_duration);
        progressBar = (ProgressBar) view.findViewById(R.id.pbMusic);
        final MusicActivity activity = (MusicActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeView(0);
            }
        });


        progressBar.setProgress(50);
        progressBar.setMax(100);


        ivToggle.setOnClickListener(this);
        ivLast.setOnClickListener(this);
        ivNext.setOnClickListener(this);

    }
    public void setDefault(Intent intent,boolean isPlay){
        this.isPlay = isPlay;
        this.musicInfo = intent.getParcelableExtra("music");
        if (toolbar != null){
            if (isPlay) {
                ivToggle.play();
            } else {
                ivToggle.pause();
            }
            toolbar.setTitle(musicInfo.getTitle());
            String duration = TimeUtils.millis2String(musicInfo.getDuration(),"mm:ss");
            tvDuration.setText(duration);
        }

    }

    public void setProgressBar(int progress){
        if (tvTime != null){
//            long p = progress * 1000;
            String duration = TimeUtils.millis2String(progress,"mm:ss");
            tvTime.setText(duration);
//            int curTime = progress / musicInfo.getDuration();
            int curTime2 = progress * 100 / musicInfo.getDuration();
            Log.d(TAG, "setProgressBar2: "+curTime2);
            progressBar.setProgress(curTime2);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivMusic_last:

                break;
            case R.id.ivMusic_toggle:
                if (isPlay) {
                    ivToggle.pause();
                    isPlay = false;
                } else {
                    ivToggle.play();
                    isPlay = true;
                }

                break;
            case R.id.ivMusic_next:

                break;
        }
    }
}
