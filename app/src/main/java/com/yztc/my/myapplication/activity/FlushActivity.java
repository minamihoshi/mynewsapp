package com.yztc.my.myapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.util.PreUtils;


public class FlushActivity extends AppCompatActivity {

    private static final long SLEEPTIME_TOTAL=2000;

    private  boolean IsGoToMain;

    private static final String KEY_ISGOTOMAIN="KEY_ISGOTOMAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flush);


        new MyAsyncTask().execute();
    }




    class MyAsyncTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            long timeMillis = System.currentTimeMillis();

           //此处进行一些耗时操作


            long timeMillis2 = System.currentTimeMillis();


            long time = timeMillis2 - timeMillis;
            if(time<SLEEPTIME_TOTAL){
                try {
                    Thread.sleep(SLEEPTIME_TOTAL-time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent();
            IsGoToMain = PreUtils.readBoolean(FlushActivity.this,KEY_ISGOTOMAIN);
            if (IsGoToMain){
                intent.setClass(FlushActivity.this,MainActivity.class);
            }else{
                intent.setClass(FlushActivity.this,GuideActivity.class);
                  // PreUtils.writeBoolean(FlushActivity.this,KEY_ISGOTOMAIN,true);
            }


            startActivity(intent);
            //第一个参数为第二个activity进入效果  第二个参数：前一个activity退出效果
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            finish();


        }
    }



}
