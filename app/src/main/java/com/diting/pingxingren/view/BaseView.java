package com.diting.pingxingren.view;

import com.diting.pingxingren.presenter.BasePresenter;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 25, 09:45.
 * Description: .
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
