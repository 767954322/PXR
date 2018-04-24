package com.diting.pingxingren.news.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.seekbar.BubbleSeekBar;
import com.diting.pingxingren.news.custom.NewsTitleBar;
import com.diting.pingxingren.util.StringUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.ShareUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2018 on 2018/1/5.
 * 具体新闻页面
 */

public class NewsActivity extends BaseActivity implements NewsTitleBar.onTitleClickListener, UMShareListener, View.OnClickListener {

    private String url = "";
    private String newsTitleStr = "";//文章标题

    private WebView mWebView;
    private List<MenuItem> mList;
    private int currentMode;//当前显示模式
    private NewsTitleBar bar;
    private BubbleSeekBar mBubbleSeekBar;
    private WebSettings setting;
    private RelativeLayout bottomLayout;

    private int minTextSize = 75;//最小字体
    private int mTextSizeStep = 25;//字体大小步长

    private Dialog dialog;

    private View ComplaintLayout;//投诉布局
    private CheckBox oneBox;
    private CheckBox twoBox;
    private CheckBox threeBox;
    private CheckBox fourBox;
    private CheckBox otherBox;//投诉布局点击其他控件
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_news);
        initViews();
        initEvents();
//        upDateMode();
    }

    protected void initViews() {
        url = getIntent().getStringExtra("url");
        newsTitleStr = getIntent().getStringExtra("title");
        bar = findViewById(R.id.title_bar);
        bottomLayout = findViewById(R.id.rl_bottom);
        LinearLayout layout = findViewById(R.id.ll_layout);
        ComplaintLayout = getLayoutInflater().inflate(R.layout.news_complaint_dialog, layout, false);
        oneBox = ComplaintLayout.findViewById(R.id.cb_one);
        twoBox = ComplaintLayout.findViewById(R.id.cb_two);
        threeBox = ComplaintLayout.findViewById(R.id.cb_three);
        fourBox = ComplaintLayout.findViewById(R.id.cb_four);
        otherBox = ComplaintLayout.findViewById(R.id.cb_other);
        editText = ComplaintLayout.findViewById(R.id.et_content);
        Button complaintSubmit = ComplaintLayout.findViewById(R.id.bt_submit);
        complaintSubmit.setOnClickListener(this);
        otherBox.setOnClickListener(this);
        mBubbleSeekBar = findViewById(R.id.seekBar);
        bar.setOtherTitle(null, true, true, this);
        mWebView = findViewById(R.id.web_view);
        setting = mWebView.getSettings();
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress == 100) dismissLoadingDialog();
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                if(!isAgain){
                    if(!newsTitleStr.equals(title))mWebView.setVisibility(View.GONE);
//                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //5.0以上时
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Uri clickUrl = request.getUrl();
//                    Intent intent = new Intent(NewsActivity.this, NewsActivity.class);
//                    intent.putExtra("url", clickUrl.getEncodedPath());
//                    startActivity(NewsActivity.class);
//                    return true;
//                }
                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingDialog(StringUtil.getString(R.string.loading));
            }
        });
        int progress = MySharedPreferences.getNewsFont();
        setting.setTextZoom(minTextSize + mTextSizeStep * progress);
        mWebView.loadUrl(url);
        mBubbleSeekBar.setProgress(progress);
    }

    //初始化显示模式
