package com.diting.pingxingren.push;

import android.content.Context;

import com.diting.pingxingren.util.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Creator: Gu FanFan.
 * Date: 2017/12/24, 20:44.
 * Description: .
 */

public class CustomNotificationHandler extends UmengNotificationClickHandler {

    @Override
    public void dismissNotification(Context context, UMessage uMessage) {
        super.dismissNotification(context, uMessage);
        // 清楚通知
        MobclickAgent.onEvent(context, "dismiss_notification");
        LogUtils.e(uMessage.toString());
    }

    @Override
    public void launchApp(Context context, UMessage uMessage) {
        super.launchApp(context, uMessage);
        // 启动APP
        Map<String, String> map = new HashMap<>();
        map.put("action", "launch_app");
        MobclickAgent.onEvent(context, "click_notification", map);
        LogUtils.e(uMessage.toString());
    }

    @Override
    public void openActivity(Context context, UMessage uMessage) {
        super.openActivity(context, uMessage);
        // 启动activity
        Map<String, String> map = new HashMap<>();
        map.put("action", "open_activity");
        MobclickAgent.onEvent(context, "click_notification", map);
        LogUtils.e(uMessage.toString());
    }

    @Override
    public void openUrl(Context context, UMessage uMessage) {
        super.openUrl(context, uMessage);
        // 打开URL
        Map<String, String> map = new HashMap<>();
        map.put("action", "open_url");
        MobclickAgent.onEvent(context, "click_notification", map);
        LogUtils.e(uMessage.toString());
    }

    @Override
    public void dealWithCustomAction(Context context, UMessage uMessage) {
        super.dealWithCustomAction(context, uMessage);
        // 打开自定义消息
        Map<String, String> map = new HashMap<>();
        map.put("action", "custom_action");
        MobclickAgent.onEvent(context, "click_notification", map);
        LogUtils.e(uMessage.toString());
    }

    @Override
    public void autoUpdate(Context context, UMessage uMessage) {
        super.autoUpdate(context, uMessage);
        // 更新
        Map<String, String> map = new HashMap<>();
        map.put("action", "auto_update");
        MobclickAgent.onEvent(context, "click_notification", map);
        LogUtils.e(uMessage.toString());
    }
}
