package org.chinasafety.liu.anjiantong.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferenceUtil
 * Created by cqj on 17/5/22.
 */

public class SharedPreferenceUtil {

    private SharedPreferenceUtil(){
        throw new IllegalArgumentException();
    }

    public static void savePreferences(Context context,String key,String value){
        SharedPreferences sp =context.getSharedPreferences(AppConstant.SP_CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key,value);
        edit.apply();
    }

    public static String getString(Context context,String key){
        return getString(context, key,"");
    }


    public static String getString(Context context,String key,String defaultValue){
        SharedPreferences sp =context.getSharedPreferences(AppConstant.SP_CONFIG, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

}
