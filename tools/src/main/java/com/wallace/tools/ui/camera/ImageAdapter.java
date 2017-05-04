package com.wallace.tools.ui.camera;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wallace.tools.R;
import com.wallace.tools.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Package com.wallace.tools.ui.camera
 * Created by Wallace.
 * on 2017/5/4.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder>{

    private Context mContext;
    private ArrayList<HashMap<String,String>> list;

    public ImageAdapter(Context mContext, ArrayList<HashMap<String, String>> list) {
        this.mContext = mContext;
        this.list = list;
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ImageHolder holder = new ImageHolder(
                LayoutInflater.from(mContext)
                        .inflate(R.layout.simple_image_item,parent,false));
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
    public void onBindViewHolder(ImageHolder holder, int position) {
        HashMap<String,String> map = list.get(position);

        holder.textView.setText(map.get("text"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView textView;
        public ImageHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivSimple_icon);
            textView = (TextView) itemView.findViewById(R.id.tvSimple_title);
        }
    }
}
