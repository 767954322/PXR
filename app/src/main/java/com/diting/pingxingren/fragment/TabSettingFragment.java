package com.diting.pingxingren.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.diting.pingxingren.R;
import com.diting.pingxingren.activity.ChildRobotActivity;
import com.diting.pingxingren.activity.GeneralActivity;
import com.diting.pingxingren.activity.MyCollectionActivity;
import com.diting.pingxingren.activity.MyLuckyActivity;
import com.diting.pingxingren.activity.QRCodeActivity;
import com.diting.pingxingren.activity.RobotManagerActivity;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseFragment;
import com.diting.pingxingren.custom.BadgeView;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.entity.Info;
import com.diting.pingxingren.model.UploadImageModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.net.observers.UploadImageObserver;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.UmengUtil;
import com.diting.pingxingren.util.Utils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.diting.pingxingren.R.id.ivPhoto;
import static com.diting.pingxingren.R.id.rlGeneral;
import static com.diting.pingxingren.R.id.rlPromotion;
import static com.diting.pingxingren.R.id.rlRobotManage;

//import com.diting.pingxingren.util.ImageUtil;

/**
 * Created by asus on 2017/2/22.
 * 我的
 */

public class TabSettingFragment extends BaseFragment implements View.OnClickListener {

    private CircleImageView mPhoto; // 机器人头像
    private TextView mRobotName; // 机器人名称
    private TextView mCompanyNameView; // 公司名称
    private TextView mConcernCount; // 粉丝数
    private RelativeLayout mRobotManagerLayout; // 机器人管理
    private RelativeLayout mPromotionLayout; // 推广机器人
    private RelativeLayout mGeneralLayout; // 通用
    private RelativeLayout mChildRobotLayout; // 子机器人
    private BadgeView mBadgeView;
    private BadgeView mLuckyBadgeView;
    private Bitmap photo;
    private String mHeadImageUrl;
    private File mHeadImageFile;
    private RelativeLayout rel_bussiness;
    private LinearLayout lay_bussiness_open;
    private RelativeLayout mMycollect;
    private RelativeLayout mMylucky;
    private TextView mValueCount;
    private RelativeLayout mRobotSetting;
    private ImageView ivBussiness;
    private Button reappoint;
    private Button luckyRed;
    private ImageView mSetting;
    private TextView mBussinessPhone;


    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_tab, null);
        initViews(view);
        initEvents();
        initData();
        return view;
    }

    private void initTitleBarView(View view) {
        TitleBarView titleBarView =  view.findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setTitleText("我的");
    }

    private void initViews(View view) {
        initTitleBarView(view);
        mPhoto = view.findViewById(R.id.ivPhoto);
        mRobotName = view.findViewById(R.id.tvRobotName);
        mCompanyNameView = view.findViewById(R.id.tvCompanyName);
        mConcernCount = view.findViewById(R.id.tvConcernCount);
        mRobotManagerLayout = view.findViewById(R.id.rlRobotManage);
        mPromotionLayout = view.findViewById(rlPromotion);
        mGeneralLayout = view.findViewById(R.id.rlGeneral);
        mChildRobotLayout = view.findViewById(R.id.rlChildRobot);
        rel_bussiness = view.findViewById(R.id.rel_bussiness);
        lay_bussiness_open = view.findViewById(R.id.lay_bussiness_open);
        mMycollect = view.findViewById(R.id.rlMy_collect);
        mMylucky = view.findViewById(R.id.rlMy_Lucky);
        mValueCount = view.findViewById(R.id.tvValueCount);
        mRobotSetting = view.findViewById(R.id.rel_RobotSetting);
        ivBussiness = view.findViewById(R.id.iv_bussiness);
        reappoint = view.findViewById(R.id.btn_mail_red);
        luckyRed = view.findViewById(R.id.btn_lucky_red);
        mSetting = view.findViewById(R.id.iv_mine_setting);
        mBussinessPhone=   view.findViewById(R.id.tv_bussiness_phone);
        initBadgeView(reappoint);
        initLuckyBadgeView(luckyRed);
    }

    private void initEvents() {
        mPhoto.setOnClickListener(this);
        mRobotManagerLayout.setOnClickListener(this);
        mPromotionLayout.setOnClickListener(this);
        mGeneralLayout.setOnClickListener(this);
        mChildRobotLayout.setOnClickListener(this);
        rel_bussiness.setOnClickListener(this);
        mMycollect.setOnClickListener(this);
        mMylucky.setOnClickListener(this);
        mRobotSetting.setOnClickListener(this);
        mSetting.setOnClickListener(this);
        mBussinessPhone.setOnClickListener(this);
    }

    private void initBadgeView(View mBtnMail) {
        mBadgeView = new BadgeView(getActivity(), mBtnMail);// 创建一个BadgeView对象，view为你需要显示提醒的控件// 需要显示的提醒类容
        mBadgeView.setBadgePosition(BadgeView.POSITION_CENTER);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        mBadgeView.setBackgroundResource(R.drawable.point);
        mBadgeView.setTextSize(12); // 文本大小
        mBadgeView.setBadgeMargin(40, 20); // 水平和竖直方向的间距
    }

    private void initLuckyBadgeView(View mBtnMail) {
        mLuckyBadgeView = new BadgeView(getActivity(), mBtnMail);// 创建一个BadgeView对象，view为你需要显示提醒的控件// 需要显示的提醒类容
        mLuckyBadgeView.setBadgePosition(BadgeView.POSITION_CENTER);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        mLuckyBadgeView.setBackgroundResource(R.drawable.point);
        mLuckyBadgeView.setTextSize(12); // 文本大小
        mLuckyBadgeView.setBadgeMargin(40, 20); // 水平和竖直方向的间距
    }

    private void initData() {
        if ((MyApplication.unreadLetterNum + MyApplication.unreadMessageNum) > 0) {
            mBadgeView.show();
        }
        if (!Utils.hasLuckyCount()) {
            mLuckyBadgeView.show();
        }

        mRobotName.setText("机器人名称:" + MySharedPreferences.getRobotName());
        mCompanyNameView.setText("主人名称:" + MySharedPreferences.getCompanyName());
        mConcernCount.setText("粉丝数 : " + MySharedPreferences.getFansCount());
        mValueCount.setText("价值 :" + MySharedPreferences.getRobotVal());
        String headImagePath = MySharedPreferences.getHeadPortrait();
//        if (Utils.isNotEmpty(headImagePath))
            Glide.with(this).load(headImagePath)
                .placeholder(R.mipmap.icon_right)
                .error(R.mipmap.icon_right)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mPhoto);
