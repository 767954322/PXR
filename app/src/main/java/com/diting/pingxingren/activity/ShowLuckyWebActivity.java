package com.diting.pingxingren.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.diting.pingxingren.R;
import com.diting.pingxingren.app.MyApplication;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.MyLuckyModel;
import com.diting.pingxingren.net.ApiManager;
import com.diting.pingxingren.net.ResultCallBack;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ShareUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ShowLuckyWebActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    private WebView webView;
    private MyLuckyModel luckyModel;
    private   String urlStr;
    private int id;
   private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lucky_web);
        initViews();
        initEvents();
        initDate();
    }

    private void initDate() {

        luckyModel=getIntent().getParcelableExtra("luckyModel");
        id=luckyModel.getId();
        urlStr = luckyModel.getActivityUrl()+"?appplat=1&phonenumber="+MySharedPreferences.getUsername();
//        LogUtils.e("urlStr==="+urlStr);
          webSettings = webView.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(false);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.loadUrl(urlStr);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                mTitleBarView.setTitleText(view.getTitle());
            }
        });

    }

    @Override
    protected void initViews() {
        initTitleView();
        webView =  findViewById(R.id.web_view);

    }

    private void initTitleView() {
        mTitleBarView = findViewById(R.id.title_bar);
        mTitleBarView.setCommonTitle(View.VISIBLE,
                View.VISIBLE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(R.mipmap.icon_back,null);
        mTitleBarView.setTitleText("活动详情");
        mTitleBarView.setBtnRightText("分享");
    }

    private ShareUtil shareUtil;
    @Override
    protected void initEvents() {
        mTitleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = luckyModel.getShareUrl();
//                String description = Utils.isEmpty(MySharedPreferences.getProfile()) ? Const.commonProfile : MySharedPreferences.getProfile();
                String description = luckyModel.getContent();
                if(shareUtil == null)shareUtil = new ShareUtil(ShowLuckyWebActivity.this);
                shareUtil.shareWeb(url, luckyModel.getHeadline(), luckyModel.getActivityPicture(), description,
                        umShareListener);
            }
        });

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {


            webView.reload();
            ApiManager.luckyShare(id,new ResultCallBack() {
                @Override
                public void onResult(Object result) {
                    MyApplication.shareTimes++;
                    if(Utils.hasLuckyCount()){
                        EventBus.getDefault().post("hideRedPoint");
                    }
                }

                @Override
                public void onResult(List<?> resultList) {

                }

                @Override
                public void onError(Object o) {

                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
                ToastUtils.showShortToastSafe(t.getMessage());

            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };
    @Override
    protected void onDestroy() {
        webView.setVisibility(View.GONE);
        webView.destroy();

        super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);

    }
}
