package com.wallace.tools.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.wallace.tools.BaseActivity;
import com.wallace.tools.R;
import com.wallace.tools.adater.MainAdapter;
import com.wallace.tools.bean.Main;
import com.wallace.tools.ui.camera.Camera2Activity;
import com.wallace.tools.ui.camera.ImageActivity;
import com.wallace.tools.ui.card.CardActivity;
import com.wallace.tools.ui.example.ExampleActivity;
import com.wallace.tools.ui.music.MusicActivity;
import com.wallace.tools.ui.transition.Transition1Activity;
import com.wallace.tools.ui.video.VideoActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private RecyclerView rvList;
    private Toolbar toolbar;
    private List<Main> list;
    private MainAdapter mAdapter;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        rvList = (RecyclerView) findViewById(R.id.rvMain);
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);

        rvList.setLayoutManager(new GridLayoutManager(this,2));

        setSupportActionBar(toolbar);

        getData();
        mAdapter = new MainAdapter(this,list);
        rvList.setAdapter(mAdapter);
        mAdapter.setOnMainItemClickListener(new MainAdapter.OnMainItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position){
                    case 0:
                        intent = new Intent(MainActivity.this, VideoActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, RulerActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, Transition1Activity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, Camera2Activity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, MusicActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, ExampleActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(MainActivity.this, ImageActivity.class);
                        startActivity(intent);
                        break;
                    case 7:
                        intent = new Intent(MainActivity.this, CardActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });


    }

    private void getData(){
        list = new ArrayList<>();
        Main main = new Main();
        main.setTitle("手电筒");
        main.setImage(R.drawable.ic_highlight_black_24dp);
        list.add(main);
        main = new Main();
        main.setTitle("测量尺");
        main.setImage(R.drawable.ic_toys_black_24dp);
        list.add(main);
        main = new Main();
        main.setTitle("随机事件");
        main.setImage(R.drawable.ic_casino_black_24dp);
        list.add(main);
        main = new Main();
        main.setTitle("照妖镜");
        main.setImage(R.drawable.ic_camera_enhance_black_24dp);
        list.add(main);

        main = new Main();
        main.setTitle("音乐示例");
        main.setImage(R.drawable.ic_music_note_black_24dp);
        list.add(main);
        main = new Main();
        main.setTitle("example");
        main.setImage(R.drawable.ic_description_black_24dp);
        list.add(main);
        main = new Main();
        main.setTitle("example2");
        main.setImage(R.drawable.ic_android_black_24dp);
        list.add(main);
        main = new Main();
        main.setTitle("card");
        main.setImage(R.drawable.ic_credit_card_black_24dp);
        list.add(main);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }
}
