package com.yztc.my.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.yztc.my.myapplication.R;
import com.yztc.my.myapplication.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity {
   private ViewPager mviewpager;
    private LinearLayout mlinear;
    private MyViewPagerAdapter madapter;
    private List<View> list;
    private int [] viewdatas= new int[] {R.drawable.openimage,R.drawable.slide2,R.drawable.slide3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initDataView();
        initView();
        initDot();
        initAdapter();

    }

    private void initDot() {
        for(int i =0;i<list.size();i++){

            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50,50);
            params.rightMargin=10;
            iv.setLayoutParams(params);
            iv.setImageResource(R.drawable.dot_selector);
            if(i==0){
                iv.setSelected(true);
            }

           mlinear.addView(iv);

        }


    }

    private void initDataView() {
        list = new ArrayList<>();
        for(int i =0;i<viewdatas.length;i++){
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            iv.setImageResource(viewdatas[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            list.add(iv);
            if(i==2){
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    }
                });
            }

        }



    }

    private void initAdapter() {

        madapter = new MyViewPagerAdapter(list);
        mviewpager.setAdapter(madapter);
    }

    private void initView() {
        mlinear = (LinearLayout) findViewById(R.id.linear_guide);
        mviewpager = (ViewPager) findViewById(R.id.mviewpager);
        mviewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                   for(int i =0;i<list.size();i++){
                       if(position ==i){
                           mlinear.getChildAt(i).setSelected(true);
                       }else{
                           mlinear.getChildAt(i).setSelected(false);
                       }
                   }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


}
