package org.chinasafety.liu.anjiantong.presenter.impl;

import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;
import org.chinasafety.liu.anjiantong.model.provider.GlobalDataProvider;
import org.chinasafety.liu.anjiantong.presenter.ICenterFragmentPresenter;
import org.chinasafety.liu.anjiantong.utils.UploadDataHelper;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mini on 17/5/24.
 */
public class CenterFragmentPresenterImpl implements ICenterFragmentPresenter {

    private IView mViewCallback;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public CenterFragmentPresenterImpl(IView view) {
        mViewCallback = view;
    }

    @Override
    public void getUnReadMessageCount() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                ArrayList<HashMap<String, Object>> result;
                try {
                    String keys[] = {"Emid", "DayCount", "TopCount",
                            "InfoID", "viewed"};
                    Object values[] = {GlobalDataProvider.INSTANCE.getCompanyInfo().getEmid(),365,1000,0,false};
                    result = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getWebInformFroEmID",WebServiceUtil.HUIWEI_5VIN_URL,WebServiceUtil.HUIWEI_NAMESPACE);
                    emitter.onNext(String.valueOf(result.size()));
                }catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String value) {
                        mViewCallback.setUnReadMessageCount(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewCallback.toast("出现错误：" + e.getMessage());
                        mViewCallback.setUnReadMessageCount("0");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getUnStudyClassCount() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                ArrayList<HashMap<String, Object>> result;
                try {
                    String keys[] = {"Emid", "SStart", "SEnd",
                            "IsStudyed", "IsExamed"};
                    Object values[] = {GlobalDataProvider.INSTANCE.getCompanyInfo().getEmid(), UploadDataHelper.getBeforeOneYearDate(),UploadDataHelper.getNowDate(),0,-1};
                    result = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getLessonFromEm",WebServiceUtil.HUIWEI_5VHR_URL);
                    emitter.onNext(String.valueOf(result.size()));
                }catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(String value) {
                        mViewCallback.setUnStydyClassCount(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mViewCallback.toast("出现错误：" + e.getMessage());
                        mViewCallback.setUnStydyClassCount("0");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onDestroy() {
        mCompositeDisposable.dispose();
    }
}
