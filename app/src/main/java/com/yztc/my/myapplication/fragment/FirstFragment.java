package com.yztc.my.myapplication.fragment;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.activity.MainActivity;
import com.yztc.my.myapplication.activity.WebActivity;
import com.yztc.my.myapplication.adapter.MyRecyViewAdapter_Best;
import com.yztc.my.myapplication.adapter.MyRecyclerViewAdapter;
import com.yztc.my.myapplication.base.App;
import com.yztc.my.myapplication.constant.MyConstants;
import com.yztc.my.myapplication.javabean.BestNewsData;
import com.yztc.my.myapplication.javabean.MyNewsData;
import com.yztc.my.myapplication.javabean.NewsData;
import com.yztc.my.myapplication.util.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment implements MyRecyclerViewAdapter.MyItemClickListener,MyRecyViewAdapter_Best.MyItemClickListenerBest{
   private XRecyclerView recyclerView;
    private List<BestNewsData.ResultBean.DataBean> list_best;
    private List<NewsData.ResultBean.DataBean> list;
    private List<BestNewsData.ResultBean.DataBean> list_best_temp;
    private List<NewsData.ResultBean.DataBean> list_temp;
    private MyRecyclerViewAdapter recyclerViewAdapter;
    private MyRecyViewAdapter_Best recyViewAdapter_best;
    private String  string;
    private String type;
    private PopupWindow popupWindow;
    private int itemposition;
    private Button mbtn_openweb,mbtn_save;
    private MyNewsData myNewsBean;
    private View headerView;
    private View footView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1 :
                    recyViewAdapter_best.notifyDataSetChanged();
                    Log.e("TAG", "handleMessage: "+"notifyyyyyyyyyyyyyyyyyyyyyyyy");
                    break;
                case 2:
                    recyclerViewAdapter.notifyDataSetChanged();
                    Log.e("TAG", "handleMessage: "+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                    break;
                case 3:
                    recyclerView.refreshComplete();
                    footView.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    };

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View rootview = inflater.inflate(R.layout.fragment_first, container, false);
     Bundle bundle = getArguments();
     type = bundle.getString(MyConstants.TYPE_KEY);

      string = String.format(MyConstants.FORMAT_UTLSTRING,type);
     Log.e("TAG", "onCreateView: "+string );
        initDatas(string);
        initView(rootview);
        initAdapter(type);
        initPopup();

        return rootview;
    }

 private void initDatas(String string) {
     OkHttpUtils.doGETRequest(string, new Callback() {
         @Override
         public void onFailure(Call call, IOException e) {
             Message message = new Message();
             message.what =3;
             handler.sendMessage(message);
         }

         @Override
         public void onResponse(Call call, Response response) throws IOException {
             ResponseBody body = response.body();
             String datastring = body.string();

             Gson gson = new Gson();

             if(!TextUtils.isEmpty(datastring)&&type.equals("top")){
                 BestNewsData bestNewsData = gson.fromJson(datastring, BestNewsData.class);
                list_best_temp = bestNewsData.getResult().getData();
                 list_best.addAll(0,list_best_temp);
                 Message message = new Message();
                 message.what =1;

                 handler.sendMessage(message);

             }else{
                 NewsData newsData = gson.fromJson(datastring, NewsData.class);
                 list_temp=newsData.getResult().getData();
                 list.addAll(0,list_temp);
                 Log.e("TAG", "onResponse: "+list_temp.get(0).getCategory());
                 Message message = new Message();
                 message.what =2;
                 handler.sendMessage(message);
             }


             Message message = new Message();
             message.what =3;
             handler.sendMessage(message);





         }
     });


 }

 private void initAdapter(String type) {
     list= new ArrayList<>();
     list_best = new ArrayList<>();
  recyclerViewAdapter = new MyRecyclerViewAdapter(list,getActivity(),this);
  recyViewAdapter_best = new MyRecyViewAdapter_Best(list_best,getActivity(),this);
     if(!TextUtils.isEmpty(type)&&type.equals("top")){
         recyclerView.setAdapter(recyViewAdapter_best);
     }else{
         recyclerView.setAdapter(recyclerViewAdapter);
     }

 }

 private void initView(View rootview) {
        recyclerView = (XRecyclerView) rootview.findViewById(R.id.myrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
     recyclerView.setLoadingMoreEnabled(true);
     recyclerView.setPullRefreshEnabled(true);
     recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
         @Override
         public void onRefresh() {
             //Toast.makeText(getActivity(),"123",Toast.LENGTH_LONG).show();
             initDatas(string);
         }

         @Override
         public void onLoadMore() {
             Toast.makeText(getActivity(),"没有了去看看别的吧~",Toast.LENGTH_LONG).show();
             recyclerView.loadMoreComplete();
         }
     });

     headerView = LayoutInflater.from(getActivity()).inflate(R.layout.listview_header, recyclerView, false);
    footView =LayoutInflater.from(getActivity()).inflate(R.layout.listview_footer,recyclerView,false);
     recyclerView.addHeaderView(headerView);
     recyclerView.addFootView(footView);

     recyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);


    }
   //其他新闻的点击回调
    @Override
    public void onClick(int position) {
        openweb_other(position);


    }

    private void openweb_other(int position) {
        //Toast.makeText(getActivity(),"other",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), WebActivity.class);
        Bundle bundle = new Bundle();
        NewsData.ResultBean.DataBean dataBean = list.get(position);
        bundle.putSerializable(MyConstants.KEY_WEB,dataBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onlongClick(int position) {

        popupWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
        itemposition =position;


    }
    //头条新闻的回调
    @Override
    public void onClickBest(int position) {
        openweb(position);
    }

    private void openweb(int position) {
        Toast.makeText(getActivity(),"top",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), WebActivity.class);
        Bundle bundle = new Bundle();
        BestNewsData.ResultBean.DataBean dataBean = list_best.get(position);
        bundle.putSerializable(MyConstants.KEY_WEB,dataBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onlongClickBest(int position) {
        Toast.makeText(getActivity(),"toplong",Toast.LENGTH_SHORT).show();
        popupWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
        itemposition =position;
    }


//    public void scrollTop(){
//        recyclerView.smoothScrollToPosition(0);
//    }


    private void initPopup() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popupwindow_main, null);


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        mbtn_openweb = (Button) view.findViewById(R.id.btn_openweb);
        mbtn_openweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if(list_best.size()!=0){
                    openweb(itemposition);
                }else{

                    openweb_other(itemposition);
                }

            }
        });

        mbtn_save = (Button) view.findViewById(R.id.btn_save);
        mbtn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list_best.size()!=0){
                    BestNewsData.ResultBean.DataBean dataBean = list_best.get(itemposition);
                    myNewsBean = new MyNewsData(dataBean.getTitle(),dataBean.getDate(),dataBean.getAuthor_name(),dataBean.getThumbnail_pic_s(),dataBean.getThumbnail_pic_s03(),dataBean.getUrl(),dataBean.getType(),dataBean.getRealtype());
                }else{
                    NewsData.ResultBean.DataBean dataBean = list.get(itemposition);
                    myNewsBean = new MyNewsData(dataBean.getTitle(),dataBean.getDate(),dataBean.getAuthor_name(),dataBean.getThumbnail_pic_s(),dataBean.getThumbnail_pic_s03(),dataBean.getUrl(),dataBean.getCategory(),dataBean.getCategory());
                }

                long savecode = ((App) (getActivity().getApplication())).liteOrm.insert(myNewsBean);
                Log.e("TAG", "----------------------------------"+savecode);

                if(savecode>0){
                    Toast.makeText(getActivity(),"收藏成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"已经收藏过了~",Toast.LENGTH_SHORT).show();
                }

                popupWindow.dismiss();
                // adapter.notifyItemRemoved(itemposition);
                //不通知list中的所有item改变下标 原因不明  在判断position时有问题
               // adapter.notifyDataSetChanged();

            }
        });
    }


}
