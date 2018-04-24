package com.diting.voice.utils;

import android.os.CountDownTimer;

import com.diting.voice.TimerCallBack;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/25, 10:16.
 * Description: 自定义倒计时.
 */

public class CountDownTimerUtils extends CountDownTimer {

    private TimerCallBack mTimerCallBack;

    public CountDownTimerUtils(long millisInFuture,
                               long countDownInterval,
                               TimerCallBack timerCallBack) {
        super(millisInFuture, countDownInterval);
        mTimerCallBack = timerCallBack;
    }

    @Override
    public void onTick(long l) {
        mTimerCallBack.onTick(l / 1000);
    }

    @Override
    public void onFinish() {
        mTimerCallBack.onFinish();
    }


}
