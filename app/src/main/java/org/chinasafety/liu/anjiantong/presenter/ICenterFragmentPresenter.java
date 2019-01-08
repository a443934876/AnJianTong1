package org.chinasafety.liu.anjiantong.presenter;

/**
 * Created by mini on 17/5/24.
 */

public interface ICenterFragmentPresenter extends IBasePresenter{

    void getUnReadMessageCount();

    void getUnStudyClassCount();

    interface IView extends IBasePresenter.IView
    {
        void setUnReadMessageCount(String count);

        void setUnStydyClassCount(String classCount);
    }

}
