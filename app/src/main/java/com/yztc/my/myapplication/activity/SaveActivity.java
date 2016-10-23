package com.yztc.my.myapplication.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.adapter.MySaveAdapter;
import com.yztc.my.myapplication.base.App;
import com.yztc.my.myapplication.constant.MyConstants;
import com.yztc.my.myapplication.javabean.MyNewsData;
import com.yztc.my.myapplication.javabean.NewsData;

import java.util.ArrayList;
import java.util.List;

public class SaveActivity extends AppCompatActivity implements MySaveAdapter.MyItemClickListener {

  private long backStartTime ,backEndTime;
    private RecyclerView recyclerView;
    private MySaveAdapter adapter;
    private List<MyNewsData> list;
    private MyNewsData databean;
    private PopupWindow popupWindow;
    private int itemposition;

    private Button mbtn_openweb,mbtn_del;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        initData();
        initView();
        initAdapter();
        initPopup();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("收藏夹");

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    backEndTime = System.currentTimeMillis();
                    if (backEndTime - backStartTime > 2000) {
                        //Toast.makeText(SaveActivity.this, "滚动", Toast.LENGTH_SHORT).show();
                        backStartTime = backEndTime;
                    }else{
                        recyclerView.smoothScrollToPosition(0);
                    }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initPopup() {
        View view = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);


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
                openweb(itemposition);
            }
        });

        mbtn_del = (Button) view.findViewById(R.id.btn_del);
        mbtn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((App)getApplication()).liteOrm.delete(list.get(itemposition));
                list.remove(itemposition);

                popupWindow.dismiss();
               // adapter.notifyItemRemoved(itemposition);
                //不通知list中的所有item改变下标 原因不明  在判断position时有问题
                adapter.notifyDataSetChanged();

            }
        });
    }

    private void initAdapter() {

        adapter = new MySaveAdapter(list, this, this);
        recyclerView.setAdapter(adapter);

    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_save);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void initData() {
        list = new ArrayList<>();
        list = ((App) getApplication()).liteOrm.query(MyNewsData.class);
        Log.e("TAG", "initData: " + list.size());

    }

    @Override
    public void onClick(int position) {
        openweb(position);
    }

    private void openweb(int position) {
        databean = list.get(position);
        Intent intent = new Intent(this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstants.KEY_WEB, databean);
        intent.putExtras(bundle);
        startActivity(intent);
       // Toast.makeText(this, "lalalalala", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onlongClick(int position) {


//        // 在某个控件的下方展示
//        popupWindow.showAsDropDown(recyclerView);
//        popupWindow.showAsDropDown(recyclerView, 100, 100);
        // 在屏幕中心显示
        popupWindow.showAtLocation(recyclerView, Gravity.CENTER, 0, 0);
        itemposition =position;

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
