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
import com.yztc.my.myapplication.javabean.MyNewsData;

import java.util.List;

/**
 * Created by My on 2016/10/22.
 */
public class MySaveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyNewsData> list;
    private Context context;
    private MyItemClickListener listener;

    public MySaveAdapter(List<MyNewsData> list, Context context, MyItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    public interface  MyItemClickListener{
        void onClick(int position);

        void onlongClick(int position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.layout_newsitem,parent,false);

        return new MyViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder)holder).tv_title.setText(list.get(position).getTitle());
        ((MyViewHolder)holder).tv_content.setText(list.get(position).getDate());
        ((MyViewHolder)holder).miv.setImageResource(R.mipmap.ic_launcher);

        if(!TextUtils.isEmpty(list.get(position).getThumbnail_pic_s())){
            Picasso.with(context).load(list.get(position).getThumbnail_pic_s()).fit()
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(((MyViewHolder)holder).miv);
        }else{
            Picasso.with(context).load(R.mipmap.ic_launcher).resize(300,150).centerCrop()
                    .placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                    .into(((MyViewHolder)holder).miv);
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
        TextView tv_title ,tv_content ;
        ImageView miv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content= (TextView) itemView.findViewById(R.id.tv_content);
            miv = (ImageView) itemView.findViewById(R.id.miv);


        }
    }
}
