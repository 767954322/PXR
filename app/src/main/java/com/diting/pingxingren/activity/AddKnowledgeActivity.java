package com.diting.pingxingren.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.dialog.VoiceInputDialog;
import com.diting.pingxingren.easypermissions.AppSettingsDialog;
import com.diting.pingxingren.easypermissions.EasyPermissions;
import com.diting.pingxingren.model.AskListModel;
import com.diting.pingxingren.model.AskRobotModel;
import com.diting.pingxingren.model.CommonLanguageModel;
import com.diting.pingxingren.model.KnowledgeItemModel;
import com.diting.pingxingren.model.UploadAnnexModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.net.observers.AskChangeSaveObserver;
import com.diting.pingxingren.net.observers.ResultMessageObserver;
import com.diting.pingxingren.net.observers.UploadAnnexObserver;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.TextLengthUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
import com.diting.pingxingren.util.VideoThumbLoaderUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by asus on 2017/1/6.
 * 添加知识内容页面
 */

public class AddKnowledgeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, VoiceInputDialog.InputResultCallBack, View.OnClickListener {

    public static Intent callingIntent(Context context) {
        return new Intent(context, AddKnowledgeActivity.class);
    }

    public static Intent callingIntent(Context context, Bundle bundle) {
        Intent intent = new Intent(context, AddKnowledgeActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent callingIntent(Context context, AskListModel askListModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("askListModel", askListModel);
        return callingIntent(context, bundle);
    }

    public static Intent callingIntent(Context context, KnowledgeItemModel knowledgeItemModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("knowledge", knowledgeItemModel);
        return callingIntent(context, bundle);
    }

    private TitleBarView titleBarView;
    private ImageView iv_photo;
    private ImageView mDeleteView;
    private EditText et_question;
    private EditText et_answer;
    private EditText et_scene;
    private Button btn_commit;
    private String mAnnexPath;
    private Uri mAnnexUri;
    private LinearLayout ll_main;
    private boolean isFromAdd = true;
    private boolean isAsk = false;
    private KnowledgeItemModel mKnowledgeItemModel;
    private LinearLayout ll_tip;
    private int mAnnexType = -1;
    private AddKnowledgeActivityHandler mHandler;
    private ImageView mVoiceInput;
    private VoiceInputDialog mInputDialog;
    private ImageView ivQuVoiceInput;
    private boolean isAnswer;
    private LinearLayout lay_copyLink;
    private EditText et_Hyperlink;
    private ImageView ivLinkDelete;
    private AskListModel askListModel;
    private TextView tvQuVoiceTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_knowledge);
        mHandler = new AddKnowledgeActivityHandler(this);
        initViews();
        initEvents();

        if (getIntent().getExtras() != null) {
            mKnowledgeItemModel = getIntent().getExtras().getParcelable("knowledge");
            askListModel = getIntent().getExtras().getParcelable("askListModel");

            if (mKnowledgeItemModel != null) {//初始化编辑已存在内容
                isFromAdd = false;
                isAsk = false;
                titleBarView.setTitleText("编辑内容");
                et_question.setText(mKnowledgeItemModel.getQuestion());
                et_answer.setText(mKnowledgeItemModel.getAnswer());
                et_scene.setText(mKnowledgeItemModel.getScene());
                String picture = mKnowledgeItemModel.getImg_url();
                final String video = mKnowledgeItemModel.getVideo_url();
                String audio = mKnowledgeItemModel.getAudio_url();
                String file = mKnowledgeItemModel.getFile_url();
                String hyperLink = mKnowledgeItemModel.getHyperlink_url();
                if (!Utils.isEmpty(picture)) {
                    mAnnexType = AnnexUtil.PICTURE;
                    mAnnexPath = picture;
                    Glide.with(this).load(mAnnexPath).placeholder(R.drawable.ic_preloading).into(iv_photo);
                } else if (!Utils.isEmpty(audio)) {
                    mAnnexType = AnnexUtil.AUDIO;
                    mAnnexPath = audio;
                    iv_photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio));
                } else if (!Utils.isEmpty(file)) {
                    mAnnexType = AnnexUtil.FILE;
                    mAnnexPath = file;
                    iv_photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_file));
                } else if (!Utils.isEmpty(video)) {
                    mAnnexType = AnnexUtil.VIDEO;
                    mAnnexPath = video;
                    iv_photo.setTag(video);
                    VideoThumbLoaderUtil.getInstance().showThumbByAsyncTack(video, iv_photo);
                } else if (!Utils.isEmpty(hyperLink)) {
                    mAnnexType = AnnexUtil.HYPERLINK;
                    mAnnexPath = hyperLink;
                    iv_photo.setVisibility(View.GONE);
                    mDeleteView.setVisibility(View.GONE);
                    lay_copyLink.setVisibility(View.VISIBLE);
                    et_Hyperlink.setVisibility(View.VISIBLE);
                    et_Hyperlink.setText(hyperLink);
                    ivLinkDelete.setVisibility(View.VISIBLE);
                } else {
                    mAnnexType = -1;
                }

                if (mAnnexType != -1 && mAnnexType != 5) {
                    mDeleteView.setVisibility(View.VISIBLE);
                } else {
                    mDeleteView.setVisibility(View.GONE);
                }

                ll_tip.setVisibility(View.GONE);
            } else if (askListModel != null) {
                isFromAdd = true;
                isAsk = true;
                ivQuVoiceInput.setVisibility(View.GONE);
                tvQuVoiceTip.setVisibility(View.GONE);
                et_question.setText(askListModel.getQuestion());
                et_question.setFocusable(false);
            }
        }
    }

    private void initTitleBarView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setTitleText("添加内容");
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarView.setBtnRightText("保存");
    }


    @Override
    protected void initViews() {
        initTitleBarView();
        et_question = findViewById(R.id.et_question);
        et_answer = findViewById(R.id.et_answer);
        et_scene = findViewById(R.id.et_scene);
        iv_photo = findViewById(R.id.iv_photo);
        mDeleteView = findViewById(R.id.ivDelete);
        //btn_commit = (Button)findViewById(R.id.btn_commit);
        btn_commit = titleBarView.getBtnRight();
        ll_main = findViewById(R.id.ll_main);
        ll_tip = findViewById(R.id.ll_tip);
        mVoiceInput = findViewById(R.id.ivVoiceInput);
        ivQuVoiceInput = findViewById(R.id.ivQuVoiceInput);
        lay_copyLink = findViewById(R.id.lay_copyLink);
        et_Hyperlink = findViewById(R.id.et_Hyperlink);
        ivLinkDelete = findViewById(R.id.ivLinkDelete);
        tvQuVoiceTip = findViewById(R.id.tvQuVoiceTip);
    }

    @Override
    protected void initEvents() {
        et_question.setFilters(new InputFilter[]{new TextLengthUtil(this, 61, "输入问题的内容不能超过60字")});
        et_answer.setFilters(new InputFilter[]{new TextLengthUtil(this, 241, "输入答案的问题内容不能超过240个字")});


        mDeleteView.setOnClickListener(this);
        iv_photo.setOnClickListener(this);
        mVoiceInput.setOnClickListener(this);
        ivQuVoiceInput.setOnClickListener(this);
        ivLinkDelete.setOnClickListener(this);
//        mDeleteView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAnnexType = -1;
//                iv_photo.setImageResource(R.mipmap.icon_add_image);
//                mDeleteView.setVisibility(View.GONE);
//            }
//        });
//        iv_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.hideSoftInput(AddKnowledgeActivity.this, v);
////
//                if (mAnnexType != -1) {
//                    showDetail();
//                } else {
//                    AnnexUtil.showChooseAnnexDialog(AddKnowledgeActivity.this);
//                }
//            }
//        });

        //保存
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromAdd) {
                    addQuestion();
                } else {
                    editQuestion();
                }
            }
        });
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.hideSoftInput(AddKnowledgeActivity.this, v);
                return false;
            }
        });
