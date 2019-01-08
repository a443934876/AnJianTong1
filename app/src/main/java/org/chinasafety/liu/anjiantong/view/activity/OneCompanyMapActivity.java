package org.chinasafety.liu.anjiantong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;

import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;
import org.chinasafety.liu.anjiantong.presenter.IMapSelectPresenter;
import org.chinasafety.liu.anjiantong.presenter.impl.OneCompanyMapPresenter;

import java.util.List;

public class OneCompanyMapActivity extends MapSelectActivity {

    public static final String KEY_ORG_ID = "orgId";

    @Override
    protected IMapSelectPresenter getPresenter() {
        return new OneCompanyMapPresenter(getIntent().getStringExtra(KEY_ORG_ID),this);
    }

    public static void start(Context context,String orgId) {
        Intent starter = new Intent(context, OneCompanyMapActivity.class);
        starter.putExtra(KEY_ORG_ID,orgId);
        context.startActivity(starter);
    }

    @Override
    public void addCompanyList(List<CompanyDetailInfo> value) {
        if (value.get(0).getName()==null){
            Toast.makeText(this, " 无此公司信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (value.get(0).getLat()==0 || value.get(0).getLon()==0){
            Toast.makeText(this, "此公司无坐标！", Toast.LENGTH_SHORT).show();
            return;
        }
        super.addCompanyList(value);
        if(value.size()>0){
            CompanyDetailInfo companyDetailInfo = value.get(0);
            setInfoToDetail(companyDetailInfo);
            LatLng latLng = new LatLng(companyDetailInfo.getLat(),
                    companyDetailInfo.getLon());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(latLng).zoom(15.0f);
            animateMapStatus(builder.build());
        }
    }
}
