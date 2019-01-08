package org.chinasafety.liu.anjiantong.model.provider;

import org.chinasafety.liu.anjiantong.model.CompanyInfo;
import org.chinasafety.liu.anjiantong.model.UserInfo;

/**
 * Created by mini on 17/5/21.
 */

public enum GlobalDataProvider {
    //单例
    INSTANCE;

    private UserInfo mUserInfo;
    private CompanyInfo mCompanyInfo;

    private String mSafeCheckThisObjComId;

    public String getSafeCheckThisObjComId() {
        return mSafeCheckThisObjComId;

    }

    public void setSafeCheckThisObjComId(String safeCheckThisObjComId) {
        mSafeCheckThisObjComId = safeCheckThisObjComId;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public CompanyInfo getCompanyInfo() {
        return mCompanyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        mCompanyInfo = companyInfo;
    }
}
