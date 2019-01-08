package org.chinasafety.liu.anjiantong.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;
import org.chinasafety.liu.anjiantong.model.YhfcInfo;
import org.chinasafety.liu.anjiantong.model.provider.HiddenllnessProvider;
import org.chinasafety.liu.anjiantong.view.activity.EmployeeActivity;
import org.chinasafety.liu.anjiantong.view.activity.SafeCheckActivity;
import org.chinasafety.liu.anjiantong.view.widget.baidu_map_support.BNDemoGuideActivity;
import org.chinasafety.liu.anjiantong.view.widget.baidu_map_support.BNEventHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


public class CompanyMapDetailFragment extends Fragment {
    private final static String authBaseArr[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String HWAFT_CACHE = "hwajtCache";
    private String mSDCardPath = null;
    private final static int authBaseRequestCode = 1;
    private final static int authComRequestCode = 2;
    private final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    private boolean hasInitSuccess = false;
    private boolean hasRequestComAuth = false;
    private TextView mTvFirstYh;
    private TextView mTvSecondYh;
    private Button mToCheck;
    private Button mToNavi;
    private Button mEmployee;
    private CompanyDetailInfo mDetailInfo;
    private TextView mTvCompanyName;
    private TextView mTvCompanyAddress;
    private TextView mTvCompanyPeople;
    private TextView mTvCompanyPhone;
    private Disposable mDisposable;
    private final PublishSubject<CompanyDetailInfo> mPublishSubject = PublishSubject.create();
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private LatLng mLatLng;

    public static CompanyMapDetailFragment newInstance() {

        Bundle args = new Bundle();
        CompanyMapDetailFragment fragment = new CompanyMapDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setLatLng(LatLng latLng) {
        this.mLatLng = latLng;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_company_map_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (initDirs()) {
            initNavi();
        }
    }

    private void initView(View view) {
        mTvCompanyName = (TextView) view.findViewById(R.id.map_company_name);
        mTvCompanyAddress = (TextView) view.findViewById(R.id.map_company_address);
        mTvCompanyPeople = (TextView) view.findViewById(R.id.map_company_people);
        mTvCompanyPhone = (TextView) view.findViewById(R.id.map_company_phone);
        mTvFirstYh = (TextView) view.findViewById(R.id.map_company_yh_first);
        mTvSecondYh = (TextView) view.findViewById(R.id.map_company_yh_second);
        mToNavi = (Button) view.findViewById(R.id.map_company_to_navi);
        mToNavi.setOnClickListener(mClickListener);
        mToCheck = (Button) view.findViewById(R.id.map_company_to_check);
        mToCheck.setOnClickListener(mClickListener);
        mEmployee = (Button) view.findViewById(R.id.map_company_to_employee);
        mEmployee.setOnClickListener(mClickListener);
        setDetailToView();
    }

    private void setDetailToView() {
        if (mTvCompanyAddress == null || mDetailInfo == null) {
            return;
        }
        mTvCompanyAddress.setText(mDetailInfo.getAddress());
        mTvCompanyName.setText(mDetailInfo.getName());
        mTvCompanyPeople.setText(mDetailInfo.getPeople());
        mTvCompanyPhone.setText(mDetailInfo.getPhone());
        HiddenllnessProvider
                .getHiddenllnessByName(mDetailInfo.getName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<YhfcInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(List<YhfcInfo> value) {
                        if (getActivity() == null || getActivity().isFinishing()) {
                            return;
                        }
                        if (value.size() == 1) {
                            String string = getFormatString(value.get(0));
                            mTvFirstYh.setText(string);
                            mTvFirstYh.setVisibility(View.VISIBLE);
                            mTvSecondYh.setVisibility(View.GONE);
                        } else if (value.size() >= 2) {
                            String string = getFormatString(value.get(0));
                            mTvFirstYh.setText(string);
                            mTvFirstYh.setVisibility(View.VISIBLE);
                            String second = getFormatString(value.get(1));
                            mTvSecondYh.setText(second);
                            mTvSecondYh.setVisibility(View.VISIBLE);
                        } else {
                            mTvFirstYh.setVisibility(View.GONE);
                            mTvSecondYh.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private String getFormatString(YhfcInfo value) {
        StringBuilder stringBuffer = new StringBuilder();
        String checkDate = value.getCheckDate();
        try {
            checkDate = checkDate.length() >= 10 ? checkDate.substring(0, 10) : "";
            stringBuffer.append(checkDate);
            stringBuffer.append("：");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String safetyTrouble = value.getSafetyTrouble();
        safetyTrouble = safetyTrouble.length() > 8 ? safetyTrouble.substring(0, 8) + "……" : safetyTrouble;
        stringBuffer.append(safetyTrouble);
        String text;
        if (TextUtils.isEmpty(value.getFinishDate())) {
            text = "未整改";
        } else if (TextUtils.isEmpty(value.getReviewDate())) {
            text = "已整改未复查";
        } else {
            text = "已整改已复查";
        }
        stringBuffer.append(text);
        return stringBuffer.toString();
    }

    private boolean hasCompletePhoneAuth() {
        PackageManager pm = getActivity().getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, getActivity().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void routeplanToNavi() {
        if (mLatLng == null) {
            Toast.makeText(getActivity(), "未获取当前位置", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!hasInitSuccess) {
            Toast.makeText(getActivity(), "还未初始化!", Toast.LENGTH_SHORT).show();
            return;
        }
        // 权限申请
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // 保证导航功能完备
            if (!hasCompletePhoneAuth()) {
                if (!hasRequestComAuth) {
                    hasRequestComAuth = true;
                    this.requestPermissions(authComArr, authComRequestCode);
                    return;
                } else {
                    Toast.makeText(getActivity(), "没有完备的权限!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        BNRoutePlanNode sNode = new BNRoutePlanNode(mLatLng.longitude, mLatLng.latitude, "当前位置", null, BNRoutePlanNode.CoordinateType.BD09LL);
        BNRoutePlanNode eNode = new BNRoutePlanNode(mDetailInfo.getLon(), mDetailInfo.getLat(), mDetailInfo.getName(), null, BNRoutePlanNode.CoordinateType.BD09LL);
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        // 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
        BaiduNaviManager.getInstance().launchNavigator(getActivity(), list, 1, true, new DemoRoutePlanListener(sNode),
                eventListerner);
    }

    private class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        DemoRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */
            Intent intent = new Intent(getActivity(), BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(getActivity(), "算路失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void initSetting() {
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
        BNaviSettingManager.setIsAutoQuitWhenArrived(true);
        Bundle bundle = new Bundle();
        // 必须设置APPID，否则会静音
        bundle.putString(BNCommonSettingParam.TTS_APP_ID, "9694176");
        BNaviSettingManager.setNaviSdkParam(bundle);
    }

    BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

        @Override
        public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
            BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    Toast.makeText(getActivity(), "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            initNavi();
        } else if (requestCode == authComRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                }
            }
            routeplanToNavi();
        }
    }

    private void initNavi() {

        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (!hasBasePhoneAuth()) {

                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;

            }
        }

        BaiduNaviManager.getInstance().init(getActivity(), mSDCardPath, HWAFT_CACHE, new BaiduNaviManager.NaviInitListener() {


            @Override
            public void onAuthResult(int status, String msg) {
                if (0 != status) {
                    Toast.makeText(getActivity(), "key校验失败, ", Toast.LENGTH_LONG).show();
                }
            }

            public void initSuccess() {
                hasInitSuccess = true;
                initSetting();
            }

            public void initStart() {
            }

            public void initFailed() {
                Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
            }

        }, null, null, null);

    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = getActivity().getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, getActivity().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, HWAFT_CACHE);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mDetailInfo == null) {
                return;
            }
            if (view.getId() == mToCheck.getId()) {
                SafeCheckActivity.start(getActivity(), mDetailInfo.getId(),mDetailInfo.getName());
            } else if (view.getId() == mToNavi.getId()) {
//                mPublishSubject.onNext(mDetailInfo);
                routeplanToNavi();
            } else if (view.getId() == mEmployee.getId()) {
                EmployeeActivity.start(getActivity(), mDetailInfo.getId());
            }
        }
    };

    public Observable<CompanyDetailInfo> getOnNaviClick() {
        return mPublishSubject;
    }

    public void setCompanyDetail(CompanyDetailInfo info) {
        mDetailInfo = info;
        setDetailToView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
