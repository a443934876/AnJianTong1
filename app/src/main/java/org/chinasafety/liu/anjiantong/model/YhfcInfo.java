package org.chinasafety.liu.anjiantong.model;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table YHFC_INFO.
 */
public class YhfcInfo {

    private Long id;
    private String hTroubleID;
    private Boolean isYhzg;
    private String checkDate;
    private String limitDate;
    private String troubleGrade;
    private String checkObject;
    private String finishDate;
    private String safetyTrouble;
    private String actionOrgName;
    private String esCost;
    private String dightCost;
    private String LiabelEmid;
    private String LiabelName;
    private String areaName;
    private String induName;
    private String reviewDate;

    public YhfcInfo() {
    }

    public YhfcInfo(Long id) {
        this.id = id;
    }

    public YhfcInfo(Long id, String hTroubleID, Boolean isYhzg, String checkDate, String limitDate, String troubleGrade, String checkObject, String finishDate, String safetyTrouble, String actionOrgName, String esCost, String dightCost, String LiabelEmid, String LiabelName, String areaName, String induName, String reviewDate) {
        this.id = id;
        this.hTroubleID = hTroubleID;
        this.isYhzg = isYhzg;
        this.checkDate = checkDate;
        this.limitDate = limitDate;
        this.troubleGrade = troubleGrade;
        this.checkObject = checkObject;
        this.finishDate = finishDate;
        this.safetyTrouble = safetyTrouble;
        this.actionOrgName = actionOrgName;
        this.esCost = esCost;
        this.dightCost = dightCost;
        this.LiabelEmid = LiabelEmid;
        this.LiabelName = LiabelName;
        this.areaName = areaName;
        this.induName = induName;
        this.reviewDate = reviewDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHTroubleID() {
        return hTroubleID;
    }

    public void setHTroubleID(String hTroubleID) {
        this.hTroubleID = hTroubleID;
    }

    public Boolean getIsYhzg() {
        return isYhzg;
    }

    public void setIsYhzg(Boolean isYhzg) {
        this.isYhzg = isYhzg;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    public String getTroubleGrade() {
        return troubleGrade;
    }

    public void setTroubleGrade(String troubleGrade) {
        this.troubleGrade = troubleGrade;
    }

    public String getCheckObject() {
        return checkObject;
    }

    public void setCheckObject(String checkObject) {
        this.checkObject = checkObject;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public String getSafetyTrouble() {
        return safetyTrouble;
    }

    public void setSafetyTrouble(String safetyTrouble) {
        this.safetyTrouble = safetyTrouble;
    }

    public String getActionOrgName() {
        return actionOrgName;
    }

    public void setActionOrgName(String actionOrgName) {
        this.actionOrgName = actionOrgName;
    }

    public String getEsCost() {
        return esCost;
    }

    public void setEsCost(String esCost) {
        this.esCost = esCost;
    }

    public String getDightCost() {
        return dightCost;
    }

    public void setDightCost(String dightCost) {
        this.dightCost = dightCost;
    }

    public String getLiabelEmid() {
        return LiabelEmid;
    }

    public void setLiabelEmid(String LiabelEmid) {
        this.LiabelEmid = LiabelEmid;
    }

    public String getLiabelName() {
        return LiabelName;
    }

    public void setLiabelName(String LiabelName) {
        this.LiabelName = LiabelName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getInduName() {
        return induName;
    }

    public void setInduName(String induName) {
        this.induName = induName;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

}
