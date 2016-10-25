package com.yztc.my.myapplication.base;

import android.app.Application;

import com.litesuits.orm.LiteOrm;

/**
 * Created by My on 2016/10/22.
 */
public class App extends Application{
    public LiteOrm liteOrm;
    public LiteOrm liteOrm2;
    @Override
    public void onCreate() {
        super.onCreate();
        liteOrm = LiteOrm.newSingleInstance(this,"news.db");
        liteOrm2 =LiteOrm.newSingleInstance(this,"record.db");
    }
}
