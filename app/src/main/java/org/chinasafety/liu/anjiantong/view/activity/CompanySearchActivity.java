package org.chinasafety.liu.anjiantong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.SearchCompanyInfo;
import org.chinasafety.liu.anjiantong.model.provider.ServiceCompanyProvider;
import org.chinasafety.liu.anjiantong.utils.rxbus.RxBus;
import org.chinasafety.liu.anjiantong.utils.rxbus.event.SetLocationEvent;
import org.chinasafety.liu.anjiantong.view.BaseActivity;
import org.chinasafety.liu.anjiantong.view.MyApplication;
import org.chinasafety.liu.anjiantong.view.adapter.LinearLayoutManagerWrapper;
import org.chinasafety.liu.anjiantong.view.adapter.RecyclerBaseAdapter;
import org.chinasafety.liu.anjiantong.view.adapter.RecyclerItemDecoration;
import org.chinasafety.liu.anjiantong.view.adapter.viewholder.SearchCompanyHolder;
import org.chinasafety.liu.anjiantong.view.widget.baidu_map_support.service.LocationService;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CompanySearchActivity extends BaseActivity {

    private LocationService locationService;
    private Button mBtnSearch;
    private EditText mEdtSearch;
    private ProgressBar mProgressBar;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private RecyclerBaseAdapter<SearchCompanyInfo, SearchCompanyHolder> mAdapter;
    private List<SearchCompanyInfo> mDataListForSearch = new ArrayList<>();
    private LatLng mLatLng;
    private double mAltitude;
    private RecyclerView companyListView;

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mAltitude = location.getAltitude();
            }
        }

        public void onConnectHotSpotMessage(String s, int i) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_search);
        initView();
        initEvent();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEdtSearch = (EditText) findViewById(R.id.company_search_edit);
        mBtnSearch = (Button) findViewById(R.id.company_search_btn);
        mProgressBar = (ProgressBar) findViewById(R.id.company_list_progress_bar);
         companyListView = (RecyclerView) findViewById(R.id.search_company_result_list_view);
        companyListView.setLayoutManager(new LinearLayoutManagerWrapper(this, LinearLayoutManager.VERTICAL, false));
        companyListView.addItemDecoration(new RecyclerItemDecoration());
        mAdapter = new RecyclerBaseAdapter<>(R.layout.item_search_company_info, SearchCompanyHolder.class);
        companyListView.setAdapter(mAdapter);
        searchCompany();
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((MyApplication) getApplication()).locationService;
        locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        RxBus.getInstance().observer(SetLocationEvent.class)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mCompositeDisposable.add(disposable);
                    }
                })
                .subscribe(new Consumer<SetLocationEvent>() {
                    @Override
                    public void accept(SetLocationEvent setLocationEvent) throws Exception {
                        if(mLatLng==null){
                            Toast.makeText(CompanySearchActivity.this, "暂未获取到当前位置信息，请稍后再试", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (setLocationEvent == null) {
                            return;
                        }
                        SearchCompanyInfo info = setLocationEvent.getCompanyInfo();
                        if (info == null) {
                            return;
                        }
                        ServiceCompanyProvider
                                .setLocation(info.getCompanyId(),mLatLng.latitude,mLatLng.longitude,mAltitude)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if(!aBoolean){
                                    Toast.makeText(CompanySearchActivity.this, "设置位置失败，请重试", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(CompanySearchActivity.this, "设置位置成功", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void initEvent() {
        mAdapter.itemClickObserve()
                .subscribe(new Observer<SearchCompanyInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SearchCompanyInfo value) {
                        OneCompanyMapActivity.start(CompanySearchActivity.this, value.getCompanyId());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mBtnSearch.setOnClickListener(mClickListener);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mBtnSearch.getId()) {
                companyListView.setVisibility(View.VISIBLE);
                String searchContent = mEdtSearch.getText().toString();
                if (TextUtils.isEmpty(searchContent)) {
                    mAdapter.setAll(mDataListForSearch);

                    return;
                }
                mAdapter.removeAll();
                for (SearchCompanyInfo companyInfo : mDataListForSearch) {
                    if (companyInfo.getCompanyName().contains(searchContent)) {
                        mAdapter.add(companyInfo);
                    }
                }
            }
        }
    };

    private void searchCompany() {
        mProgressBar.setVisibility(View.VISIBLE);
        ServiceCompanyProvider.getCheckCompany()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<SearchCompanyInfo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(List<SearchCompanyInfo> value) {
                        mDataListForSearch.clear();
                        mDataListForSearch.addAll(value);
                        mAdapter.setAll(value);
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, CompanySearchActivity.class);
        context.startActivity(starter);
    }
}
