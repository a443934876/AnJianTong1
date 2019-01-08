package org.chinasafety.liu.anjiantong.presenter;

import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;

import java.util.List;


public interface IMapSelectPresenter extends IBasePresenter {


    void getOrgFromDistance(double lat,double lng);

    interface IView extends IBasePresenter.IView{

        void addCompanyList(List<CompanyDetailInfo> value);

    }
}
