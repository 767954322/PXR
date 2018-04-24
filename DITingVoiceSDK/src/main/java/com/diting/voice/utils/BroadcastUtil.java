package com.diting.voice.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Creator: Gu FanFan.
 * Date: 2017/7/19, 9:47.
 * Description: 广播Utils.
 */

public class BroadcastUtil {

    private static BroadcastReceiver receiver;

    /**
     * 构造函数设为private，防止外部实例化
     */
    private void BroadcastManager() {
    }

    /**
     * 注册广播接收器
     *
     * @param ctx       Context(不可空)
     * @param iReceiver IReceiver(不可空)
     * @param action    Intent.Action(不可空)
     */
    public static void registerReceiver(Context ctx, final IReceiver iReceiver, final String action) {
        if (ctx == null || iReceiver == null || action == null)
            throw new IllegalArgumentException("使用registerReceiver()注册广播时，参数Context、IReceiver和Action不能为空！");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                iReceiver.onReceive(context, intent);
            }
        };
        IntentFilter filter = new IntentFilter(action);
        ctx.registerReceiver(receiver, filter);
    }

    /**
     * 反注册广播接收器
     *
     * @param ctx Context(不可空)
     */
    public static void unRegisterReceiver(Context ctx) {
        if (ctx == null)
            throw new IllegalArgumentException("unRegisterReceiver()注册广播时，参数Context、不能为空！");
        try {
            ctx.unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送广播
     *
     * @param ctx    Context(不可空)
     * @param i      Intent（可为null），不需要设置Action，请把Action作为参数传入
     * @param action Intent.Action（不可空）
     */
    public static void send(Context ctx, Intent i, final String action) {
        if (ctx == null || action == null)
            throw new IllegalArgumentException("使用send()发送广播时，参数Context和Action不能为空！");

        if (i == null) i = new Intent();
        i.setAction(action);
        ctx.sendBroadcast(i);
    }

    /**
     * 接收广播的页面需要实现，把接收后的操作覆写在OnReceive()方法里
     */
    public interface IReceiver {
        void onReceive(Context ctx, Intent intent);
    }
}
