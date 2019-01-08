package org.chinasafety.liu.anjiantong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.CompanyEmployeeInfo;
import org.chinasafety.liu.anjiantong.model.provider.ServiceCompanyProvider;
import org.chinasafety.liu.anjiantong.view.BaseActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class EmployeeActivity extends BaseActivity {
    private static final String KEY_ORG_ID = "orgId";
    private TextView mEmName, mOfficePhone, mMobilePhone,
            mQqNumber, mEmailAddress, mTempPart, mTempDuty, mEmid;

    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("联系人详情");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();


        String orgId = getIntent().getStringExtra(KEY_ORG_ID);

        ServiceCompanyProvider.getEmployeeDetail(orgId).
                observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<CompanyEmployeeInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CompanyEmployeeInfo value) {

                        initData(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });


    }


    private void initData(CompanyEmployeeInfo mEmployeeInfo) {
        bar.setVisibility(View.GONE);
        mEmName.setText(mEmployeeInfo.getName());
        mOfficePhone.setText(mEmployeeInfo.getOphone());
        mMobilePhone.setText(mEmployeeInfo.getMphone());
        mQqNumber.setText(mEmployeeInfo.getQq());
        mEmailAddress.setText(mEmployeeInfo.getEmail());
        mTempPart.setText(mEmployeeInfo.getPart());
        mTempDuty.setText(mEmployeeInfo.getDuty());
        mEmid.setText(mEmployeeInfo.getEmid());

    }

    private void initView() {
        mEmName = (TextView) findViewById(R.id.emName);
        mOfficePhone = (TextView) findViewById(R.id.officePhone);
        mMobilePhone = (TextView) findViewById(R.id.mobilePhone);
        mQqNumber = (TextView) findViewById(R.id.qqNumber);
        mEmailAddress = (TextView) findViewById(R.id.emailAddress);
        mTempPart = (TextView) findViewById(R.id.tempPart);
        mTempDuty = (TextView) findViewById(R.id.tempDuty);
        mEmid = (TextView) findViewById(R.id.Emid);
        bar = (ProgressBar) findViewById(R.id.employee_progressBar);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public static void start(Context context, String orgId) {
        Intent starter = new Intent(context, EmployeeActivity.class);
        starter.putExtra(KEY_ORG_ID, orgId);
        context.startActivity(starter);
    }
}
