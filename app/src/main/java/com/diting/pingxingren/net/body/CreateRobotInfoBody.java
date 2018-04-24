package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 09, 17:20.
 * Description: .
 */

public class CreateRobotInfoBody implements Parcelable {

    private String name;
    private String profile;
    private boolean enable;
    private String hangye;
    private String hangyedengji;
    private String shengri;
    private int sex;
    private String robotHeadImg;
    private String outTradeNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getHangye() {
        return hangye;
    }

    public void setHangye(String hangye) {
        this.hangye = hangye;
    }

    public String getHangyedengji() {
        return hangyedengji;
    }

    public void setHangyedengji(String hangyedengji) {
        this.hangyedengji = hangyedengji;
    }

    public String getShengri() {
        return shengri;
    }

    public void setShengri(String shengri) {
        this.shengri = shengri;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getRobotHeadImg() {
        return robotHeadImg;
    }

    public void setRobotHeadImg(String robotHeadImg) {
        this.robotHeadImg = robotHeadImg;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.profile);
        dest.writeByte(this.enable ? (byte) 1 : (byte) 0);
        dest.writeString(this.hangye);
        dest.writeString(this.hangyedengji);
        dest.writeString(this.shengri);
        dest.writeInt(this.sex);
        dest.writeString(this.robotHeadImg);
        dest.writeString(this.outTradeNo);
    }

    public CreateRobotInfoBody() {
    }

    protected CreateRobotInfoBody(Parcel in) {
        this.name = in.readString();
        this.profile = in.readString();
        this.enable = in.readByte() != 0;
        this.hangye = in.readString();
        this.hangyedengji = in.readString();
        this.shengri = in.readString();
        this.sex = in.readInt();
        this.robotHeadImg = in.readString();
        this.outTradeNo = in.readString();
    }

    public static final Parcelable.Creator<CreateRobotInfoBody> CREATOR = new Parcelable.Creator<CreateRobotInfoBody>() {
        @Override
        public CreateRobotInfoBody createFromParcel(Parcel source) {
            return new CreateRobotInfoBody(source);
        }

        @Override
        public CreateRobotInfoBody[] newArray(int size) {
            return new CreateRobotInfoBody[size];
        }
    };
}
