package org.chinasafety.liu.anjiantong.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.YhDetailInfo;
import org.chinasafety.liu.anjiantong.utils.StringUtils;
import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;
import org.chinasafety.liu.anjiantong.view.BaseActivity;
import org.chinasafety.liu.anjiantong.view.adapter.GridAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class YhDetailActivity extends BaseActivity {

    public static final String YHID = "yhid";
    private float dp;
    private ProgressDialog mProgressDialog;
    private String mStringExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yh_detail);
        initComplement();
        getData();
    }

    private void getData() {
        pendingDialog(R.string.xlistview_header_hint_loading);
        Observable.create(new ObservableOnSubscribe<List<YhDetailInfo>>() {

            @Override
            public void subscribe(ObservableEmitter<List<YhDetailInfo>> pSubscriber) throws Exception{
                try {
                    String keys2[] = {"hiid"};
                    Object values2[] = {mStringExtra};
                    ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getHiddenTroubleDetail",
                            WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                    List<YhDetailInfo> list = parseYhDetail(result);
                    if (list.size() == 0) {
                        pSubscriber.onError(new Throwable());
                    } else {
                        pSubscriber.onNext(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pSubscriber.onError(e);
                }
                pSubscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<YhDetailInfo>>() {

                    @Override
                    public void onError(Throwable e) {
                        cancelDialog();
                        toast(R.string.getyhdetail_failed);
                    }

                    @Override
                    public void onComplete() {
                        cancelDialog();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<YhDetailInfo> pS) {
                        setContent(pS);
                    }
                });
    }

    private void setContent(List<YhDetailInfo> pS) {
        ((TextView) findViewById(R.id.bjcdw)).setText(pS.get(0).getCheckObject());
        ((TextView) findViewById(R.id.yhdj)).setText(pS.get(0).getTroubleGrade());
        ((TextView) findViewById(R.id.fxrq)).setText(pS.get(0).getCheckDate());
        ((TextView) findViewById(R.id.zgxq)).setText(pS.get(0).getLimitDate());
        ((TextView) findViewById(R.id.wczgrq)).setText(pS.get(0).getFinishDate());
        ((TextView) findViewById(R.id.zgfa)).setText(pS.get(0).getDightScheme());
        ((TextView) findViewById(R.id.zgjg)).setText(pS.get(0).getdResult());
        ((TextView) findViewById(R.id.yhms)).setText(pS.get(0).getSafetyTrouble());
        ((TextView) findViewById(R.id.zxjcdw)).setText(pS.get(0).getActionComname());
        ((TextView) findViewById(R.id.fcsj)).setText(pS.get(0).getReviewDate());
        ((TextView) findViewById(R.id.fcqk)).setText(pS.get(0).getReviewDetail());
        ((TextView) findViewById(R.id.cyfcry)).setText(pS.get(0).getReviewNames());
        ((TextView) findViewById(R.id.ygfy)).setText(pS.get(0).getEsCost());
        ((TextView) findViewById(R.id.sjzgfy)).setText(pS.get(0).getDightCost());
        ((TextView) findViewById(R.id.zgzrr)).setText(pS.get(0).getLiabelMasterName());
        ((TextView) findViewById(R.id.szqy)).setText(pS.get(0).getAreaName());
        ((TextView) findViewById(R.id.fzrsjhm)).setText(pS.get(0).getLiabelMasterMPhone());
        GridView zgqtpGr = (GridView) findViewById(R.id.zgqtp_grid);
        gridviewInit(pS.get(0).getImgPatch(),zgqtpGr);
        GridView zghtpGr = (GridView) findViewById(R.id.zghtp_grid);
        gridviewInit(pS.get(0).getDightedImgPaths(),zghtpGr);

    }

    private List<YhDetailInfo> parseYhDetail(ArrayList<HashMap<String, Object>> pResult) {
        List<YhDetailInfo> infos = new ArrayList<YhDetailInfo>();
        for (HashMap<String, Object> map : pResult) {
            YhDetailInfo rwInfo = new YhDetailInfo();
            rwInfo.setCheckDate(StringUtils.noNull(map.get("checkDate")));
            rwInfo.setLimitDate(StringUtils.noNull(map.get("limitDate")));
            rwInfo.setTroubleGrade(StringUtils.noNull(map.get("troubleGrade")));
            rwInfo.setCheckObject(StringUtils.noNull(map.get("checkObject")));
            rwInfo.setFinishDate(StringUtils.noNull(map.get("finishDate")));
            rwInfo.setDightScheme(StringUtils.noNull(map.get("dightScheme")));
            rwInfo.setdResult(StringUtils.noNull(map.get("dResult")));
            rwInfo.setSafetyTrouble(StringUtils.noNull(map.get("safetyTrouble")));
            rwInfo.setActionComname(StringUtils.noNull(map.get("actionComname")));
            rwInfo.setImgPatch(StringUtils.noNull(map.get("imgPatch")));
            rwInfo.setReviewDate(StringUtils.noNull(map.get("reviewDate")));
            rwInfo.setReviewDetail(StringUtils.noNull(map.get("reviewDetail")));
            rwInfo.setReviewNames(StringUtils.noNull(map.get("reviewNames")));
            rwInfo.setEsCost(StringUtils.noNull(map.get("esCost")));
            rwInfo.setDightCost(StringUtils.noNull(map.get("dightCost")));
            rwInfo.setLiabelMasterName(StringUtils.noNull(map.get("liabelMasterName")));
            rwInfo.setLiabelEmid(StringUtils.noNull(map.get("liabelEmid")));
            rwInfo.setAreaName(StringUtils.noNull(map.get("areaName")));
            rwInfo.setDightedImgPaths(StringUtils.noNull(map.get("DightedImgPaths")));
            rwInfo.setLiabelMasterMPhone(StringUtils.noNull(map.get("LiabelMasterMPhone")));

            infos.add(rwInfo);
        }
        return infos;
    }

    private void initComplement() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dp = getResources().getDimension(R.dimen.collection_dp);
        mStringExtra = getIntent().getStringExtra(YHID);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void gridviewInit(final List<String> bmp, GridView gridview) {
        GridAdapter adapter = new GridAdapter(YhDetailActivity.this, bmp);
        int size = bmp.size();
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        params.width = size * (int) (dp * 9.4f);
        gridview.setLayoutParams(params);
        gridview.setColumnWidth((int) (dp * 9.4f));
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    Intent intent =new Intent(YhDetailActivity.this,ShowImageActivity.class);
                    intent.putExtra("data",bmp.get(position));
                    startActivity(intent);
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
            }
        });

    }


    public void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    public void toast(int toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    public void pendingDialog(int resId) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.setMessage(getString(resId));
        mProgressDialog.show();
    }

    public void cancelDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.cancel();
        }
    }

    public static void start(Context context, String yhId) {
        Intent intent = new Intent(context, YhDetailActivity.class);
        intent.putExtra(YHID, yhId);
        context.startActivity(intent);
    }
}
