package org.chinasafety.liu.anjiantong.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.CompanyInfo;
import org.chinasafety.liu.anjiantong.model.provider.GlobalDataProvider;
import org.chinasafety.liu.anjiantong.model.provider.UserInfoProvider;
import org.chinasafety.liu.anjiantong.utils.AppConstant;
import org.chinasafety.liu.anjiantong.utils.SharedPreferenceUtil;
import org.chinasafety.liu.anjiantong.view.activity.CompanyListActivity;
import org.chinasafety.liu.anjiantong.view.activity.LoginActivity;
import org.chinasafety.liu.anjiantong.view.widget.sweet_dialog.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mini on 17/5/23.
 */

public class RightFragment extends Fragment {

    private Button mLogout, mCompanyChoose;

    public static RightFragment newInstance() {
        
        Bundle args = new Bundle();
        
        RightFragment fragment = new RightFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page_right,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogout = (Button) view.findViewById(R.id.home_page_right_logout);
        mLogout.setOnClickListener(mClickListener);
        mCompanyChoose = (Button) view.findViewById(R.id.home_page_right_to_CompanyChoose);
        mCompanyChoose.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == mLogout.getId()){
                new SweetAlertDialog(getActivity(),
                        SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getResources().getString(R.string.dialog_default_title))
                        .setContentText("你确定退出登录吗？").setCancelText("点错了")
                        .setConfirmText("是的！").showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {

                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {

                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                        logout();
                    }
                }).show();

            } else if (view.getId() == mCompanyChoose.getId()) {

                UserInfoProvider.getCompanyList(GlobalDataProvider.INSTANCE.getUserInfo().getUid())
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<CompanyInfo>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<CompanyInfo> value) {
                                CompanyListActivity.start(getActivity(), (ArrayList<CompanyInfo>) value);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });

            }
        }
    };

    private void logout() {
        GlobalDataProvider.INSTANCE.setUserInfo(null);
        GlobalDataProvider.INSTANCE.setCompanyInfo(null);
        SharedPreferenceUtil.savePreferences(getActivity(), AppConstant.SP_KEY_USER_INFO,"");
        SharedPreferenceUtil.savePreferences(getActivity(), AppConstant.SP_KEY_COMPANY_INFO,"");
        LoginActivity.start(getActivity());
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