//    private void upDateMode(){
//        currentMode = MySharedPreferences.getNewsShowMode();
//        mList.clear();
//        switch (currentMode){
//            case Constants.MODE_DAY:
//                mList.add(new MenuItem(R.drawable.news_night_icon, StringUtil.getString(R.string.night_mode)));
//                bar.setTitleBgColor(R.color.bg_color);
//                mWebView.setBackgroundColor(StringUtil.getColor(R.color.bg_color));
//                break;
//            case Constants.MODE_NIGHT:
//                mList.add(new MenuItem(R.drawable.news_day_icon, StringUtil.getString(R.string.day_mode)));
//                bar.setTitleBgColor(R.color.night_mode_bg_color);
//                mWebView.setBackgroundColor(StringUtil.getColor(R.color.night_mode_bg_color));
//                break;
//        }
//        mList.add(new MenuItem(R.drawable.news_font, StringUtil.getString(R.string.font_setting)));
//    }

    protected void initEvents() {
        mList = new ArrayList<>();
        mList.add(new MenuItem(R.drawable.news_day_icon, StringUtil.getString(R.string.complaint)));
        mList.add(new MenuItem(R.drawable.news_font, StringUtil.getString(R.string.font_setting)));
        mBubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                MySharedPreferences.saveNewsFont(progress);
                EventBus.getDefault().post("fontSizeChange");
                setting.setTextZoom(minTextSize + mTextSizeStep * progress);
            }
        });
    }

    @Override
    public void onBack(boolean isFinish) {
        if(isHide()){
            back();
        }
    }

    @Override
    public void onBackPressed() {
        if(isHide()){
            finish();
        }
    }

    private void back(){
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            finish();
        }
    }

    @Override
    public void onSetting() {
        if(isHide())showTopRightMenu();
    }

    private ShareUtil shareUtil;
    @Override
    public void onShare() {
        if(shareUtil == null)shareUtil = new ShareUtil(this);
        if(isHide())shareUtil.shareWeb(url, newsTitleStr, MySharedPreferences.getHeadPortrait(), "",
                this);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtils.showShortToast("分享成功!");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        ToastUtils.showShortToast("分享取消!");
    }

    private void showTopRightMenu() {
        final TopRightMenu mTopRightMenu = new TopRightMenu(this);
        mTopRightMenu
                .setHeight(ScreenUtil.dip2px(this, 96))     //默认高度480
                .setWidth(ScreenUtil.dip2px(this, 130))      //默认宽度wrap_content
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .addMenuList(mList)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        mTopRightMenu.dismiss();
                        switch (position) {
//                            case 0://切换模式
//                                MySharedPreferences.saveNewsShowMode(currentMode);
//                                upDateMode();
                            case 0://投诉
//                                if(dialog == null){
//                                    dialog = DialogUtil.createDialog(NewsActivity.this, ComplaintLayout, 0, null);
//                                    dialog.setCancelable(true);
//                                    dialog.setCanceledOnTouchOutside(true);
//                                }
//                                dialog.show();
                                getDialog().show();
                                break;
                            case 1://调整字体
                                bottomLayout.setOnClickListener(NewsActivity.this);
                                bottomLayout.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }).showAsDropDown(bar,
                getWindow().getDecorView().getWidth()
                        - ScreenUtil.dip2px(this, 140), ScreenUtil.dip2px(this, 6));    //带偏移量
    }

    //判断mBubbleSeekBar是否可见,若可见,则将其隐藏
    private boolean isHide(){
        if(bottomLayout.getVisibility() == View.VISIBLE){
            bottomLayout.setVisibility(View.GONE);
            bottomLayout.setOnClickListener(null);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_bottom:
                isHide();
                break;
            case R.id.cb_other:
                if(otherBox.isChecked()){
                    editText.setEnabled(true);
                    editText.setFocusable(true);
                }else{
                    editText.setEnabled(false);
                    editText.setFocusable(false);
                }
                break;
            case R.id.bt_submit:
                if(oneBox.isChecked() || twoBox.isChecked() || threeBox.isChecked() || fourBox.isChecked()){
                    dialog.dismiss();
                    ToastUtils.showShortToastSafe(StringUtil.getString(R.string.complaint_really_commit));
                } else {
                    if(otherBox.isChecked()){
                        if(StringUtil.isNotEmpty(editText.getText().toString())){
                            dialog.dismiss();
                            ToastUtils.showShortToastSafe(StringUtil.getString(R.string.complaint_really_commit));
                        }
                    } else {
                        ToastUtils.showShortToastSafe(StringUtil.getString(R.string.please_select_cause));
                    }
                }
                break;
        }
    }

    private Dialog getDialog(){
        if(dialog == null){
            dialog = new Dialog(this, 0);
            dialog.setContentView(ComplaintLayout);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
        }
        return dialog;
    }
}
