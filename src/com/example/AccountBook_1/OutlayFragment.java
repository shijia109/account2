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
import model.Income;
import model.Outlay;

import java.util.*;

/**
 * Created by shijia on 2015/4/10.
 */
public class OutlayFragment extends Fragment {
    private Spinner spinner_income;
    // private Spinner spinner_small;
    private List<Map<String,String>> list;
    private List<String> list_in;
    private ArrayAdapter<String>adapter;
    private TextView textView_date;
    private EditText editText_title;
    private EditText editText_money;
    private TextView textView_typeBig;
    private TextView textView_typeSmall;
    private Date date;
    private Calendar calendar;
    private int current_year,current_month,current_day;
    private int year,month,day;
    private DatePickerDialog datePickerDialog;
    private AlertDialog.Builder builder;
    private Context context;
    private String[] arrOutlayTypeBig;
    private String[][] arrOutlayTypeSmall;
    private int type_selection;
    private int type_selection_small;
    private Button button_save_exit;
    private Button button_save;
    private Button button_cancel;
    private String str_date;
    private String title;
    private float money;
    private String type_big;
    private String type_small;
    private  boolean hasError;

    private DbUtils dbUtils;
    private long curr_time;
  //  private String dateType;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
        builder=new AlertDialog.Builder(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // dateType=getArguments().getString("date_type");
        View view=inflater.inflate(R.layout.fragment_account_outlay,null);
        textView_date= (TextView) view.findViewById(R.id.textView_account_outlay_date);
        editText_title= (EditText) view.findViewById(R.id.editText_account_outlay_title);
        editText_money= (EditText) view.findViewById(R.id.editText_account_outlay_money);
        textView_typeBig= (TextView) view.findViewById(R.id.textView_account_outlay_type_big);
        textView_typeSmall= (TextView) view.findViewById(R.id.textView_account_outlay_type_small);

        //按钮的初始化
        button_save_exit= (Button) view.findViewById(R.id.button_account_outlay_save_and_exit);
        button_save= (Button) view.findViewById(R.id.button_account_outlay_save);
        button_cancel= (Button) view.findViewById(R.id.button_account_outlay_cancel);

        //Canlendar
        calendar=Calendar.getInstance();
        current_year=calendar.get(Calendar.YEAR);
        current_month=calendar.get(Calendar.MONTH);
        current_day=calendar.get(Calendar.DAY_OF_MONTH);
        curr_time=System.currentTimeMillis();
        year=current_year;
        month=current_month;
        day=current_day;

        //初始化两个数组
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

        textView_typeBig.setText(arrOutlayTypeBig[0]);
        textView_typeSmall.setText(arrOutlayTypeSmall[0][0]);
        //主选项监听
        textView_typeBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("支出总类别");
                builder.setSingleChoiceItems(arrOutlayTypeBig, type_selection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textView_typeBig.setText(arrOutlayTypeBig[i]);
                        textView_typeSmall.setText(arrOutlayTypeSmall[i][0]);
                        type_selection=i;
                      //  Toast.makeText(context,"i="+i,Toast.LENGTH_SHORT).show();
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
        //子选项监听
        textView_typeSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("支出子类别");
                builder.setSingleChoiceItems(arrOutlayTypeSmall[type_selection], type_selection_small, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textView_typeSmall.setText(arrOutlayTypeSmall[type_selection][i]);
                        type_selection_small=i;
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
                if(!hasError){
                    getActivity().finish();
                }
            }
        });

        //继续保存按钮
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSave();
                editText_title.setText("");
                editText_money.setText("");
                textView_typeBig.setText(arrOutlayTypeBig[0]);
                textView_typeSmall.setText(arrOutlayTypeSmall[0][0]);
                textView_date.setText(current_year+"-"+(current_month+1)+"-"+current_day);
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
                }
        );


        return view;
    }

    private void initData(){


        arrOutlayTypeBig = getResources().getStringArray(R.array.arrOutlayBig);
        //arrOutlayTypeSmall=getResources().getStringArray(R.array.)
        arrOutlayTypeSmall=new String[7][];
        arrOutlayTypeSmall[0]= getResources().getStringArray(R.array.arrOutlaySmall_0);
        arrOutlayTypeSmall[1]= getResources().getStringArray(R.array.arrOutlaySmall_1);
        arrOutlayTypeSmall[2]= getResources().getStringArray(R.array.arrOutlaySmall_2);
        arrOutlayTypeSmall[3]= getResources().getStringArray(R.array.arrOutlaySmall_3);
        arrOutlayTypeSmall[4]= getResources().getStringArray(R.array.arrOutlaySmall_4);
        arrOutlayTypeSmall[5]= getResources().getStringArray(R.array.arrOutlaySmall_5);
        arrOutlayTypeSmall[6]= getResources().getStringArray(R.array.arrOutlaySmall_6);




    }

    private  void clickSave(){
        str_date=textView_date.getText().toString().trim();
        title=editText_title.getText().toString().trim();
        if (title==null||"".equals(title)){
            title="无标题";
        }
        type_big=textView_typeBig.getText().toString().trim();
        type_small=textView_typeSmall.getText().toString().trim();
        try {
            String money_text=editText_money.getText().toString().trim();
            if(money_text!=null) {
                money = (float) (Math.round(Float.valueOf(money_text) * 100)) / 100;
                Outlay outlay=new Outlay();
                outlay.setYear(year);
                outlay.setMonth(month+1);
                outlay.setDay(day);
                outlay.setTime(curr_time);
                outlay.setTitle(title);
                outlay.setMoney(money);
                outlay.setType_big(type_big);
                outlay.setType_small(type_small);

                try {
                    dbUtils.save(outlay);//保存到数据库
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
