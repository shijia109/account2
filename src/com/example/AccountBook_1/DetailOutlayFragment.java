package com.example.AccountBook_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import model.Income;
import model.Outlay;

import java.util.*;

/**
 * Created by shijia on 2015/4/11.
 */
public class DetailOutlayFragment extends Fragment {

    private String dateType;
    private ListView listView_detail;

    private List<Map<String,Object>> list_listView_data_out;
    private List<Map<String,Object>> list_listView_data;

    private List<Income> list_income;
    private List<Outlay> list_outlay;

    private Calendar calendar;
    private int curr_year;
    private int curr_month;
    private int curr_day;
    private int DAY_OF_WEEK;

    private DbUtils dbUtils;
    private int year_mon,year_sun;
    private int month_mon,month_sun;
    private int day_mon,day_sun;
    private int tmp_year,tmp_month,tmp_day;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar=Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        curr_year=calendar.get(Calendar.YEAR);
        curr_month=calendar.get((Calendar.MONTH));
        curr_day=calendar.get(Calendar.DAY_OF_MONTH);
        DAY_OF_WEEK=calendar.get(Calendar.DAY_OF_WEEK);
        tmp_year=curr_year;
        tmp_month=curr_month;
        tmp_day=curr_day;
        initWeek();

        list_listView_data_out=new ArrayList<Map<String, Object>>();
        list_listView_data=new ArrayList<Map<String, Object>>();


