package org.chinasafety.liu.anjiantong.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mini on 17/5/21.
 */

public class UserInfo {

    private static final String KEY_NAME="姓名";
    private static final String KEY_NICK_NAME ="昵称";
    private static final String KEY_PWD ="密码";
    private static final String KEY_UID ="Uid";
    private static final String KEY_CHANNELS ="通道";
    private static final String KEY_SERIAL_NUM ="序列号";
    private static final String KEY_PHONE_NUM ="手机";

    private static final String SEPARATOR = "##";


    private String name;
    private String pwd;
    private String phone;
    private String uid;
    private String channels;
    private String serialNum;
    private String nickName;


    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getPhone() {
        return phone;
    }

    public String getUid() {
        return uid;
    }

    public String getChannels() {
        return channels;
    }

    public String getNickName() {
        return nickName;
    }

    public String getSerialNum() {
        return serialNum;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(SEPARATOR);
        sb.append(pwd);
        sb.append(SEPARATOR);
        sb.append(phone);
        sb.append(SEPARATOR);
        sb.append(uid);
        sb.append(SEPARATOR);
        sb.append(channels);
        sb.append(SEPARATOR);
        sb.append(nickName);
        sb.append(SEPARATOR);
        sb.append(serialNum);
        return sb.toString();
    }

    public static UserInfo fromString(String saveString) throws Exception{
        if(TextUtils.isEmpty(saveString)){
            throw new IllegalArgumentException("字符串为空");
        }
        String[] params = saveString.split(SEPARATOR);
        if(params.length<7){
            throw new IllegalArgumentException("传入的不是UserInfo的数据");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.name = params[0];
        userInfo.pwd = params[1];
        userInfo.phone = params[2];
        userInfo.uid = params[3];
        userInfo.channels = params[4];
        userInfo.nickName = params[5];
        userInfo.serialNum = params[6];
        return userInfo;
    }

    @Nullable
    public static UserInfo fromData(ArrayList<HashMap<String, Object>> result) throws Exception{
        if(result==null || result.isEmpty()){
            return null;
        }
        UserInfo info =new UserInfo();
        HashMap<String, Object> map = result.get(0);
        info.name = (String) map.get(KEY_NAME);
        info.pwd = (String) map.get(KEY_PWD);
        info.phone = (String) map.get(KEY_PHONE_NUM);
        info.uid = (String) map.get(KEY_UID);
        info.channels = (String) map.get(KEY_CHANNELS);
        info.serialNum = (String) map.get(KEY_SERIAL_NUM);
        info.nickName = (String) map.get(KEY_NICK_NAME);
        return info;
    }
}
