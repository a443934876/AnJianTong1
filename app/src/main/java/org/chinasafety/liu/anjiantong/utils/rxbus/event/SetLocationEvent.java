package org.chinasafety.liu.anjiantong.utils.rxbus.event;

import org.chinasafety.liu.anjiantong.model.SearchCompanyInfo;

/**
 * 作用：
 * Created by cqj on 2017-08-08.
 */
public class SetLocationEvent {

    private SearchCompanyInfo companyInfo;

    public SetLocationEvent(SearchCompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    public SearchCompanyInfo getCompanyInfo() {
        return companyInfo;
    }
}
