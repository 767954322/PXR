package com.diting.pingxingren.entity;

import android.graphics.Bitmap;

/**
 * Created by asus on 2017/2/21.
 */

public class Info {
    private String robotName;
    private String companyName;
    private Bitmap headPortrait;

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Bitmap getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(Bitmap headPortrait) {
        this.headPortrait = headPortrait;
    }
}
