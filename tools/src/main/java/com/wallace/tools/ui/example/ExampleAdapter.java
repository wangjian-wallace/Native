package com.wallace.tools.ui.example;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.wallace.tools.GlideApp;
import com.wallace.tools.R;
import com.wallace.tools.listener.SampleListener;
import com.wallace.tools.ui.video.SampleCoverVideo;
import com.wallace.tools.view.round.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.example
 * Created by Wallace.
 * on 2017/4/28.
 */

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleHolder>{

    private Context mContext;
    private ArrayList<HashMap<String,String>> list;

    public void setList(ArrayList<HashMap<String, String>> list) {
        this.list = list;
    }

    public ExampleAdapter(Context context, ArrayList<HashMap<String,String>> list){
        this.mContext = context;
        this.list = list;
    }

    @Override
    public ExampleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExampleHolder(mContext,LayoutInflater.from(mContext).inflate(R.layout.simple_example_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final ExampleHolder holder, int position) {
        HashMap<String,String> map = list.get(position);
        holder.textView.setText(map.get("text"));
        holder.tvName.setText(map.get("name"));
        if (map.get("image").equals("false")){
            holder.ivIcon.setVisibility(View.GONE);
        }else {
            holder.ivIcon.setVisibility(View.VISIBLE);
            GlideApp.with(mContext)
                    .load(map.get("image"))
                    .into(holder.ivIcon);
        }

        Glide.with(mContext)
                .load(map.get("author"))
                .into(holder.ivAuthor);

        if (map.get("video").equals("false")){
            holder.gsyVideoPlayer.setVisibility(View.GONE);
        }else {
            holder.gsyVideoPlayer.setVisibility(View.VISIBLE);
            holder.gsyVideoPlayer.loadCoverImage(map.get("videoImage"));
            String url = map.get("video");
            String title = map.get("name");

            holder.gsyVideoOptionBuilder
                    .setIsTouchWiget(false)
                    .setUrl(url)
                    .setVideoTitle(title)
                    .setCacheWithPlay(true)
                    .setRotateViewAuto(true)
                    .setLockLand(true)
                    .setPlayTag("VIDEO")
                    .setShowFullAnimation(true)
                    .setNeedLockFull(true)
                    .setPlayPosition(position)
                    .setStandardVideoAllCallBack(new SampleListener() {
                        @Override
                        public void onPrepared(String url, Object... objects) {
                            super.onPrepared(url, objects);
                            if (!holder.gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                                //静音
                                GSYVideoManager.instance().setNeedMute(true);
                            }

                        }

                        @Override
                        public void onQuitFullscreen(String url, Object... objects) {
                            super.onQuitFullscreen(url, objects);
                            //全屏不静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }

                        @Override
                        public void onEnterFullscreen(String url, Object... objects) {
                            super.onEnterFullscreen(url, objects);
                            GSYVideoManager.instance().setNeedMute(false);
                        }
                    }).build(holder.gsyVideoPlayer);


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
        }

//        holder.tvComments.setText(map.get("good"));
//        String m = map.get("god");
//
//        if (m.length() > 0){
//            String num = map.get("godNum");
//            String m2 = m.replace(num,"");
//            int i = m2.indexOf("：");
//            SpannableString s = new SpannableString(m2);
//            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
//            s.setSpan(colorSpan, 0, i, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            holder.tvGodCms.setText(s);
//        }else {
//            holder.tvGodCms.setText(m);
//        }
        //默认缓存路径
//        holder.gsyVideoPlayer.setUp(url, true, null, "这是title");

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
//        holder.gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
//
//        //设置返回键
//        holder.gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
//
//        //设置全屏按键功能
//        holder.gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                resolveFullBtn(holder.gsyVideoPlayer);
//            }
//        });
//        holder.gsyVideoPlayer.setRotateViewAuto(true);
//        holder.gsyVideoPlayer.setLockLand(true);
//        holder.gsyVideoPlayer.setPlayTag(TAG);
//        holder.gsyVideoPlayer.setShowFullAnimation(true);
//        holder.gsyVideoPlayer.setIsTouchWiget(false);
//        //循环
//        //holder.gsyVideoPlayer.setLooping(true);
//        holder.gsyVideoPlayer.setNeedLockFull(true);
//
//        //holder.gsyVideoPlayer.setSpeed(2);
//
//        holder.gsyVideoPlayer.setPlayPosition(position);
//
//        holder.gsyVideoPlayer.setStandardVideoAllCallBack(new SampleListener() {
//            @Override
//            public void onPrepared(String url, Object... objects) {
//                super.onPrepared(url, objects);
//                Debuger.printfLog("onPrepared");
//                if (!holder.gsyVideoPlayer.isIfCurrentIsFullscreen()) {
//                    GSYVideoManager.instance().setNeedMute(true);
//                }
//
//            }
//
//            @Override
//            public void onQuitFullscreen(String url, Object... objects) {
//                super.onQuitFullscreen(url, objects);
//                GSYVideoManager.instance().setNeedMute(true);
//            }
//
//            @Override
//            public void onEnterFullscreen(String url, Object... objects) {
//                super.onEnterFullscreen(url, objects);
//                GSYVideoManager.instance().setNeedMute(false);
//            }
//        });
    }
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(mContext, false, true);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    class ExampleHolder extends RecyclerView.ViewHolder{

        SampleCoverVideo gsyVideoPlayer;

        ImageView imageView;

        GSYVideoOptionBuilder gsyVideoOptionBuilder;

        TextView textView,tvName,tvComments,tvGodCms;
        ImageView ivIcon;
        RoundedImageView ivAuthor;
        ExampleHolder(Context context,View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvSimple_title);
            tvName = (TextView) itemView.findViewById(R.id.tvSimple_name);
            tvComments = (TextView) itemView.findViewById(R.id.tvSimple_comments);
            tvGodCms = (TextView) itemView.findViewById(R.id.tvSimple_godCmt);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivSimple_icon);
            ivAuthor = (RoundedImageView) itemView.findViewById(R.id.ivSimple_author);
            gsyVideoPlayer = (SampleCoverVideo) itemView.findViewById(R.id.vSimple_video);

            imageView = new ImageView(context);
            gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        }
        public void onBind(final int position) {


        }
    }
}
