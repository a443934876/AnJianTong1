package org.chinasafety.liu.anjiantong.presenter;

import android.support.annotation.StringRes;

/**
 * Created by mini on 17/5/21.
 */

public interface IBasePresenter {


    void onDestroy();

    interface IView{
        void toast(@StringRes int msg);

        void toast(String msg);
    }

}
