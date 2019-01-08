package org.chinasafety.liu.anjiantong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;
import org.chinasafety.liu.anjiantong.presenter.IMapSelectPresenter;
import org.chinasafety.liu.anjiantong.presenter.impl.MapSelectPresenterImpl;
import org.chinasafety.liu.anjiantong.view.BaseActivity;
import org.chinasafety.liu.anjiantong.view.fragment.CompanyMapDetailFragment;
import org.chinasafety.liu.anjiantong.view.fragment.MapCompanyListFragment;
import org.chinasafety.liu.anjiantong.view.widget.DrivingRouteOverlay;
import org.chinasafety.liu.anjiantong.view.widget.baidu_map_support.service.LocationService;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MapSelectActivity extends BaseActivity implements IMapSelectPresenter.IView {

    private LocationService locationService;
    private IMapSelectPresenter mPresenter;
    private BaiduMap mBaiduMap;
    private CompanyMapDetailFragment mDetailFragment;
    private MapCompanyListFragment mCompanyListFragment;
    private final HashMap<Marker, CompanyDetailInfo> mMarkerList = new HashMap<>();
    private MapView mMapView;
    private final CompositeDisposable mListenerDisposable = new CompositeDisposable();
    private RoutePlanSearch mPlanSearch;
    private LatLng mLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_select);
        initView();
        initEvent();

    }



    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMapView = (MapView) findViewById(R.id.map_view);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mPresenter = getPresenter();
        mPlanSearch = RoutePlanSearch.newInstance();
        mDetailFragment = CompanyMapDetailFragment.newInstance();
        mCompanyListFragment = MapCompanyListFragment.newInstance();
        mCompanyListFragment.setItemClickAction(mClickCallback);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.map_override_view, mCompanyListFragment)
                .add(R.id.map_override_view, mDetailFragment)
                .hide(mCompanyListFragment)
                .hide(mDetailFragment)
                .commit();
        if (mLatLng == null) {
            locationService = new LocationService(getApplicationContext());
            locationService.registerListener(mListener);
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            locationService.start();
        }
    }

    protected IMapSelectPresenter getPresenter() {
        return new MapSelectPresenterImpl(this);
    }

    private void initEvent() {
        mPlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(MapSelectActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);//路线覆盖物，MyDrivingRouteOverlay代码下面给出
//            mBaidumap.setOnMarkerClickListener(overlay);
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();


                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });
        Observable
                .create(new ObservableOnSubscribe<Marker>() {
                    @Override
                    public void subscribe(final ObservableEmitter<Marker> e) throws Exception {
                        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                e.onNext(marker);
                                return true;
                            }
                        });
                    }
                })
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Marker>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mListenerDisposable.add(d);
                    }

                    @Override
                    public void onNext(Marker value) {
                        setSelectIcon(value);
                        CompanyDetailInfo companyDetailInfo = mMarkerList.get(value);
                        if (companyDetailInfo != null) {
                            setInfoToDetail(companyDetailInfo);
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

    private void setSelectIcon(Marker value) {
        reStoreAllIcon();
        value.setIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.gwlb_icon2));
    }

    private void reStoreAllIcon() {
        for (Marker marker : mMarkerList.keySet()) {
            marker.setIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding));
        }
    }

    protected void setInfoToDetail(CompanyDetailInfo companyDetailInfo) {
        getSupportFragmentManager().beginTransaction()
                .hide(mCompanyListFragment)
                .show(mDetailFragment)
                .commit();

        mDetailFragment.getOnNaviClick()
                .subscribe(mObserver);
        mDetailFragment.setCompanyDetail(companyDetailInfo);
        LatLng latLng = new LatLng(companyDetailInfo.getLat(),companyDetailInfo.getLon());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng).zoom(15.0f);
        animateMapStatus(builder.build());
    }

    private Observer<CompanyDetailInfo> mObserver = new Observer<CompanyDetailInfo>() {
        @Override
        public void onSubscribe(Disposable d) {
            mListenerDisposable.add(d);
        }

        @Override
        public void onNext(CompanyDetailInfo value) {
            if (mLatLng == null) {
                toast("暂未获取到当前位置");
                return;
            }
            PlanNode stNode = PlanNode.withLocation(mLatLng);
            PlanNode enNode = PlanNode.withLocation(new LatLng(value.getLat(), value.getLon()));
            mPlanSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_select_search, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.map_select_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSupportFragmentManager().beginTransaction()
                        .hide(mDetailFragment)
                        .show(mCompanyListFragment)
                        .commit();
                mCompanyListFragment.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        mMapView.setVisibility(View.VISIBLE);
        mMapView.onResume();
        super.onResume();

    }

    @Override
    protected void onPause() {
        mMapView.setVisibility(View.INVISIBLE);
        mMapView.onPause();
        super.onPause();

    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        mListenerDisposable.dispose();
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mPresenter.getOrgFromDistance(location.getLatitude(), location.getLongitude());
                MyLocationData locationData = new MyLocationData.Builder()
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(locationData);
                mLatLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mDetailFragment.setLatLng(mLatLng);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(mLatLng).zoom(15.0f);
                animateMapStatus(builder.build());
                locationService.unregisterListener(mListener); //注销掉监听
                locationService.stop(); //停止定位服务
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    private MapCompanyListFragment.ClickCallback mClickCallback = new MapCompanyListFragment.ClickCallback() {
        @Override
        public void call(CompanyDetailInfo info) {
            if(info.getLat()==0||info.getLon()==0){
                SafeCheckActivity.start(MapSelectActivity.this, info.getId(),info.getName());
                return;
            }
            try {
                LatLng point = new LatLng(info.getLat(), info.getLon());
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                Marker marker = (Marker) mBaiduMap.addOverlay(option);
                setSelectIcon(marker);
            } catch (Exception ignored) {
            }
            setInfoToDetail(info);
        }
    };

    protected void animateMapStatus(MapStatus status) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(status));
    }

    @Override
    public void toast(@StringRes int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public static void start(Context context) {

        Intent starter = new Intent(context, MapSelectActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void addCompanyList(List<CompanyDetailInfo> value) {
        for (CompanyDetailInfo companyDetailInfo : value) {
            LatLng point = new LatLng(companyDetailInfo.getLat(), companyDetailInfo.getLon());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            Marker marker = (Marker) mBaiduMap.addOverlay(option);
            mMarkerList.put(marker, companyDetailInfo);
        }
    }


}
