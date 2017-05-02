package com.wallace.tools.adater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wallace.tools.R;
import com.wallace.tools.bean.Main;

import java.util.List;

/**
 * Package com.wallace.adater
 * Created by Wallace.
 * on 2017/3/13.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder>{

    private List<Main> list;
    private Context mContext;
    private OnMainItemClickListener onMainItemClickListener;

    public void setOnMainItemClickListener(OnMainItemClickListener onMainItemClickListener) {
        this.onMainItemClickListener = onMainItemClickListener;
    }

    public MainAdapter(Context mContext, List<Main> list){
        this.mContext = mContext;
        this.list = list;
    }
    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final MainHolder holder = new MainHolder(
                LayoutInflater.from(mContext).inflate(R.layout.simple_main_item,parent,false));
        if (onMainItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    onMainItemClickListener.onClick(holder.itemView, pos);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
//        int height = metric.heightPixels;
        Main main = list.get(position);
        holder.imageView.setImageResource(main.getImage());
        holder.textView.setText(main.getTitle());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width/2,width/2);
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class MainHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private TextView textView;
         MainHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.ivSimple_icon);
            textView = (TextView) itemView.findViewById(R.id.tvSimple_title);
        }
    }
    public interface OnMainItemClickListener{
        void onClick(View view, int position);
    }

}
