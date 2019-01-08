package org.chinasafety.liu.anjiantong.presenter.impl;

import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;
import org.chinasafety.liu.anjiantong.model.provider.ServiceCompanyProvider;
import org.chinasafety.liu.anjiantong.presenter.IMapSelectPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mini on 17/6/3.
 */

public class OneCompanyMapPresenter implements IMapSelectPresenter {

    private String orgId;
    private IView mViewCallback;
    private Disposable mDisposable;

    public OneCompanyMapPresenter(String orgId, IView viewCallback) {
        this.orgId = orgId;
        mViewCallback = viewCallback;
    }

    @Override
    public void getOrgFromDistance(double lat, double lng) {
        ServiceCompanyProvider.getCompanyDetail(orgId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CompanyDetailInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }
                    @Override
                    public void onNext(CompanyDetailInfo value) {
                        List<CompanyDetailInfo> list = new ArrayList<>();
                        list.add(value);
                        mViewCallback.addCompanyList(list);
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