        dbUtils = DbUtils.create(
                getActivity(),
                "account.db",
                1,//版本进行升级
                new DbUtils.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                        if (newVersion > oldVersion) {
                            //  db.execSQL("DROP TABLE IF EXISTS tb_collection");
                            //  onCreate(db);
                        }
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dateType=getArguments().getString("date_type");
        View view=inflater.inflate(R.layout.fragment_detail_outlay,container,false);
        listView_detail= (ListView) view.findViewById(R.id.listView_detail_outlay);

        try {
            list_income=dbUtils.findAll(Income.class);
            list_outlay=dbUtils.findAll(Outlay.class);
        } catch (DbException e) {
            e.printStackTrace();
        }

        Log.i("","DAY_OF_WEEK="+String.valueOf(Calendar.DAY_OF_WEEK)+", (curr_day - DAY_OF_WEEK + 7)="+ (curr_day - DAY_OF_WEEK + 7));

        if (list_outlay != null) {
            for (Outlay out : list_outlay) {
                if ("thisYear".equals(dateType)) {
                    if (curr_year == out.getYear()) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("date", out.getYear() + "-" + out.getMonth() + "-" + out.getDay());
                        map.put("title", out.getTitle());
                        map.put("type", out.getType_big());
                        map.put("time",out.getTime());
                        map.put("type_small", out.getType_small());
                        map.put("money", String.valueOf(out.getMoney()));
                        list_listView_data_out.add(map);
                    }
                } else if ("thisMonth".equals(dateType)) {
                    if (curr_year == out.getYear() && (curr_month + 1) == out.getMonth()) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("date", out.getYear() + "-" + out.getMonth() + "-" + out.getDay());
                        map.put("title", out.getTitle());
                        map.put("type", out.getType_big());
                        map.put("time",out.getTime());
                        map.put("type_small", out.getType_small());
                        map.put("money", String.valueOf(out.getMoney()));
                        list_listView_data_out.add(map);
                    }
                } else if ("thisWeek".equals(dateType)) {
                    if (curr_year == out.getYear() && (curr_month + 1) == out.getMonth() && out.getDay()>=day_mon&&out.getDay()<=day_sun) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("date", out.getYear() + "-" + out.getMonth() + "-" + out.getDay());
                        map.put("title", out.getTitle());
                        map.put("type", out.getType_big());
                        map.put("time",out.getTime());
                        map.put("type_small", out.getType_small());
                        map.put("money", String.valueOf(out.getMoney()));
                        list_listView_data_out.add(map);
                    }
                } else if ("today".equals(dateType)) {
                    if (curr_year == out.getYear() && (curr_month + 1) == out.getMonth() && curr_day == out.getDay()) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("date", out.getYear() + "-" + out.getMonth() + "-" + out.getDay());
                        map.put("title", out.getTitle());
                        map.put("type", out.getType_big());
                        map.put("time",out.getTime());
                        map.put("type_small", out.getType_small());
                        map.put("money", String.valueOf(out.getMoney()));
                        list_listView_data_out.add(map);
                    }
                }
            }
        }

        MyAdapterListView adapter=new MyAdapterListView(getActivity(),list_listView_data_out);
        listView_detail.setAdapter(adapter);


        return view;
    }



    public  void initWeek(){
        if(DAY_OF_WEEK!=1){
            tmp_day+=7;
            if(tmp_day>getDays(tmp_month, isLeap(tmp_year))){
                tmp_day-=getDays(tmp_month, isLeap(tmp_year));
                tmp_month+=1;
                if(tmp_month>12){
                    tmp_month=1;
                    tmp_year+=1;
                }
            }
            //  System.out.println("-->"+DAY_OF_WEEK);


            year_mon=tmp_year;
            month_mon=tmp_month;
            day_mon=tmp_day-DAY_OF_WEEK+2-7;
            System.out.println("周一是:"+tmp_year+"-"+tmp_month+"-"+day_mon);

            if((tmp_day-DAY_OF_WEEK+8-7)>getDays(tmp_month, isLeap(tmp_year))){
                tmp_day-=getDays(tmp_month,isLeap(tmp_year));
                tmp_month+=1;
                if(tmp_month>12){
                    tmp_month=1;
                    tmp_year+=1;
                }
            }

            year_sun=tmp_year;
            month_sun=tmp_year;
            day_sun=tmp_day-DAY_OF_WEEK+8-7;


            System.out.println("周天是:"+tmp_year+"-"+tmp_month+"-"+day_sun);

            //  arr[0]=new int[]{year,month,day};

            if((tmp_day-DAY_OF_WEEK+8)>getDays(tmp_month, isLeap(tmp_year))){
                tmp_day-=getDays(tmp_month,isLeap(tmp_year));
                tmp_month+=1;
                if(tmp_month>12){
                    tmp_month=1;
                    tmp_year+=1;
                }
            }


        }else{//是周天，向后算
            tmp_day-=7;
            if(tmp_day<1){
                tmp_month-=1;
                if(tmp_month<0){
                    tmp_year-=1;
                    tmp_month=12;
                }
                tmp_day+=getDays(tmp_month, isLeap(tmp_year));
                //    System.out.println("-->"+tmp_day);
            }



            year_mon=tmp_year;
            month_mon=tmp_month;
            day_mon=tmp_day-DAY_OF_WEEK+2;

            System.out.println("周一是:"+tmp_year+"-"+tmp_month+"-"+day_mon);


            if((tmp_day-DAY_OF_WEEK+8)>getDays(tmp_month, isLeap(tmp_year))){
                tmp_day-=getDays(tmp_month,isLeap(tmp_year));
                tmp_month+=1;
                if(tmp_month>12){
                    tmp_month=1;
                    tmp_year+=1;
                }
            }

            year_sun=tmp_year;
            month_sun=tmp_month;
            day_sun=tmp_day-DAY_OF_WEEK+8;



            System.out.println("周天是:"+tmp_year+"-"+tmp_month+"-"+day_sun);

        }

    }

    public static boolean isLeap(int year){
        Calendar calendar=Calendar.getInstance();
        calendar.set(year,2,1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);

        if(calendar.get(Calendar.DAY_OF_MONTH)==29){
            //  System.out.println("是润年！！！");
            return true;
        }
        else
        {
            //  System.out.println("不是闰年");
            return false;
        }

    }

    public static int getDays(int month_int,boolean isLeap){
        int days=0;
        if(month_int==0||month_int==2||month_int==4||month_int==6||month_int==7
                ||month_int==9||month_int==11){
            days=31;
        }else if(month_int==3||month_int==5||month_int==8||month_int==10){
            days=30;
        }else{
            //二月，如果是闰年，则有29天，否则有28天
            if(isLeap){
                days=29;
            }else{
                days=28;
            }
        }
        return days;
    }
}
