package com.yztc.my.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.base.App;
import com.yztc.my.myapplication.constant.MyConstants;
import com.yztc.my.myapplication.javabean.BestNewsData;
import com.yztc.my.myapplication.javabean.MyNewsData;
import com.yztc.my.myapplication.javabean.NewsData;
import com.yztc.my.myapplication.util.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private String webstring;
    private String title;
    private NewsData.ResultBean.DataBean dataBean;
    private BestNewsData.ResultBean.DataBean dataBean_best;
    private MyNewsData myNewsBean;
    private long backStartTime ,backEndTime;

   // private NewsInfoManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Serializable serializable = bundle.getSerializable(MyConstants.KEY_WEB);

        if(serializable instanceof BestNewsData.ResultBean.DataBean){
            dataBean_best =(BestNewsData.ResultBean.DataBean)serializable;

            myNewsBean =new MyNewsData(dataBean_best.getTitle(),
                    dataBean_best.getDate(),
                    dataBean_best.getAuthor_name(),
                    dataBean_best.getThumbnail_pic_s(),
                    dataBean_best.getThumbnail_pic_s03(),
                    dataBean_best.getUrl(),
                    dataBean_best.getType(),
                    dataBean_best.getRealtype()
                    );
            title =dataBean_best.getTitle();
            webstring= dataBean_best.getUrl();
            Log.e("TAG", "onCreate: "+webstring );
        }else if(serializable instanceof NewsData.ResultBean.DataBean){
            dataBean =(NewsData.ResultBean.DataBean)serializable;
            myNewsBean =new MyNewsData(dataBean.getTitle(),
                    dataBean.getDate(),
                    dataBean.getAuthor_name(),
                    dataBean.getThumbnail_pic_s(),
                    dataBean.getThumbnail_pic_s03(),
                    dataBean.getUrl(),
                    dataBean.getCategory(),
                    dataBean.getCategory()
            );


            title = dataBean.getTitle();
            webstring =dataBean.getUrl();
            Log.e("TAG", "onCreate: "+webstring );
        }else{
            myNewsBean = (MyNewsData) serializable;
            title =myNewsBean.getTitle();
            webstring =myNewsBean.getUrl();
            Log.e("TAG", "onCreate: "+webstring );
        }


        Log.e("TAG", "onCreate: "+title );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);



        toolbar.setTitleTextColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backEndTime = System.currentTimeMillis();
                if (backEndTime - backStartTime > 2000) {
                    //Toast.makeText(SaveActivity.this, "滚动", Toast.LENGTH_SHORT).show();
                    backStartTime = backEndTime;
                }else{
                   webView.scrollTo(0,0);

                }
            }
        });

        initView();
        initWeb();
       // initWebData();
        //initDataManager();



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initWeb() {

        webView.loadUrl(webstring);
    }



    private void initView() {

        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.e("TAG", "shouldOverrideUrlLoading: "+url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home :
                finish();
                webView.scrollTo(0,0);
                break;
            case R.id.save:

                long savecode = ((App) getApplication()).liteOrm.insert(myNewsBean);
                Log.e("TAG", "----------------------------------"+savecode);

                if(savecode>0){
                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"已经收藏过了~",Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       if (keyCode== KeyEvent.KEYCODE_BACK){

           if(webView.canGoBack()){
               webView.goBack();
                return true;
           }

       }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        webView.stopLoading();
        ((ViewGroup)webView.getParent()).removeView(webView);
        webView.removeAllViews();
        webView.clearCache(true);
        webView.clearHistory();
        webView.destroy();

        super.onDestroy();
    }


}
