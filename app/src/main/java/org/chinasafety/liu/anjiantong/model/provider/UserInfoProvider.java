package org.chinasafety.liu.anjiantong.model.provider;

import org.chinasafety.liu.anjiantong.model.CompanyInfo;
import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 项目名称：Anfutong1
 * 创建时间：2017/12/15 20:46
 * 注释说明：
 */

public class UserInfoProvider {

    public static Observable<CompanyInfo> getCompanyList(final String uId) {
        return Observable.create(new ObservableOnSubscribe<CompanyInfo>() {
            @Override
            public void subscribe(ObservableEmitter<CompanyInfo> e) throws Exception {
                try {
                    String[] keys = {"uPersonalID", "sState"};
                    Object[] values = {uId, "在职"};
                    ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getMoreComs");
                    for (HashMap<String, Object> map : result) {
                        CompanyInfo info = CompanyInfo.fromData(map);
                        e.onNext(info);
                    }
                    e.onComplete();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e.onError(new Throwable("该帐号没有关联公司，请更换帐号。"));
                }
            }
        });
    }
}
