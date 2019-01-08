package org.chinasafety.liu.anjiantong.view.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.utils.AppConstant;
import org.chinasafety.liu.anjiantong.utils.DownloadIntentService;
import org.chinasafety.liu.anjiantong.utils.GetVersionUtil;
import org.chinasafety.liu.anjiantong.utils.NotificationsUtils;
import org.chinasafety.liu.anjiantong.utils.SharedPreferenceUtil;
import org.chinasafety.liu.anjiantong.utils.StringUtil;
import org.chinasafety.liu.anjiantong.utils.StringUtils;
import org.chinasafety.liu.anjiantong.utils.WebServiceUtil;
import org.chinasafety.liu.anjiantong.view.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 初始化的view
 */
public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    public static final String AFT_PACKAGEID = "32";
    public static final String AFT_COMID = "6";
    public static final String AFT_Cache = "/hwajtCache";
    public static final String URL = "http://huiweioa.chinasafety.org/ClientDownload/huiwei_ajt_android.apk";
    public static final int num = 123;
    private Disposable mDisposable;
    private Disposable mCheckUpdateDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPerm();
    }

    @AfterPermissionGranted(num)
    private void checkPerm() {
        String[] params = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.BODY_SENSORS
        };

        if (EasyPermissions.hasPermissions(this, params)) {
            boolean b = StringUtils.isFolderExists(Environment.getExternalStorageDirectory() + AFT_Cache);
            if (b) {
                //获取版本
                getVersion();
            } else {
                Toast.makeText(this, "无法创建目录！", Toast.LENGTH_SHORT).show();
            }
        } else {
            EasyPermissions.requestPermissions(this, "需要权限", num, params);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //如果checkPerm方法，没有注解AfterPermissionGranted，也可以在这里调用该方法。

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //这里需要重新设置Rationale和title，否则默认是英文格式
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        }
    }

    private void checkLogin() {
        Observable.timer(2, TimeUnit.SECONDS)
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(Long aLong) throws Exception {
                        String string = SharedPreferenceUtil.getString(SplashActivity.this, AppConstant.SP_KEY_USER_INFO);
                        String company = SharedPreferenceUtil.getString(SplashActivity.this, AppConstant.SP_KEY_COMPANY_INFO);
                        return TextUtils.isEmpty(string) || TextUtils.isEmpty(company);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Boolean value) {
                        if (value) {
                            LoginActivity.start(SplashActivity.this);
                        } else {
                            HomePageActivity.start(SplashActivity.this);
                        }
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoginActivity.start(SplashActivity.this);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        if (mCheckUpdateDisposable != null) {
            mCheckUpdateDisposable.dispose();
        }
    }

    public void setNewVersion(String ver, String content) {
        int newVer = GetVersionUtil.getVersion(this);
        if (newVer < Integer.parseInt(ver)) {
            ShowDialog(content);
        } else {
            checkLogin();
        }

    }

    private void ShowDialog(String content) {
        if (!NotificationsUtils.isNotificationEnabled(SplashActivity.this)) {
            Toast.makeText(this, "未打开通知栏权限，请点击更新跳转手动打开", Toast.LENGTH_LONG).show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (!NotificationsUtils.isNotificationEnabled(SplashActivity.this)) {
                            Intent localIntent = new Intent();
                            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            if (Build.VERSION.SDK_INT >= 9) {
                                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                localIntent.setData(Uri.fromParts("package", SplashActivity.this.getPackageName(), null));
                            } else if (Build.VERSION.SDK_INT <= 8) {
                                localIntent.setAction(Intent.ACTION_VIEW);
                                localIntent.setClassName("com.android.settings",
                                        "com.android.settings.InstalledAppDetails");
                                localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                        SplashActivity.this.getPackageName());
                            }

                            startActivity(localIntent);
                            finish();
                        } else {
                            appDownload();
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        checkLogin();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);//设置这个对话框不能被用户按[返回键]而取消掉
        alertDialog.show();


    }

    private void appDownload() {
        Intent intent = new Intent(this, DownloadIntentService.class);
        intent.putExtra(DownloadIntentService.INTENT_DOWNLOAD_URL, URL);
        startService(intent);

    }

    private void deleteDate(File path, String name) {
        //删除本地文件
        File file = new File(path + "/" + name);
        if (file.exists()) {
            file.delete();
        }
    }

    private void getVersion() {
        Observable.create(new ObservableOnSubscribe<HashMap<String, Object>>() {
            @Override
            public void subscribe(final ObservableEmitter<HashMap<String, Object>> e) throws Exception {
                try {
                    String keys[] = {"packageid", "ver", "comid"};
                    Object values[] = {AFT_PACKAGEID, "", AFT_COMID};
                    ArrayList<HashMap<String, Object>> result = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getPackageVersion",
                            WebServiceUtil.HUIWEI_5VPM_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                    HashMap<String, Object> map = new HashMap<>();
                    if (result.size() > 0) {
                        if (StringUtil.isNotEmpty(StringUtil.noNull(result.get(0).get("ver")))) {
                            map.put("ver", result.get(0).get("ver"));
                        } else {
                            map.put("ver", "");
                        }
                        if (StringUtil.isNotEmpty(StringUtil.noNull(result.get(0).get("funmo")))) {
                            map.put("funmo", result.get(0).get("funmo"));
                        } else {
                            map.put("funmo", "");
                        }
                    }
                    e.onNext(map);
                } catch (Exception ep) {
                    ep.printStackTrace();
                    e.onError(ep);
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap<String, Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCheckUpdateDisposable = d;
                    }

                    @Override
                    public void onNext(HashMap<String, Object> value) {
                        setNewVersion(value.get("ver").toString(), value.get("funmo").toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SplashActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
