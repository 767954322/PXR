package com.diting.pingxingren.entity;

import java.io.Serializable;

/**
 * Created by asus on 2017/2/9.
 */

public class ExternalOptions implements Serializable {
    private String appName;
    private int id;
    private boolean isChoose;

    public ExternalOptions() {
    }

    public ExternalOptions(String appName, boolean isChoose) {
        this.appName = appName;
        this.isChoose = isChoose;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
