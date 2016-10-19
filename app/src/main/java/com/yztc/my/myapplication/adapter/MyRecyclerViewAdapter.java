package com.yztc.my.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.javabean.NewsData;


import java.util.List;

/**
 * Created by My on 2016/10/11.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsData.ResultBean.DataBean> list;
    private Context context;
    private MyItemClickListener listener;

    public interface  MyItemClickListener{
        void onClick(int position);

        void onlongClick(int position);
    }

    public MyRecyclerViewAdapter(List list, Context context,MyItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         View rootview = LayoutInflater.from(context).inflate(R.layout.layout_newsitem,parent,false);
        return new MyViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        NewsData.ResultBean.DataBean data = list.get(position);
        ((MyViewHolder)holder).miv_news.setImageResource(R.mipmap.ic_launcher);
        ((MyViewHolder)holder).mtv_title.setText(data.getTitle());
        ((MyViewHolder)holder).mtv_content.setText(data.getDate());

        if(!TextUtils.isEmpty(data.getThumbnail_pic_s())){
            Picasso.with(context).load(data.getThumbnail_pic_s()).fit()
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(((MyViewHolder)holder).miv_news);
        }else{
            Picasso.with(context).load(R.mipmap.ic_launcher).resize(300,150).centerCrop()
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(((MyViewHolder)holder).miv_news);
        }

        ((MyViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(position);
            }
        });
        ((MyViewHolder)holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                listener.onlongClick(position);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mtv_title ,mtv_content;
        ImageView miv_news;
        public MyViewHolder(View itemView) {
            super(itemView);

            mtv_title = (TextView) itemView.findViewById(R.id.tv_title);
            mtv_content = (TextView) itemView.findViewById(R.id.tv_content);
            miv_news = (ImageView) itemView.findViewById(R.id.miv);
        }
    }
}
