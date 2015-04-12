package com.example.AccountBook_1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by shijia on 2015/4/10.
 */
public class MyAdapterListView extends BaseAdapter {
    private List<Map<String,Object>> list;
    private Context context;

    public MyAdapterListView(Context context,List<Map<String, Object>> list ) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.list_item,null);
            viewHolder.textView_date= (TextView) convertView.findViewById(R.id.textView_detail_date);
            viewHolder.textView_title= (TextView) convertView.findViewById(R.id.textView_detail_title);
            viewHolder.textView_type= (TextView) convertView.findViewById(R.id.textView_detail_type);
            viewHolder.textView_money= (TextView) convertView.findViewById(R.id.textView_detail_money);
            convertView.setTag(viewHolder);
        }else
            viewHolder= (ViewHolder) convertView.getTag();
        viewHolder.textView_date.setText((String)list.get(i).get("date").toString());
        viewHolder.textView_title.setText((String)list.get(i).get("title").toString());
        viewHolder.textView_type.setText((String)list.get(i).get("type").toString());
        if(list.get(i).get("type_small")==null) {
            viewHolder.textView_money.setText((String) list.get(i).get("money").toString());
            viewHolder.textView_money.setTextColor(Color.rgb(233,17,17));
        }else{
            viewHolder.textView_money.setText("-"+(String) list.get(i).get("money").toString());
            viewHolder.textView_money.setTextColor(Color.rgb(17, 233, 17));
        }

        return convertView;
    }


    class ViewHolder{
        TextView textView_date;
        TextView textView_title;
        TextView textView_type;
        TextView textView_money;
    }
}
