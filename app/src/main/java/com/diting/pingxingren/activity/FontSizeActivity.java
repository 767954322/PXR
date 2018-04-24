package com.diting.pingxingren.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.diting.pingxingren.R;
import com.diting.pingxingren.base.BaseActivity;
import com.diting.pingxingren.custom.seekbar.BubbleSeekBar;
import com.diting.pingxingren.util.MySharedPreferences;
import com.diting.pingxingren.util.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 10, 15:23.
 * Description: 字体大小设置.
 */

public class FontSizeActivity extends BaseActivity {

    private View mBackView;
    private BubbleSeekBar mBubbleSeekBar;
    private TextView mMessageView;
    private float mTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size);
        initViews();
        initEvents();
    }

    @Override
    protected void initViews() {
        mBackView = findViewById(R.id.llBack);
        mBubbleSeekBar =  findViewById(R.id.seekBar);
        mMessageView =  findViewById(R.id.textMessage);

        mMessageView.setText(MySharedPreferences.getProfile());
        mMessageView.setTextSize(MySharedPreferences.getFontSize());

        if(Utils.isEmpty(MySharedPreferences.getProfile())){
            mMessageView.setText("机器人");
        }  else{
            mMessageView.setText(MySharedPreferences.getProfile());
        }

        mBubbleSeekBar.setProgress(getProgress());
    }

    @Override
    protected void initEvents() {
        mBubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                switch (progress) {
                    case 0:
                        // mTextSize = ScreenUtil.px2dip(FontSizeActivity.this, getResources().getDimension(R.dimen.dimen_12_dp));
                        mTextSize = 12;
                        MySharedPreferences.saveFontSize(mTextSize);
                        EventBus.getDefault().post("fontSizeChange");
                        break;
                    case 1:
                        // mTextSize = ScreenUtil.px2dip(FontSizeActivity.this, getResources().getDimension(R.dimen.dimen_16_dp));
                        mTextSize = 16;
                        MySharedPreferences.saveFontSize(mTextSize);
                        EventBus.getDefault().post("fontSizeChange");
                        break;
                    case 2:
                        // mTextSize = ScreenUtil.px2dip(FontSizeActivity.this, getResources().getDimension(R.dimen.dimen_20_dp));
                        mTextSize = 20;
                        MySharedPreferences.saveFontSize(mTextSize);
                        EventBus.getDefault().post("fontSizeChange");
                        break;
                    case 3:
                        // mTextSize = ScreenUtil.px2dip(FontSizeActivity.this, getResources().getDimension(R.dimen.dimen_24_dp));
                        mTextSize = 24;
                        MySharedPreferences.saveFontSize(mTextSize);
                        EventBus.getDefault().post("fontSizeChange");
                        break;
                    case 4:
                        // mTextSize = ScreenUtil.px2dip(FontSizeActivity.this, getResources().getDimension(R.dimen.dimen_28_dp));
                        mTextSize = 28;
                        MySharedPreferences.saveFontSize(mTextSize);
                        EventBus.getDefault().post("fontSizeChange");
                        break;
                    default:
                        // mTextSize = ScreenUtil.px2dip(FontSizeActivity.this, getResources().getDimension(R.dimen.dimen_20_dp));
                        mTextSize = 20;
                        MySharedPreferences.saveFontSize(mTextSize);
                        EventBus.getDefault().post("fontSizeChange");
                        break;
                }
                mMessageView.setTextSize(mTextSize);
            }
        });

        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int getProgress() {
        int fontSize = (int) MySharedPreferences.getFontSize();
        switch (fontSize) {
            case 12:
                return 0;
            case 16:
                return 1;
            case 20:
                return 2;
            case 24:
                return 3;
            case 28:
                return 4;
            default:
                return 2;
        }
    }
}
