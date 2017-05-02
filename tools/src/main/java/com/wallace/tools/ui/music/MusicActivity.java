package com.wallace.tools.ui.music;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wallace.tools.R;
import com.wallace.tools.bean.MusicInfo;
import com.wallace.tools.service.MusicService;
import com.wallace.tools.ui.music.detail.MusicDetailFragment;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.wallace.tools.service.MusicService.ACTION_PLAY_START;

public class MusicActivity extends AppCompatActivity implements MusicFragment.OnMusicServiceListener{

    private MusicFragment fragment;
    private MusicDetailFragment detailFragment;
    private MusicReceiver musicReceiver;
    private Intent intent;
    private boolean isBind = false;

    private int lastSelectedPosition = 0;
    private Fragment[] fragments;

    private MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);


        IntentFilter filter = new IntentFilter();
        filter.addAction("MusicReceiver");
        musicReceiver = new MusicReceiver();
        registerReceiver(musicReceiver, filter);

        locationAndContactsTask(savedInstanceState);
    }
    private static final int RC_LOCATION_CONTACTS_PERM = 124;

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask(Bundle savedInstanceState) {
        String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Have permissions, do the thing!
//            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
            if (savedInstanceState != null) {
                fragment = (MusicFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, "MusicFragment");
                detailFragment = (MusicDetailFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, "MusicDetailFragment");
            } else {
                fragment = MusicFragment.newInstance();
                detailFragment = MusicDetailFragment.newInstance();
            }
            fragments = new Fragment[]{fragment,detailFragment};

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();

            new MusicPresenter(fragment);
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(this, "需要一些权限",
                    RC_LOCATION_CONTACTS_PERM, perms);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "MusicFragment", fragment);
        getSupportFragmentManager().putFragment(outState, "MusicDetailFragment", detailFragment);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        fragment = null;
        detailFragment = null;
        if (musicReceiver!= null){
            unregisterReceiver(musicReceiver);
        }
        if (isBind){
            unbindService(connection);
        }
        super.onDestroy();
    }

    @Override
    public void bind(ArrayList<MusicInfo> list) {
        isBind = true;
        intent = new Intent(this, MusicService.class);
        intent.putParcelableArrayListExtra("name",list);
        intent.putExtra("index",0);
        intent.setFlags(0);
        bindService(intent,connection ,0);
    }

    @Override
    public void addDetail() {
        changeView(1);
        handler.postDelayed(runnable,1000);
    }
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (intent2 != null && lastSelectedPosition == 1){
                if (intent2.getStringExtra("action").equals(ACTION_PLAY_START)){
                    detailFragment.setDefault(intent2,true);
                }else {
                    detailFragment.setDefault(intent2,false);
                }
            }
        }
    };
    public void changeView(int position){
        if (lastSelectedPosition != position){
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[lastSelectedPosition]);
            if (!fragments[position].isAdded()) {
                trx.add(R.id.container, fragments[position]);
            }else {
                trx.show(fragments[position]);
            }
            trx.addToBackStack(null);
            trx.commit();
        }

        lastSelectedPosition = position;
    }
    private ServiceConnection connection = new ServiceConnection() {
        /**
         * 获取服务对象时的操作
         **/
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("MusicActivity", "onServiceConnected: 获取");
            musicService = ((MusicService.MyBinder)service).getService();
            musicService.setOnProgressListener(new MusicService.OnProgressListener() {
                @Override
                public void onProgress(int progress) {
                    if (detailFragment != null){
                        detailFragment.setProgressBar(progress);
                    }
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("MusicActivity", "onServiceDisconnected: 销毁");
        }
    };

    private class MusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                intent2 = intent;
                if (fragment != null){
                    fragment.changeView(intent);
                }
                if (detailFragment != null){
                    if (intent.getStringExtra("action").equals(ACTION_PLAY_START)){
                        detailFragment.setDefault(intent,true);
                    }else {
                        detailFragment.setDefault(intent,false);
                    }
                }
            }
        }
    }
    private Intent intent2;

}
