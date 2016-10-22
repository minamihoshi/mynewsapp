package com.yztc.my.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.adapter.MySaveAdapter;
import com.yztc.my.myapplication.base.App;
import com.yztc.my.myapplication.constant.MyConstants;
import com.yztc.my.myapplication.javabean.MyNewsData;
import com.yztc.my.myapplication.javabean.NewsData;

import java.util.ArrayList;
import java.util.List;

public class SaveActivity extends AppCompatActivity implements MySaveAdapter.MyItemClickListener {


    private RecyclerView recyclerView;
    private MySaveAdapter adapter;
    private List<MyNewsData> list;
    private MyNewsData databean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        initData();
        initView();
        initAdapter();



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

    private void initAdapter() {

        adapter = new MySaveAdapter(list,this,this);
        recyclerView.setAdapter(adapter);

    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycler_save);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    private void initData() {
       list = new ArrayList<>();
        list = ((App) getApplication()).liteOrm.query(MyNewsData.class);
        Log.e("TAG", "initData: "+list.size() );

    }

    @Override
    public void onClick(int position) {
        databean = list.get(position);
        Intent intent = new Intent(this,WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MyConstants.KEY_WEB,databean);
        intent.putExtras(bundle);
        startActivity(intent);
        Toast.makeText(this,"lalalalala",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onlongClick(int position) {

    }
}
