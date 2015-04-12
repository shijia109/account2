package com.example.AccountBook_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijia on 2015/4/11.
 */
public class DetailActivity extends FragmentActivity {
    private ViewPager viewPager_main;
    private PagerTabStrip pagerViewStrip_main;
    private List<Fragment> list;
    private String[] arrTabTitles;
    private String dateType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_detail);

        dateType=getIntent().getExtras().get("date_type").toString();
        /////////////////界面相关
        viewPager_main=(ViewPager) findViewById(R.id.viewPager_main);
        pagerViewStrip_main=(PagerTabStrip) findViewById(R.id.PagerTabStrip_main);
        pagerViewStrip_main.setTextColor(Color.WHITE);
        pagerViewStrip_main.setPadding(0,20,0,20);
        pagerViewStrip_main.setBackgroundColor(Color.rgb(102,136,255));
        pagerViewStrip_main.setDrawFullUnderline(true);
        pagerViewStrip_main.setTextSize(2,24);
        pagerViewStrip_main.setTabIndicatorColor(Color.WHITE);

        list=new ArrayList<Fragment>();

        String str_dateType=null;
        if("today".equals(dateType)){
            str_dateType="今日";
        }else if("thisWeek".equals(dateType)){
            str_dateType="本周";
        }else if("thisMonth".equals(dateType)){
            str_dateType="本月";
        }else if("thisYear".equals(dateType)){
            str_dateType="全年";
        }
        arrTabTitles=new String[]{str_dateType+"支出明细",str_dateType+"收入明细"};

        DetailIncomeFragment detailIncomeFragment = new DetailIncomeFragment();
        DetailOutlayFragment detailOutlayFragment = new DetailOutlayFragment();
        Bundle bundle1=new Bundle();
        bundle1.putString("date_type",dateType);
        detailIncomeFragment.setArguments(bundle1);
        detailOutlayFragment.setArguments(bundle1);
        list.add(detailOutlayFragment);
        list.add(detailIncomeFragment);


        MyAdapterViewPager adapter=new MyAdapterViewPager(getSupportFragmentManager(),list,arrTabTitles);
        viewPager_main.setAdapter(adapter);
        ///////////////////////以上是界面




    }


}