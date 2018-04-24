package com.diting.pingxingren.smarteditor.listener;

import android.view.View;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 29, 16:54.
 * Description: .
 */

public interface LongClickListener {

    void onLongClick(View view);

    void onLongClick(View view, Object o);

    void onLongClick(View view, Object o, int position);
}
