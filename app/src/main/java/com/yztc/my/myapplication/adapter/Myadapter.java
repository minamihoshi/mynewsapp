package com.yztc.my.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by My on 2016/9/22.
 */
public class Myadapter extends FragmentPagerAdapter{
  private ArrayList<Fragment> list;
    private String [] TABS ;

    public Myadapter(FragmentManager fm,ArrayList<Fragment> list,String [] TABS ) {
        super(fm);
        this.list=list;
        this.TABS =TABS;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }
}
