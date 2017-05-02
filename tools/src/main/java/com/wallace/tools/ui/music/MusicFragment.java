package com.wallace.tools.ui.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wallace.tools.R;
import com.wallace.tools.bean.MusicInfo;
import com.wallace.tools.interfaces.OnItemClickListener;
import com.wallace.tools.service.MusicService;

import java.util.ArrayList;

/**
 * Package com.wallace.tools.ui.music
 * Created by Wallace.
 * on 2017/4/1.
 */

public class MusicFragment extends Fragment implements MusicContract.View,View.OnClickListener{


    private RecyclerView recyclerView;
    private TextView tvTitle,tvSinger;
    private ImageView ivToggle,ivNext;
    private LinearLayout lyBottom;
//    private TextView tvEmpty;
    private Toolbar toolbar;
    private MusicContract.Presenter presenter;

    private MusicAdapter mAdapter;
    private Intent intent;

    private OnMusicServiceListener mListener;

    public static MusicFragment newInstance() {
        return new MusicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_music, container, false);

        initViews(view);

        presenter.getData();
        return view;
    }


    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        toolbar = (Toolbar) view.findViewById(R.id.toolbarMusic);
        tvTitle = (TextView) view.findViewById(R.id.tvMusic_title);
        tvSinger = (TextView) view.findViewById(R.id.tvMusic_singer);
        ivToggle = (ImageView) view.findViewById(R.id.ivMusic_toggle);
        ivNext = (ImageView) view.findViewById(R.id.ivMusic_next);
        lyBottom = (LinearLayout) view.findViewById(R.id.lyMusic_bottom);
//        tvEmpty = (TextView) view.findViewById(R.id.tvMusic_empty);

        final MusicActivity activity = (MusicActivity) getActivity();

        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        ivToggle.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        lyBottom.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.toStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.toStop();
    }

    @Override
    public void setPresenter(MusicContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private String TAG = "MUSIC";
    @Override
    public void findList(ArrayList<MusicInfo> list) {
        if (mListener != null){
            mListener.bind(list);
        }
        mAdapter = new MusicAdapter(getActivity(),list);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: ");
                intent = new Intent(getActivity(), MusicService.class);
                intent.putExtra("index",position);
                intent.setFlags(position);
                intent.setAction(MusicService.ACTION_PLAY_START);
                getActivity().startService(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, "onItemLongClick: ");
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMusicServiceListener) {
            mListener = (OnMusicServiceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    @Override
    public void showError() {

    }

    @Override
    public Activity getAppCompatActivity(){
        return getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivMusic_toggle:
                intent = new Intent(getActivity(), MusicService.class);
//                intent.putExtra("index",mPosition);
//                intent.setFlags(mPosition);
                intent.setAction(MusicService.ACTION_PLAY_TOGGLE);
                getActivity().startService(intent);
                break;
            case R.id.ivMusic_next:
                intent = new Intent(getActivity(), MusicService.class);
//                intent.putExtra("index",mPosition);
//                intent.setFlags(mPosition);
                intent.setAction(MusicService.ACTION_PLAY_NEXT);
                getActivity().startService(intent);
                break;
            case R.id.lyMusic_bottom:
                mListener.addDetail();
                break;
        }
    }

    public void changeView(Intent intent){
        MusicInfo musicInfo = intent.getParcelableExtra("music");
        int index = intent.getIntExtra("index",0);
        tvTitle.setText(musicInfo.getTitle());
        tvSinger.setText(musicInfo.getArtist());


        switch (intent.getStringExtra("action")){
            case MusicService.ACTION_PLAY_START:
                ivToggle.setImageResource(R.drawable.ic_pause_black_24dp);
                if (mAdapter != null){
                    mAdapter.play(index,true);
                }
                break;
            case MusicService.ACTION_PLAY_PAUSE:
                ivToggle.setImageResource(R.drawable.ic_play_arrow_black_24dp);
//                        if (mAdapter != null){
//                            mAdapter.play(index,false);
//                        }
                break;
        }
    }

    interface OnMusicServiceListener{
        void bind(ArrayList<MusicInfo> list);
        void addDetail();
    }

}
