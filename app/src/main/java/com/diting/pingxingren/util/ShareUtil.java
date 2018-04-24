package com.diting.pingxingren.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.ShareBoardConfig;

public class ShareUtil implements View.OnClickListener {

    private Activity a;
    private View layout;
    private TextView title;
    private TextView qq;
    private TextView wechat;
    private TextView firend;
    private TextView qzone;
    private Dialog dialog;
    private RelativeLayout click;
    private TextView cancel;

    private static ShareBoardConfig getConfig() {
        ShareBoardConfig config = new ShareBoardConfig();
        config.setCancelButtonVisibility(false);
        return config;
    }

    public ShareUtil(Activity a) {
        this.a = a;
        this.layout = LayoutInflater.from(a).inflate(R.layout.share_dialog, null);
        this.title = layout.findViewById(R.id.tv_title);
        this.click = layout.findViewById(R.id.click);
        this.qq = layout.findViewById(R.id.tv_qq);
        this.wechat = layout.findViewById(R.id.tv_wechat);
        this.firend = layout.findViewById(R.id.tv_wechat_friend);
        this.qzone = layout.findViewById(R.id.tv_qzone);
        this.cancel = layout.findViewById(R.id.tv_cancel);
        setTitleText("");
    }

    private void shareWeb(SHARE_MEDIA type, String url, String title, String img_url, String description, UMShareListener umShareListener) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        if (!Utils.isEmpty(img_url)) {
            web.setThumb(new UMImage(a, img_url));  //缩略图
        }
        web.setDescription(description);  //描述
        new ShareAction(a).setPlatform(type)
                .withMedia(web)
                .setCallback(umShareListener)
                .share();
        dismiss();
    }

    private void sharePicture(SHARE_MEDIA type, Bitmap bitmap) {
        new ShareAction(a).setPlatform(type)
                .withMedia(new UMImage(a, bitmap))
                .share();
        dismiss();
    }

    public void sharePicture(final Bitmap bit) {
        if (layout != null){
            if(dialog == null){
                dialog = new Dialog(a,R.style.mdialog);
                dialog.setContentView(layout);
                Window dialogWindow = dialog.getWindow();
                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialogWindow.setAttributes(lp);
            }
            qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePicture(SHARE_MEDIA.QQ, bit);
                }
            });
            wechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePicture(SHARE_MEDIA.WEIXIN, bit);
                }
            });
            firend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePicture(SHARE_MEDIA.WEIXIN_CIRCLE, bit);
                }
            });
            qzone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharePicture(SHARE_MEDIA.QZONE, bit);
                }
            });
            cancel.setOnClickListener(this);
            click.setOnClickListener(this);
            dialog.show();
        }
    }

    public void shareWeb(final String url, final String title, final String img_url, final String description,final UMShareListener listener) {
        if (layout != null){
            if(dialog == null){
                dialog = new Dialog(a,R.style.mdialog);
                dialog.setContentView(layout);
                Window dialogWindow = dialog.getWindow();
                dialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.BOTTOM;
                dialogWindow.setAttributes(lp);
            }
            qq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareWeb(SHARE_MEDIA.QQ, url, title, img_url, description, listener);
                }
            });
            wechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareWeb(SHARE_MEDIA.WEIXIN, url, title, img_url, description, listener);
                }
            });
            firend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareWeb(SHARE_MEDIA.WEIXIN_CIRCLE, url, title, img_url, description, listener);
                }
            });
            qzone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareWeb(SHARE_MEDIA.QZONE, url, title, img_url, description, listener);
                }
            });
            cancel.setOnClickListener(this);
            click.setOnClickListener(this);
            dialog.show();
        }
    }

    public void setTitleText(String text){
        if(StringUtil.isNotEmpty(text)){
            this.title.setText(text);
        }else{
            this.title.setText("选择要分享到的平台");
        }
    }

    public void dismiss(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.click:
            case R.id.tv_cancel:
                if(dialog.isShowing()){
                    dialog.dismiss();
                }
                break;
        }
    }

    public static void shareWeb(String url, String title, Activity activity, String img_url, String description, UMShareListener umShareListener) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        if (!Utils.isEmpty(img_url)) {
            web.setThumb(new UMImage(activity, img_url));  //缩略图
        }
        web.setDescription(description);  //描述
        new ShareAction(activity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE)
                .withMedia(web)
                .setCallback(umShareListener)
                .open(getConfig());
    }

    public static void sharePicture(Activity activity, Bitmap bitmap) {
        new ShareAction(activity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE)
                .withMedia(new UMImage(activity, bitmap))
                .open();
    }

}
