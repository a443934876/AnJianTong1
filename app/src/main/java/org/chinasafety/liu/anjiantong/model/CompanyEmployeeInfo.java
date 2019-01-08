package org.chinasafety.liu.anjiantong.model;

import org.chinasafety.liu.anjiantong.utils.StringUtils;

import java.util.HashMap;

/**
 * 项目名称：Anfutong1
 * 创建时间：2017/12/13 8:56
 * 注释说明：
 */

public class CompanyEmployeeInfo {
    private static final String KEY_NAME = "emName";
    private static final String KEY_EMAIL = "emailAddress";
    private static final String KEY_PART = "tempPart";
    private static final String KEY_DUTY = "tempDuty";
    private static final String KEY_EMID = "Emid";
    private static final String KEY_ADDRESS = "orgaddress";
    private static final String KEY_OPHONE = "officePhone";
    private static final String KEY_MPHONE = "mobilePhone";
    private static final String KEY_QQ = "qqNumber";


    private String id;
    private String idStr;
    private String name;
    private String email;
    private String part;
    private String duty;
    private String emid;
    private String address;
    private String ophone;
    private String mphone;
    private String qq;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getEmid() {
        return emid;
    }

    public void setEmid(String emid) {
        this.emid = emid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOphone() {
        return ophone;
    }

    public void setOphone(String ophone) {
        this.ophone = ophone;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public static CompanyEmployeeInfo fromMap(HashMap<String, Object> result) {
        CompanyEmployeeInfo info = new CompanyEmployeeInfo();
        info.setName(StringUtils.noNull(result.get(KEY_NAME)));
        info.setEmail(StringUtils.noNull(result.get(KEY_EMAIL)));
        info.setPart(StringUtils.noNull(result.get(KEY_PART)));
        info.setDuty(StringUtils.noNull(result.get(KEY_DUTY)));
        info.setEmid(StringUtils.noNull(result.get(KEY_EMID)));
        info.setAddress(StringUtils.noNull(result.get(KEY_ADDRESS)));
        info.setOphone(StringUtils.noNull(result.get(KEY_OPHONE)));
        info.setMphone(StringUtils.noNull(result.get(KEY_MPHONE)));
        info.setQq(StringUtils.noNull(result.get(KEY_QQ)));
        return info;
    }
}
