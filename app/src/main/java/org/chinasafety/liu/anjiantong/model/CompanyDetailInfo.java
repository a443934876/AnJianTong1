package org.chinasafety.liu.anjiantong.model;

import org.chinasafety.liu.anjiantong.utils.StringUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by mini on 17/5/30.
 */

public class CompanyDetailInfo implements Serializable{

    private static final String KEY_NAME ="orgname";
    private static final String KEY_LAT ="lat";
    private static final String KEY_LON ="lon";
//    private static final String KEY_PEOPLE ="recemname";
    private static final String KEY_PEOPLE ="mastername";
    private static final String KEY_ADDRESS ="orgaddress";
    private static final String KEY_PHONE ="orgphone";
    public static final String KEY_ORG_STR ="标识号";

    private String id;
    private String idStr;
    private String name;
    private double lat;
    private double lon;
    private String people;
    private String address;
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    private void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    private void setLon(double lon) {
        this.lon = lon;
    }

    public String getPeople() {
        return people;
    }

    private void setPeople(String people) {
        this.people = people;
    }

    public String getAddress() {
        return address;
    }

    private void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    private void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public static CompanyDetailInfo fromMap(HashMap<String,Object> result){
        CompanyDetailInfo info =new CompanyDetailInfo();
        info.setAddress(StringUtils.noNull(result.get(KEY_ADDRESS)));
        info.setName(StringUtils.noNull(result.get(KEY_NAME)));
        try {
            String latStr = StringUtils.noNull(result.get(KEY_LAT));
            double lat = Double.parseDouble(latStr);
            info.setLat(lat);
            String lonStr = StringUtils.noNull(result.get(KEY_LON));
            double lon = Double.parseDouble(lonStr);
            info.setLon(lon);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        info.setPeople(StringUtils.noNull(result.get(KEY_PEOPLE)));
        info.setPhone(StringUtils.noNull(result.get(KEY_PHONE)));

        return info;
    }
}
