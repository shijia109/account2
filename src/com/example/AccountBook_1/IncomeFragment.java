package com.example.AccountBook_1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import model.Income;

import java.util.*;

/**
 * Created by shijia on 2015/4/10.
 */
public class IncomeFragment extends Fragment {
    private Spinner spinner_income;
   // private Spinner spinner_small;
    private List<Map<String,String>> list;
    private List<String> list_in;
    private ArrayAdapter<String>adapter;
    private TextView textView_date;
    private EditText editText_title;
    private EditText editText_money;
    private TextView textView_type;
    private Date date;
    private Calendar calendar;
    private int current_year,current_month,current_day;
    private int year,month,day;
    private DatePickerDialog datePickerDialog;
    private AlertDialog.Builder builder;
    private Context context;
    private String[] arrIncomeType;
    private int type_selection;
    private Button button_save_exit;
    private Button button_save;
    private Button button_cancel;
    private String str_date;
    private String title;
    private float money;
    private String type;
    private  boolean hasError;

    private DbUtils dbUtils;
    private long curr_time;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        builder=new AlertDialog.Builder(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_account_income,null);
        textView_date= (TextView) view.findViewById(R.id.textView_account_income_date);
        editText_title= (EditText) view.findViewById(R.id.editText_account_income_title);
        editText_money= (EditText) view.findViewById(R.id.editText_account_income_money);
        textView_type= (TextView) view.findViewById(R.id.textView_account_income_type);

        //按钮的初始化
        button_save_exit= (Button) view.findViewById(R.id.button_account_income_save_and_exit);
        button_save= (Button) view.findViewById(R.id.button_account_income_save);
        button_cancel= (Button) view.findViewById(R.id.button_account_income_cancel);

        //Canlendar
        calendar=Calendar.getInstance();
        current_year=calendar.get(Calendar.YEAR);
        current_month=calendar.get(Calendar.MONTH);
        current_day=calendar.get(Calendar.DAY_OF_MONTH);
        curr_time=System.currentTimeMillis();

        year=current_year;
        month=current_month;
        day=current_day;

        initData();
        textView_date.setText(current_year+"-"+(current_month+1)+"-"+current_day);
        textView_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(context,"点击了date",Toast.LENGTH_SHORT).show();;
                datePickerDialog=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        year=i;
                        month=i1;
                        day=i2;
                        textView_date.setText(year+"-"+(month+1)+"-"+day);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        textView_type.setText(arrIncomeType[0]);
        textView_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("收入类别");
                builder.setSingleChoiceItems(arrIncomeType, type_selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textView_type.setText(arrIncomeType[i]);
                        type_selection=i;
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }
        });

        //三个按钮监听

        //保存按钮
        button_save_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSave();
                if(!hasError) {
                    getActivity().finish();
                }
            }
        });

        //继续保存按钮
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSave();
                if(!hasError) {
                    editText_title.setText("");
                    editText_money.setText("");
                    textView_type.setText(arrIncomeType[0]);
                    textView_date.setText(current_year+"-"+current_month+"-"+current_day);
                }
            }
        });

        //取消按钮
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });



        //////////////////////数据库相关
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
                });




        return view;
    }

    private void initData(){

        list_in=new ArrayList<String>();
        arrIncomeType = getResources().getStringArray(R.array.arrIncome);
        for (int i = 0; i < arrIncomeType.length; i++) {
            list_in.add(arrIncomeType[i]);
        }
    }

    public IncomeFragment() {
    }

    private  void clickSave(){
        str_date=textView_date.getText().toString().trim();
        title=editText_title.getText().toString().trim();
        if (title==null||"".equals(title)){
            title="无标题";
        }
        type=textView_type.getText().toString().trim();
        try {
            String money_text=editText_money.getText().toString().trim();
            if(money_text!=null||"".equals(money_text)) {
                money = (float) (Math.round(Float.valueOf(money_text) * 100)) / 100;

                Income income=new Income();
                income.setYear(year);
                income.setMonth(month+1);
                income.setDay(day);
                income.setTime(curr_time);
                income.setTitle(title);
                income.setMoney(money);
                income.setType(type);

                try {
                    dbUtils.save(income);//保存到数据库
                    hasError=false;
                }catch (Exception e){
                    hasError=true;
                    Toast.makeText(context,"数据库存储错误",Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(context,"请输入金额",Toast.LENGTH_SHORT).show();
                hasError=true;
            }

        }catch(Exception e){
            Toast.makeText(context,"金额输入有误！",Toast.LENGTH_SHORT).show();
            hasError=true;
        }
    }

}
