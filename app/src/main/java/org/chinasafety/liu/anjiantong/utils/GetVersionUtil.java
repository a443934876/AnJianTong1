package org.chinasafety.liu.anjiantong.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 项目名称：Anfutong1
 * 创建时间：2017/12/14 20:25
 * 注释说明：
 */

public class GetVersionUtil {
    public static int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}

