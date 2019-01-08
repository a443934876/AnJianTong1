package org.chinasafety.liu.anjiantong.utils;

import java.io.File;

/**
 * Created by mini on 17/5/21.
 */

public class StringUtils {

    public static String noNull(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    public static String integerNoNull(Object obj) {
        if (obj == null) {
            return "0";
        } else {
            return obj.toString();
        }
    }

    public static   boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);

        if (!file.exists()) {
            if (file.mkdir()) {
                return true;
            } else
                return false;
        }
        return true;
    }
}
