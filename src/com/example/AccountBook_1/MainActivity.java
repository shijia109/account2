package com.example.AccountBook_1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.DbModelSelector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import model.Income;
import model.Outlay;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private Button button_keepAccount;
  //  private MySQLiteOpenHelper myHealper;
    private Calendar calendar;
    private int curr_year;
    private int curr_month;
    private int curr_day;

    private TextView textView_income_thisMonth;
    private TextView textView_outlay_thisMonth;
    private TextView textView_remain_thisMonth;

    private TextView textView_income_today;
    private TextView textView_outlay_today;

    private TextView textView_income_thisWeek;
    private TextView textView_outLay_thisWeek;

    private TextView textView_income_thisYear;
    private TextView textView_outlay_thisYear;

    private TextView textView_month_now;

    private   int DAY_OF_WEEK;





    private List<Map<String,Object>> list_income,list_outlay;

    private LinearLayout layout_today;
    private  LinearLayout layout_thisWeek;
    private  LinearLayout layout_thisMonth;
    private  LinearLayout layout_thisYear;

    private DbUtils dbUtils;

    private int year_mon,year_sun;
    private int month_mon,month_sun;
    private int day_mon,day_sun;
    private int tmp_year,tmp_month,tmp_day;



    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.main);

        button_keepAccount= (Button) findViewById(R.id.button_keep_account);
       // myHealper=new MySQLiteOpenHelper(this);
        //获得当前日期
        calendar=Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        curr_year=calendar.get(Calendar.YEAR);
        curr_month=calendar.get((Calendar.MONTH))+1;
        curr_day=calendar.get(Calendar.DAY_OF_MONTH);
        DAY_OF_WEEK=calendar.get(Calendar.DAY_OF_WEEK);
        tmp_year=curr_year;
        tmp_month=curr_month;
        tmp_day=curr_day;

        initWeek();//week初始化

        //控件的初始化
        textView_income_thisMonth= (TextView) findViewById(R.id.textView_main_income_this_month);
        textView_outlay_thisMonth= (TextView) findViewById(R.id.textView_main_outlay_this_month);
        textView_remain_thisMonth= (TextView) findViewById(R.id.textView_main_remaining_this_month);

        textView_income_today= (TextView) findViewById(R.id.textView_main_income_today);
        textView_outlay_today= (TextView) findViewById(R.id.textView_main_outlay_today);

        textView_income_thisYear= (TextView) findViewById(R.id.textView_main_income_this_year);
        textView_outlay_thisYear= (TextView) findViewById(R.id.textView_main_outlay_this_year);

        textView_income_thisWeek= (TextView) findViewById(R.id.textView_main_income_this_week);
        textView_outLay_thisWeek= (TextView) findViewById(R.id.textView_main_outlay_this_week);

        textView_month_now= (TextView) findViewById(R.id.textView_main_month_now);

        //下边3个导航条及上面一个
        layout_today= (LinearLayout) findViewById(R.id.layout_main_today);
        layout_thisWeek= (LinearLayout) findViewById(R.id.layout_main_this_week);
        layout_thisYear= (LinearLayout) findViewById(R.id.layout_main__this_year);
        layout_thisMonth= (LinearLayout) findViewById(R.id.layout_main__this_month);

        //////////点击监听
        //----------------------记账按钮
        button_keepAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AccountActivity.class);
                startActivity(intent);
            }
        });

        //-------今天
        layout_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("date_type","today");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        layout_thisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("date_type","thisWeek");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        layout_thisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("date_type","thisMonth");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        layout_thisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("date_type","thisYear");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        dbUtils = DbUtils.create(
                this,
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

    @Override
    protected void onResume() {
        super.onResume();
        getDBdata();
        textView_month_now.setText((curr_month)+"月");

    }


    private  void getDBdata(){
        Log.i("","------>getFirstDayOfWeek="+calendar.getFirstDayOfWeek());
         float sum_income_today=0,sum_outlay_today=0;
         float sum_income_thisMonth=0,sum_outlay_thisMonth=0;
         float sum_income_thisYear=0,sum_outlay_thisYear=0;
         float sum_income_thisWeek=0,sum_outlay_thisWeek=0;


        try {
            List<Income> list_in=dbUtils.findAll(Income.class);
            if(list_in!=null) {
                for (Income in : list_in) {
                    if (in.getYear() == curr_year) {
                        sum_income_thisYear += in.getMoney();
                        if (in.getMonth() == curr_month) {
                            sum_income_thisMonth += in.getMoney();
                            if(in.getDay()==curr_day){
                                sum_income_today+=in.getMoney();
                            }
                        }
                    }
                    if(in.getDay()>=day_mon&&in.getDay()<=day_sun&&curr_year==in.getYear()&&curr_month==month_mon){
                        sum_income_thisWeek+=in.getMoney();

                    }


                }

                textView_income_today.setText(String.valueOf(sum_income_today));
                textView_income_thisYear.setText(String.valueOf(sum_income_thisYear));
                textView_income_thisMonth.setText(String.valueOf(sum_income_thisMonth));
                textView_income_thisWeek.setText(String.valueOf(sum_income_thisWeek));
            }
        } catch (DbException e) {
            e.printStackTrace();
            Toast.makeText(this,"读取收入数据库出错",Toast.LENGTH_SHORT).show();
            Log.i("","读取收入数据库出错");
        }


        try {
            List<Outlay> list_out=dbUtils.findAll(Outlay.class);
            if(list_out!=null) {
                for (Outlay out : list_out) {
                    if (out.getYear() == curr_year) {
                        sum_outlay_thisYear += out.getMoney();
                        if (out.getMonth() == curr_month) {
                            sum_outlay_thisMonth += out.getMoney();
                            if (out.getDay() == curr_day) {
                                sum_outlay_today += out.getMoney();
                            }

                        }
                    }
                    if(out.getDay()>=day_mon&&out.getDay()<=day_sun&&curr_year==out.getYear()&&curr_month==month_mon){
                        sum_outlay_thisWeek+=out.getMoney();

                    }
                }
                textView_outlay_today.setText(String.valueOf(sum_outlay_today));
                textView_outlay_thisYear.setText(String.valueOf(sum_outlay_thisYear));
                textView_outlay_thisMonth.setText(String.valueOf(sum_outlay_thisMonth));
                textView_outLay_thisWeek.setText(String.valueOf(sum_outlay_thisWeek));
                float remain=sum_income_thisMonth-sum_outlay_thisMonth;
                textView_remain_thisMonth.setText(String.valueOf(remain));
            }
        } catch (DbException e) {
            e.printStackTrace();
            Toast.makeText(this,"读取支出数据库出错",Toast.LENGTH_SHORT).show();
            Log.i("","读取支出数据库出错");
        }







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
