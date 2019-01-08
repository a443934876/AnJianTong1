package org.chinasafety.liu.anjiantong.model;

/**
 * Created by mini on 17/5/25.
 */

public class SearchCompanyInfo implements IChooseItem{

    private String companyId;
    private String companyName;
    private String companyLocation;
    private boolean isChoose;

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyId() {
        return companyId;
    }


    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    @Override
    public String getItemName() {
        return companyName;
    }

    @Override
    public String getItemId() {
        return companyId;
    }


}
