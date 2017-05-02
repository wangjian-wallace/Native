package com.wallace.tools.ui.music;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallace.tools.R;
import com.wallace.tools.bean.MusicInfo;
import com.wallace.tools.interfaces.OnItemClickListener;

import java.util.List;

/**
 * Package com.wallace.tools.ui.music
 * Created by Wallace.
 * on 2017/4/1.
 */

class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder>{

    private Context context;
    private List<MusicInfo> list;

    private int index = 0;
    private boolean isVisible = false;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    MusicAdapter(Context context, List<MusicInfo> list){
        this.context = context;
        this.list = list;
    }
    @Override
    public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final MusicHolder holder = new MusicHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.simple_music_item,parent,false));
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(v,pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(v,pos);
                    return false;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MusicHolder holder, int position) {
        MusicInfo musicInfo = list.get(position);
        holder.tvTitle.setText(musicInfo.getTitle());
        holder.tvName.setText(musicInfo.getArtist() + " · " + musicInfo.getAlbum());
        if (isVisible && index == position){
            holder.ivPlay.setVisibility(View.VISIBLE);
        }else {
            holder.ivPlay.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    void play(int index,boolean isVisible){
        this.index = index;
        this.isVisible = isVisible;

        notifyDataSetChanged();
    }

    class MusicHolder extends RecyclerView.ViewHolder{

        TextView tvName,tvTitle;
        ImageView ivIcon,ivPlay;
        MusicHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvSimple_title);
            tvName = (TextView) itemView.findViewById(R.id.tvSimple_name);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivSimple_icon);
            ivPlay = (ImageView) itemView.findViewById(R.id.ivSimple_play);
        }
    }
}
