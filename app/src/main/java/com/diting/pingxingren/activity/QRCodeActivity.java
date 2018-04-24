package com.diting.pingxingren.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.util.Const;
import com.diting.pingxingren.util.FileSaveUtil;
import com.diting.pingxingren.util.ImageUtil;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.ShareUtil;
import com.diting.pingxingren.util.Utils;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import static com.diting.pingxingren.util.Const.URL_ROBOT;
import static com.diting.pingxingren.util.ImageUtil.getLocalOrNetBitmap;

/**
 * Created by asus on 2017/1/16.
 * 二维码分享机器人页面
 */

public class QRCodeActivity extends BaseActivity {
    private TitleBarView titleBarView;
    private ImageView iv_qr_code;
    private Bitmap codebitmap;
    private Button btn_save;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qr_code);
        initViews();
        initEvents();
    }

    private void initTitleBarView() {
        titleBarView = findViewById(R.id.title_bar);

        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("二维码");
        titleBarView.setBtnRightText("分享");
    }

    private ShareUtil shareUtil;
    private void initTitleBarEvents() {
        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBarView.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                //Bitmap bitmap = ((BitmapDrawable)(iv_girl).getDrawable()).getBitmap();
//                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null,null));
//                intent.putExtra(Intent.EXTRA_STREAM,uri);
//                intent.setType("image/*");
//                startActivity(Intent.createChooser(intent,"share"));
//                new ShareAction(QRCodeActivity.this).setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE)
//                        .withMedia(new UMImage(QRCodeActivity.this,bitmap))
//                        .open();
                // 设置控件允许绘制缓存
                relativeLayout.setDrawingCacheEnabled(true);
                // 获取控件的绘制缓存（快照）
                Bitmap bitmap = relativeLayout.getDrawingCache();
                if (bitmap != null && codebitmap != null) {
                    if(shareUtil == null)shareUtil = new ShareUtil(QRCodeActivity.this);
                    shareUtil.sharePicture(bitmap);
                }
            }
        });
    }

    @Override
    protected void initViews() {
        initTitleBarView();
        iv_qr_code = findViewById(R.id.iv_qr_code);
        btn_save = findViewById(R.id.btn_save);
        TextView tv_username = findViewById(R.id.tv_username);
        relativeLayout = findViewById(R.id.relativeLayout);
        tv_username.setText(MySharedPreferences.getRobotName());

        iv_qr_code.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                String headPortrait = MySharedPreferences.getHeadPortrait();
                if (!Utils.isEmpty(headPortrait)) {
                    codebitmap = CodeUtils.createImage(URL_ROBOT + MySharedPreferences.getUniqueId(), 400, 400, ImageUtil.toRoundBitmap(getLocalOrNetBitmap(headPortrait)));
                } else {
                    codebitmap = CodeUtils.createImage(URL_ROBOT + MySharedPreferences.getUniqueId(), 400, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.icon_left));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_qr_code.setImageBitmap(codebitmap);
                    }
                });
            }
        }).start();
    }

    @Override
    protected void initEvents() {
        initTitleBarEvents();
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = FileSaveUtil.saveBitmap(codebitmap);
                if (file != null) {
                    showShortToast("图片已保存至" + Const.IMAGE_PATH);
                    FileSaveUtil.sendSaveBroadcast(QRCodeActivity.this, file);
                } else {
                    showShortToast("保存失败");
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