//        mVoiceInput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.hideSoftInput(AddKnowledgeActivity.this, v);
//                isAnswer = true;
//                if (mInputDialog == null) {
//                    mInputDialog = new VoiceInputDialog(AddKnowledgeActivity.this);
//                    mInputDialog.setResultCallBack(AddKnowledgeActivity.this);
//                }
//
//                mInputDialog.showDialog();
//            }
//        });
//        ivQuVoiceInput.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isAnswer = false;
//                Utils.hideSoftInput(AddKnowledgeActivity.this, v);
//                if (mInputDialog == null) {
//                    mInputDialog = new VoiceInputDialog(AddKnowledgeActivity.this);
//                    mInputDialog.setResultCallBack(AddKnowledgeActivity.this);
//                }
//                mInputDialog.showDialog();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivDelete:
                mAnnexType = -1;
                iv_photo.setImageResource(R.mipmap.icon_add_image);
                mDeleteView.setVisibility(View.GONE);
                break;
            case R.id.iv_photo://选择附件
                Utils.hideSoftInput(AddKnowledgeActivity.this, v);
                if (mAnnexType != -1) {
                    showDetail();
                } else {
                    AnnexUtil.showChooseAnnexDialog(AddKnowledgeActivity.this);
                }
                break;
            case R.id.ivVoiceInput:
                et_question.clearFocus();
                et_answer.requestFocus();
                Utils.hideSoftInput(AddKnowledgeActivity.this, v);
                isAnswer = true;
                if (mInputDialog == null) {
                    mInputDialog = new VoiceInputDialog(AddKnowledgeActivity.this);
                    mInputDialog.setResultCallBack(AddKnowledgeActivity.this);
                    mInputDialog.resetView();
                }

                mInputDialog.showDialog();
                break;
            case R.id.ivQuVoiceInput:
                isAnswer = false;
                et_question.requestFocus();
                et_answer.clearFocus();
                Utils.hideSoftInput(AddKnowledgeActivity.this, v);
                if (mInputDialog == null) {
                    mInputDialog = new VoiceInputDialog(AddKnowledgeActivity.this);
                    mInputDialog.setResultCallBack(AddKnowledgeActivity.this);
                    mInputDialog.resetView();
                }

                mInputDialog.showDialog();
                break;
            case R.id.ivLinkDelete:
                mAnnexType = -1;
                et_Hyperlink.setText("");
                lay_copyLink.setVisibility(View.GONE);
                iv_photo.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            if (requestCode != 6)
                mDeleteView.setVisibility(View.VISIBLE);
            switch (requestCode) {
                case AnnexUtil.CHOOSE_PICTURE:
                    mAnnexType = AnnexUtil.PICTURE;
                    mAnnexUri = data.getData();
                    mAnnexPath = AnnexUtil.getImagePath(this.getContentResolver(), mAnnexUri);
                    iv_photo.setImageBitmap(AnnexUtil.lessenUriImage(mAnnexPath));
                    break;
                case AnnexUtil.CHOOSE_VIDEO:
                    mAnnexType = AnnexUtil.VIDEO;
                    mAnnexUri = data.getData();
                    mAnnexPath = AnnexUtil.getVideoPath(this.getContentResolver(), mAnnexUri);
                    iv_photo.setImageBitmap(ThumbnailUtils.createVideoThumbnail(mAnnexPath, MediaStore.Images.Thumbnails.MICRO_KIND));
                    break;
                case AnnexUtil.TAKE_PICTURE:
                    mAnnexType = AnnexUtil.PICTURE;
                    mAnnexPath = AnnexUtil.sTempPath;
                    iv_photo.setImageBitmap(AnnexUtil.lessenUriImage(mAnnexPath));
                    break;
                case AnnexUtil.CHOOSE_AUDIO:
                    mAnnexType = AnnexUtil.AUDIO;
                    mAnnexUri = data.getData();
                    mAnnexPath = AnnexUtil.getAudioPath(this.getContentResolver(), mAnnexUri);
                    iv_photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio));
                    break;
                case AnnexUtil.CHOOSE_FILE:
                    mAnnexType = AnnexUtil.FILE;
                    mAnnexUri = data.getData();
                    mAnnexPath = AnnexUtil.getFilePath(this, mAnnexUri);
                    if (Utils.isEmpty(mAnnexPath)) {
                        mDeleteView.setVisibility(View.GONE);
                        mAnnexType = -1;
                        ToastUtils.showShortToastSafe("请选择合适的文件");
                    } else {
                        iv_photo.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_file));
                    }
                    break;
                case 6:
                    String result = data.getStringExtra("result");
                    mInputDialog.resetView();
                    onResult(result);
                    break;
                default:
                    mAnnexType = -1;
                    mAnnexPath = null;
                    mDeleteView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case Constants.REQUEST_CODE_STORAGE_PHOTO:
                AnnexUtil.choosePhoto(this);
                break;
            case Constants.REQUEST_CODE_STORAGE_VIDEO:
                AnnexUtil.chooseVideo(this);
                break;
            case Constants.REQUEST_CODE_CAMERA_AND_STORAGE:
                AnnexUtil.takePhoto(this);
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    //添加内容提交
    private void addQuestion() {
        btn_commit.setEnabled(false);
        if (TextUtils.isEmpty(et_question.getText().toString().trim())) {
            showShortToast("问题不能为空");
            btn_commit.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(et_answer.getText().toString().trim())) {
            showShortToast("答案不能为空");
            btn_commit.setEnabled(true);
            return;
        }

        if (!Utils.isEmpty(mAnnexPath) && mAnnexType != 5) {
            File annexFile = FileUtil.getFileByPath(mAnnexPath);
            if (checkAnnexSize(annexFile)) {
                showLoadingDialog("正在上传附件...");
                ApiManager.uploadAnnex(annexFile, new UploadAnnexObserver(mResultCallBack));
            }
        } else {
            mAnnexPath = et_Hyperlink.getText().toString();
            addKnowledge(mAnnexPath);
        }
    }

    private void addKnowledge(final String annexPath) {
        showLoadingDialog("正在添加内容...");
        ApiManager.addOrEditKnowledge(true, -1, et_question.getText().toString(), et_answer.getText().toString(),
                annexPath, et_scene.getText().toString(), mAnnexType, new ResultMessageObserver(mResultCallBack));
    }

    //编辑内容提交
    private void editQuestion() {
        btn_commit.setEnabled(false);
        if (TextUtils.isEmpty(et_question.getText().toString())) {
            showShortToast("问题不能为空");
            btn_commit.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(et_answer.getText().toString())) {
            showShortToast("答案不能为空");
            btn_commit.setEnabled(true);
            return;
        }

        if (!Utils.isEmpty(mAnnexPath) && !mAnnexPath.contains("http") && mAnnexType != 5) {
            File annexFile = FileUtil.getFileByPath(mAnnexPath);
            showLoadingDialog("正在上传附件...");
            ApiManager.uploadAnnex(annexFile, new UploadAnnexObserver(mResultCallBack));
        } else {
            if (mAnnexType != 5) {
                editKnowledge(mAnnexPath);
            } else {
                mAnnexPath = et_Hyperlink.getText().toString();
                editKnowledge(mAnnexPath);
            }


        }
    }

    private void showDetail() {
        Intent detailIntent = new Intent();
        switch (mAnnexType) {
            case AnnexUtil.PICTURE:
                if (!Utils.isEmpty(mAnnexPath)) {
                    detailIntent.setAction("photo");
                    detailIntent.putExtra("media_url", mAnnexPath);
                    detailIntent.setClass(this, EnclosureShowActivity.class);
                } else {
                    showShortToast("无效地址");
                }
                break;
            case AnnexUtil.VIDEO:
                if (!Utils.isEmpty(mAnnexPath)) {
                    detailIntent.setAction("video");
                    detailIntent.putExtra("media_url", mAnnexPath);
                    detailIntent.setClass(this, EnclosureShowActivity.class);
                } else {
                    showShortToast("无效地址");
                }
                break;
            case AnnexUtil.FILE:
                if (mAnnexPath.contains("http")) {
                    detailIntent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(mAnnexPath);
                    detailIntent.setData(content_url);
                    // detailIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                } else {
                    File detailFile = FileUtil.getFileByPath(mAnnexPath);
                    detailIntent.setAction(Intent.ACTION_VIEW);
                    detailIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    detailIntent.setDataAndType(mAnnexUri, FileUtil.getMIMEType(detailFile));
                }
                break;
            case AnnexUtil.AUDIO:
                if (!Utils.isEmpty(mAnnexPath)) {
                    detailIntent.setAction("audio");
                    detailIntent.putExtra("media_url", mAnnexPath);
                    detailIntent.setClass(this, EnclosureShowActivity.class);
                } else {
                    showShortToast("无效地址");
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

    private void editKnowledge(final String annexPath) {
        ApiManager.addOrEditKnowledge(false, mKnowledgeItemModel.getId(), et_question.getText().toString(),
                et_answer.getText().toString(), annexPath, et_scene.getText().toString(),
                mAnnexType, new ResultMessageObserver(mResultCallBack));

    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof UploadAnnexModel) {
                dismissLoadingDialog();
                UploadAnnexModel uploadImageModel = (UploadAnnexModel) result;
                String annexUrl = uploadImageModel.getUrl();
                if (isFromAdd)
                    addKnowledge(annexUrl);
                else
                    editKnowledge(annexUrl);
            } else if (result instanceof String) {
                dismissLoadingDialog();
                String msg = (String) result;
                showShortToast(msg);
                if (msg.contains("添加成功")) {
                    EventBus.getDefault().post("add_success");
                    btn_commit.setEnabled(true);
                    if (isAsk) {
                        ApiManager.askChangeSave(askListModel.getId(), new AskChangeSaveObserver(this));
                        askListModel.setAskChange(true);
                        askListModel.setAskrobot(null);
                        EventBus.getDefault().post(askListModel);
                    }
                } else if (msg.contains("修改成功")) {
                    btn_commit.setEnabled(true);
                    EventBus.getDefault().post("updateSuccess");
                }
                finish();
            } else if (result instanceof CommonLanguageModel) {
                finish();
                EventBus.getDefault().post("getCommonLanguage");
            } else if (result instanceof AskRobotModel) {
                AskRobotModel askRobotModel = (AskRobotModel) result;
                EventBus.getDefault().post("add_success");
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            dismissLoadingDialog();
            btn_commit.setEnabled(true);
            if (o instanceof String)
                showShortToast((String) o);
            else
                showShortToast("保存失败.");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResult(String result) {
        if (isAnswer) {
            String answer = et_answer.getText().toString();
            if (Utils.isNotEmpty(answer))
                answer = answer + result;
            else
                answer = result;
            Message message = new Message();
            message.what = 1;
            message.obj = answer;
            mHandler.sendMessage(message);
        } else {
            String question = et_question.getText().toString();
            if (Utils.isNotEmpty(question))
                question = question + result;
            else
                question = result;
            Message message = new Message();
            message.what = 2;
            message.obj = question;
            mHandler.sendMessage(message);
        }
    }

    //检查文件是否大于规定大小
    private boolean checkAnnexSize(File file) {
        long fileSize = FileUtil.getFileSize(file, FileUtil.FILE_SIZE_MB);
        switch (mAnnexType) {
            case AnnexUtil.FILE:
                if (fileSize > 10) {
                    btn_commit.setEnabled(true);
                    showShortToast("文件大小超过10M, 请重新选择.");
                    return false;
                }
                return true;
            case AnnexUtil.PICTURE:
                if (fileSize > 2) {
                    btn_commit.setEnabled(true);
                    showShortToast("图片大小超过2M, 请重新选择.");
                    return false;
                }
                return true;
            case AnnexUtil.VIDEO:
                if (fileSize > 20) {
                    btn_commit.setEnabled(true);
                    showShortToast("视频大小超过20M, 请重新选择.");
                    return false;
                }
                return true;
            case AnnexUtil.AUDIO:
                if (fileSize > 5) {
                    btn_commit.setEnabled(true);
                    showShortToast("音频大小超过5M, 请重新选择.");
                    return false;
                }
                return true;
            default:
                return true;
        }
    }

    public void copyLinkResult(String result) {
        if (Utils.isNotEmpty(result)) {
            mAnnexType = AnnexUtil.HYPERLINK;
            mAnnexPath = result;
            et_Hyperlink.setVisibility(View.VISIBLE);
            iv_photo.setVisibility(View.GONE);
            lay_copyLink.setVisibility(View.VISIBLE);
            ivLinkDelete.setVisibility(View.VISIBLE);
            et_Hyperlink.setText(result);

        } else {
            showShortToast("没有内容");
        }

    }


    private static class AddKnowledgeActivityHandler extends Handler {
        private WeakReference<AddKnowledgeActivity> mActivityWeakReference;

        public AddKnowledgeActivityHandler(AddKnowledgeActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddKnowledgeActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        activity.iv_photo.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case 1:
                        activity.et_answer.setText((String) msg.obj);
                        break;
                    case 2:
                        activity.et_question.setText((String) msg.obj);
                        break;

                }
            }
        }
    }
}
