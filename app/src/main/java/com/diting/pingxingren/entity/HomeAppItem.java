package com.diting.pingxingren.entity;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 11 - 17, 14:51.
 * Description: .
 */

public class HomeAppItem {

    private String mTitle;
    private String mSubTitle;
    private int mImageRes;
    private boolean isCoding = false;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    public int getImageRes() {
        return mImageRes;
    }

    public void setImageRes(int imageRes) {
        mImageRes = imageRes;
    }

    public boolean isCoding() {
        return isCoding;
    }

    public void setCoding(boolean coding) {
        isCoding = coding;
    }
}
