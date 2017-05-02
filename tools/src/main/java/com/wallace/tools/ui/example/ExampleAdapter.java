package com.wallace.tools.ui.example;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wallace.tools.R;
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
        return new ExampleHolder(LayoutInflater.from(mContext).inflate(R.layout.simple_example_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ExampleHolder holder, int position) {
        HashMap<String,String> map = list.get(position);
        holder.textView.setText(Html.fromHtml(map.get("text")));
        holder.tvName.setText(map.get("name"));
        if (map.get("image").equals("false")){
            holder.ivIcon.setVisibility(View.GONE);
        }else {
            holder.ivIcon.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load("http:"+map.get("image"))
                    .error(R.drawable.example)
                    .fitCenter()
                    .into(holder.ivIcon);
        }
        Glide.with(mContext)
                .load("http:"+map.get("author"))
                .error(R.drawable.example)
                .fitCenter()
                .into(holder.ivAuthor);
        holder.tvComments.setText(map.get("good"));
        String m = map.get("god");

        if (m.length() > 0){
            String num = map.get("godNum");
            String m2 = m.replace(num,"");
            int i = m2.indexOf("：");
            SpannableString s = new SpannableString(m2);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
            s.setSpan(colorSpan, 0, i, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvGodCms.setText(s);
        }else {
            holder.tvGodCms.setText(m);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ExampleHolder extends RecyclerView.ViewHolder{

        TextView textView,tvName,tvComments,tvGodCms;
        ImageView ivIcon;
        RoundedImageView ivAuthor;
        ExampleHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tvSimple_title);
            tvName = (TextView) itemView.findViewById(R.id.tvSimple_name);
            tvComments = (TextView) itemView.findViewById(R.id.tvSimple_comments);
            tvGodCms = (TextView) itemView.findViewById(R.id.tvSimple_godCmt);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivSimple_icon);
            ivAuthor = (RoundedImageView) itemView.findViewById(R.id.ivSimple_author);
        }
    }
}
