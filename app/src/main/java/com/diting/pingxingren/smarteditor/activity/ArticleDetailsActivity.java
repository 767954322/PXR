package com.diting.pingxingren.smarteditor.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.diting.pingxingren.R;
import com.diting.pingxingren.databinding.ActivityArticleDetailsBinding;
import com.diting.pingxingren.listener.ClickListener;
import com.diting.pingxingren.smarteditor.base.BaseActivity;
import com.diting.pingxingren.smarteditor.net.RequestApi;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ScreenUtil;
import com.diting.pingxingren.util.ShareUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 31, 16:13.
 * Description: .
 */

public class ArticleDetailsActivity extends BaseActivity {

    public static Intent getCallIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ArticleDetailsActivity.class);
        return intent;
    }

    public static Intent getCallIntent(Context context, Bundle bundle) {
        Intent intent = getCallIntent(context);
        if (bundle != null) intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallIntent(Context context, String articleId) {
        Bundle bundle = new Bundle();
        bundle.putString("articleId", articleId);
        return getCallIntent(context, bundle);
    }

    private ActivityArticleDetailsBinding mBinding;
    private TopRightMenu mTopRightMenu;
    private String mArticleId;
    private String mArticleUrl;

    @Override
    protected void initView() {
        super.initView();
        mBinding = DataBindingUtil.setContentView(mActivity, R.layout.activity_article_details);
        mBinding.setTitle("文章详情");
        mBinding.setListener(mClickListener);
        mBinding.titleLayout.ivTitleRight.setVisibility(View.VISIBLE);
        mBinding.titleLayout.ivTitleRight.setImageResource(R.drawable.ic_editor_more);

        // 初始化WebView设置
        WebSettings webSettings = mBinding.detailsWebView.getSettings();
        //支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webView的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setJavaScriptEnabled(true); // //启用JS
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webView中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        initRightMenu();
    }

    @Override
    protected void initData() {
        super.initData();
        EventBus.getDefault().register(mActivity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mArticleId = bundle.getString("articleId");
            mArticleUrl = RequestApi.EDITOR_ARTICLE_DETAILS + mArticleId;
            WebViewClient webViewClient = new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    if (Utils.hasLollipop()) {
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    } else {
                        return shouldOverrideUrlLoading(view, request.getUrl().toString());
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            };
            mBinding.detailsWebView.setWebViewClient(webViewClient);
            mBinding.detailsWebView.loadUrl(mArticleUrl);
        } else {
            showLongToast("获取文章失败, 请检查");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mActivity);
    }

    private ShareUtil shareUtil;

    private void initRightMenu() {
        mTopRightMenu = new TopRightMenu(mActivity);
        mTopRightMenu.addMenuItem(new MenuItem(R.drawable.ic_editor_share, "分享"))
                .addMenuItem(new MenuItem(R.drawable.ic_edit, "编辑"));
        mTopRightMenu
                .setHeight(ScreenUtil.dip2px(mContext, 100))
                .setWidth(ScreenUtil.dip2px(mContext, 100))
                .showIcon(true)     //显示菜单图标，默认为true
                .dimBackground(true)        //背景变暗，默认为true
                .needAnimationStyle(true)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        switch (position) {
                            case 0:
                                String description = "给你分享一篇好的文章!";
                                if (shareUtil == null)
                                    shareUtil = new ShareUtil(ArticleDetailsActivity.this);
                                shareUtil.shareWeb(mArticleUrl, MySharedPreferences.getRobotName(),
                                        MySharedPreferences.getHeadPortrait(), description,
                                        umShareListener);
                                break;
                            case 1:
                                startActivity(ArticleActivity.getCallIntent(mContext, false, mArticleId, ""));
                                break;
                        }
                    }
                });
    }

    private ClickListener mClickListener = new ClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            switch (viewId) {
                case R.id.ivTitleLeft:
                    finish();
                    break;
                case R.id.ivTitleRight:
                    mTopRightMenu.showAsDropDown(mBinding.titleLayout.ivTitleRight, -50, -40);
                    break;
            }
        }

        @Override
        public void onClick(Object o) {
        }
    };

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                ToastUtils.showShortToastSafe(t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandling(String message) {
        if (message.equals("RefreshArticle")) {
            LogUtils.e("12321321");
            mBinding.detailsWebView.reload();
        }
    }
}
