package org.chinasafety.liu.anjiantong.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.CompanyDetailInfo;
import org.chinasafety.liu.anjiantong.model.SearchCompanyInfo;
import org.chinasafety.liu.anjiantong.model.provider.GlobalDataProvider;
import org.chinasafety.liu.anjiantong.model.provider.ServiceCompanyProvider;
import org.chinasafety.liu.anjiantong.view.adapter.LinearLayoutManagerWrapper;
import org.chinasafety.liu.anjiantong.view.adapter.RecyclerBaseAdapter;
import org.chinasafety.liu.anjiantong.view.adapter.RecyclerItemDecoration;
import org.chinasafety.liu.anjiantong.view.adapter.viewholder.SearchCompanyDetailHolder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MapCompanyListFragment extends Fragment {

    private final List<CompanyDetailInfo> mDataList = new ArrayList<>();
    private String name;
    private ProgressBar bar;

    public static MapCompanyListFragment newInstance() {
        Bundle args = new Bundle();
        MapCompanyListFragment fragment = new MapCompanyListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerBaseAdapter<SearchCompanyInfo, SearchCompanyDetailHolder> mAdapter;
    private List<SearchCompanyInfo> mDataListForSearch;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Disposable mClickDisposable;
    private ClickCallback mItemClickAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_company_map_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView companyListView = (RecyclerView) view.findViewById(R.id.map_company_list_view);
        companyListView.setLayoutManager(new LinearLayoutManagerWrapper(getActivity(), LinearLayoutManager.VERTICAL, false));
        companyListView.addItemDecoration(new RecyclerItemDecoration());
        mAdapter = new RecyclerBaseAdapter<>(R.layout.item_search_company_info, SearchCompanyDetailHolder.class);
        companyListView.setAdapter(mAdapter);
        bar = (ProgressBar) getActivity().findViewById(R.id.search_progressBar);
        initEvent();
        getData();
    }


    private void getData() {
        ServiceCompanyProvider.getCheckCompany()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<SearchCompanyInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<SearchCompanyInfo> value) {
                        if (getActivity() == null || getActivity().isFinishing()) {
                            return;
                        }
                        mDataListForSearch = new ArrayList<>();
                        mDataListForSearch.clear();
                        mDataListForSearch.addAll(value);
                        if (!TextUtils.isEmpty(name)) {
                            bar.setVisibility(View.GONE);
                            search(name);
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

    private void initEvent() {
        mAdapter.itemClickObserve()
                .doOnSubscribe(new Consumer<Disposable>() {

                    @Override
                    public void accept(Disposable disposable) throws Exception {

                        mClickDisposable = disposable;
                    }
                })
                .subscribe(new Consumer<SearchCompanyInfo>() {
                    @Override
                    public void accept(SearchCompanyInfo searchCompanyInfo) throws Exception {
                        if(searchCompanyInfo.getCompanyName().equals("点击添加公司")){
                            CompanyDetailInfo companyDetailInfo = new CompanyDetailInfo();
                            companyDetailInfo.setId("209975");
                            mItemClickAction.call(companyDetailInfo);

                        }else {ServiceCompanyProvider.getCompanyDetail(searchCompanyInfo.getCompanyId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Observer<CompanyDetailInfo>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                    }

                                    @Override
                                    public void onNext(CompanyDetailInfo value) {
                                        if (getActivity() == null || getActivity().isFinishing()) {
                                            return;
                                        }
                                        if (mItemClickAction != null) {
                                            mItemClickAction.call(value);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });}


                    }
                });
    }

    public void search(String name) {
        this.name = name;
        if (mDataListForSearch == null) {
            bar.setVisibility(View.VISIBLE);
            return;
        }

        if (mDataListForSearch.size() == 0) {
            Toast.makeText(getActivity(), "服务器暂无数据！", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(name)) {
            mAdapter.setAll(mDataListForSearch);
            return;
        }
        mAdapter.removeAll();
        boolean isContains = true;

        for (SearchCompanyInfo companyInfo : mDataListForSearch) {
            if (companyInfo.getCompanyName().contains(name)) {
                mAdapter.add(companyInfo);
                isContains = false;
            }
        }
        if (isContains) {
            SearchCompanyInfo companyInfo = new SearchCompanyInfo();
            companyInfo.setCompanyName("点击添加公司");
            companyInfo.setCompanyId( GlobalDataProvider.INSTANCE.getCompanyInfo().getOrgId());
            mAdapter.add(companyInfo);
        }
    }

    public void setItemClickAction(ClickCallback itemClickAction) {
        this.mItemClickAction = itemClickAction;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        if (mClickDisposable != null) {
            mClickDisposable.dispose();
        }
    }

    public interface ClickCallback {
        void call(CompanyDetailInfo info);
    }
}
