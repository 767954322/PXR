package com.diting.pingxingren.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.SmoothImageView;
import com.diting.pingxingren.util.LogUtils;
import com.diting.pingxingren.util.MediaManager;
import com.diting.pingxingren.util.Utils;

public class EnclosureShowActivity extends BaseActivity implements View.OnClickListener {
    //传递过来的数据  UIl
    private String media_url;
    //显示图片
    private SmoothImageView smoothImageView;
    //图片保存按钮
    private ImageView image_save;
    //传递的action
    private String action;
    //播放视频的控件
    private VideoView video_player_view;
    //显示图片的布局
    private RelativeLayout image_rel;
    //显示视频的布局
    private RelativeLayout video_rel;
    //显示播放音乐的布局
    private RelativeLayout audio_rel;
    //播放视频的按钮
    private ImageView ivPlayArea;
    //播放音乐的按钮
    private ImageView iv_play;
    //播放音乐的控制
    private boolean mPlarMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enclosure_show);
        action = getIntent().getAction();
        if (!Utils.isEmpty(getIntent().getStringExtra("media_url"))) {
            media_url = getIntent().getStringExtra("media_url");
        } else {
            showShortToast("无效地址");
        }

        initViews();
        initEvents();
        initDate();

    }

    @Override
    protected void initViews() {
        smoothImageView = findViewById(R.id.enclosure_image);
        image_save = findViewById(R.id.image_save);
        image_rel = findViewById(R.id.image_rel);
        video_rel = findViewById(R.id.video_rel);
        audio_rel = findViewById(R.id.audio_rel);


        video_player_view = findViewById(R.id.video_player_view);
        ivPlayArea = findViewById(R.id.ivPlayArea);
        iv_play = findViewById(R.id.iv_play);

    }

    private void initDate() {
        switch (action) {
            case "photo":
                image_rel.setVisibility(View.VISIBLE);
                video_rel.setVisibility(View.GONE);
                audio_rel.setVisibility(View.GONE);
                image_save.setVisibility(View.GONE);

                if (!Utils.isEmpty(media_url)) {
                    Glide.with(this).load(media_url).into(smoothImageView);
                }

                break;
            case "video":
                image_rel.setVisibility(View.GONE);
                video_rel.setVisibility(View.VISIBLE);
                audio_rel.setVisibility(View.GONE);

                video_player_view.setVideoURI(Uri.parse(media_url));
                video_player_view.setMediaController(new MediaController(this));
           /*     ivPlayArea.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        video_player_view.start();
                        ivPlayArea.setVisibility(View.GONE);
                    }
                });*/


                break;
            case "audio":
                image_rel.setVisibility(View.GONE);
                video_rel.setVisibility(View.GONE);
                audio_rel.setVisibility(View.VISIBLE);
                mPlarMusic = true;
//                changeMusicState();
                break;
        }


    }

    public void changeMusicState() {
        MediaManager.playSound(media_url, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlarMusic = false;
            }
        }, new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {


                showShortToast("\"播放失败，请换一首\"");

                return false;
            }
        });
    }


    @Override
    protected void initEvents() {
        ivPlayArea.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        smoothImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                smoothImageView.transformOut();
            }
        });
    }

    @Override
    protected void onDestroy() {
        MediaManager.pause();
        MediaManager.release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        switch (action) {
            case "photo":
                smoothImageView.setOnTransformListener(new SmoothImageView.TransformListener() {
                    @Override
                    public void onTransformComplete(int mode) {
                        if (mode == 2) {
                            finish();
                        }
                    }
                });
                smoothImageView.transformOut();
                break;
            case "video":
                finish();
                break;
            case "audio":
                finish();
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivPlayArea:
                video_player_view.start();
                ivPlayArea.setVisibility(View.GONE);
                break;
            case R.id.iv_play:
                if (mPlarMusic) {
                    changeMusicState();
                    mPlarMusic = false;
                    iv_play.setImageResource(R.mipmap.icon_stop);
                } else {
                    mPlarMusic = true;
                    MediaManager.pause();
                    iv_play.setImageResource(R.mipmap.icon_play);
                }
                break;
        }
    }
}
