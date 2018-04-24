package com.diting.pingxingren.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.Window;
import android.widget.Toast;

import com.diting.pingxingren.BuildConfig;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.easypermissions.AppSettingsDialog;
import com.diting.pingxingren.easypermissions.EasyPermissions;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.model.LoginResultModel;
import com.diting.pingxingren.model.NewVersionModel;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.model.UserInfoModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ChildRobotsObserver;
import com.diting.pingxingren.net.observers.LoginObserver;
import com.diting.pingxingren.net.observers.NewVersionObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.net.observers.UserInfoObserver;
import com.diting.pingxingren.util.AppUtil;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by asus on 2016/11/30.
 * 欢迎页
 */
public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    // 下载应用的进度条
    private ProgressDialog progressDialog;

    private SplashActivityHandler mHandler;
    private NewVersionModel mVersionModel;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new SplashActivityHandler(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        if (Utils.hasStoragePermission(this)) {
            FileUtil.createOrExistsDir(Constants.FILE_PATH);
        } else {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.rationale_photo),
                    Constants.REQUEST_CODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return;
        }
        getUpdateMsg();
    }

    @Override
    protected void initViews() {
    }

    @Override
    protected void initEvents() {
    }

    private ResultCallBack  mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof NewVersionModel) {
                mVersionModel = (NewVersionModel) result;
                int thisVersionCode = AppUtil.getAppVersionCode();
                if (thisVersionCode < mVersionModel.getVersionCode()) {//检查到新版本
                    showNoticeDialog();
                } else {//没有新版本
                    enterHome();
                }
            } else if (result instanceof LoginResultModel) {
                LoginResultModel loginResultModel = (LoginResultModel) result;
                MySharedPreferences.saveHeadPortrait(loginResultModel.getHeadImgUrl());
                MobclickAgent.onProfileSignIn(mUserName);
                ApiManager.getUserInfo(new UserInfoObserver(mResultCallBack));
            } else if (result instanceof UserInfoModel) {
                UserInfoModel userInfoModel = (UserInfoModel) result;
                String robotName = userInfoModel.getRobotName();
                String companyName = userInfoModel.getCompanyName();

                if (Utils.isNotEmpty(robotName) || Utils.isNotEmpty(companyName)) {
                    // 保存用户信息
                    MySharedPreferences.saveUniqueId(userInfoModel.getUnique_id());
                    MySharedPreferences.savePhoneSwitch(userInfoModel.getTelephoneSwitch());
                    MySharedPreferences.saveHeadPortrait(userInfoModel.getHeadPortrait());
                    MySharedPreferences.saveProfile(userInfoModel.getProfile());
                    MySharedPreferences.saveRobotName(robotName);
                    MySharedPreferences.saveDefaultRobotName(robotName);
                    MySharedPreferences.saveDefaultRobot(robotName);
                    MySharedPreferences.saveCompanyName(companyName);
                    MySharedPreferences.saveFansCount(userInfoModel.getFansCount());

                    ApiManager.getChildRobots(new ChildRobotsObserver(mResultCallBack));
                } else {
                    showShortToast("身份信息已过期, 请重新登录");
                    mHandler.sendEmptyMessage(0);
                }
            } else if (result instanceof Boolean) {
                ApiManager.getRobotInfo(new RobotInfoObserver(this));
            } else if (result instanceof RobotInfoModel) {
                RobotInfoModel robotInfoModel = (RobotInfoModel) result;
                MySharedPreferences.saveUniqueId(robotInfoModel.getUniqueId());
                MySharedPreferences.saveProfile(robotInfoModel.getProfile());
                MySharedPreferences.saveRobotName(robotInfoModel.getName());
                MySharedPreferences.saveDefaultRobot(robotInfoModel.getName());
                MySharedPreferences.saveCompanyName(robotInfoModel.getCompanyName());
                MySharedPreferences.saveRobotHeadPortrait(robotInfoModel.getRobotHeadImg());
                MySharedPreferences.saveIndustry(robotInfoModel.getHangye());
                MySharedPreferences.savePrice(robotInfoModel.getZidingyi());
                MySharedPreferences.setShengri(robotInfoModel.getShengri());
                MySharedPreferences.saveHangYeLevel(robotInfoModel.getHangyedengji());
                MySharedPreferences.saveSex(robotInfoModel.getSex());
                MySharedPreferences.saveRobotVal(robotInfoModel.getRobotVal());
                MySharedPreferences.saveEnable(robotInfoModel.isEnable());
                MySharedPreferences.saveUniversalAnswer1(robotInfoModel.getInvalidAnswer1());
                MySharedPreferences.saveUniversalAnswer2(robotInfoModel.getInvalidAnswer2());
                MySharedPreferences.saveUniversalAnswer3(robotInfoModel.getInvalidAnswer3());
                mHandler.sendEmptyMessage(1);
            }
        }

        @Override
        public void onResult(List<?> resultList) {
            Class<?> aClass = resultList.get(0).getClass();
            if (aClass.getName().equals(ChildRobotModel.class.getName())) {
                Utils.sChildRobotModels = (List<ChildRobotModel>) resultList;
                ApiManager.chooseChildRobot(
                        Utils.getRobotUniqueIdByChildRobotList(MySharedPreferences.getRobotName()), this);
            }
        }

        @Override
        public void onError(Object o) {
            if (o instanceof NewVersionModel) {
                NewVersionModel versionModel = (NewVersionModel) o;
                if (versionModel.getDescription().equals("检查失败!")) {
                    enterHome();
                }
            } else if (o instanceof String) {
                showShortToast("身份信息已过期, 请重新登录");
                mHandler.sendEmptyMessage(0);
            }
        }
    };

    private void getUpdateMsg() {
        ApiManager.checkNewVersion(new NewVersionObserver(mResultCallBack));
    }

    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("检测到新版本！")
                .setMessage(mVersionModel.getDescription())
                .setOnCancelListener(new DialogInterface.OnCancelListener() { //
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss(); // 取消显示对话框
                        enterHomeNow();   // 进入主页面
                    }
                }).setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
                download();
            }
        }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHomeNow();
            }
        }).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 下载新版本应用
     */
    private void download() {
        // 下载APK，并且替换安装
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//得到sd卡的状态
            // sdcard存在
            // 使用afnal下载apk文件
            FinalHttp finalhttp = new FinalHttp();
            // 得到sdcard路径
            String path = Const.FILE_PATH + "/" + Const.APP_NAME + "_V_" + mVersionModel.getVersion() + ".apk";
            finalhttp.download(mVersionModel.getApkUrl(), path, new AjaxCallBack<File>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    t.printStackTrace();
                    Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                    enterHomeNow();
                    super.onFailure(t, errorNo, strMsg);
                }

                /**
                 * count:为总的大小
                 * current:为当前下载的大小
                 */
                @Override
                public void onLoading(long count, long current) {//正在下载
                    super.onLoading(count, current);
                    //当前下载百分比
                    int progress = (int) (current * 100 / count);
                    //tv_update_info.setText("下载进度：" + progress +"%");
                    progressDialog.setProgress(progress);
                }

                /**
                 * file t：表示文件的路径
                 */
                @Override
                public void onSuccess(File t) {
                    super.onSuccess(t);
                    progressDialog.dismiss();
                    //成功的时候要安装apk
                    installAPK(t);
                }

                /**
                 * 安装APK
                 *
                 * @param t ：文件下载的路径
                 */
                private void installAPK(File t) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(SplashActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", t);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }

//                    intent.addCategory("android.intent.category.DEFAULT");
//                    intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
        } else {// sdcard不存在
            Toast.makeText(SplashActivity.this, "没有sdcard，请安装上在试", Toast.LENGTH_LONG).show();
        }
    }

    private void enterHomeNow() {
        if(MySharedPreferences.isFirstOpen()){//应用第一次启动
            mHandler.sendEmptyMessageDelayed(3, 2000);
        } else {
            if ("".equals(MySharedPreferences.getUsername())) {
                mHandler.sendEmptyMessage(0);
            } else {
                login();
            }
        }
    }

    /**
     * 没有检查到新版本或检查失败时执行
     */
    private void enterHome() {
        if(MySharedPreferences.isFirstOpen()){//应用第一次启动
            mHandler.sendEmptyMessageDelayed(3, 2000);
        } else {
            if ("".equals(MySharedPreferences.getUsername())) {//用户没有登录过
                mHandler.sendEmptyMessageDelayed(0, 0);
            } else {
                // login();
                mHandler.sendEmptyMessageDelayed(2, 0);
            }
        }
    }

    private void login() {
        if (MySharedPreferences.getLogin()) {
            mUserName = MySharedPreferences.getUsername();
            String password = MySharedPreferences.getPassword();
            ApiManager.login(mUserName, password, new LoginObserver(mResultCallBack));
        } else {
            startActivity(LoginActivity.class);
            finish();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_STORAGE:
                FileUtil.createOrExistsDir(Constants.FILE_PATH);
                getUpdateMsg();
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private static class SplashActivityHandler extends Handler {
        private WeakReference<SplashActivity> mActivityWeakReference;

        public SplashActivityHandler(SplashActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0://进入登录页面
                        activity.startActivity(LoginActivity.class);
                        activity.finish();
                        break;
                    case 1://进入主页面
                        activity.startActivity(HomeActivity.class);
                        activity.finish();
                        break;
                    case 2:
                        activity.login();
                        break;
                    case 3://进入引导页
                        activity.startActivity(GuideActivity.class);
                        activity.finish();
                        break;
                }
            }
        }
    }
}
