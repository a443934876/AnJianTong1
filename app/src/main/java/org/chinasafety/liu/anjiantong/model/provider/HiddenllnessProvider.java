package org.chinasafety.liu.anjiantong.model.provider;

import android.text.format.DateFormat;

import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;
import org.chinasafety.liu.anjiantong.model.YhfcInfo;
import org.chinasafety.liu.anjiantong.utils.StringUtils;
import org.chinasafety.liu.anjiantong.utils.UploadDataHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;


/**
 * Created by mini on 17/5/31.
 */

public class HiddenllnessProvider {


    private HiddenllnessProvider() {
        throw new IllegalArgumentException();
    }

    public static Observable<List<YhfcInfo>> getHiddenllnessByName(final String companyName) {
        return Observable
                .create(new ObservableOnSubscribe<List<YhfcInfo>>() {

                    @Override
                    public void subscribe(ObservableEmitter<List<YhfcInfo>> emitter) throws Exception {
                        try {
                            String keys2[] = {"uComid", "isFinished", "isReviewed", "cStart",
                                    "cEnd", "hgrade", "areaRangeID", "industryStr", "objOrgName"};
                            int comid = Integer.parseInt(GlobalDataProvider.INSTANCE.getCompanyInfo().getComId());
                            String sDate = UploadDataHelper.getBeforeOneYearDate();
                            String eDate = String.format("%sT00:00:00.000", DateFormat.format("yyyy-MM-dd",new Date()));
                            Object values2[] = {comid, 2, 0, sDate, eDate, "", 0, "", companyName};
                            ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                                    "getRelHiddenIllness",
                                    WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                            emitter.onNext(parseYhfcInfo(result));
                        } catch (Exception e) {
                            e.printStackTrace();
                            emitter.onError(e);
                        }
                        emitter.onComplete();
                    }
                })
                .map(new Function<List<YhfcInfo>, List<YhfcInfo>>() {
                    @Override
                    public List<YhfcInfo> apply(List<YhfcInfo> pYhfcInfoList) throws Exception {
                        Collections.sort(pYhfcInfoList, new Comparator<YhfcInfo>() {
                            @Override
                            public int compare(YhfcInfo pYhfcInfo, YhfcInfo pT1) {
                                int ret = 0;
                                try {
                                    Calendar otherCalendar = Calendar.getInstance();
                                    SimpleDateFormat sformat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = sformat.parse(pYhfcInfo.getCheckDate());
                                    otherCalendar.setTime(date);
                                    Calendar nowCalendar = Calendar.getInstance();
                                    Date serDate = sformat.parse(pT1.getCheckDate());
                                    nowCalendar.setTime(serDate);
                                    ret = nowCalendar.compareTo(otherCalendar);
                                } catch (ParseException pE) {
                                    pE.printStackTrace();
                                }
                                /**
                                 * 0 if the times of the two Calendars are equal, -1 if the time of
                                 * this Calendar is before the other one, 1 if the time of this
                                 * Calendar is after the other one.
                                 */
                                return ret;
                            }
                        });
                        return pYhfcInfoList;
                    }
                });
    }

    public static List<YhfcInfo> parseYhfcInfo(ArrayList<HashMap<String, Object>> pResult) {
        List<YhfcInfo> infos = new ArrayList<>();
        for (HashMap<String, Object> map : pResult) {
            YhfcInfo yhInfo = new YhfcInfo();
            yhInfo.setActionOrgName(StringUtils.noNull(map.get("actionOrgName")));
            yhInfo.setAreaName(StringUtils.noNull(map.get("areaName")));
            yhInfo.setCheckDate(StringUtils.noNull(map.get("checkDate")));
            yhInfo.setCheckObject(StringUtils.noNull(map.get("checkObject")));
            yhInfo.setDightCost(StringUtils.noNull(map.get("dightCost")));
            yhInfo.setEsCost(StringUtils.noNull(map.get("esCost")));
            yhInfo.setFinishDate(StringUtils.noNull(map.get("finishDate")));
            yhInfo.setHTroubleID(StringUtils.noNull(map.get("hTroubleID")));
            yhInfo.setInduName(StringUtils.noNull(map.get("induName")));
            yhInfo.setLiabelEmid(StringUtils.noNull(map.get("LiabelEmid")));
            yhInfo.setLiabelName(StringUtils.noNull(map.get("LiabelName")));
            yhInfo.setLimitDate(StringUtils.noNull(map.get("limitDate")));
            yhInfo.setReviewDate(StringUtils.noNull(map.get("reviewDate")));
            yhInfo.setSafetyTrouble(StringUtils.noNull(map.get("safetyTrouble")));
            yhInfo.setTroubleGrade(StringUtils.noNull(map.get("troubleGrade")));
            infos.add(yhInfo);
        }
        return infos;
    }
}
