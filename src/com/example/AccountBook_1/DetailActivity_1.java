package com.example.AccountBook_1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.*;


/**
 * Created by shijia on 2015/4/10.
 */
public class DetailActivity_1 extends Activity {
    private String dateType;
    private List<Map<String,Object>> list_listView_data_in;
    private List<Map<String,Object>> list_listView_data_out;
    private List<Map<String,Object>> list_db_income;
    private List<Map<String,Object>> list_db_outlay;
    private ListView listView_detail;
    private MySQLiteOpenHelper myHelper;
    private Calendar calendar;
    private int curr_year,curr_month,curr_day,DAY_OF_WEEK;
    private  String sql;
    private List<Map<String,Object>> list_listView_data;
    private int contextMenuPosition;
    private AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_detail_1);

        dateType=getIntent().getExtras().get("date_type").toString();

        //Toast.makeText(this,dateType,Toast.LENGTH_SHORT).show();

        listView_detail= (ListView) findViewById(R.id.listView_detail);
        myHelper=new MySQLiteOpenHelper(this);
        list_listView_data_in=new ArrayList<Map<String, Object>>();
        list_listView_data_out=new ArrayList<Map<String, Object>>();
        list_listView_data=new ArrayList<Map<String, Object>>();


        //日期初始化
        calendar=Calendar.getInstance();
        curr_year=calendar.get(Calendar.YEAR);
        curr_month=calendar.get(Calendar.MONTH)+1;
        curr_day=calendar.get(Calendar.DAY_OF_MONTH);
        DAY_OF_WEEK=calendar.get(Calendar.DAY_OF_WEEK);

        //

        //获取数据库数据
        list_db_income=myHelper.selectList("select * from tb_income",null);
        list_db_outlay=myHelper.selectList("select * from tb_outlay",null);



        if("today".equals(dateType)){
            for (int i = 0; i < list_db_income.size(); i++) {
                String db_date=list_db_income.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year&&curr_month==db_month&&curr_day==db_day){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_income.get(i).get("date").toString());
                    map.put("title",list_db_income.get(i).get("title").toString());
                    map.put("type",list_db_income.get(i).get("type").toString());
                    map.put("table","tb_income");
                    map.put("money",list_db_income.get(i).get("money").toString());
                    list_listView_data_in.add(map);
                }
            }

            for (int i = 0; i < list_db_outlay.size(); i++) {
                String db_date=list_db_outlay.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year&&curr_month==db_month&&curr_day==db_day){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_outlay.get(i).get("date").toString());
                    map.put("title",list_db_outlay.get(i).get("title").toString());
                    map.put("type",list_db_outlay.get(i).get("type_big").toString());
                    map.put("type_small",list_db_outlay.get(i).get("type_small").toString());
                    map.put("table","tb_outlay");
                    map.put("money",list_db_outlay.get(i).get("money").toString());
                    list_listView_data_out.add(map);
                }
            }


        }else  if("thisWeek".equals(dateType)){
            for (int i = 0; i < list_db_income.size(); i++) {
                String db_date=list_db_income.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year&&curr_month==db_month){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_income.get(i).get("date").toString());
                    map.put("title",list_db_income.get(i).get("title").toString());
                    map.put("type",list_db_income.get(i).get("type").toString());
                    map.put("table","tb_income");
                    map.put("money",list_db_income.get(i).get("money").toString());
                    list_listView_data_in.add(map);
                }
            }

            for (int i = 0; i < list_db_outlay.size(); i++) {
                String db_date=list_db_outlay.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year&&curr_month==db_month&&db_day>=DAY_OF_WEEK&&db_day<DAY_OF_WEEK+7){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_outlay.get(i).get("date").toString());
                    map.put("title",list_db_outlay.get(i).get("title").toString());
                    map.put("type",list_db_outlay.get(i).get("type_big").toString());
                    map.put("type_small",list_db_outlay.get(i).get("type_small").toString());
                    map.put("table","tb_outlay");
                    map.put("money",list_db_outlay.get(i).get("money").toString());
                    list_listView_data_out.add(map);
                }
            }

        }else if("thisMonth".equals(dateType)){
            for (int i = 0; i < list_db_income.size(); i++) {
                String db_date=list_db_income.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year&&curr_month==db_month){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_income.get(i).get("date").toString());
                    map.put("title",list_db_income.get(i).get("title").toString());
                    map.put("type",list_db_income.get(i).get("type").toString());
                    map.put("table","tb_income");
                    map.put("money",list_db_income.get(i).get("money").toString());
                    list_listView_data_in.add(map);
                }
            }

            for (int i = 0; i < list_db_outlay.size(); i++) {
                String db_date=list_db_outlay.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year&&curr_month==db_month){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_outlay.get(i).get("date").toString());
                    map.put("title",list_db_outlay.get(i).get("title").toString());
                    map.put("type",list_db_outlay.get(i).get("type_big").toString());
                    map.put("type_small",list_db_outlay.get(i).get("type_small").toString());
                    map.put("table","tb_outlay");
                    map.put("money",list_db_outlay.get(i).get("money").toString());
                    list_listView_data_out.add(map);
                }
            }

        }else if("thisYear".equals(dateType)){
            for (int i = 0; i < list_db_income.size(); i++) {
                String db_date=list_db_income.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_income.get(i).get("date").toString());
                    map.put("title",list_db_income.get(i).get("title").toString());
                    map.put("type",list_db_income.get(i).get("type").toString());
                    map.put("table","tb_income");
                    map.put("money",list_db_income.get(i).get("money").toString());
                    map.put("id",list_db_income.get(i).get("_id").toString());
                    list_listView_data_in.add(map);
                }
            }

            for (int i = 0; i < list_db_outlay.size(); i++) {
                String db_date=list_db_outlay.get(i).get("date").toString();
                String[] arrDate=db_date.split("-");
                int db_year=Integer.parseInt( arrDate[0]);
                int db_month=Integer.parseInt(arrDate[1]) ;
                int db_day=Integer.parseInt(arrDate[2]);
                if(curr_year==db_year){
                    Map<String,Object> map=new HashMap<String,Object>();
                    map.put("date",list_db_outlay.get(i).get("date").toString());
                    map.put("title",list_db_outlay.get(i).get("title").toString());
                    map.put("type",list_db_outlay.get(i).get("type_big").toString());
                    map.put("type_small",list_db_outlay.get(i).get("type_small").toString());
                    map.put("table","tb_outlay");
                    map.put("money",list_db_outlay.get(i).get("money").toString());
                    map.put("id",list_db_income.get(i).get("_id").toString());
                    list_listView_data_out.add(map);
                }
            }




        }


        list_listView_data.addAll(list_listView_data_in);
        list_listView_data.addAll(list_listView_data_out);
        MyAdapterListView adapter=new MyAdapterListView(this,list_listView_data);
        listView_detail.setAdapter(adapter);

        registerForContextMenu(listView_detail);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
       // menu.setHeaderIcon(R.drawable.ic_launcher);
        contextMenuPosition=info.position;
        menu.setHeaderTitle("选择操作");
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
       // AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        switch (item.getItemId()){
            case R.id.action_context_delete:
                builder=new AlertDialog.Builder(this);
                builder.setTitle("提示");
                String ac_type=list_listView_data.get(contextMenuPosition).get("table").toString();
                String acType;
                if("tb_income".equals(ac_type)){
                    acType="收入";
                }else{
                    acType="支出";
                }
                builder.setMessage("确定删除 " + list_listView_data.get(contextMenuPosition).get("date").toString() +" 的"+acType+"记录 \""
                        + list_listView_data.get(contextMenuPosition).get("title").toString()+"\" 吗？");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myHelper.execData("delete from ? where id=?",new String[]{});
                    }
                });
                builder.create().show();
                break;
            case R.id.action_context_edit:
                break;
        }

        return super.onContextItemSelected(item);
    }
}