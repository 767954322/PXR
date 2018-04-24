package com.diting.pingxingren.smarteditor.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.diting.pingxingren.R;
import com.diting.pingxingren.adapter.decoration.RecyclerViewDivider;
import com.diting.pingxingren.smarteditor.adapter.ItemDragAdapter;
import com.diting.pingxingren.smarteditor.base.BaseActivity;
import com.diting.pingxingren.smarteditor.dialog.UsualAlertDialog;
import com.diting.pingxingren.smarteditor.listener.ClickListener;
import com.diting.pingxingren.smarteditor.listener.LongClickListener;
import com.diting.pingxingren.smarteditor.model.ArticleContentModel;
import com.diting.pingxingren.smarteditor.model.ArticleIdModel;
import com.diting.pingxingren.smarteditor.model.ArticleResultModel;
import com.diting.pingxingren.smarteditor.model.CodeResultModel;
import com.diting.pingxingren.smarteditor.model.ContentModel;
import com.diting.pingxingren.smarteditor.model.EditListModel;
import com.diting.pingxingren.smarteditor.model.SaveTitleResultModel;
import com.diting.pingxingren.smarteditor.net.ApiManager;
import com.diting.pingxingren.smarteditor.net.ResultCallBack;
import com.diting.pingxingren.smarteditor.net.observers.ArticleContentObserver;
import com.diting.pingxingren.smarteditor.net.observers.CodeResultObserver;
import com.diting.pingxingren.smarteditor.net.observers.SaveArticleTitleObserver;
import com.diting.pingxingren.smarteditor.util.EditorUtils;
import com.diting.pingxingren.util.AnnexUtil;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 09, 20:05.
 * Description: 添加文章界面.
 */

