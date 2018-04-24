package com.diting.voice.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/9/15.
 */

public class CallInfo implements Parcelable{
    private String robotName;
    private String companyName;
    private String time;
    private String headImg;

    public CallInfo(String robotName, String companyName, String time, String headImg) {
        this.robotName = robotName;
        this.companyName = companyName;
        this.time = time;
        this.headImg = headImg;
    }

    protected CallInfo(Parcel in) {
        robotName = in.readString();
        companyName = in.readString();
        time = in.readString();
        headImg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(robotName);
        dest.writeString(companyName);
        dest.writeString(time);
        dest.writeString(headImg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CallInfo> CREATOR = new Creator<CallInfo>() {
        @Override
        public CallInfo createFromParcel(Parcel in) {
            return new CallInfo(in);
        }

        @Override
        public CallInfo[] newArray(int size) {
            return new CallInfo[size];
        }
    };

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
