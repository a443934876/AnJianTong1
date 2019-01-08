package org.chinasafety.liu.anjiantong.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：Anfutong
 * 创建时间：2017/12/9 15:16
 * 注释说明：
 */

public class MyListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HashMap<String, Object>> data;
    private String[] keys;

    public MyListAdapter(ArrayList<HashMap<String, Object>> data,
                         Context context, String[] keys) {
        this.context = context;
        this.data = data;
        this.keys = keys;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.gwlist_item, null);
            vHolder = new ViewHolder();
            vHolder.iv = (ImageView) convertView.findViewById(R.id.gwitem_iv);
            vHolder.content = (TextView) convertView
                    .findViewById(R.id.gwitem_content);
            vHolder.comname = (TextView) convertView
                    .findViewById(R.id.gwitem_comname);
            vHolder.date = (TextView) convertView
                    .findViewById(R.id.gwitem_time);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, Object> map = data.get(position);
        if (map != null) {
            if ("".equals(StringUtil.noNull(map.get(keys[0])))) {
                vHolder.iv
                        .setImageResource(R.drawable.gwlb_icon3);
            } else if ("0".equals(StringUtil.noNull(map.get(keys[0])))) {
                vHolder.iv.setImageResource(R.drawable.gwlb_icon2);
            } else {
                vHolder.iv.setImageResource(R.drawable.gwlb_icon1);
            }
            vHolder.content.setText(StringUtil.noNull(map.get(keys[1])));
            vHolder.comname.setText(StringUtil.noNull(map.get(keys[2])));
            String date = StringUtil.noNull(map.get(keys[3]));
            if(date.length()>10){
                date =date.substring(0,10);
            }
            vHolder.date.setText(date);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView iv;
        TextView content;
        TextView comname;
        TextView date;
    }

}
