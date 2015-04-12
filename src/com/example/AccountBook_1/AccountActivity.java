package com.example.AccountBook_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijia on 2015/4/9.
 */
public class AccountActivity extends FragmentActivity {
    private ViewPager viewPager_main;
    private PagerTabStrip pagerViewStrip_main;
    private List<Fragment> list;
    private String[] arrTabTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_account);

        viewPager_main=(ViewPager) findViewById(R.id.viewPager_main);
        pagerViewStrip_main=(PagerTabStrip) findViewById(R.id.PagerTabStrip_main);
        pagerViewStrip_main.setTextColor(Color.WHITE);
        pagerViewStrip_main.setPadding(0,20,0,20);
        pagerViewStrip_main.setBackgroundColor(Color.rgb(102,136,255));
        pagerViewStrip_main.setDrawFullUnderline(true);
        pagerViewStrip_main.setTextSize(2,24);
        pagerViewStrip_main.setTabIndicatorColor(Color.WHITE);

        list=new ArrayList<Fragment>();
        arrTabTitles=new String[]{"支出","收入"};

        IncomeFragment incomeFragment = new IncomeFragment();
        OutlayFragment outlayFragment = new OutlayFragment();
        list.add(outlayFragment);
        list.add(incomeFragment);


        MyAdapter adapter=new MyAdapter(getSupportFragmentManager(),list,arrTabTitles);
        viewPager_main.setAdapter(adapter);


    }



    class MyAdapter extends FragmentPagerAdapter {
        private List<Fragment>list;
        private String[] arrTitles;


        public MyAdapter(FragmentManager fm, List<Fragment> list,String[] arrTitles) {
            super(fm);
            this.list = list;
            this.arrTitles=arrTitles;
        }

        public MyAdapter(FragmentManager fm) {
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
}
