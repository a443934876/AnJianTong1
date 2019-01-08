package org.chinasafety.liu.anjiantong.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/4/10.
 */
public class RxCountDown {

    public static Observable<Integer> countdown(int time, final boolean isUp) {
        if (time < 0) time = 0;

        final int countTime = time;
        final int upTime = 0;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long increaseTime) throws Exception {
                        if (isUp) {
                            if (upTime < countTime) {
                                return upTime + increaseTime.intValue();
                            } else {
                                return countTime;
                            }
                        } else {
                            return countTime - increaseTime.intValue();
                        }
                    }
                })
                .take(countTime + 1);

    }
}