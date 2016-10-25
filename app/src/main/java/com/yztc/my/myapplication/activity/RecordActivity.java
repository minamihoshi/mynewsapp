package com.yztc.my.myapplication.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.adapter.MySaveAdapter;
import com.yztc.my.myapplication.base.App;
import com.yztc.my.myapplication.javabean.MyNewsData;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements MySaveAdapter.MyItemClickListener {
    private List<MyNewsData> list;
    private RecyclerView recyclerView;
    private MySaveAdapter adapter;
    private LinearLayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        initData();
        initView();
        initAdapter();

    }

    private void initAdapter() {

        adapter = new MySaveAdapter(list,this,this);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);

        //设置倒序显示
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);

        recyclerView.setAdapter(adapter);

    }

    private void initData() {
        list = new ArrayList<>();
        list =((App)getApplication()).liteOrm2.query(MyNewsData.class);
    }

    private void initView() {


        recyclerView = (RecyclerView) findViewById(R.id.recycler_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onlongClick(int position) {

    }
}
