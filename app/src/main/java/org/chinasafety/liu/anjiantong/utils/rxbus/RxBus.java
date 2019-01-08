package org.chinasafety.liu.anjiantong.utils.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 作用：多线程事件传递
 * Created by cqj on 2017-07-13.
 */
public class RxBus {

    public static RxBus getInstance() {
        return RxbusHolder.instance;
    }

    public static class RxbusHolder{
        private static final RxBus instance = new RxBus();
    }

    private final Subject bus = PublishSubject.create().toSerialized();

    public void send(Object obj){
        bus.onNext(obj);
    }

    public <T> Observable<T> observer(Class<T> clazz){
        return bus.ofType(clazz);
    }
}
