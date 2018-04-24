package com.diting.pingxingren.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.MultiEditInputView;
import com.diting.pingxingren.databinding.ActivitySettingBinding;
import com.diting.pingxingren.dialog.VoiceInputDialog;
import com.diting.pingxingren.entity.RobotConcern;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.ChildRobotModel;
import com.diting.pingxingren.model.RobotNameIsExistsModel;
import com.diting.pingxingren.model.UploadAnnexModel;
import com.diting.pingxingren.model.UploadImageModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CheckRobotNameObserver;
import com.diting.pingxingren.net.observers.ChildRobotsObserver;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.net.observers.UploadAnnexObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/10, 15:50.
 * 登录界面.
 */

public class SettingActivity extends BaseActivity implements ClickListener,
        View.OnClickListener, VoiceInputDialog.InputResultCallBack,
        MultiEditInputView.TextChangeListener {

    private static final String TYPE_TEXT = "TYPE_TEXT";//文字
    private static final String TYPE_AUDIO = "TYPE_AUDIO";//音频
    private static final String TYPE_FILE = "TYPE_FILE";//文件
    private static final String TYPE_PICTURE = "TYPE_PICTURE";//图片
    private static final String SPACE = ";";//间隔符

    private static final String EXTRA = "robot";
    private ActivitySettingBinding mBinding;
    private String[] industries;
    private int industry = 1;
    private Button btnCommit;
    private Bitmap photo = null;
    public String mIndustry;
    private String mCompanyName;
    private String mRobotName;
    private String mProfile;
    private VoiceInputDialog mInputDialog;

    private MultiEditInputView mProfileInputView;
    private ImageView mWelcomePic;

    private int mAnnexType = -1;
    private String headUrl = "";//头像上传后返回的路径
    String url = "";//数据类型+欢迎仪式上传后返回的路径
    private int picIndex;//判断返回的图片的作用:(0:修改头像;1:添加欢迎仪式)
    private boolean isUpDateHead;//是否修改了头像
    private boolean isUpdateWelcomeContent;//是否修改了欢迎仪式的内容

    public static Intent getCallingIntent(Context context, RobotConcern robotConcern) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        intent.putExtra(EXTRA, robotConcern);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        industries = getResources().getStringArray(R.array.industry_list);
        initViews();
        initEvents();
        initData();
    }

    @Override
    protected void initViews() {
        mBinding.titleBar.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        mBinding.titleBar.setTitleText(R.string.robot_manage);
        mBinding.titleBar.setBtnLeft(R.mipmap.icon_back, null);
        mBinding.titleBar.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.titleBar.setBtnRightText("完成");
        btnCommit = mBinding.titleBar.getBtnRight();
        mBinding.titleBar.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();


            }
        });
        mWelcomePic = findViewById(R.id.iv_content);
        mProfileInputView = findViewById(R.id.etProfile);
        ImageView clickPic = findViewById(R.id.iv_pic);
        ImageView clickAudio = findViewById(R.id.iv_audio);
        ImageView clickFile = findViewById(R.id.iv_file);
        ImageView clickText = findViewById(R.id.iv_text);
        clickPic.setOnClickListener(this);
        clickAudio.setOnClickListener(this);
        clickFile.setOnClickListener(this);
        clickText.setOnClickListener(this);
    }

    @Override
    protected void initEvents() {
        mBinding.setClickListener(this);
        mWelcomePic.setOnClickListener(this);
        mProfileInputView.addTextChangeListener(this);
    }

    @Override
    public void afterChange(String text) {
        isUpdateWelcomeContent = true;
        mAnnexType = AnnexUtil.TEXT;
    }

    @Override
    public void numberExceedLimit() {
        showShortToast("已经超过字数限制, 请检查");
    }

    private void initData() {
    }

    private void commit() {
        mCompanyName = mBinding.etCompanyName.getText().toString();
        mRobotName = mBinding.etRobotName.getText().toString();

        if (mAnnexType == AnnexUtil.TEXT) {
            mProfile = mProfileInputView.getContentText().trim();
            url = TYPE_TEXT + SPACE + mProfile;
        }
        btnCommit.setEnabled(false);
        if (TextUtils.isEmpty(mCompanyName)) {
            showShortToast("所有者名称不能为空");
            btnCommit.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(mRobotName)) {
            showShortToast("机器人名称不能为空");
            btnCommit.setEnabled(true);
            return;
        }
        if (!Utils.isCompany(mCompanyName)) {
            showShortToast(R.string.tip_error_company);
            btnCommit.setEnabled(true);
            return;
        }
        if (!Utils.isRobot(mRobotName)) {
            showShortToast(R.string.tip_error_robot);
            btnCommit.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(mIndustry)) {
            showShortToast("请选择行业");
            btnCommit.setEnabled(true);
            return;
        }
        if (mAnnexType == -1 || Utils.isEmpty(mProfile)) {
            showShortToast(getResources().getString(R.string.tv_no_welcome));
            btnCommit.setEnabled(true);
            return;
        }
//        if (!Utils.isEmpty(mAnnexPath)) {
//            File annexFile = FileUtil.getFileByPath(mAnnexPath);
//            ApiManager.uploadImage(annexFile, new UploadImageObserver(mResultCallBack));
//        } else {
//            ApiManager.saveCompanyInfo(mCompanyName, MySharedPreferences.getRobotHeadPortrait(),
//                    new ResultMessageObserver(mResultCallBack));
//        }
        showLoadingDialog("提交中");
        ApiManager.checkRobotName(mRobotName,new CheckRobotNameObserver(mResultCallBack));

//        if (isUpDateHead) {//修改了头像
//            File file = FileUtil.getFileByPath(mAnnexPath);
//            ApiManager.uploadImage(file, new UploadImageObserver(mResultCallBack));
//        } else {//未修改头像
//            uploadWelcomeFile();
//        }
    }

    private boolean checkAnnexSize(File file) {
        long fileSize = FileUtil.getFileSize(file, FileUtil.FILE_SIZE_MB);
        switch (mAnnexType) {
            case AnnexUtil.FILE:
                if (fileSize > 10) {
                    showShortToast("文件大小超过10M, 请重新选择.");
                    return false;
                }
                return true;
            case AnnexUtil.PICTURE:
                if (fileSize > 2) {
                    showShortToast("图片大小超过2M, 请重新选择.");
                    return false;
                }
                return true;
            case AnnexUtil.AUDIO:
                if (fileSize > 5) {
                    showShortToast("音频大小超过5M, 请重新录音.");
                    return false;
                }
            default:
                return true;
        }
    }

    //提交欢迎仪式内容
    private void uploadWelcomeFile() {
        if (isUpdateWelcomeContent) {//修改了欢迎仪式
            if (mAnnexType != AnnexUtil.TEXT && mAnnexType != -1) {//如果是文件
                File f = FileUtil.getFileByPath(mProfile);
                if (f != null) {//如果文件存在
                    if (checkAnnexSize(f)){
                        ApiManager.uploadAnnex(f, new UploadAnnexObserver(mResultCallBack));
                    }else{
                        dismissLoadingDialog();
                    }
                } else {//如果文件不存在
                    dismissLoadingDialog();
                    ToastUtils.showShortToast("文件出错，请重试");
                }
            } else if (mAnnexType == AnnexUtil.TEXT) {//如果是文字
                mProfile = mProfileInputView.getContentText();
                if (Utils.isEmpty(mProfile)) {//如果没有输入文字
                    showShortToast(getResources().getString(R.string.tv_no_welcome));
                } else {//输入框有内容
                    url = TYPE_TEXT + SPACE + mProfile;
                    ApiManager.saveCompanyInfo(mCompanyName, headUrl, new ResultMessageObserver(mResultCallBack));
                }
            }
        } else {//没有修改个性签名
            ApiManager.saveCompanyInfo(mCompanyName, MySharedPreferences.getHeadPortrait(), new ResultMessageObserver(mResultCallBack));
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof UploadImageModel) {
                UploadImageModel uploadImageModel = (UploadImageModel) result;
                headUrl = uploadImageModel.getUrl() + "?imageMogr2";
                MySharedPreferences.saveHeadPortrait(headUrl);
                EventBus.getDefault().post("updateHeadImage");
                isUpDateHead = false;
                uploadWelcomeFile();
            } else if (result instanceof UploadAnnexModel) {
                UploadAnnexModel uploadAnnexModel = (UploadAnnexModel) result;
                switch (mAnnexType) {
                    case AnnexUtil.AUDIO:
                        url = TYPE_AUDIO + SPACE + uploadAnnexModel.getUrl();
                        break;
                    case AnnexUtil.FILE:
                        url = TYPE_FILE + SPACE + uploadAnnexModel.getUrl();
                        break;
                    case AnnexUtil.PICTURE:
                        url = TYPE_PICTURE + SPACE + uploadAnnexModel.getUrl();
                        break;
                }
                isUpdateWelcomeContent = false;
                ApiManager.saveCompanyInfo(mCompanyName, headUrl, new ResultMessageObserver(this));
            } else if (result instanceof String) {
                String s = (String) result;
                if (s.equals("企业信息保存成功！")) {


                    ApiManager.createRobot(mRobotName, "null", url, mIndustry, "普通", "",
                            "1", true, "", mResultCallBack);


//                    ApiManager.saveRobotInfo("", mCompanyName, mRobotName,
//                            url, false, Integer.valueOf("1"),
//                            mIndustry, "普通", "", "", "", "", "", new ResultMessageObserver(this));
                } else if (s.contains("子机器人创建成功")) {
                    dismissLoadingDialog();
                    MySharedPreferences.saveIndustry(mIndustry);
                    MySharedPreferences.saveRobotName(mRobotName);
                    MySharedPreferences.saveDefaultRobotName(mRobotName);
                    MySharedPreferences.saveDefaultRobot(mRobotName);
                    MySharedPreferences.saveCompanyName(mCompanyName);
                    ApiManager.getChildRobots(new ChildRobotsObserver(this));
                }
            } else if (result instanceof Boolean) {
                startActivity(HomeActivity.class);
                finish();
            } else if (result instanceof JSONObject) {
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {
                        dismissLoadingDialog();
                        MySharedPreferences.saveIndustry(mIndustry);
                        MySharedPreferences.saveRobotName(mRobotName);
                        MySharedPreferences.saveDefaultRobotName(mRobotName);
                        MySharedPreferences.saveDefaultRobot(mRobotName);
                        MySharedPreferences.saveCompanyName(mCompanyName);
                        ApiManager.getChildRobots(new ChildRobotsObserver(this));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (result instanceof RobotNameIsExistsModel) {
                RobotNameIsExistsModel model = (RobotNameIsExistsModel) result;
                String message = model.getMessage();
//                dismissLoadingDialog();
                if (message.contains("success")) {
                    if (isUpDateHead) {//修改了头像
                        File file = FileUtil.getFileByPath(mAnnexPath);
                        ApiManager.uploadImage(file, new UploadImageObserver(mResultCallBack));
                    } else {//未修改头像
                        uploadWelcomeFile();
                    }
                }else{   dismissLoadingDialog();
                    showShortToast(message);
                }
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
            dismissLoadingDialog();
            btnCommit.setEnabled(true);
            if (o instanceof String) {
                String s = (String) o;
                ToastUtils.showShortToastSafe(s);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_photo://选择头像
                picIndex = 0;
                AnnexUtil.showChooseImgDialog("选择头像", SettingActivity.this);
                break;
            case R.id.tv_industry:
                startActivityForResult(SelectIndustryActivity.callingIntent(this,
                        mBinding.tvIndustry.getText().toString()), 4);
                break;
            case R.id.iv_file://文件
                AnnexUtil.chooseFile(this);
                Utils.hideSoftInput(this, mProfileInputView);
                break;
            case R.id.iv_pic://图片
                picIndex = 1;
                AnnexUtil.showChooseImgDialog("选择图片", SettingActivity.this);
                Utils.hideSoftInput(this, mProfileInputView);
                break;
            case R.id.iv_audio://录音
                if (mInputDialog == null) {
                    mInputDialog = new VoiceInputDialog(this);
                    mInputDialog.setResultCallBack(SettingActivity.this);
                }
                mInputDialog.showDialog();
                Utils.hideSoftInput(this, mProfileInputView);
                break;
            case R.id.iv_text:
                setmWelcomeType(0, AnnexUtil.TEXT);
                break;
            case R.id.iv_content://点击欢迎语内容
                showDetail();
                break;
        }
    }

    private String getWelcomeUrl() {
        return !Utils.isEmpty(mProfile) ? mProfile : url.split(SPACE)[1];
    }

    private void showDetail() {
        Intent detailIntent = new Intent();
        switch (mAnnexType) {
            case AnnexUtil.PICTURE:
                if (!Utils.isEmpty(url)) {
                    detailIntent.setAction("photo");
                    detailIntent.putExtra("media_url", getWelcomeUrl());
                    detailIntent.setClass(this, EnclosureShowActivity.class);
                } else {
                    showShortToast("无效地址");
                }
                break;
            case AnnexUtil.VIDEO:
                if (!Utils.isEmpty(url)) {
                    detailIntent.setAction("video");
                    detailIntent.putExtra("media_url", getWelcomeUrl());
                    detailIntent.setClass(this, EnclosureShowActivity.class);
                } else {
                    showShortToast("无效地址");
                }
                break;
            case AnnexUtil.AUDIO:
                if (!Utils.isEmpty(url)) {
                    detailIntent.setAction("audio");
                    detailIntent.putExtra("media_url", getWelcomeUrl());
                    detailIntent.setClass(this, EnclosureShowActivity.class);
                } else {
                    showShortToast("无效地址");
                }
                break;
            case AnnexUtil.FILE:
                if (getWelcomeUrl().contains("http")) {
                    detailIntent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(getWelcomeUrl());
                    detailIntent.setData(content_url);
                    // detailIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                } else {
                    File detailFile = FileUtil.getFileByPath(getWelcomeUrl());
                    detailIntent.setAction(Intent.ACTION_VIEW);
                    detailIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    detailIntent.setDataAndType(mAnnexUri, FileUtil.getMIMEType(detailFile));
                }
                break;
            case AnnexUtil.HYPERLINK:
                mAnnexType = -1;
                break;
        }
        if (detailIntent.resolveActivity(getPackageManager()) != null)
            startActivity(detailIntent);
        else
            ToastUtils.showShortToastSafe("没有查看的应用, 请下载WPS或者其他查看工具.");
    }

    /**
     * 设置欢迎仪式内容类型显示控件
     *
     * @param type 0:文字输入;1:图片及视频等内容
     */
    private void setmWelcomeType(int type, int mAnnextype) {
        isUpdateWelcomeContent = true;
        switch (type) {
            case 0://显示输入框
                mAnnexType = AnnexUtil.TEXT;
                mProfileInputView.setContentText("");
                mProfileInputView.setVisibility(View.VISIBLE);
                mWelcomePic.setVisibility(View.GONE);
                break;
            case 1://显示图片框
                this.mAnnexType = mAnnextype;
                mWelcomePic.setVisibility(View.VISIBLE);
                mProfileInputView.setVisibility(View.INVISIBLE);//此处输入框支撑parent的高度
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Constants.REQUEST_CODE_STORAGE_PHOTO) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                AnnexUtil.takePhoto(SettingActivity.this);
            } else {
                Utils.showMissingPermissionDialog(SettingActivity.this, "拍照");
            }
            return;
        }

        if (requestCode == Constants.REQUEST_CODE_STORAGE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AnnexUtil.choosePhoto(SettingActivity.this);
            } else {
                // Permission Denied
                Utils.showMissingPermissionDialog(SettingActivity.this, "手机存储");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String mAnnexPath;
    private Uri mAnnexUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case AnnexUtil.TAKE_PICTURE:
                    switch (picIndex) {
                        case 0://头像
                            AnnexUtil.startPhotoZoom(AnnexUtil.sTempUri, SettingActivity.this); // 开始对图片进行裁剪处理
                            break;
                        case 1://欢迎仪式
                            mProfile = AnnexUtil.sTempPath;
                            mWelcomePic.setImageBitmap(ImageUtil.lessenUriImage(mProfile));
                            setmWelcomeType(1, AnnexUtil.PICTURE);
                            break;
                    }
                    break;
                case AnnexUtil.CHOOSE_PICTURE:
                    switch (picIndex) {
                        case 0://头像
                            AnnexUtil.startPhotoZoom(data.getData(), SettingActivity.this); // 开始对图片进行裁剪处理
                            break;
                        case 1://欢迎仪式
                            Uri mAnnexUri = data.getData();
                            mProfile = AnnexUtil.getImagePath(this.getContentResolver(), mAnnexUri);
                            mWelcomePic.setImageBitmap(ImageUtil.lessenUriImage(mProfile));
                            setmWelcomeType(1, AnnexUtil.PICTURE);
                            break;
                    }
                    break;
                case AnnexUtil.CROP_SMALL_PICTURE:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap mAnnexUri = extras.getParcelable("data");
                        File file = FileSaveUtil.saveBitmap(mAnnexUri);
                        if (file != null) {
                            isUpDateHead = true;
                            mAnnexPath = file.getAbsolutePath();
                        }
                        mBinding.ivPhoto.setImageBitmap(mAnnexUri);
                    }
                    break;
                case 4:
                    if (data != null) {
                        String industry = data.getStringExtra("industry");
                        mIndustry = industry;
                        if (!Utils.isEmpty(industry))
                            mBinding.tvIndustry.setText(industry);
                    }
                    break;
                case AnnexUtil.CHOOSE_FILE:
                    mAnnexUri = data.getData();
                    mProfile = AnnexUtil.getFileAbsolutePath(this, mAnnexUri);
                    if (Utils.isEmpty(mProfile)) {
                        ToastUtils.showShortToastSafe("请选择合适的文件");
                    } else {
                        setmWelcomeType(1, AnnexUtil.FILE);
                        mWelcomePic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_file));
                    }
                    break;
            }
        }
    }

    @Override
    public void onClick(Object o) {
    }

    @Override
    public void onResult(String result) {
        mProfile = mInputDialog.getmRecognizerPath();
        mWelcomePic.setImageResource(R.mipmap.icon_play);
        setmWelcomeType(1, AnnexUtil.AUDIO);
    }
}
