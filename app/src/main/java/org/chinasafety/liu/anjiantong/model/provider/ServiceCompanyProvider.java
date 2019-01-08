package org.chinasafety.liu.anjiantong.model.provider;

import android.util.Log;

import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;
import org.chinasafety.liu.anjiantong.model.CompanyEmployeeInfo;
import org.chinasafety.liu.anjiantong.model.SearchCompanyInfo;
import org.chinasafety.liu.anjiantong.utils.StringUtil;
import org.chinasafety.liu.anjiantong.utils.StringUtils;
import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;

import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by mini on 17/5/25.
 */

public class ServiceCompanyProvider {

    private ServiceCompanyProvider() {
        throw new IllegalArgumentException();
    }

    public static Observable<List<SearchCompanyInfo>> getCheckCompany() {
        return Observable.create(new ObservableOnSubscribe<List<SearchCompanyInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchCompanyInfo>> e) throws Exception {
                String keys2[] = {"orgid", "relType", "includeself"};
                Object values2[] = {
                        GlobalDataProvider.INSTANCE.getCompanyInfo().getOrgId(),
                        "安全监管",
                        false};
                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                        "getRelationOrg", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                List<SearchCompanyInfo> parse = parse(result);
               for (int i = 0 ; i<parse.size();i++){
                   Log.e("TAG", "subscribe: " + parse.get(i).getCompanyName());
                   Log.e("TAG", "subscribe: " + i);
               }
//                for (SearchCompanyInfo i : parse) {
//
//                    Log.e("TAG", "subscribe: " + i.getCompanyName());
//
//                }
                e.onNext(parse);
                e.onComplete();
            }
        });
    }

    public static Observable<SearchCompanyInfo> getCompanysByDistance(final double lat, final double lng) {
        return Observable
                .create(new ObservableOnSubscribe<SearchCompanyInfo>() {
                    @Override
                    public void subscribe(ObservableEmitter<SearchCompanyInfo> e) throws Exception {
                        String keys2[] = {"orgid", "relType", "includeself", "lat", "lon", "dis"};
                        Object values2[] = {
                                GlobalDataProvider.INSTANCE.getCompanyInfo().getOrgId(),
                                "安全咨询",
                                false,
                                lat,//119.314974,25.53859
                                lng,
                                20 * 1000
                        };
                        ArrayList<HashMap<String, Object>> result = null;
                        try {
                            result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                    "getRelationOrgFromDistance", new String[]{"relOrgName", "relOrgID"}, WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                        } catch (InterruptedIOException e1) {
                            e.onError(e1);
                        }
                        if (result != null) {
                            for (HashMap<String, Object> map : result) {
                                SearchCompanyInfo info = new SearchCompanyInfo();
                                info.setCompanyId(StringUtils.noNull(map.get("relOrgID")));
                                info.setCompanyName(StringUtils.noNull(map.get("relOrgName")));
                                e.onNext(info);
                            }
                        }
                        e.onComplete();
                    }
                });
    }


    private static List<SearchCompanyInfo> parse(ArrayList<HashMap<String, Object>> result) {
        List<SearchCompanyInfo> companyInfoList = new ArrayList<>();
        for (HashMap<String, Object> map : result) {
            SearchCompanyInfo info = new SearchCompanyInfo();
            info.setCompanyId(StringUtils.noNull(map.get("relOrgID")));
            info.setCompanyName(StringUtils.noNull(map.get("relOrgName")));
            companyInfoList.add(info);
        }
        return companyInfoList;
    }

    public static Observable<CompanyEmployeeInfo> getEmployeeDetail(final String orgID) {
        return Observable.create(new ObservableOnSubscribe<CompanyEmployeeInfo>() {
            @Override
            public void subscribe(ObservableEmitter<CompanyEmployeeInfo> e) throws Exception {
                String keys2[] = {"orgid"};
                Object values2[] = {orgID};
                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                        "getAllEmployeeFromOrgID", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                if (result.size() > 0) {
                    CompanyEmployeeInfo employeeInfo = CompanyEmployeeInfo.fromMap(result.get(0));
                    employeeInfo.setId(orgID);
                    e.onNext(employeeInfo);
                } else {
                    CompanyEmployeeInfo employeeInfo = new CompanyEmployeeInfo();
                    e.onNext(employeeInfo);
                }
                e.onComplete();
            }
        });
    }

    public static Observable<CompanyDetailInfo> getCompanyDetail(final String orgId) {
        return Observable.create(new ObservableOnSubscribe<CompanyDetailInfo>() {
            @Override
            public void subscribe(ObservableEmitter<CompanyDetailInfo> e) throws Exception {
                String keys2[] = {"orgid", "comid"};
                Object values2[] = {
                        orgId,
                        GlobalDataProvider.INSTANCE.getCompanyInfo().getComId()};
                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                        "getOrgDetail", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                String keys[] = {"orgID"};
                Object values[] = {orgId};
                String orgidtoOrgidstr = WebServiceUtil.getMsg(keys, values,
                        "OrgidtoOrgidstr", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                if (result.size() > 0) {
                    CompanyDetailInfo detailInfo = CompanyDetailInfo.fromMap(result.get(0));
                    if (StringUtil.isNotEmpty( orgidtoOrgidstr)){
                        detailInfo.setIdStr(orgidtoOrgidstr);
                    }
                    detailInfo.setId(orgId);
                    e.onNext(detailInfo);
                } else {
                    CompanyDetailInfo detailInfo = new CompanyDetailInfo();
                    e.onNext(detailInfo);
                }
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> setLocation(final String orgId, final double lat, final double lon, final double alt) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                String keys2[] = {"orgid", "lon", "lat", "alt"};
                Object values2[] = {
                        orgId, lon, lat, alt};
                ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                        "setOrgLocation", WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                if (result.size() > 0 && result.get(0) != null && result.get(0).containsKey("error")) {
                    e.onNext(false);
                } else {
                    e.onNext(true);
                }
                e.onComplete();
            }
        });
    }
}
