package com.diting.pingxingren.smarteditor.listener;

import android.view.View;

/**
 * Creator: Gu FanFan.
 * Date: 2017/8/4, 16:33.
 * Description: 点击监听.
 */

public interface ClickListener {

    void onClick(View view);

    void onClick(View view, Object o);

    void onClick(View view, Object o, int position);
}