public class ArticleActivity extends BaseActivity implements View.OnClickListener,
        TextView.OnEditorActionListener {

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ArticleActivity.class);
        return intent;
    }

    public static Intent getCallIntent(Context context, Bundle bundle) {
        Intent intent = getCallIntent(context);
        if (bundle != null) intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallIntent(Context context, boolean add, String articleId, String category) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAdd", add);
        bundle.putString("category", category);
        if (!add) bundle.putString("articleId", articleId);
        return getCallIntent(context, bundle);
    }

    private static final int HANDLER_WHAT_UPDATE_TITLE = 0x001; // 每秒修改一次标题时间.
    private static final int HANDLER_WHAT_RECOVERY_DATA = 0x002; // 恢复上一次为完成的数据.
    private static final int HANDLER_WHAT_SAVE_TITLE = 0x003; // 保存文章标题.
    private static final int HANDLER_WHAT_SAVE_CONTENT = 0x004; // 保存文章内容.
    private static final int HANDLER_WHAT_UPDATE_VIEW = 0x005; // 加载文章数据后, 修改视图.

    private List<EditListModel> mArticleData;//添加的数据集合
    private RecyclerView mArticleDataRecyclerView;
    private ItemDragAdapter mAdapter;

    private EditText mTitleEditText;//输入标题
    private TextView mDateTextView;//标题时间显示控件
    private CheckBox sound;//录音按钮
    private TextView mNotDataTextView;//没有数据时的提示
    private Button mCompleteButton;

    private String mAnnexPath;
    private AddArticleActivityHandler mHandler;
    private String mNoteEditContent;
    private boolean isEdit;//是否点击了item进入编辑
    private int mCurrentEditPosition = -1;//当前编辑的下标
    private boolean isDeleteTips = false;
    private EditListModel mEditListModel;
    private UsualAlertDialog mRecoveryUsualAlertDialog;
    private UsualAlertDialog mDeleteUsualAlertDialog;
    private boolean mIsAdd = true;
    private String mArticleId;
    private String mArticleTitle;
    private String mArticleCategory;
    private List<ArticleIdModel> mDeleteIds;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_article_edit);
        mArticleDataRecyclerView = findViewById(R.id.rv);
        mArticleDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewDivider viewDivider = new RecyclerViewDivider(mContext, RecyclerViewDivider.HORIZONTAL);
        viewDivider.setDividerHeight(10);
        viewDivider.setDividerColor(R.color.transparent);
        mArticleDataRecyclerView.addItemDecoration(viewDivider);
        sound = findViewById(R.id.cb_record);
        mNotDataTextView = findViewById(R.id.tv_not_data);
        mNotDataTextView.setVisibility(View.VISIBLE);
        mCompleteButton = findViewById(R.id.bt_finish);
        mCompleteButton.setOnClickListener(this);
        Button btAdd = findViewById(R.id.bt_add);
        btAdd.setOnClickListener(this);
        ImageView backImageView = findViewById(R.id.ivBack);
        backImageView.setOnClickListener(this);
        sound.setOnClickListener(this);
        mTitleEditText = findViewById(R.id.et_title);
        mTitleEditText.setOnEditorActionListener(this);
        mTitleEditText.setOnClickListener(this);
        mDateTextView = findViewById(R.id.tv_date);
    }

    @Override
    protected void initData() {
        super.initData();
        Bundle bundle = getIntent().getExtras();
        mHandler = new AddArticleActivityHandler(this);
        mDeleteIds = new ArrayList<>();

        if (bundle != null) {
            mIsAdd = bundle.getBoolean("isAdd");
            mArticleCategory = bundle.getString("category");
            if (!mIsAdd) {
                mArticleId = bundle.getString("articleId");
                mHandler.removeMessages(HANDLER_WHAT_UPDATE_TITLE);
                showLoading();
                ApiManager.queryArticleById(Integer.valueOf(mArticleId),
                        new ArticleContentObserver(mResultCallBack));
            } else {
                mHandler.sendEmptyMessageDelayed(HANDLER_WHAT_UPDATE_TITLE, 10000);
                mDateTextView.setText(TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD_HM_E2));
                mNoteEditContent = MySharedPreferences.getNoteEditContent();
                if (Utils.isNotEmpty(mNoteEditContent)) showRecoveryDataDialog();
            }
        }

        mArticleData = new ArrayList<>();
        mAdapter = new ItemDragAdapter(mArticleData, mClickListener, mLongClickListener);
        ItemDragAndSwipeCallback mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mArticleDataRecyclerView);
        mAdapter.enableDragItem(mItemTouchHelper);
        mArticleDataRecyclerView.setAdapter(mAdapter);
        initRecord();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isEdit) mCurrentEditPosition = -1;
    }

    //弹出对话框询问用户是否恢复上次编辑的数据
    private void showRecoveryDataDialog() {
        isDeleteTips = false;
        if (mRecoveryUsualAlertDialog == null) {
            mRecoveryUsualAlertDialog = new UsualAlertDialog(this);
            mRecoveryUsualAlertDialog.setTitle(StringUtil.getString(R.string.tips));
            mRecoveryUsualAlertDialog.setContent(StringUtil.getString(R.string.exsit_not_finish_content));
            mRecoveryUsualAlertDialog.setCancelable(false);
            mRecoveryUsualAlertDialog.setClickListener(mClickListener);
        }
        mRecoveryUsualAlertDialog.showDialog();
    }

    private void recoveryData() {
        showLoading(StringUtil.getString(R.string.recoverie));
        mRecoveryUsualAlertDialog.dismissDialog();
        mHandler.post(mRecoveryDataRunnable);
    }

    private Runnable mRecoveryDataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                JSONArray array = new JSONArray(mNoteEditContent);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    mArticleData.add(new EditListModel((int) obj.get("type"),
                            (String) obj.get("path"), (String) obj.get("addTime")));
                    mHandler.sendEmptyMessage(HANDLER_WHAT_RECOVERY_DATA);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mArticleData.clear();
                notifyChanged(-1);
                MySharedPreferences.clearNotEditContent();
                showShortToast(StringUtil.getString(R.string.recoverie_failed));
                dismissLoading();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                if (mArticleData.size() > 0 && mIsAdd)
                    MySharedPreferences.saveNoteEditContent(mArticleData);
                finish();
                break;
            case R.id.bt_add:
                /*if (mIat != null) {
                    if (mIat.isListening()) {
                        mIat.startListening(mRecognizerListener);
                    }
                }*/
                AnnexUtil.showAddContentDialog("选择内容", this, mClickListener);
                break;
            case R.id.cb_record://录音
                if (sound.isChecked()) {
                    showShortToast("开始录音...");
                    /*if (timeThread == null)
                        timeThread = new Thread(mTimeSixtyRunnable);*/
                    isRecord = true;
                    // timeThread.start();
                    mIat.startListening(mRecognizerListener);
                } else {
                    if (mIat.isListening()) {
                        synchronized (mIat) {
                            if (mIat.isListening()) {
                                mIat.stopListening();
                                showShortToast(StringUtil.getString(R.string.stop_record));
                            }
                        }
                    }
                }
                break;
            case R.id.et_title:
                mTitleEditText.setFocusable(true);
                break;
            case R.id.bt_finish:
                if (mIsAdd)
                    mHandler.sendEmptyMessage(HANDLER_WHAT_SAVE_TITLE);
                else {
                    String title = mTitleEditText.getText().toString().trim();
                    if (!StringUtil.isNotEmpty(title)) {
                        showShortToast("请先完善文章标题.");
                        return;
                    }

                    if (mArticleData.size() <= 0) {
                        showShortToast("请先完善文章内容.");
                        return;
                    }

                    String checkFile = checkFile();
                    if (StringUtil.isNotEmpty(checkFile)) {
                        showShortToast(StringUtil.getString(R.string.upload_file_too_load_tip, checkFile));
                    } else {
                        if (!title.equals(mArticleTitle)) {
                            ApiManager.updateArticle(mArticleId, title, "", "", "",
                                    new CodeResultObserver(0, mResultCallBack));
                        } else {
                            completeContent(Integer.valueOf(mArticleId));
                        }
                    }
                }
                break;
        }
    }

        /**
         * 刷新列表
         *
         * @param index index为-1时,刷新全部
         */
    private void notifyChanged(int index) {
        if (index >= 0 && index < mArticleData.size()) {
            mAdapter.notifyItemChanged(index);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (mArticleData.size() == 0) {
            mNotDataTextView.setVisibility(View.VISIBLE);
        } else {
            mNotDataTextView.setVisibility(View.INVISIBLE);
        }
        mArticleDataRecyclerView.smoothScrollToPosition(mArticleData.size());
    }

    int second = 0;
    private boolean isRecord = true;//是否正在录音
    private boolean isStop = false;//是否退出线程

    //用于处理录音60s后自动隔断并重启录音
    private Runnable mTimeSixtyRunnable = new Runnable() {
        @Override
        public void run() {
            while (!isStop) {
                if (isRecord) {//正在录音,则记录时间;否则一直循环
                    if (second >= 60) {
                        if (mIat.isListening()) {
                            synchronized (mIat) {
                                if (mIat.isListening()) {
                                    mIat.stopListening();
                                    mResultBuffer.setLength(0);
                                    mIat.startListening(mRecognizerListener);
                                }
                            }
                        }
                    } else {
                        second = 0;
                    }
                    second++;
                } else {
                    second = 0;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mArticleData.size() > 0 && mIsAdd)
            MySharedPreferences.saveNoteEditContent(mArticleData);
        mHandler.removeMessages(HANDLER_WHAT_UPDATE_TITLE);
        isStop = true;
        if (mIat != null)
            mIat.stopListening();
        mHandler = null;
    }

    // private Thread timeThread;//计时线程,用于判断录音是否超过60s

    private void initRecord() {
        InitListener initCallBack = new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS)
                    ToastUtils.showShortToastSafe("讯飞语音引擎初始化失败!");
            }
        };
        if (mIat == null)
            mIat = SpeechRecognizer.createRecognizer(this, initCallBack);
        setParams();
    }

    private SpeechRecognizer mIat;

    private void setParams() {
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "100000");
    }

    StringBuffer mResultBuffer;
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
        }

        @Override
        public void onBeginOfSpeech() {
            // mHandler.postDelayed(mTimeSixtyRunnable, 60000);
            mEditListModel = new EditListModel(EditListModel.TYPE_SOUND);
            mEditListModel.setAddTime(getTime());
            mArticleData.add(mEditListModel);
        }

        @Override
        public void onEndOfSpeech() {
            sound.setChecked(false);
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            try {
                JSONTokener JSONToken = new JSONTokener(recognizerResult.getResultString());
                JSONObject JSONObject = new JSONObject(JSONToken);
                if (mResultBuffer == null)
                    mResultBuffer = new StringBuffer();
                String result = getResult(JSONObject);
                mResultBuffer.append(result);
                mEditListModel.setObj(mResultBuffer.toString());
                if (StringUtil.isNotEmpty(mEditListModel.getObj() + ""))
                    notifyChanged(mArticleData.size() - 1);
                if (b) {
                    mEditListModel.setObj(mResultBuffer.toString());
                    mEditListModel.setPath("");
                    if (!StringUtil.isNotEmpty(mEditListModel.getObj() + ""))
                        mArticleData.remove(mArticleData.size() - 1);
                    mResultBuffer.setLength(0);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 处理识别JSON串.
         */
        private String getResult(JSONObject jsonObject) {
            StringBuilder builder = new StringBuilder();
            try {
                JSONArray words = jsonObject.getJSONArray("ws");
                JSONArray items;
                JSONObject word;
                for (int i = 0; i < words.length(); i++) {
                    items = words.getJSONObject(i).getJSONArray("cw");
                    word = items.getJSONObject(0);
                    builder.append(word.getString("w"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return builder.toString();
        }

        @Override
        public void onError(SpeechError speechError) {
            Object o = mEditListModel.getObj();
            if (!Utils.isNotEmpty(o)) mArticleData.remove(mEditListModel);
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    //判断是否编辑返回的数据,将数据合并入集合中
    private void upDateData(int dataType) {
        if (mCurrentEditPosition != -1) {//编辑返回的数据
            mArticleData.get(mCurrentEditPosition).setObj(mAnnexPath);
            notifyChanged(mCurrentEditPosition);
            mCurrentEditPosition = -1;
        } else {
            mArticleData.add(new EditListModel(dataType, mAnnexPath, getTime()));
            notifyChanged(mCurrentEditPosition);
        }
    }

    private void updateView(ArticleResultModel articleResultModel) {
        mArticleTitle = articleResultModel.getTitle();
        mTitleEditText.setText(mArticleTitle);
        mDateTextView.setText(articleResultModel.getUpdatedtime());
        List<ArticleContentModel> articleContentModels = articleResultModel.getEditortion();
        EditListModel editListModel;
        for (ArticleContentModel articleContentModel : articleContentModels) {
            editListModel = new EditListModel(articleContentModel.getContenttype());
            editListModel.setId(articleContentModel.getId());
            editListModel.setObj(articleContentModel.getContent());
            editListModel.setAddTime(getTime());
            editListModel.setEditor_id(String.valueOf(articleContentModel.getEditor_id()));
            mArticleData.add(editListModel);
        }

        if (mArticleData.size() > 0) mNotDataTextView.setVisibility(View.GONE);
        mAdapter.setNewData(mArticleData);
    }

    private void completeTitle() {
        String title = mTitleEditText.getText().toString().trim();
        if (!StringUtil.isNotEmpty(title)) {
            showShortToast("请先完善文章标题.");
            return;
        }

        if (mArticleData.size() <= 0) {
            showShortToast("请先完善文章内容.");
            return;
        }

        String checkFile = checkFile();
        if (StringUtil.isNotEmpty(checkFile)) {
            showShortToast(StringUtil.getString(R.string.upload_file_too_load_tip, checkFile));
        } else {
            mCompleteButton.setEnabled(false);
            showLoading("正在上传文章, 请稍后...");
            ApiManager.saveArticleTitle(title, MySharedPreferences.getUniqueId(),
                    EditorUtils.getArticleCategory(mArticleCategory), new SaveArticleTitleObserver(mResultCallBack));
        }
    }

    private void completeContent(int articleId) {
        if (articleId != -1) {
            List<ContentModel> contentModels = new ArrayList<>();
            List<ArticleContentModel> updateContentModels = new ArrayList<>();

            ContentModel contentModel;
            ArticleContentModel updateContentModel;
            EditListModel editListModel;

            int size = mArticleData.size();
            int type;
            String id;
            String editorId;
            String content;
            for (int i = 0; i < size; i++) {
                editListModel = mArticleData.get(i);
                editorId = editListModel.getEditor_id();
                id = editListModel.getId();
                type = editListModel.getType();
                content = editListModel.getObj();
                if (StringUtil.isNotEmpty(editorId) && StringUtil.isNotEmpty(id)) {
                    updateContentModel = new ArticleContentModel();
                    updateContentModel.setId(id);
                    updateContentModel.setEditor_id(editorId);
                    updateContentModel.setContent(content);
                    updateContentModel.setContentindex(i + 1);
                    updateContentModel.setContenttype(type);
                    updateContentModels.add(updateContentModel);
                } else {
                    contentModel = new ContentModel();
                    contentModel.setContent(content);
                    contentModel.setContentindex(i + 1);
                    contentModel.setContenttype(type);
                    contentModel.setEditor_id(articleId);
                    contentModels.add(contentModel);
                }
            }
            ApiManager.saveArticleContent(contentModels, updateContentModels, mDeleteIds, mResultCallBack);
        }
    }

    private ResultCallBack mResultCallBack = new ResultCallBack() {
        @Override
        public void onResult(Object result) {
            if (result instanceof SaveTitleResultModel) {//保存文章标题成功
                SaveTitleResultModel saveTitleResultModel = (SaveTitleResultModel) result;
                Message message = new Message();
                message.obj = saveTitleResultModel.getId();
                message.what = HANDLER_WHAT_SAVE_CONTENT;
                mHandler.sendMessage(message);
            } else if (result instanceof String) {
                String s = (String) result;
                if (s.startsWith("正在上传")) {
                    showLoading(s);
                } else {
                    showShortToast(s);
                }
            } else if (result instanceof CodeResultModel) {
                CodeResultModel codeResultModel = (CodeResultModel) result;
                String code = codeResultModel.getCode();
                int type = codeResultModel.getRequestType();
                if (code.equals("1")) {
                    if (type != 0) {
                        dismissLoading();
                        showShortToast(codeResultModel.getMessage());
                        mArticleData.clear();
                        finish();
                        EventBus.getDefault().post("RefreshArticle");
                    } else {
                        Message message = new Message();
                        message.obj = Integer.valueOf(mArticleId);
                        message.what = HANDLER_WHAT_SAVE_CONTENT;
                        mHandler.sendMessage(message);
                    }
                } else {
                    onError("文章保存失败!");
                }
            } else if (result instanceof ArticleResultModel) {
                dismissLoading();
                ArticleResultModel articleResultModel = (ArticleResultModel) result;
                Message message = new Message();
                message.what = HANDLER_WHAT_UPDATE_VIEW;
                message.obj = articleResultModel;
                mHandler.sendMessage(message);
            }
        }

        @Override
        public void onResult(List<?> resultList) {
        }

        @Override
        public void onError(Object o) {
            if (o instanceof String) {
                dismissLoading();
                showShortToast((String) o);
            }
        }
    };

    private String getTime() {
        return TimeUtil.getNowTimeString(TimeUtil.PATTERN_HM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            isEdit = false;
            Uri annexUri;
            switch (requestCode) {
                case AnnexUtil.CHOOSE_PICTURE://选择图片
                    annexUri = data.getData();
                    mAnnexPath = AnnexUtil.getImagePath(this.getContentResolver(), annexUri);
                    upDateData(EditListModel.TYPE_PICTURE);
                    break;
                case AnnexUtil.TAKE_PICTURE://拍照
                    mAnnexPath = AnnexUtil.sTempPath;
                    upDateData(EditListModel.TYPE_PICTURE);
                    break;
                case AnnexUtil.CHOOSE_VIDEO://选择视频
                    annexUri = data.getData();
                    mAnnexPath = AnnexUtil.getVideoPath(this.getContentResolver(), annexUri);
                    upDateData(EditListModel.TYPE_VIDEO);
                    break;
                case Constants.REQUEST_CODE:
                    mAnnexPath = data.getStringExtra("content");
                    upDateData(EditListModel.TYPE_TEXT);
                    break;
                default:
                    mAnnexPath = null;
                    break;
            }
        }
    }

    //禁止回车
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }

    private ClickListener mClickListener = new ClickListener() {
        @Override
        public void onClick(View view) {
        }

        @Override
        public void onClick(View view, Object o) {
            if (view != null) {
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.bt_enter:
                        if (o.equals("OK")) {
                            if (isDeleteTips) {
                                if (!mIsAdd)
                                    mDeleteIds.add(new ArticleIdModel(
                                            mArticleData.get(mCurrentEditPosition).getId()));
                                mDeleteUsualAlertDialog.dismissDialog();
                                mArticleData.remove(mCurrentEditPosition);
                                notifyChanged(-1);
                                mCurrentEditPosition = -1;
                            } else {
                                recoveryData();
                            }
                        }
                        break;
                }
            } else {
                if (o instanceof Integer) {
                    int i = (int) o;
                    switch (i) {
                        case 0:
                            AnnexUtil.choosePhoto(mActivity);
                            break;
                        case 1:
                            AnnexUtil.chooseVideo(mActivity);
                            break;
                        case 2:
                            AnnexUtil.takePhoto(mActivity);
                            break;
                        case 3:
                            startActivityForResult(EditTextActivity.class, Constants.REQUEST_CODE);
                            break;
                    }
                }
            }
        }

        @Override
        public void onClick(View view, Object o, int position) {
            int viewId = view.getId();
            mCurrentEditPosition = position;
            isEdit = true;
            switch (viewId) {
                case R.id.tv_text:
                    Bundle bundle = new Bundle();
                    bundle.putString("content", (String) o);
                    bundle.putBoolean("isEdit", isEdit);
                    startActivityForResult(EditTextActivity.class, bundle, Constants.REQUEST_CODE);
                    break;
                case R.id.iv_picture:
                    AnnexUtil.showChooseImgDialog("选择图片", mActivity);
                    break;
                case R.id.rl_video:
                    AnnexUtil.chooseVideo(mActivity);
                    break;
            }
        }
    };

    private LongClickListener mLongClickListener = new LongClickListener() {
        @Override
        public void onLongClick(View view) {
        }

        @Override
        public void onLongClick(View view, Object o) {
            isDeleteTips = true;
            if (mDeleteUsualAlertDialog == null) {
                mDeleteUsualAlertDialog = new UsualAlertDialog(mActivity);
                mDeleteUsualAlertDialog.setTitle(StringUtil.getString(R.string.tips));
                mDeleteUsualAlertDialog.setContent(StringUtil.getString(R.string.deleted_content_not_recovery_tips));
                mDeleteUsualAlertDialog.setCancelable(false);
                mDeleteUsualAlertDialog.setClickListener(mClickListener);
            }

            mDeleteUsualAlertDialog.showDialog();
            mCurrentEditPosition = (int) o;
        }

        @Override
        public void onLongClick(View view, Object o, int position) {
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    mTitleEditText.clearFocus();
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    private String checkFile() {
        boolean isOK;
        StringBuilder stringBuilder = new StringBuilder();
        String toast;
        for (int i = 0; i < mArticleData.size(); i++) {
            EditListModel editListModel = mArticleData.get(i);
            int type = editListModel.getType();
            if (type != 1) {
                isOK = checkAnnexSize(type, editListModel.getObj());
                if (!isOK) {
                    stringBuilder.append(i + 1).append(", ");
                }
            }
        }

        toast = stringBuilder.toString();
        if (StringUtil.isNotEmpty(toast))
            return toast.substring(0, toast.length() - 2);
        else
            return "";
    }

    //检查文件是否大于规定大小
    private boolean checkAnnexSize(int type, String filePath) {
        long fileSize = FileUtil.getFileSize(FileUtil.getFileByPath(filePath), FileUtil.FILE_SIZE_MB);
        switch (type) {
            case 2:
                if (fileSize > 10) {
                    return false;
                }
                return true;
            case 3:
                if (fileSize > 20) {
                    return false;
                }
                return true;
            default:
                return true;
        }
    }

    private static class AddArticleActivityHandler extends Handler {
        private WeakReference<ArticleActivity> mActivityWeakReference;

        AddArticleActivityHandler(ArticleActivity activity) {
            mActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ArticleActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case HANDLER_WHAT_UPDATE_TITLE:
                        activity.mDateTextView.setText(TimeUtil.getNowTimeString(TimeUtil.PATTERN_YMD_HM_E2));
                        break;
                    case HANDLER_WHAT_RECOVERY_DATA:
                        activity.notifyChanged(-1);
                        activity.dismissLoading();
                        break;
                    case HANDLER_WHAT_SAVE_TITLE:
                        activity.completeTitle();
                        break;
                    case HANDLER_WHAT_SAVE_CONTENT:
                        String checkFile = activity.checkFile();
                        if (StringUtil.isNotEmpty(checkFile)) {
                            activity.showShortToast(StringUtil.getString(R.string.upload_file_too_load_tip, checkFile));
                        } else {
                            activity.completeContent((Integer) msg.obj);
                        }
                        break;
                    case HANDLER_WHAT_UPDATE_VIEW:
                        activity.updateView((ArticleResultModel) msg.obj);
                        break;
                }
            }
        }
    }
}
