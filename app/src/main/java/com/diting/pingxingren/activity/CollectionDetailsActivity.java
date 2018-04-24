package com.diting.pingxingren.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.TitleBarView;
import com.diting.pingxingren.model.CollectionModel;
import com.diting.pingxingren.util.Constants;
import com.diting.pingxingren.util.FileUtil;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.TimeUtil;
import com.diting.pingxingren.util.ToastUtils;
import com.diting.pingxingren.util.Utils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import java.io.File;


public class CollectionDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final String EXTRA = "collection";
    private TitleBarView titleBarView;
    private CollectionModel model;
    private TextView mCollName;
    private TextView mCollContent;
    private ImageView mCollImage;
    private TextView mCollTime;
    private int sort;
    private String url;
    private String mContent;
    private VideoView mCollVideo;
    private ImageView mCollAudio;
    private ImageView mCollFile;

    private RelativeLayout audio_rel;
    private RelativeLayout mCollVideoRel;
    private ImageView ivPlayArea;

    //播放音乐的控制
    private boolean mPlarMusic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_details);
        initViews();
        initEvents();
        initDate();
    }

    public static Intent getCallingIntent(Context context, CollectionModel collectionModel) {
        Intent intent = new Intent();
        intent.setClass(context, CollectionDetailsActivity.class);
        intent.putExtra(EXTRA, collectionModel);
        return intent;
    }

    @Override
    protected void initViews() {
        initTitleView();
        mCollName = findViewById(R.id.tv_collect_name);
        mCollContent = findViewById(R.id.tv_collection_content);
        mCollTime = findViewById(R.id.tv_collection_time);
        mCollVideoRel = findViewById(R.id.video_rel);
        ivPlayArea = findViewById(R.id.ivPlayArea);
        audio_rel = findViewById(R.id.audio_rel);
        mCollVideo = findViewById(R.id.video_player_view);
        mCollImage = findViewById(R.id.iv_collection_image);
        mCollAudio = findViewById(R.id.iv_play);
       mCollFile= findViewById(R.id.iv_collection_File);


    }

    private void initTitleView() {
        titleBarView = findViewById(R.id.title_bar);
        titleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE, View.GONE);
        titleBarView.setBtnLeft(R.mipmap.icon_back, null);
        titleBarView.setTitleText("详情");

        titleBarView.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initDate() {
        model = getIntent().getParcelableExtra(EXTRA);
        mCollName.setText(model.getChatrobotname());
        mContent = model.getText();
        url = model.getUrl();
        sort = model.getSort();
        mCollTime.setText("收藏于" + TimeUtil.millis2String(model.getTimer(), TimeUtil.DEFAULT_PATTERN));

        if (sort == 1) {//1.音频
            mCollContent.setText(mContent);
            mCollFile.setVisibility(View.GONE);
            audio_rel.setVisibility(View.VISIBLE);
            mCollImage.setVisibility(View.GONE);
            mCollVideoRel.setVisibility(View.GONE);
            mCollVideo.setVisibility(View.GONE);
            mPlarMusic = true;
        } else if (sort == 2) {//2.文件
            mCollFile.setVisibility(View.VISIBLE);
            mCollImage.setVisibility(View.GONE);
            audio_rel.setVisibility(View.GONE);
            mCollVideoRel.setVisibility(View.GONE);
            mCollContent.setText(mContent);
        } else if (sort == 3) {// 3.  图片
            mCollFile.setVisibility(View.GONE);
            mCollImage.setVisibility(View.VISIBLE);
            audio_rel.setVisibility(View.GONE);
            mCollVideoRel.setVisibility(View.GONE);
            Glide.with(this).load(url).into(mCollImage);
            mCollContent.setText(mContent);
        } else if (sort == 4) {//   4.视频
            mCollFile.setVisibility(View.GONE);
            mCollImage.setVisibility(View.GONE);
            audio_rel.setVisibility(View.GONE);
            mCollVideoRel.setVisibility(View.VISIBLE);
            mCollContent.setText(mContent);
            mCollVideo.setVideoURI(Uri.parse(url));
            mCollVideo.setMediaController(new MediaController(this));

        } else if (sort == 5) {// 5.文本
            mCollFile.setVisibility(View.GONE);
            mCollImage.setVisibility(View.GONE);
            audio_rel.setVisibility(View.GONE);
            mCollVideoRel.setVisibility(View.GONE);
            mCollContent.setText(mContent);
        }
    }

    public void changeMusicState() {
        MediaManager.playSound(url, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlarMusic = false;

                if (mPlarMusic) {
//                    changeMusicState();
                    mPlarMusic = false;
                    mCollAudio.setImageResource(R.mipmap.icon_stop);
                } else {
                    mPlarMusic = true;
                    MediaManager.pause();
                    mCollAudio.setImageResource(R.mipmap.icon_play);
                }
            }
        }, new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {


                showShortToast("\"播放失败\"");

                return false;
            }
        });
    }
    @Override
    protected void initEvents() {
        ivPlayArea.setOnClickListener(this);
        mCollAudio.setOnClickListener(this);
        mCollFile.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        MediaManager.pause();
        MediaManager.release();
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPlayArea:
                mCollVideo.start();
                ivPlayArea.setVisibility(View.GONE);
                break;
            case R.id.iv_play:
                changeMusicState();
                if (mPlarMusic) {
//                    changeMusicState();
                    mPlarMusic = false;
                    mCollAudio.setImageResource(R.mipmap.icon_stop);
                } else {
                    mPlarMusic = true;
                    MediaManager.pause();
                    mCollAudio.setImageResource(R.mipmap.icon_play);
                }
                break;
            case  R.id.iv_collection_File:
                showNoticeDialog();
                break;
        }
    }
    private String mFileUrl;
    private String mFilePath;
    private ProgressDialog mProgressDialog;
    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {mFileUrl=url;
        new AlertDialog.Builder(CollectionDetailsActivity.this)
                .setTitle("确定下载此文件吗?")
                .setOnCancelListener(new DialogInterface.OnCancelListener() { //
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss(); // 取消显示对话框
                    }
                }).setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FileUtil.createOrExistsDir(Constants.FILE_PATH_FILES);
                mFilePath = Constants.FILE_PATH_FILES + "/" + String.valueOf(mFileUrl.hashCode())
                        + "." + Utils.getFileExtensionFromUrl(mFileUrl);
                if (FileUtil.isFileExists(mFilePath)) {
                    ToastUtils.showShortToast("文件已保存至" + mFilePath);
                } else {
                    showDownloadDialog();
                    download();
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {
        mProgressDialog = new ProgressDialog(CollectionDetailsActivity.this);
        mProgressDialog.setTitle("正在下载...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.show();
    }
    private void download() {
        // 下载APK，并且替换安装
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//得到sd卡的状态
            FinalHttp finalhttp = new FinalHttp();
            finalhttp.download(mFileUrl, mFilePath, new AjaxCallBack<File>() {
                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    t.printStackTrace();
                    super.onFailure(t, errorNo, strMsg);
                    mProgressDialog.dismiss();
                    ToastUtils.showShortToastSafe("下载失败.");
                }

                /**
                 * count:为总的大小
                 * current:为当前下载的大小
                 */
                @Override
                public void onLoading(long count, long current) {//正在下载
                    super.onLoading(count, current);
                    int progress = (int) (current * 100 / count);
                    mProgressDialog.setProgress(progress);
                }

                /**
                 * file t：表示文件的路径
                 */
                @Override
                public void onSuccess(File t) {
                    super.onSuccess(t);
                    mProgressDialog.dismiss();
                }
            });
        } else {
            ToastUtils.showShortToastSafe("没有sdcard!");
        }
    }
}
