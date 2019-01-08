package org.chinasafety.liu.anjiantong.view.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.CompanyInfo;
import org.chinasafety.liu.anjiantong.view.adapter.BaseHolder;
import org.chinasafety.liu.anjiantong.view.adapter.IParamContainer;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

/*
 * Created by mini on 17/5/22.
 */

public class CompanyHolder extends BaseHolder<CompanyInfo> {


    private TextView mCompanyName;

    public CompanyHolder(View itemView) {
        super(itemView);
        mCompanyName = (TextView) itemView.findViewById(R.id.company_name);
    }

    @Override
    public void bind(List<CompanyInfo> data, int position, IParamContainer container, final PublishSubject<CompanyInfo> itemClick) {
        final CompanyInfo companyInfo = data.get(position);
        mCompanyName.setText(companyInfo.getComName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClick.onNext(companyInfo);
            }
        });

    }
}
