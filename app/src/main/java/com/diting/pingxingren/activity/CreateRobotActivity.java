package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.IndustryGradePopwindow;
import com.diting.pingxingren.custom.MultiEditInputView;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.SelectBirthdayPopWindow;
import com.diting.pingxingren.databinding.ActivityCreateRobotBinding;
import com.diting.pingxingren.dialog.VoiceInputDialog;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.model.RobotNameIsExistsModel;
import com.diting.pingxingren.model.UploadAnnexModel;
import com.diting.pingxingren.model.UploadImageModel;
import com.diting.pingxingren.model.WXPayModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CheckRobotNameObserver;
import com.diting.pingxingren.net.observers.UploadAnnexObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
import com.diting.pingxingren.util.WXPayUtils;
import com.hyphenate.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 18, 16:17.
 * Description: 创建机器人.
 */

public class CreateRobotActivity extends BaseActivity implements ClickListener,
        RadioGroup.OnCheckedChangeListener, SelectBirthdayPopWindow.OnTimeResultListener,
        IndustryGradePopwindow.OnInGradeResultListener, View.OnClickListener,
        VoiceInputDialog.InputResultCallBack, MultiEditInputView.TextChangeListener {

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, CreateRobotActivity.class);
        return intent;
    }

    public static Intent getCallIntent(Context context, Bundle bundle) {
        Intent intent = getCallIntent(context);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallIntent(Context context, double price) {
        Bundle bundle = new Bundle();
        bundle.putDouble("price", price);
        return getCallIntent(context, bundle);
    }

    private static final String TYPE_TEXT = "TYPE_TEXT";//文字
    private static final String TYPE_AUDIO = "TYPE_AUDIO";//音频
    private static final String TYPE_FILE = "TYPE_FILE";//文件
    private static final String TYPE_PICTURE = "TYPE_PICTURE";//图片
    private static final String SPACE = ";";//间隔符

    private ActivityCreateRobotBinding mBinding;
    private double mRobotPrice;
    private String mRobotName;
    private String mProfile;
    private String mIndustry;
    private String mIndustryLevel;
    private String mBirthday;
    private String mSex;
    private boolean mGeneralFunctionEnable;
    //选择生日的弹出框
    private SelectBirthdayPopWindow replaceBirthdayPopWindow;
    //选择行业等级选择
    private IndustryGradePopwindow industryGradePopwindow;
    private File mHeadImage;
    private String mOutTradeNo;//微信返回的订单号
    private CreateRobotActivityHandler mHandler;

    private MultiEditInputView mProfileInputView;
    private ImageView mWelcomePic;

    private VoiceInputDialog mInputDialog;

    private int mAnnexType = -1;
    private String headUrl = "";//头像上传后返回的路径
    String url = "";//数据类型+欢迎仪式上传后返回的路径
    private int picIndex;//判断返回的图片的作用:(0:修改头像;1:添加欢迎仪式)
    private boolean isUpDateHead;//是否修改了头像
    private boolean isUpdateWelcomeContent;//是否修改了欢迎仪式的内容
    private Uri mAnnexUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new CreateRobotActivityHandler(this);
        EventBus.getDefault().register(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_robot);
        mBinding.setClick(this);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        replaceBirthdayPopWindow = new SelectBirthdayPopWindow(this, mBinding.tvBirthdayDate);
        industryGradePopwindow = new IndustryGradePopwindow(this, mBinding.tvIndustryLevel);
        mBinding.scrCreate.smoothScrollTo(0, 0);
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
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRobotPrice = bundle.getDouble("price");
            mBinding.tvPrice.setText(getString(R.string.tv_price, String.valueOf(mRobotPrice)));
        }
        replaceBirthdayPopWindow.setOnTimeResultListener(this);
        industryGradePopwindow.setOnTimeResultListener(this);
        mBinding.sexRadioGroup.setOnCheckedChangeListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btPayNow://立即支付按钮
                if (checkRobotInfo()) {
                    //判断机器人名称是否重复
                    if(NetUtils.hasNetwork(this)){
                        showLoadingDialog(StringUtil.getString(R.string.loading));
                        ApiManager.checkRobotName(mRobotName,new CheckRobotNameObserver(mResultCallBack));
                    }
                } else {
                    // mResultCallBack.onError("请先完善机器人信息.");
                }
                break;
            case R.id.tvIndustry:
                startActivityForResult(SelectIndustryActivity.callingIntent(this,
                        mBinding.tvIndustry.getText().toString()), 4);
                break;
            case R.id.tvIndustryLevel:
                Utils.hideSoftInput(this, mBinding.industryLevelLayout);
                industryGradePopwindow.show(mBinding.tvIndustryLevel.getText().toString());
                break;
            case R.id.ivHeadImage:
                picIndex = 0;
                AnnexUtil.showChooseImgDialog("选择头像", CreateRobotActivity.this);
                break;
            case R.id.tvBirthdayDate:
                Utils.hideSoftInput(this, mBinding.tvBirthdayDate);
                replaceBirthdayPopWindow.show(mBinding.tvBirthdayDate.getText().toString());
                break;
            case R.id.switchOpen:
                if (!mGeneralFunctionEnable) {
                    showTipDialog();
                } else {
                    mGeneralFunctionEnable = false;
                    mBinding.switchOpen.setImageResource(R.mipmap.switch_on);
                }
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.iv_file://文件
                AnnexUtil.chooseFile(this);
                Utils.hideSoftInput(this, mProfileInputView);
                break;
            case R.id.iv_pic://图片
                picIndex = 1;
                AnnexUtil.showChooseImgDialog("选择图片", this);
                Utils.hideSoftInput(this, mProfileInputView);
                break;
            case R.id.iv_audio://录音
                if (mInputDialog == null) {
                    mInputDialog = new VoiceInputDialog(this);
                    mInputDialog.setResultCallBack(CreateRobotActivity.this);
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

    /**
     * 设置欢迎仪式内容类型显示控件
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

    @Override
    public void onClick(Object o) {
    }

    private void showTipDialog() {
        final MyDialog myDialog = new MyDialog(this);
        myDialog.setTitle("温馨提示");
        myDialog.setMessage("关闭后可能导致回答不准确,确定要关闭吗");
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                mGeneralFunctionEnable = true;
                mBinding.switchOpen.setImageResource(R.mipmap.switch_off);
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WXPayUtils.getInstance().onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean checkRobotInfo() {
        mRobotName = mBinding.etRobotName.getText().toString();
        if (Utils.isEmpty(mRobotName) || !Utils.isRobot(mRobotName)) {
            showShortToast("机器人名称不能为空或者格式错误, 请重新填写");
            return false;
        }

        if (mAnnexType == AnnexUtil.TEXT) {
            mProfile = mProfileInputView.getContentText().trim();
            url = TYPE_TEXT + SPACE + mProfile;
        }
        if (mAnnexType == -1 || Utils.isEmpty(mProfile)) {
            showShortToast(getResources().getString(R.string.tv_no_welcome));
            return false;
        }

        mIndustry = mBinding.tvIndustry.getText().toString().trim();
        mIndustry = checkIndustry(mIndustry);
        if (Utils.isEmpty(mIndustry)) {
            showShortToast(getString(R.string.industry_select));
            return false;
        }

        /*mIndustryLevel = mBinding.tvIndustryLevel.getText().toString().trim();
        mIndustryLevel = checkIndustryLevel(mIndustryLevel);
        if (Utils.isEmpty(mIndustryLevel)) {
            showShortToast("机器人行业等级不能为空");
            return false;
        }*/

        mBirthday = mBinding.tvBirthdayDate.getText().toString().trim();
        mBirthday = checkBirthday(mBirthday);
        if (Utils.isEmpty(mBirthday)) {
            showShortToast("机器人生日不能为空");
            return false;
        }

        if (mBinding.rbMale.isChecked()) {
            mSex = "0";
        } else if (mBinding.rbFemale.isSelected()) {
            mSex = "1";
        }
        return true;
    }

    private String checkIndustry(String industry) {
        if (getString(R.string.industry_select).equals(industry)) {
            return "";
        }
        return industry;
    }

    private String checkIndustryLevel(String industryLevel) {
        if (getString(R.string.industry_level_select).equals(industryLevel)) {
            showShortToast("请选择行业等级");
            return "";
        } else {
            return industryLevel;
        }
    }

    private String checkBirthday(String birthday) {
        if (getString(R.string.tv_chose_birthday).equals(birthday)) {
            showShortToast("请选择生日");
            return "";
        } else {
            return birthday;
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if(result instanceof RobotNameIsExistsModel){//检查机器人名称是否存在
                RobotNameIsExistsModel model = (RobotNameIsExistsModel) result;
                String message = model.getMessage();
                dismissLoadingDialog();
                if(message.contains("success")){
                    if (WXPayUtils.getInstance().isInstalled()) {
                        showLoadingDialog("正在生成订单...");
                        mBinding.btPayNow.setEnabled(false);
                        WXPayUtils.getInstance().unifiedOrder(mRobotPrice, mResultCallBack);
                    } else {
//                        if(!WXPayUtils.getInstance().isInstalled()){
                            mResultCallBack.onError("请先安装微信.");
//                        }else if(!WXPayUtils.getInstance().isSupport()){
//                            mResultCallBack.onError("不支持微信支付的设备.");
//                        }else{
//                            mResultCallBack.onError("未知错误.");
//                        }
                    }
                }else{
                    showShortToast(message);
                }
            }else if (result instanceof String) {
                String s = (String) result;
                if (s.contains("success")) {
                    dismissLoadingDialog();
                } else if (s.equals("子机器人创建成功.")) {
                    EventBus.getDefault().post("refreshRobots");
                    finish();
                }
            } else if (result instanceof WXPayModel) {
                WXPayModel wxPayModel = (WXPayModel) result;
                if (wxPayModel.getKey().equals("out_trade_no"))
                    mOutTradeNo = (String) wxPayModel.getValue();
            } else if (result instanceof UploadImageModel) {
                mHandler.sendEmptyMessage(1);
                isUpDateHead = false;
                UploadImageModel uploadImageModel = (UploadImageModel) result;
                headUrl = uploadImageModel.getUrl() + "?imageMogr2";

                uploadWelcomeFile();
            } else if (result instanceof UploadAnnexModel) {

                isUpdateWelcomeContent = false;
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
                ApiManager.createRobot(mRobotName, headUrl, url, mIndustry, mIndustryLevel, mBirthday,
                        mSex, mGeneralFunctionEnable, mOutTradeNo, mResultCallBack);
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            if (o instanceof String) {
                dismissLoadingDialog();
                String s = (String) o;
                ToastUtils.showShortToastSafe(s);
                mBinding.btPayNow.setEnabled(true);
            }
        }
    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == mBinding.rbMale.getId()) {
            mSex = "0";
        } else {
            mSex = "1";
        }
    }

    @Override
    public void onIngradeResult(String inGradeStr) {
        mBinding.tvIndustryLevel.setText(inGradeStr);
    }

    @Override
    public void onTimeResult(String timeStr, String timeType) {
        if (timeType.equals("yang")) {
            mBinding.tvBirthdayDate.setText(timeStr);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case AnnexUtil.TAKE_PICTURE:
                    switch (picIndex) {
                        case 0://头像
                            AnnexUtil.startPhotoZoom(AnnexUtil.sTempUri, CreateRobotActivity.this); // 开始对图片进行裁剪处理
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
                            AnnexUtil.startPhotoZoom(data.getData(), CreateRobotActivity.this); // 开始对图片进行裁剪处理
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
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上并上传图片
                    }
                    break;
                case 4:
                    if (data != null) {
                        String industry = data.getStringExtra("industry");
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

    private void create() {
        mHandler.sendEmptyMessage(0);
        if (isUpDateHead) {//修改了头像
            ApiManager.uploadImage(mHeadImage, new UploadImageObserver(mResultCallBack));
        } else {//未修改头像
            uploadWelcomeFile();
        }
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
                    ApiManager.createRobot(mRobotName, "null", url, mIndustry, mIndustryLevel, mBirthday,
                            mSex, mGeneralFunctionEnable, mOutTradeNo, mResultCallBack);
                }
            }
        } else {//没有修改个性签名
            showShortToast(getResources().getString(R.string.tv_no_welcome));
        }
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap headPhoto = extras.getParcelable("data");
        if (headPhoto != null) {

            File file = FileSaveUtil.saveBitmap(headPhoto);
            if (file != null) {
                isUpDateHead = true;
                mHeadImage = file;
            }
            mBinding.ivHeadImage.setImageBitmap(headPhoto);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void paySuccess(String message) {
        if (message.equals("paySuccess")) {
            create();
        } else if (message.equals("payCancel")) {
            dismissLoadingDialog();
            mBinding.btPayNow.setEnabled(true);
        }
    }

    @Override
    public void onResult(String result) {
        mProfile = mInputDialog.getmRecognizerPath();
        mWelcomePic.setImageResource(R.mipmap.icon_play);
        setmWelcomeType(1, AnnexUtil.AUDIO);
    }

    private static class CreateRobotActivityHandler extends Handler {
        private WeakReference<CreateRobotActivity> mActivityWeakReference;

        public CreateRobotActivityHandler(CreateRobotActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CreateRobotActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        activity.showLoadingDialog("正在创建机器人...");
                        break;
                    case 1:
                        activity.dismissLoadingDialog();
                }
            }
        }
    }
}
