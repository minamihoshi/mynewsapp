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
import com.yztc.my.myapplication.constant.MyConstants;
import com.yztc.my.myapplication.javabean.BestNewsData;
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
            title =dataBean_best.getTitle();
            webstring= dataBean_best.getUrl();
            Log.e("TAG", "onCreate: "+webstring );
        }else if(serializable instanceof NewsData.ResultBean.DataBean){
            dataBean =(NewsData.ResultBean.DataBean)serializable;
            title = dataBean.getTitle();
            webstring =dataBean.getUrl();
            Log.e("TAG", "onCreate: "+webstring );
        }


        Log.e("TAG", "onCreate: "+title );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);



        toolbar.setTitleTextColor(Color.WHITE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        initView();
        initWeb();
       // initWebData();
        //initDataManager();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initWeb() {

        webView.loadUrl(webstring);
    }

//    private void initDataManager() {
//        manager = new NewsInfoManager(this);
//    }

//    private void initWebData() {
////        String id = dataBean.getId();
////        String stringurl = String.format(URLConstants.WEB_URL, id);
//        Log.e("TAG", "initWebData: "+webstring);
//        OkHttpUtils.doGETRequest(webstring, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                ResponseBody body = response.body();
//                String string = body.string();
//
//                try {
//                    JSONObject jsonObject = new JSONObject(string);
//                    JSONObject jsonObject1 =jsonObject.getJSONObject("data");
//                    webstring =jsonObject1.getString("wap_content");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        webView.loadDataWithBaseURL(null,webstring,"text/html","UTF-8",null);
//
//
//                    }
//                });
//
//            }
//        });
//
//    }

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
                break;
            case R.id.save:

//                long insert = manager.insert(dataBean);
//                if(insert>0){
//                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(this,"已经收藏过了~",Toast.LENGTH_SHORT).show();
//                }
//                break;

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