//        else
//            mPhoto.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.icon_right));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case rlRobotManage:
//                startActivity(RobotManagerActivity.callingIntent(getActivity()));
                break;
            case rlPromotion:
                MobclickAgent.onEvent(getActivity(),UmengUtil.EVENT_CLICK_NUM_OF_MINE_EXTENSION_ROBOT);
                startActivity(QRCodeActivity.class);
                break;
            case rlGeneral://通用设置
                MobclickAgent.onEvent(getActivity(),UmengUtil.EVENT_CLICK_NUM_OF_MINE_GENERAL_SETTING);
                startActivity(GeneralActivity.class);
                break;
            case ivPhoto:
//                AnnexUtil.showChooseImgDialog(TabSettingFragment.this);
                break;
            case R.id.rlChildRobot://子机器人
                MobclickAgent.onEvent(getActivity(),UmengUtil.EVENT_CLICK_NUM_OF_MINE_CHILD_ROBOT);
                startActivity(ChildRobotActivity.class);
                break;
            case R.id.rel_bussiness://商务合作
                MobclickAgent.onEvent(getActivity(),UmengUtil.EVENT_CLICK_NUM_OF_MINE_BUSINESS_COOPERATION);
                Utils.setImageView(ivBussiness, lay_bussiness_open.getVisibility()
                        == View.VISIBLE ? ContextCompat.getDrawable(getActivity(), R.mipmap.icon_arrow_right)
                        : ContextCompat.getDrawable(getActivity(), R.mipmap.icon_arrow_dowm));
                lay_bussiness_open.setVisibility(lay_bussiness_open.getVisibility()
                        == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.rlMy_collect://收藏
                MobclickAgent.onEvent(getActivity(),UmengUtil.EVENT_CLICK_NUM_OF_MINE_COLLECTION);
                startActivity(MyCollectionActivity.class);
                break;
            case R.id.rlMy_Lucky://活动
                MobclickAgent.onEvent(getActivity(),UmengUtil.EVENT_CLICK_NUM_OF_MINE_ACTIVITY);
                startActivity(MyLuckyActivity.class);
                break;
            case R.id.rel_RobotSetting:
//                startActivity(RobotManagerActivity.callingIntent(getActivity()));
                break;
            case R.id.iv_mine_setting://机器人设置
                MobclickAgent.onEvent(getActivity(), UmengUtil.EVENT_CLICK_NUM_OF_MINE_SETTING);
                startActivity(RobotManagerActivity.callingIntent(getActivity()));
                break;
            case R.id.tv_bussiness_phone:
                call(mBussinessPhone.getText().toString());
                break;


        }
    }
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE_STORAGE_PHOTO) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                AnnexUtil.takePhoto(getActivity());
            } else {
                Utils.showMissingPermissionDialog(getActivity(), "拍照");
            }
            return;
        }

        if (requestCode == Constants.REQUEST_CODE_STORAGE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AnnexUtil.choosePhoto(getActivity());
            } else {
                // Permission Denied
                Utils.showMissingPermissionDialog(getActivity(), "手机存储");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(Info info) {
        mRobotName.setText(info.getRobotName());
        mCompanyNameView.setText(info.getCompanyName());
        if (info.getHeadPortrait() != null) {
            mPhoto.setImageBitmap(info.getHeadPortrait());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mailReaded(String message) {
        if (message.equals("hideRedPoint")) {
            if(!Utils.hasUnreadMail()){
                mBadgeView.hide();
            }
            if(Utils.hasLuckyCount()){
                mLuckyBadgeView.hide();
            }
        } else if (message.equals("showRedPoint")) {
            mBadgeView.show();
        } else if (message.equals("chooseChild")) {
            initData();
        } else if (message.equals("updateHeadImage")) {
            initData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case AnnexUtil.TAKE_PICTURE:
                    AnnexUtil.startPhotoZoom(AnnexUtil.sTempUri, getActivity()); // 开始对图片进行裁剪处理
                    break;
                case AnnexUtil.CHOOSE_PICTURE:
                    AnnexUtil.startPhotoZoom(data.getData(), getActivity()); // 开始对图片进行裁剪处理
                    break;
                case AnnexUtil.CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上并上传图片
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");

            if (photo != null) {
                mHeadImageFile = FileSaveUtil.saveBitmap(photo);
                if (mHeadImageFile != null) {
                    showLoadingDialog("正在上传图片...");
                    mHeadImageUrl = mHeadImageFile.getAbsolutePath();
                    File annexFile = FileUtil.getFileByPath(mHeadImageUrl);
                    ApiManager.uploadImage(annexFile, new UploadImageObserver(mResultCallBack));

                }
            }
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof UploadImageModel) {
                UploadImageModel uploadImageModel = (UploadImageModel) result;
                mHeadImageUrl = uploadImageModel.getUrl() + "?imageMogr2";
                ApiManager.saveCompanyInfo(MySharedPreferences.getCompanyName(),
                        mHeadImageUrl, new ResultMessageObserver(mResultCallBack));
            } else if (result instanceof String) {
                String s = (String) result;
                if (s.equals("企业信息保存成功！")) {
                    ToastUtils.showShortToastSafe("保存信息成功!");
                    dismissLoadingDialog();
                    MySharedPreferences.saveHeadPortrait(mHeadImageUrl);
                    EventBus.getDefault().post("updateHeadImage");
                    mPhoto.setImageBitmap(photo);
                } else {
                    dismissLoadingDialog();
                    ToastUtils.showShortToastSafe("上传失败!");
                }
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            ToastUtils.showShortToastSafe("上传失败!");
        }
    };
}
