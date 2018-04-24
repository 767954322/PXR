package com.diting.pingxingren.util;

import android.os.CountDownTimer;
import android.widget.Button;

public class MyCountDownTimer extends CountDownTimer {
    private Button btn_djs;
    public MyCountDownTimer(Button btn_djs,long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.btn_djs = btn_djs;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        //防止计时过程中重复点击
        btn_djs.setClickable(false);
        btn_djs.setText(l / 1000 + "秒后重试");
    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        //重新给Button设置文字
        btn_djs.setText("重新获取");
        //设置可点击
        btn_djs.setClickable(true);
    }
}

