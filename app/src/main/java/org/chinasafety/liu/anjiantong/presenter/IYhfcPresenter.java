package org.chinasafety.liu.anjiantong.presenter;

import android.content.Context;

import org.chinasafety.liu.anjiantong.model.YhfcInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public interface IYhfcPresenter {

    void getYhfcList(String startDate, String endDate, String name);

    interface View{
        void getYhfcListSuccess(List<YhfcInfo> pYhfcInfoList);
        void pendingDialog();
        void cancelDialog();
        void toast(String toast);
        void toast(int toast);
        Context getContext();
        int getReview();
        int getFinished();

        boolean isYhzg();
    }
}
