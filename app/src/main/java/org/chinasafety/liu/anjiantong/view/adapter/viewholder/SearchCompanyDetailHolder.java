package org.chinasafety.liu.anjiantong.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.SearchCompanyInfo;
import org.chinasafety.liu.anjiantong.utils.StringUtils;
import org.chinasafety.liu.anjiantong.view.adapter.BaseHolder;
import org.chinasafety.liu.anjiantong.view.adapter.IParamContainer;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

/*
 * Created by mini on 17/5/25.
 */

public class SearchCompanyDetailHolder extends BaseHolder<SearchCompanyInfo> {


    private TextView mCompanyName;

    public SearchCompanyDetailHolder(View itemView) {
        super(itemView);
        mCompanyName = (TextView) itemView.findViewById(R.id.item_search_company_name);
        TextView mCompanyLocation = (TextView) itemView.findViewById(R.id.item_search_company_location);
    }

    @Override
    public void bind(List<SearchCompanyInfo> data, int position, IParamContainer container, final PublishSubject<SearchCompanyInfo> itemClick) {
        final SearchCompanyInfo companyInfo = data.get(position);
        if (companyInfo != null) {
            mCompanyName.setText(StringUtils.noNull(companyInfo.getCompanyName()));
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onNext(companyInfo);
            }
        });
    }
}
