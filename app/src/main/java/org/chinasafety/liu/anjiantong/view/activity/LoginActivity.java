package org.chinasafety.liu.anjiantong.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.model.CompanyInfo;
import org.chinasafety.liu.anjiantong.presenter.ILoginPresenter;
import org.chinasafety.liu.anjiantong.presenter.impl.LoginPresenterImpl;
import org.chinasafety.liu.anjiantong.utils.AppConstant;
import org.chinasafety.liu.anjiantong.utils.Code;
import org.chinasafety.liu.anjiantong.view.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements ILoginPresenter.IView {

    private ImageView mIvVerification;
    private EditText mEdtAccount;
    private EditText mEdtPwd;
    private Button mLoginBtn;
    private ImageView mIvRememberPwd;
    private EditText mEdtVerification;
    private String mVerificationStr;
    private SharedPreferences mSharedPreferences;
    private boolean mIsRemember;
    private ILoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        mIvVerification = (ImageView) findViewById(R.id.yzm_iv);
        mEdtAccount = (EditText) findViewById(R.id.zhanghao_edt);
        mEdtPwd = (EditText) findViewById(R.id.mima_edt);
        mLoginBtn = (Button) findViewById(R.id.main_loginbtn);
        mLoginBtn.setOnClickListener(mClickListener);
        mIvRememberPwd = (ImageView) findViewById(R.id.jzmm);
        mIvRememberPwd.setOnClickListener(mClickListener);
        mEdtVerification = (EditText) findViewById(R.id.yzm_edt);
        mIvVerification.setImageBitmap(Code.getInstance().createBitmap());
        mIvVerification.setOnClickListener(mClickListener);
        mVerificationStr = Code.getInstance().getCode();
        mSharedPreferences = getSharedPreferences(AppConstant.SP_CONFIG, MODE_PRIVATE);
        mIsRemember = mSharedPreferences.getBoolean(AppConstant.SP_KEY_IS_REMEMBER, false);
        if (mIsRemember) {
            mIvRememberPwd.setBackgroundResource(R.drawable.bnt01);
        } else {
            mIvRememberPwd.setBackgroundResource(R.drawable.bnt02);
        }
        // 显示密码
        if (mIsRemember) {
            String account = mSharedPreferences.getString(AppConstant.SP_KEY_ACCOUNT, "");
            mEdtAccount.setText(account);
            mEdtAccount.setSelection(account.length());
            mEdtPwd.setText(mSharedPreferences.getString(AppConstant.SP_KEY_PWD, ""));
        }
        mPresenter = new LoginPresenterImpl(this);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == mIvVerification.getId()) {
                mIvVerification.setImageBitmap(Code.getInstance().createBitmap());
                mVerificationStr = Code.getInstance().getCode();
            } else if (view.getId() == mIvRememberPwd.getId()) {
                if (mIsRemember) {
                    mIvRememberPwd.setBackgroundResource(R.drawable.bnt02);
                    mIsRemember = false;
                } else {
                    mIvRememberPwd.setBackgroundResource(R.drawable.bnt01);
                    mIsRemember = true;
                }
            } else if (view.getId() == mLoginBtn.getId()) {
                login();
            }
        }
    };


    private void login() {
        String zhStr = mEdtAccount.getText().toString();
        String mmStr = mEdtPwd.getText().toString();
        String yzm_ = mEdtVerification.getText().toString();
        if (TextUtils.isEmpty(zhStr)) {
            toast("帐号不能为空！");
            return;
        }
        if (TextUtils.isEmpty(mmStr)) {
            toast("密码不能为空！");
            return;
        }
//        if (!mVerificationStr.equalsIgnoreCase(yzm_)) {
//            toast("非法验证码！");
//            return;
//        }
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(AppConstant.SP_KEY_IS_REMEMBER, mIsRemember);
        // 记住密码
        if (mIsRemember) {
            edit.putString(AppConstant.SP_KEY_ACCOUNT, mEdtAccount.getText().toString());
            edit.putString(AppConstant.SP_KEY_PWD, mEdtPwd.getText().toString());
        }
        edit.apply();
        mPresenter.login(this, zhStr, mmStr);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

    @Override
    public void toast(@StringRes int msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeButtonStyle(boolean isPress, String msg) {
        if (isPress) {
            mLoginBtn.setText(msg);
            mLoginBtn.setEnabled(false);
        } else {
            mLoginBtn.setEnabled(true);
            mLoginBtn.setText("登　录");
        }
    }

    @Override
    public void toHomePage() {
        HomePageActivity.start(this);
        finish();
    }

    @Override
    public void toCompanyChoose(List<CompanyInfo> info) {
        CompanyListActivity.start(this, (ArrayList<CompanyInfo>) info);
        finish();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }
}
