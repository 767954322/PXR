package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.IndustryGradePopwindow;
import com.diting.pingxingren.custom.MultiEditInputView;
import com.diting.pingxingren.custom.MyCustomDialog;
import com.diting.pingxingren.custom.MyDialog;
import com.diting.pingxingren.custom.SelectBirthdayPopWindow;
import com.diting.pingxingren.dialog.VoiceInputDialog;
import com.diting.pingxingren.entity.ExternalOptions;
import com.diting.pingxingren.model.RobotInfoModel;
import com.diting.pingxingren.model.UploadAnnexModel;
import com.diting.pingxingren.model.UploadImageModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.CommonFeaturesObserver;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.net.observers.RobotInfoObserver;
import com.diting.pingxingren.net.observers.UploadAnnexObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 02, 16:28.
 * Description: 机器人管理.
 */

public class RobotManagerActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, SelectBirthdayPopWindow.OnTimeResultListener,
        IndustryGradePopwindow.OnInGradeResultListener, VoiceInputDialog.InputResultCallBack, MultiEditInputView.TextChangeListener {

    public static Intent callingIntent(Context context) {
        return new Intent(context, RobotManagerActivity.class);
    }

    private static final String TYPE_TEXT = "TYPE_TEXT";//文字
    private static final String TYPE_AUDIO = "TYPE_AUDIO";//音频
    private static final String TYPE_FILE = "TYPE_FILE";//文件
    private static final String TYPE_PICTURE = "TYPE_PICTURE";//图片
    private static final String SPACE = ";";//间隔符

    private CircleImageView mHeadImageView;
    private EditText mRobotNameEditText;
    private EditText mCompanyNameEditText;
    private MultiEditInputView mProfileInputView;
    private ImageView mWelcomePic;
    private RadioButton mMaleRadioButton;
    private RadioButton mFemaleRadioButton;
    private TextView mBirthdayDateView;
    private TextView mIndustryView;
    private TextView mIndustryLevelView;
    private TextView mOfficialPriceView;
    private TextView mCustomizePriceView;

    private ImageView mSwitchImage;
    private RadioGroup mSexRadioGroup;
    private TextView mComplete;
    private RelativeLayout mGeneralFunctionLayout;
    private ImageView mBackView;
    private ImageView mPricePrompt;
    //选择生日的弹出框
    private SelectBirthdayPopWindow replaceBirthdayPopWindow;
    //选择行业等级选择
    private IndustryGradePopwindow industryGradePopwindow;
    private String mRobotName;
    private String mCompanyName;
    private String mCustomizePrice;
    private String mIndustry;
    private String mIndustryLevel;
    private String mBirthday;
    private String mSex;
    private boolean mGeneralFunctionEnable;
    private ArrayList<ExternalOptions> mFunctionList;
    private ArrayList<Integer> mOpenFunctionList;
    private ArrayList<Integer> mCloseFunctionList;

    private int mAnnexType = -1;
    private String mProfile;
    private boolean isUpDateHead;//是否修改了头像
    private boolean isUpdateWelcomeContent;//是否修改了欢迎仪式的内容

    private Bitmap mHeadPhoto = null;
    private String mAnnexPath;
    private ScrollView mScrollview;
    private RelativeLayout mChangeHeader;
    private VoiceInputDialog mInputDialog;
    private Uri mAnnexUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_manger);
        initViews();
        initEvents();
        initData();
    }

    @Override
    protected void initViews() {
        mScrollview =  findViewById(R.id.home_scrollview);
        mWelcomePic = findViewById(R.id.iv_content);
        mScrollview.smoothScrollTo(0, 0);
        mHeadImageView =  findViewById(R.id.ivHeadImage);
        mRobotNameEditText = findViewById(R.id.etRobotName);
        mCompanyNameEditText =   findViewById(R.id.etCompanyName);
        mMaleRadioButton =  findViewById(R.id.rbMale);
        mFemaleRadioButton =   findViewById(R.id.rbFemale);
        /*mBirthdayLayout = (LinearLayout) findViewById(R.id.llBirthday);
        mIndustryLayout = (RelativeLayout) findViewById(R.id.industryLayout);
        mIndustryLevelLayout = (RelativeLayout) findViewById(R.id.industryLevelLayout);*/
        mBirthdayDateView =  findViewById(R.id.tvBirthdayDate);
        mIndustryView =  findViewById(R.id.tvIndustry);
        mIndustryLevelView =  findViewById(R.id.tvIndustryLevel);
        mOfficialPriceView =   findViewById(R.id.tvOfficialPrice);
        mCustomizePriceView =   findViewById(R.id.tvCustomizePrice);
        mSwitchImage =  findViewById(R.id.switchOpen);
        mSexRadioGroup =   findViewById(R.id.sexRadioGroup);
        mProfileInputView =   findViewById(R.id.etProfile);
        mComplete =   findViewById(R.id.tvComplete);
        mGeneralFunctionLayout = findViewById(R.id.rlGeneralFunction);
        mBackView =   findViewById(R.id.ivBack);
        mPricePrompt =   findViewById(R.id.ivPricePrompt);
        mChangeHeader = findViewById(R.id.rel_changeHeader);
        replaceBirthdayPopWindow = new SelectBirthdayPopWindow(this, mBirthdayDateView);
        industryGradePopwindow = new IndustryGradePopwindow(this, mIndustryLevelView);
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
        mHeadImageView.setOnClickListener(this);
        mBirthdayDateView.setOnClickListener(this);
        mSwitchImage.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        mBackView.setOnClickListener(this);
        mIndustryView.setOnClickListener(this);
        mIndustryLevelView.setOnClickListener(this);
        mGeneralFunctionLayout.setOnClickListener(this);
        mPricePrompt.setOnClickListener(this);
        mSexRadioGroup.setOnCheckedChangeListener(this);
        mCustomizePriceView.setOnClickListener(this);
        replaceBirthdayPopWindow.setOnTimeResultListener(this);
        industryGradePopwindow.setOnTimeResultListener(this);
        mChangeHeader.setOnClickListener(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case AnnexUtil.TAKE_PICTURE:
                    switch (picIndex) {
                        case 0://头像
                            AnnexUtil.startPhotoZoom(AnnexUtil.sTempUri, RobotManagerActivity.this); // 开始对图片进行裁剪处理
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
                            AnnexUtil.startPhotoZoom(data.getData(), RobotManagerActivity.this); // 开始对图片进行裁剪处理
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
                case 999:
                    if (data != null) {
                        mFunctionList = (ArrayList<ExternalOptions>) data.getSerializableExtra("functionList");
                        mOpenFunctionList = data.getIntegerArrayListExtra("openFunctionList");
                        mCloseFunctionList = data.getIntegerArrayListExtra("closeFunctionList");
                    }
                    break;
                case 4:
                    if (data != null) {
                        String industry = data.getStringExtra("industry");
                        if (!Utils.isEmpty(industry))
                            mIndustryView.setText(industry);
                    }
//                case AnnexUtil.CHOOSE_VIDEO://选择视频
//                    mAnnexType = AnnexUtil.VIDEO;
//                    Uri mAnnexUri = data.getData();
//                    mAnnexPath = AnnexUtil.getVideoPath(this.getContentResolver(), mAnnexUri);
//                    mWelcomePic.setImageBitmap(ThumbnailUtils.createVideoThumbnail(mAnnexPath, MediaStore.Video.Thumbnails.MICRO_KIND));
//                    setmWelcomeType(1);
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

    private void initData() {
        String headPortrait = MySharedPreferences.getHeadPortrait();
        if (!Utils.isEmpty(headPortrait)) {
            Glide.with(this).load(headPortrait).into(mHeadImageView);
        }

        getRobotInfo();
        ApiManager.getCommonFeatures(new CommonFeaturesObserver(mResultCallBack));
    }

    /**
     * 变化欢迎仪式内容
     *
     * @param type 类型 具体详见顶部static变量
     */
    private void initWelcomeContent(String type, String content) {
        switch (type) {
            case TYPE_TEXT:
                mProfileInputView.setVisibility(View.VISIBLE);
                mProfileInputView.setContentText(content);
                mAnnexType = AnnexUtil.TEXT;
                url = TYPE_TEXT + SPACE + content;
                break;
            case TYPE_PICTURE:
                mProfileInputView.setVisibility(View.INVISIBLE);
                mWelcomePic.setVisibility(View.VISIBLE);
                Glide.with(this).load(content).into(mWelcomePic);
                mAnnexType = AnnexUtil.PICTURE;
                url = TYPE_PICTURE + SPACE + content;
                break;
            case TYPE_AUDIO:
                mWelcomePic.setVisibility(View.VISIBLE);
                mProfileInputView.setVisibility(View.INVISIBLE);
                mWelcomePic.setImageResource(R.mipmap.icon_play);
                mAnnexType = AnnexUtil.AUDIO;
                url = TYPE_AUDIO + SPACE + content;
                break;
            case TYPE_FILE:
                mWelcomePic.setVisibility(View.VISIBLE);
                mProfileInputView.setVisibility(View.INVISIBLE);
                mWelcomePic.setImageResource(R.drawable.ic_file);
                mAnnexType = AnnexUtil.FILE;
                url = TYPE_FILE + SPACE + content;
                break;
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

    private String headUrl = "";//头像上传后返回的路径
    String url = "";//数据类型+欢迎仪式上传后返回的路径
    private String audioText = "";//语音录入的文字
    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof RobotInfoModel) {
                dismissLoadingDialog();
                setRobotInfo((RobotInfoModel) result);
            } else if (result instanceof UploadImageModel) {//上传的图片回调
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
                ApiManager.saveCompanyInfo(mCompanyName, MySharedPreferences.getHeadPortrait(), new ResultMessageObserver(this));
            } else if (result instanceof String) {
                String msg = (String) result;
                if (msg.equals("企业信息保存成功！")) {
                    ApiManager.saveRobotInfo("", mCompanyName, mRobotName,
                            url, mGeneralFunctionEnable, Integer.valueOf(mSex),
                            mIndustry, mIndustryLevel, mCustomizePrice, mBirthday,
                            MySharedPreferences.getUniversalAnswer1(),
                            MySharedPreferences.getUniversalAnswer2(),
                            MySharedPreferences.getUniversalAnswer3(),
                            new ResultMessageObserver(this));
                } else if (msg.contains("机器人信息修改成功")) {
                    LogUtils.e("=========================");
                    MySharedPreferences.saveCompanyName(mCompanyName);
                    MySharedPreferences.saveRobotName(mRobotName);
                    MySharedPreferences.saveIndustry(mIndustry);
                    MySharedPreferences.savePrice(mCustomizePrice);
                    MySharedPreferences.setShengri(mBirthday);
                    MySharedPreferences.saveHangYeLevel(mIndustryLevel);
                    MySharedPreferences.saveSex(Integer.valueOf(mSex));
                    MySharedPreferences.saveEnable(mGeneralFunctionEnable);
                    EventBus.getDefault().post("updateHeadImage");
                    ApiManager.saveCommonFeatures(mOpenFunctionList, mCloseFunctionList, new ResultMessageObserver(this));
                } else if (msg.equals("设置成功！")) {
                    dismissLoadingDialog();
                    ToastUtils.showShortToastSafe(msg);
                    EventBus.getDefault().post("updateHeadImage");
                    finish();
                }
            } else if (result instanceof Boolean) {
                ApiManager.getRobotInfo(new RobotInfoObserver(this));
            }else if(result instanceof JSONObject){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result.toString());
                    String message = jsonObject.getString("message");
                    if (message.contains("机器人信息修改成功")) {
                        LogUtils.e("========================+++++++++++++=");
                        MySharedPreferences.saveCompanyName(mCompanyName);
                        MySharedPreferences.saveRobotName(mRobotName);
                        MySharedPreferences.saveIndustry(mIndustry);
                        MySharedPreferences.savePrice(mCustomizePrice);
                        MySharedPreferences.setShengri(mBirthday);
                        MySharedPreferences.saveHangYeLevel(mIndustryLevel);
                        MySharedPreferences.saveSex(Integer.valueOf(mSex));
                        MySharedPreferences.saveEnable(mGeneralFunctionEnable);
                        EventBus.getDefault().post("updateHeadImage");
                        ApiManager.saveCommonFeatures(mOpenFunctionList, mCloseFunctionList, new ResultMessageObserver(this));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onResult(List<?> resultList) {
            if (resultList.size() > 0) {
                Class<?> aClass = resultList.get(0).getClass();
                if (aClass.getName().equals(ExternalOptions.class.getName())) {
                    mFunctionList = (ArrayList<ExternalOptions>) resultList;
                    ApiManager.getOpenCommonFeatures(this);
                } else if (aClass.getName().equals(Integer.class.getName())) {
                    List<Integer> openIds = (List<Integer>) resultList;
                    for (int i = 0; i < openIds.size(); i++) {
                        for (ExternalOptions externalOptions : mFunctionList) {
                            if (openIds.get(i) == externalOptions.getId()) {
                                externalOptions.setChoose(true);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            if (o instanceof String) {
                ToastUtils.showShortToastSafe((String) o);
            } else if (o instanceof Boolean) {
                ToastUtils.showShortToastSafe("获取机器人信息失败!");
            }
        }
    };

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
                    ApiManager.saveCompanyInfo(mCompanyName, MySharedPreferences.getHeadPortrait(), new ResultMessageObserver(mResultCallBack));
                }
            }
        } else {//没有修改个性签名
            ApiManager.saveCompanyInfo(mCompanyName, MySharedPreferences.getHeadPortrait(), new ResultMessageObserver(mResultCallBack));
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

    private void setRobotInfo(RobotInfoModel robotInfo) {
        headUrl = robotInfo.getHeadImg();
        String robotName = robotInfo.getName();
        if (!Utils.isEmpty(robotName))
            mRobotNameEditText.setText(robotName);
        String companyName = robotInfo.getCompanyName();
        if (!Utils.isEmpty(companyName))
            mCompanyNameEditText.setText(companyName);
        String profile = robotInfo.getProfile();
        if (!Utils.isEmpty(profile)) {
            String[] strings = profile.split(SPACE);
            if (strings.length != 2) {//当老用户没有该标记时
                mProfileInputView.setContentText(profile);
                setmWelcomeType(0, AnnexUtil.TEXT);
                mProfile = profile;
                url = TYPE_TEXT + SPACE + profile;
            } else {
                mProfile = strings[1];
                initWelcomeContent(strings[0], strings[1]);
            }
        } else {
            mAnnexType = AnnexUtil.TEXT;
        }
        String birthday = robotInfo.getShengri();
        if (Utils.isEmpty(birthday))
            mBirthdayDateView.setHint(getString(R.string.tv_chose_birthday));
        else
            mBirthdayDateView.setText(birthday);
        String industry = robotInfo.getHangye();
        if (Utils.isEmpty(industry))
            mIndustryView.setText(getString(R.string.industry_select));
        else
            mIndustryView.setText(industry);
        String officialPrice = robotInfo.getGuanfangjia();
        if (Utils.isEmpty(officialPrice))
            mOfficialPriceView.setText(getString(R.string.tv_price, "0"));
        else
            mOfficialPriceView.setText(getString(R.string.tv_price, officialPrice));
        String customizePrice = robotInfo.getZidingyi();
        if (Utils.isEmpty(customizePrice))
            mCustomizePriceView.setText(getString(R.string.tv_price, "0"));
        else
            mCustomizePriceView.setText(getString(R.string.tv_price, customizePrice));

        if (!robotInfo.isEnable()) {
            mGeneralFunctionEnable = false;
            mSwitchImage.setImageResource(R.mipmap.switch_on);
        } else {
            mGeneralFunctionEnable = true;
            mSwitchImage.setImageResource(R.mipmap.switch_off);
        }

        mSex = String.valueOf(robotInfo.getSex());
        if (mSex.equals("0")) {
            mMaleRadioButton.setChecked(true);
            mFemaleRadioButton.setChecked(false);
        } else {
            mMaleRadioButton.setChecked(false);
            mFemaleRadioButton.setChecked(true);
        }
    }

    private void getRobotInfo() {
        showLoadingDialog("加载中...");
//        ApiManager.getRobotInfo(new RobotInfoObserver(mResultCallBack));
        ApiManager.getRobotInfoByUniqueId(MySharedPreferences.getUniqueId(),new RobotInfoObserver(mResultCallBack));
    }

    private int picIndex;//判断返回的图片的作用:(0:修改头像;1:添加欢迎仪式)

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ivHeadImage:
                picIndex = 0;
                AnnexUtil.showChooseImgDialog("选择头像", RobotManagerActivity.this);
                break;
            case R.id.tvBirthdayDate:
                Utils.hideSoftInput(this, mBirthdayDateView);
                replaceBirthdayPopWindow.show(mBirthdayDateView.getText().toString());
                break;
            case R.id.tvIndustry:
                startActivityForResult(SelectIndustryActivity.callingIntent(this,
                        mIndustryView.getText().toString()), 4);
                break;
            case R.id.tvIndustryLevel:
//                Utils.hideSoftInput(this, mIndustryLevelView);
//                industryGradePopwindow.show(mIndustryLevelView.getText().toString());
                break;
            case R.id.switchOpen:
                if (!mGeneralFunctionEnable) {
                    showTipDialog();
                } else {
                    mGeneralFunctionEnable = false;
                    mSwitchImage.setImageResource(R.mipmap.switch_on);
                }
                break;
            case R.id.rlGeneralFunction:
                startActivityForResult(GeneralFunctionActivity
                        .callingIntent(this, mFunctionList), 999);
                break;
            case R.id.tvComplete://提交编辑
                completeData();
                break;
            case R.id.ivPricePrompt:
                showPriceTipDialog();
                break;
            case R.id.tvCustomizePrice:
                showCustomizePriceDialog();
                break;
            case R.id.rel_changeHeader:
                picIndex = 0;
                AnnexUtil.showChooseImgDialog("选择头像", RobotManagerActivity.this);
                break;
            case R.id.iv_file://文件
                AnnexUtil.chooseFile(this);
                Utils.hideSoftInput(this, mProfileInputView);
                break;
            case R.id.iv_pic://图片
                picIndex = 1;
                AnnexUtil.showChooseImgDialog("选择图片", RobotManagerActivity.this);
                Utils.hideSoftInput(this, mProfileInputView);
                break;
            case R.id.iv_audio://录音
                if (mInputDialog == null) {
                    mInputDialog = new VoiceInputDialog(this);
                    mInputDialog.setResultCallBack(RobotManagerActivity.this);
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

    private void showPriceTipDialog() {
        final MyCustomDialog myConDialog = new MyCustomDialog(this);
        myConDialog.setTitleStr(getResources().getString(R.string.tv_price_transaction_title));
        myConDialog.setConcentStr(getResources().getString(R.string.tv_price_tip));
        myConDialog.setTipFirstisShow(View.VISIBLE);
        myConDialog.setContent_tipFirstStr("注：");
        myConDialog.setContent_tipSecondStr(getResources().getString(R.string.tv_price_tipStr));
        myConDialog.setOkOnclickListener(getResources().getString(R.string.submit), new MyCustomDialog.onOkOnclickListener() {
            @Override
            public void onOkClick() {
                myConDialog.dismiss();
            }
        });
        myConDialog.show();
    }

    /**
     * 提交数据
     */
    private void completeData() {
        mRobotName = mRobotNameEditText.getText().toString().trim();
        mCompanyName = mCompanyNameEditText.getText().toString().trim();
        if (mAnnexType == AnnexUtil.TEXT) {
            mProfile = mProfileInputView.getContentText().trim();
            url = TYPE_TEXT + SPACE + mProfile;
        }
        mCustomizePrice = mCustomizePriceView.getText().toString().trim();
        mIndustry = mIndustryView.getText().toString().trim();
        mIndustryLevel = mIndustryLevelView.getText().toString().trim();
        mBirthday = mBirthdayDateView.getText().toString().trim();

        mCustomizePrice = checkPrice(mCustomizePrice);
        mIndustry = checkIndustry(mIndustry);
        mIndustryLevel = checkIndustryLevel(mIndustryLevel);
        mBirthday = checkBirthday(mBirthday);

        if (mOpenFunctionList == null)
            mOpenFunctionList = new ArrayList<>();
        if (mCloseFunctionList == null)
            mCloseFunctionList = new ArrayList<>();
        mOpenFunctionList.clear();
        mCloseFunctionList.clear();
        for (int i = 0; i < mFunctionList.size(); i++) {
            ExternalOptions externalOptions = mFunctionList.get(i);
            if (externalOptions.isChoose()) {
                mOpenFunctionList.add(externalOptions.getId());
            }
            mCloseFunctionList.add(externalOptions.getId());
        }

        if (Utils.isEmpty(mCompanyName)) {
            showShortToast("所有者名称不能为空");
            mComplete.setEnabled(true);
            return;
        }

        if (Utils.isEmpty(mRobotName)) {
            showShortToast("机器人名称不能为空");
            mComplete.setEnabled(true);
            return;
        }

        if (!Utils.isCompany(mCompanyName)) {
            showShortToast(R.string.tip_error_company);
            mComplete.setEnabled(true);
            return;
        }

        if (!Utils.isRobot(mRobotName)) {
            showShortToast(R.string.tip_error_robot);
            mComplete.setEnabled(true);
            return;
        }
        if (mAnnexType == -1 || Utils.isEmpty(mProfile)) {
            showShortToast("机器人个性签名不能为空");
            mComplete.setEnabled(true);
            return;
        }

        showLoadingDialog("提交中");
        if (isUpDateHead) {//修改了头像
            File file = FileUtil.getFileByPath(mAnnexPath);
            ApiManager.uploadImage(file, new UploadImageObserver(mResultCallBack));
        } else {//未修改头像
            uploadWelcomeFile();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == mMaleRadioButton.getId()) {
            mSex = "0";
        } else {
            mSex = "1";
        }
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
                mSwitchImage.setImageResource(R.mipmap.switch_off);
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        mHeadPhoto = extras.getParcelable("data");
        if (mHeadPhoto != null) {

            File file = FileSaveUtil.saveBitmap(mHeadPhoto);
            if (file != null) {
                isUpDateHead = true;
                mAnnexPath = file.getAbsolutePath();
            }
            mHeadImageView.setImageBitmap(mHeadPhoto);
        }
    }

    private String checkPrice(String customizePrice) {
        String price = customizePrice.replace("￥", "").trim();
        if (Integer.valueOf(price) == 0)
            return null;
        else
            return price;
    }

    private String checkIndustry(String industry) {
        if (getString(R.string.industry_select).equals(industry))
            return null;
        else
            return industry;
    }

    private String checkIndustryLevel(String industryLevel) {
        if (getString(R.string.industry_level_select).equals(industryLevel))
            return null;
        else
            return industryLevel;
    }

    private String checkBirthday(String birthday) {
        if (getString(R.string.tv_chose_birthday).equals(birthday))

            return null;
        else
            return birthday;
    }

    private void showCustomizePriceDialog() {
        final MyDialog myDialog = new MyDialog(this);
        String price = mCustomizePriceView.getText().toString().replace("￥", "").trim();

        myDialog.setContentText(price);
        myDialog.setIsContent(true);
        myDialog.setTitle("自定义");
        myDialog.setContentEditInputType(InputType.TYPE_CLASS_NUMBER);
        myDialog.setContentLength(8);
        myDialog.setYesOnclickListener("确定", new MyDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                if (!Utils.isEmpty(myDialog.getContentText())) {
                    mCustomizePriceView.setText(getString(R.string.tv_price, myDialog.getContentText()));
                } else {
                    mCustomizePriceView.setText("￥ 0");
                }
                myDialog.dismiss();
            }
        });
        myDialog.setNoOnclickListener("取消", new MyDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    /**
     * 设置完生日后返回的数据
     *
     * @param timeStr
     * @param timeType
     */
    @Override
    public void onTimeResult(String timeStr, String timeType) {
        if (timeType.equals("yang")) {
            mBirthdayDateView.setText(timeStr);
        }
    }

    /**
     * 设置完行业登记后返回的数据
     *
     * @param inGradeStr
     */
    @Override
    public void onIngradeResult(String inGradeStr) {
        mIndustryLevelView.setText(inGradeStr);
    }

    @Override
    public void onResult(String result) {
        this.audioText = result;
        mProfile = mInputDialog.getmRecognizerPath();//拿到路径
        mWelcomePic.setImageResource(R.mipmap.icon_play);
        setmWelcomeType(1, AnnexUtil.AUDIO);
    }

}
