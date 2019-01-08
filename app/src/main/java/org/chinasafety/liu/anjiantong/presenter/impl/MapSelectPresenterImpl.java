package org.chinasafety.liu.anjiantong.presenter.impl;

import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;
import org.chinasafety.liu.anjiantong.model.SearchCompanyInfo;
import org.chinasafety.liu.anjiantong.model.provider.ServiceCompanyProvider;
import org.chinasafety.liu.anjiantong.presenter.IMapSelectPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MapSelectPresenterImpl implements IMapSelectPresenter {

    private IView mViewCallback;
    private Disposable mDisposable;

    public MapSelectPresenterImpl(IView viewCallback) {
        mViewCallback = viewCallback;
    }

    @Override
    public void getOrgFromDistance(final double lat, final double lng) {
        ServiceCompanyProvider.getCompanysByDistance(lat, lng)
                .flatMap(new Function<SearchCompanyInfo, ObservableSource<CompanyDetailInfo>>() {
                    @Override
                    public ObservableSource<CompanyDetailInfo> apply(SearchCompanyInfo searchCompanyInfo) throws Exception {
                        return ServiceCompanyProvider.getCompanyDetail(searchCompanyInfo.getCompanyId());
                    }
                })
                .buffer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<CompanyDetailInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(List<CompanyDetailInfo> value) {
                        mViewCallback.addCompanyList(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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
