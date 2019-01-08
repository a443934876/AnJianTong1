package org.chinasafety.liu.anjiantong.view.adapter.viewholder;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.SearchCompanyInfo;
import org.chinasafety.liu.anjiantong.utils.StringUtils;
import org.chinasafety.liu.anjiantong.utils.rxbus.RxBus;
import org.chinasafety.liu.anjiantong.utils.rxbus.event.SetLocationEvent;
import org.chinasafety.liu.anjiantong.view.adapter.BaseHolder;
import org.chinasafety.liu.anjiantong.view.adapter.IParamContainer;
import org.chinasafety.liu.anjiantong.view.widget.sweet_dialog.SweetAlertDialog;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

/*
 * Created by mini on 17/5/25.
 */

public class SearchCompanyHolder extends BaseHolder<SearchCompanyInfo> {


    private TextView mCompanyName;
    private TextView mCompanyLocation;
    private TextView mBtnCompanyLocation;
    private Context mContext;

    public SearchCompanyHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        mCompanyName = (TextView) itemView.findViewById(R.id.item_search_company_name);
        mCompanyLocation = (TextView) itemView.findViewById(R.id.item_search_company_location);
        mBtnCompanyLocation = (TextView) itemView.findViewById(R.id.company_location);
        mBtnCompanyLocation.setText(Html.fromHtml("<u>" + "定位" + "</u>"));
    }

    @Override
    public void bind(List<SearchCompanyInfo> data, int position, IParamContainer container, final PublishSubject<SearchCompanyInfo> itemClick) {
        final SearchCompanyInfo companyInfo = data.get(position);
        if (companyInfo != null) {
            mCompanyName.setText(StringUtils.noNull(companyInfo.getCompanyName()));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onNext(companyInfo);
                }
            });
            mBtnCompanyLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLocationDialog(companyInfo);
                }
            });
        }
    }

    private void showLocationDialog(final SearchCompanyInfo companyInfo) {
        new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(
                        mContext.getResources().getString(R.string.dialog_default_title))
                .setContentText("确定将当前位置设置为【" + companyInfo.getCompanyName() + "】的位置吗？")
                .setCancelText("取消")
                .setConfirmText("是的！")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.cancel();
                RxBus.getInstance().send(new SetLocationEvent(companyInfo));

            }
        }).show();
    }
}
