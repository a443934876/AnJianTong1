package org.chinasafety.liu.anjiantong.view;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.chinasafety.liu.anjiantong.view.widget.baidu_map_support.service.LocationService;


public class MyApplication extends Application {

    public LocationService locationService;
    @Override
    public void onCreate() {
        super.onCreate();
        /*
          初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        SDKInitializer.initialize(getApplicationContext());

    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
