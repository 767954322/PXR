package com.diting.pingxingren.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.diting.pingxingren.BuildConfig;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.NewVersionModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.NewVersionObserver;
import com.diting.pingxingren.util.AppUtil;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.Diting;
import com.diting.pingxingren.util.HttpCallBack;
import com.diting.pingxingren.util.Utils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by asus on 2017/1/16.
 */

public class AboutDitingActivity extends BaseActivity {
    private TitleBarView titleBarView;
    private Button btn_check_update;
    private TextView tv_version;
    private String apk_url;
    // 下载应用的进度条
    private ProgressDialog progressDialog;


    private NewVersionModel mVersionModel;
    //新版本号和描述语言
    private String update_versionName;
    private String update_description;

    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about_diting);
        initViews();
        initEvents();
    }

    private void initTitleBarView(){
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE,View.VISIBLE,View.GONE,View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back,null);
        titleBarView.setTitleText(R.string.about_diting);
    }

    private void initTitleBarEvents(){
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void initViews() {
        initTitleBarView();
        btn_check_update = findViewById(R.id.btn_check_update);
        tv_version = findViewById(R.id.tv_version);
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        tv_version.append(Utils.getCurrentVersion(this));
        btn_check_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUpdateMsg();
            }
        });
    }

    private void getUpdateMsg() {

        ApiManager.checkNewVersion(new NewVersionObserver(mResultCallBack));








        Diting.getUpdateMsg(new HttpCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    update_versionName = response.getString("version");
                    update_description = response.getString("description");
                    apk_url = response.getString("apkurl");
                    if (!update_versionName.equals(Utils.getCurrentVersion(AboutDitingActivity.this))) {
                        showNoticeDialog();
                    } else {
                        showShortToast("已是最新版本");
                    }
                } catch (JSONException e) {
                    showShortToast("已是最新版本");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(VolleyError error) {
                showShortToast("获取版本信息失败");
            }
        });
    }






    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof NewVersionModel) {
                mVersionModel = (NewVersionModel) result;
                int thisVersionCode = AppUtil.getAppVersionCode();
                if (thisVersionCode < mVersionModel.getVersionCode()) {
                    showNoticeDialog();
                }
            }
        }

        @Override
        public void onResult(List<?> resultList) {

        }

        @Override
        public void onError(Object o) {
            if (o instanceof NewVersionModel) {
                NewVersionModel versionModel = (NewVersionModel) o;
                if (versionModel.getDescription().equals("检查失败!")) {
                    showShortToast("获取版本信息失败");
                }
            }
        }
    };


















    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                download();
            } else
            {
                // Permission Denied
                Utils.showMissingPermissionDialog(AboutDitingActivity.this,"读写存储");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        new AlertDialog.Builder(this)
                .setTitle("检测到新版本！")
                .setMessage(update_description)
                .setOnCancelListener(new DialogInterface.OnCancelListener() { //
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss(); // 取消显示对话框
                    }
                }).setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
                //download();
                if (ContextCompat.checkSelfPermission(AboutDitingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(AboutDitingActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        Utils.showMissingPermissionDialog(AboutDitingActivity.this,"读写存储");
                    }else {
                        ActivityCompat.requestPermissions(AboutDitingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
                    }
                }else {
                    download();
                }
            }
        }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
            String path = Const.FILE_PATH + "/" + Const.APP_NAME + "_V_" + update_versionName + ".apk";
            finalhttp.download(apk_url, path, new AjaxCallBack<File>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    t.printStackTrace();
                    showShortToast("下载失败");
                    progressDialog.dismiss();
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
                        Uri contentUri = FileProvider.getUriForFile(AboutDitingActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", t);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    startActivity(intent);
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
        } else {// sdcard不存在
            showShortToast("没有sdcard，请安装上在试");
        }
    }


}
