package org.chinasafety.liu.anjiantong.presenter.impl;

import android.content.Context;
import android.util.Log;

import org.chinasafety.liu.anjiantong.model.CompanyInfo;
import org.chinasafety.liu.anjiantong.model.UserInfo;
import org.chinasafety.liu.anjiantong.model.provider.UserInfoProvider;
import org.chinasafety.liu.anjiantong.presenter.ILoginPresenter;
import org.chinasafety.liu.anjiantong.utils.AppConstant;
import org.chinasafety.liu.anjiantong.utils.SharedPreferenceUtil;
import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mini on 17/5/21.
 */
public class LoginPresenterImpl implements ILoginPresenter {

    private static final String TAG = "LoginPresenterImpl";

    private IView mViewCallback;
    private Disposable mDisposable;

    public LoginPresenterImpl(IView viewCallback) {
        mViewCallback = viewCallback;
    }


    @Override
    public void login(final Context context, final String account, final String pwd) {

        mViewCallback.changeButtonStyle(true, "登录中...");
        Observable
                .create(new ObservableOnSubscribe<UserInfo>() {
                    @Override
                    public void subscribe(ObservableEmitter<UserInfo> e) throws Exception {
                        String[] keys = {"requestName", "mphone", "email", "pwd", "ret"};
                        Object[] values = {account, "", "", pwd, -1};
                        ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys, values,
                                "getSinglePersonalUserFromLogin");
                        if (result != null && result.size() > 0) {
                            if (result.get(0) != null) {
                                int intStr = Integer.valueOf((String) result.get(0)
                                        .get("ret"));
                                if (intStr != 0) {
                                    e.onError(new Throwable(parseError(intStr)));
                                } else {
                                    UserInfo info = UserInfo.fromData(result);
                                    if (info == null) {
                                        e.onError(new Throwable("服务器返回失败，请重试"));
                                        return;
                                    }
                                    SharedPreferenceUtil.savePreferences(context, AppConstant.SP_KEY_USER_INFO, info.toString());
                                    e.onNext(info);
                                    e.onComplete();
                                }
                            } else {
                                e.onError(new Throwable("服务器返回失败，请重试"));
                            }
                        } else {
                            e.onError(new Throwable("服务器返回失败，请重试"));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<UserInfo>() {
                    @Override
                    public void accept(UserInfo userInfo) throws Exception {
                        mViewCallback.changeButtonStyle(true, "获取公司信息...");
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<UserInfo, ObservableSource<CompanyInfo>>() {
                    @Override
                    public ObservableSource<CompanyInfo> apply(final UserInfo userInfo) throws Exception {
                        return UserInfoProvider.getCompanyList(userInfo.getUid());
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<CompanyInfo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onSuccess(List<CompanyInfo> value) {
                        if (value == null || value.isEmpty()) {
                            error("该帐号没有关联公司，请更换帐号。");
                            return;
                        }
                        if (value.size() == 1) {
                            CompanyInfo info = value.get(0);
                            String companySave = info.toString();
                            SharedPreferenceUtil.savePreferences(context, AppConstant.SP_KEY_COMPANY_INFO, companySave);
                            mViewCallback.toHomePage();
                        } else {
                            mViewCallback.toCompanyChoose(value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        error(e.getMessage());
                    }
                });
    }

    private void error(String msg) {
        mViewCallback.changeButtonStyle(false, "");
        mViewCallback.toast(msg);
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private String parseError(int code) {
        String ret = "";
        switch (code) {
            case 1:
                ret = "用户名不存在";
                break;

            case 2:
                ret = "手机号码不存在";
                break;
            case 3:
                ret = "电子邮件不存在";
                break;
            case 4:
                ret = "密码不正确";
                break;
            case 5:
                ret = "用户名格式不正确";
                break;

            default:
                break;
        }
        return ret;
    }
}
