package com.wallace.tools.ui.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.wallace.tools.R;
import com.wallace.tools.listener.SampleListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.video
 * Created by Wallace.
 * on 2017/6/9.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{
    private Context mContext;
    private ArrayList<HashMap<String,String>> list;
    private boolean isFullVideo;
    public void setList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
    }

    public VideoAdapter(Context context, ArrayList<HashMap<String,String>> list){
        this.mContext = context;
        this.list = list;
    }

    @Override
    public VideoAdapter.VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VideoAdapter.VideoHolder(LayoutInflater.from(mContext).inflate(R.layout.simple_video_items,parent,false));
    }

    @Override
    public void onBindViewHolder(final VideoAdapter.VideoHolder holder, int position) {
        HashMap<String,String> map = list.get(position);

        final String url = map.get("video");
        //final String url = "http://7xse1z.com1.z0.glb.clouddn.com/1491813192";
        //final String url = "http://111.198.24.133:83/yyy_login_server/pic/YB059284/97778276040859/1.mp4";

        //默认缓存路径
        holder.gsyVideoPlayer.setUp(url, true , null, "这是title");

        //holder.gsyVideoPlayer.setNeedShowWifiTip(false);

        /************************下方为其他路径************************************/
        //如果一个列表的缓存路劲都一一致
        //holder.gsyVideoPlayer.setUp(url, true, new File(FileUtils.getTestPath(), ""));

        /************************下方为其他路径************************************/
        //如果一个列表里的缓存路劲不一致
        //int playPosition = GSYVideoManager.instance().getPlayPosition();
        //避免全屏返回的时候不可用了
        /*if (playPosition < 0 || playPosition != position ||
                !GSYVideoManager.instance().getPlayTag().equals(ListNormalAdapter.TAG)) {
            holder.gsyVideoPlayer.initUIState();
        }*/
        //如果设置了点击封面可以播放，如果缓存列表路径不一致，还需要设置封面点击
        /*holder.gsyVideoPlayer.setThumbPlay(true);
        holder.gsyVideoPlayer.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要切换缓存路径的
                holder.gsyVideoPlayer.setUp(url, true, new File(FileUtils.getTestPath(), ""));
                holder.gsyVideoPlayer.startPlayLogic();
            }
        });
        holder.gsyVideoPlayer.getThumbImageViewLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要切换缓存路径的
                holder.gsyVideoPlayer.setUp(url, true, new File(FileUtils.getTestPath(), ""));
                holder.gsyVideoPlayer.startPlayLogic();
            }
        });*/

        //增加title
        holder.gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        holder.gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        holder.gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(holder.gsyVideoPlayer);
            }
        });
        holder.gsyVideoPlayer.setRotateViewAuto(true);
        holder.gsyVideoPlayer.setLockLand(true);
        holder.gsyVideoPlayer.setPlayTag("VideoAdapter");
        holder.gsyVideoPlayer.setShowFullAnimation(true);
        //循环
        //holder.gsyVideoPlayer.setLooping(true);
        holder.gsyVideoPlayer.setNeedLockFull(true);

        //holder.gsyVideoPlayer.setSpeed(2);

        holder.gsyVideoPlayer.setPlayPosition(position);

        holder.gsyVideoPlayer.setStandardVideoAllCallBack(new SampleListener(){
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("onPrepared");
                if (!holder.gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setNeedMute(true);
                }

            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
            }
        });
    }
    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(mContext, false, true);
        isFullVideo = true;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder{
        StandardGSYVideoPlayer gsyVideoPlayer;
        VideoHolder(View itemView) {
            super(itemView);
            gsyVideoPlayer = (StandardGSYVideoPlayer) itemView.findViewById(R.id.video_item_player);
        }
    }
}
