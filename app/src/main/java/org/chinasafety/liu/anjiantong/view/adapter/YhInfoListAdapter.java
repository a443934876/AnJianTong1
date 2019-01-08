package org.chinasafety.liu.anjiantong.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.YhfcInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



/*
 * Created by Administrator on 2016/5/4.
 */

public class YhInfoListAdapter extends BaseAdapter {

    private Context mContext;
    private List<YhfcInfo> mInfoList;

    public YhInfoListAdapter(Context context, List<YhfcInfo> pInfoList) {
        mContext = context;
        mInfoList = pInfoList;
    }

    @Override
    public int getCount() {
        return mInfoList == null ? 0 : mInfoList.size();
    }

    @Override
    public YhfcInfo getItem(int pI) {
        return mInfoList == null ? null : mInfoList.get(pI);
    }

    @Override
    public long getItemId(int pI) {
        return pI;
    }

    @Override
    public View getView(int pI, View convertView, ViewGroup pViewGroup) {
        ViewHolder holder =null;
        if(convertView ==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_yhinfo,pViewGroup,false);
            holder.background = (LinearLayout) convertView.findViewById(R.id.review_info_item_background);
            holder.companyName = (TextView) convertView.findViewById(R.id.item_yhinfo_company_name);
            holder.infoText = (TextView) convertView.findViewById(R.id.item_yhinfo_text);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        YhfcInfo yhfcInfo =getItem(pI);
        if(yhfcInfo!=null){
            String text = String.format("%s>%s>%s",yhfcInfo.getActionOrgName(),yhfcInfo.getTroubleGrade(),yhfcInfo.getSafetyTrouble());
            holder.infoText.setText(text);
            holder.companyName.setText(yhfcInfo.getCheckObject());
            try {
                Calendar otherCalendar = Calendar.getInstance();
                SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = sFormat.parse(yhfcInfo.getFinishDate());
                otherCalendar.setTime(date);
                Calendar nowCalendar = Calendar.getInstance();
                int ret = otherCalendar.compareTo(nowCalendar);
                if(ret==1){
                    holder.background.setBackgroundColor(mContext.getResources().getColor(R.color.blue_btn_bg_pressed_color));
                }else{
                    holder.background.setBackgroundColor(mContext.getResources().getColor(R.color.ored));
                }
            } catch (ParseException pE) {
                pE.printStackTrace();
            }
        }

        return convertView;
    }

    private final class ViewHolder{
        LinearLayout background;
        TextView companyName;
        TextView infoText;
    }
}
