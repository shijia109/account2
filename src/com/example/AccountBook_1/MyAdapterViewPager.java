package com.example.AccountBook_1;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by shijia on 2015/4/11.
 */
public class MyAdapterViewPager extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String[] arrTitles;


    public MyAdapterViewPager(FragmentManager fm, List<Fragment> list,String[] arrTitles) {
        super(fm);
        this.list = list;
        this.arrTitles=arrTitles;
    }

    public MyAdapterViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return arrTitles[position];
    }
}
