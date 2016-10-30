package com.yztc.my.myapplication.base;

import android.app.Application;

import com.litesuits.orm.LiteOrm;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by My on 2016/10/22.
 */
public class App extends Application{
    public LiteOrm liteOrm;
    public LiteOrm liteOrm2;

    {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        liteOrm = LiteOrm.newSingleInstance(this,"news.db");
        liteOrm2 =LiteOrm.newSingleInstance(this,"record.db");
        UMShareAPI.get(this);
        //Config.REDIRECT_URL = "您新浪后台的回调地址"
    }
}
