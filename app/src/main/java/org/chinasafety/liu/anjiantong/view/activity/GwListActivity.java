package org.chinasafety.liu.anjiantong.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.provider.GlobalDataProvider;
import org.chinasafety.liu.anjiantong.utils.StringUtil;
import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;
import org.chinasafety.liu.anjiantong.view.BaseActivity;
import org.chinasafety.liu.anjiantong.view.adapter.MyListAdapter;
import org.chinasafety.liu.anjiantong.view.widget.XListView;

import java.util.ArrayList;
import java.util.HashMap;

public class GwListActivity extends BaseActivity implements XListView.IXListViewListener,
        View.OnClickListener {
    private XListView mListView;
    private MyListAdapter mAdapter;
    private ArrayList<HashMap<String, Object>> data;
    private LinearLayout progressLn;
    private TextView pro_tv;
    private ProgressBar bar;

    private Handler mHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    if (data.size() == 0) {
                        bar.setVisibility(View.GONE);
                        pro_tv.setText("无数据返回 \n 点击重试");
                    } else {
                        progressLn.setVisibility(View.GONE);
                        setListViewAdapter(new String[]{"NoAuditCount",
                                "InfoTitle", "PubComname", "PubDate"});
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gwlist_view);
        initComplement();
        registListener();
    }

    @Override
    protected void onResume() {

        super.onResume();
        // if (myApp.isRefresh) {
        progressLn.setVisibility(View.VISIBLE);
        geneItems();
        // myApp.isRefresh = false;
        // }
    }

    private void registListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                position = position - 1;
                String gwid = "";
                String DocId = "";
                if (StringUtil.isNotEmpty(StringUtil.noNull(data.get(position)
                        .get("CDocID")))) {

                    DocId = data.get(position).get("CDocID").toString();
                } else {
                    DocId = data.get(position).get("obli_id").toString();

                }
                if (StringUtil.isNotEmpty(StringUtil.noNull(data.get(position)
                        .get("InfoID")))) {
                    gwid = data.get(position).get("InfoID").toString();
                } else {
                    gwid = data.get(position).get("info_id").toString();
                }
                Intent intent = null;
                if ("0".equals(StringUtil.noNull(data.get(position).get(
                        "NoAuditCount")))) {
                    intent = new Intent(GwListActivity.this,
                            Gwpy_Activity.class);
                } else {
                    intent = new Intent(GwListActivity.this, Gwsh_Activity.class);
                    intent.putExtra("nac", StringUtil.noNull(data.get(position).get(
                                    "NoAuditCount")));
                }
                intent.putExtra(
                        "AddCDocNamesStr",
                        StringUtil.noNull(data.get(position).get(
                                "AddCDocNamesStr")));
                intent.putExtra(
                        "AddFilePathsStr",
                        StringUtil.noNull(data.get(position).get(
                                "AddFilePathsStr")));
                intent.putExtra("gwid", gwid);
                intent.putExtra("DocId", DocId);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void initComplement() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("公文信息列表");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = new ArrayList<HashMap<String, Object>>();
        progressLn = (LinearLayout) findViewById(R.id.normal_progress);
        mListView = (XListView) findViewById(R.id.gw_list);
        pro_tv = (TextView) findViewById(R.id.gwlist_tv);
        bar = (ProgressBar) findViewById(R.id.gwlist_progressBar);
        // mListView.setPullLoadEnable(true);
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(false);
        mListView.setXListViewListener(this);
    }

    private void setListViewAdapter(String[] keys) {
        mAdapter = new MyListAdapter(data, GwListActivity.this, keys);
        mListView.setAdapter(mAdapter);

    }

    public void click2Refresh(View view) {
        bar.setVisibility(View.VISIBLE);
        pro_tv.setText("");
        geneItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_search:
                Intent intent = new Intent(GwListActivity.this,
                        GwSearch_Activity.class);
                startActivityForResult(intent, 110);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent it) {
        super.onActivityResult(requestCode, resultCode, it);
        if (resultCode == RESULT_OK) {
            if (requestCode == 110) {
                data = (ArrayList<HashMap<String, Object>>) it
                        .getSerializableExtra("data");
                setListViewAdapter(new String[]{"NoAuditCount", "oblititle",
                        "sim_comname", "publish_start"});
            }
        }
    }

    private void geneItems() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    String keys[] = {"Emid", "DayCount", "TopCount", "InfoID",
                            "viewed"};
                    // System.out.println("orgidstr" +
                    // orgItem.get("Emid").toString());
                    Object values[] = {
                            Integer.parseInt(GlobalDataProvider.INSTANCE.getCompanyInfo().getEmid()),
                            365, 50, 0, false};
                    data = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getWebInformFroEmID", new String[]{
                                    "NoAuditCount", "InfoTitle", "PubComname",
                                    "PubDate", "AddCDocNamesStr",
                                    "AddFilePathsStr", "CDocID", "InfoID"}, WebServiceUtil.HUIWEI_5VIN_URL,
                            WebServiceUtil.HUIWEI_NAMESPACE);

                    mHandler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                // mAdapter.notifyDataSetChanged();
                setListViewAdapter(new String[]{"NoAuditCount", "InfoTitle",
                        "PubComname", "PubDate"});
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                geneItems();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GwListActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onClick(View v) {

    }
}
