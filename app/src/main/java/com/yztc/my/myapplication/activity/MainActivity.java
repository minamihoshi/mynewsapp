package com.yztc.my.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.adapter.Myadapter;
import com.yztc.my.myapplication.constant.MyConstants;
import com.yztc.my.myapplication.fragment.FirstFragment;
import com.yztc.zxinglibrary.android.CaptureActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private long backStartTime, backEndTime;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private CollapsingToolbarLayoutState state;


    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private FirstFragment fragment;
    private ActionBar actionBar;
    private ImageView miv;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private Myadapter myadapter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    //top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
    private static final String[] TABS = {"头条", "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚"};
    private static final String[] TABTYPES = {"top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};
    private ArrayList<Fragment> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        initFragments();
        initadapter();
        initView();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        }).show();
//            }
//        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


          //TODO 双击顶端
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backEndTime = System.currentTimeMillis();
                if (backEndTime - backStartTime > 2000) {
                    //Toast.makeText(MainActivity.this, "滚动", Toast.LENGTH_SHORT).show();
                    backStartTime = backEndTime;
                }else{
                    miv.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void initadapter() {
        myadapter = new Myadapter(getSupportFragmentManager(), list, TABS);

    }

    private void initFragments() {
        for (int i = 0; i < TABS.length; i++) {
            FirstFragment fragment = new FirstFragment();

            Bundle bundle = new Bundle();
            bundle.putString(MyConstants.TYPE_KEY, TABTYPES[i]);
            fragment.setArguments(bundle);
            list.add(fragment);

        }

    }

    private void initView() {
        miv = (ImageView) findViewById(R.id.miv_yztc);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        mTabLayout = (TabLayout) findViewById(R.id.mtablayout);
        mViewPager = (ViewPager) findViewById(R.id.myviewpager);
        mViewPager.setAdapter(myadapter);
        mViewPager.setOffscreenPageLimit(5);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setBackgroundColor(getResources().getColor(R.color.kong));
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setTitleEnabled(false);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        toolbar.setTitle("");//设置title为EXPANDED
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {

                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠

                       ;
                        miv.setVisibility(View.GONE);
                        toolbar.setTitle("新闻早知道");//设置title

                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {
                        }
                        toolbar.setTitle("");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间

                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //TODO 右上角菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mode_dark) {
            return true;
        } else if (id == R.id.mode_dark) {
            return true;
        } else if (id == R.id.action_save) {
            Intent intent = new Intent(this, SaveActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_scan) {
            Intent intent = new Intent(MainActivity.this,
                    CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        }else if(id==R.id.action_history){
            Intent intent = new Intent(this,RecordActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, SaveActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(this, ScanCodeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(this,RecordActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {
            shareUM();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backEndTime = System.currentTimeMillis();
            if (backEndTime - backStartTime > 2000) {
                Toast.makeText(this, "连续按两次退出程序", Toast.LENGTH_SHORT).show();
                backStartTime = backEndTime;
                return true;
            }
//            else {
//                finish();
//            }
        }
        return super.onKeyDown(keyCode, event);
    }


    //二维码回传数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //友盟回传
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

               // http://([\w-]+\.)+[\w-]+(/[\w- ./?%&=]*)?   正则

                Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
//                qrCoded.setText("解码结果： \n" + content);
//                qrCodeImage.setImageBitmap(bitmap);


            }
        }
    }


    void shareUM(){
        UMImage image = new UMImage(MainActivity.this, R.drawable.info_flow_channel_icon_2);
        new ShareAction(MainActivity.this).setPlatform(SHARE_MEDIA.QQ)
                .withText("新闻早知道")
                .withTargetUrl("http://www.baidu.com")
                .withMedia(image)
                .setCallback(umShareListener)
                .share();

//        new ShareAction(MainActivity.this).withText("hello")
//                .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN)
//                .setCallback(umShareListener).open();

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            com.umeng.socialize.utils.Log.d("plat","platform"+platform);

            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                com.umeng.socialize.utils.Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };



}
