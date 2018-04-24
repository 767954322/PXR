package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 11, 13:53.
 * Description: 机器人信息.
 */

public class RobotInfoBody implements Parcelable {
private String uniqueId;
    private String name;
    private String profile;
    private boolean enable;
    private String hangye;
    private String hangyedengji;
    private String zidingyi;
    private String shengri;
    private String companyName;
    private int sex;
    private String robotHeadImg;
    private String invalidAnswer3;
    private String invalidAnswer2;
    private String invalidAnswer1;

    public RobotInfoBody() {
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

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

    public String getZidingyi() {
        return zidingyi;
    }

    public void setZidingyi(String zidingyi) {
        this.zidingyi = zidingyi;
    }

    public String getShengri() {
        return shengri;
    }

    public void setShengri(String shengri) {
        this.shengri = shengri;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getInvalidAnswer3() {
        return invalidAnswer3;
    }

    public void setInvalidAnswer3(String invalidAnswer3) {
        this.invalidAnswer3 = invalidAnswer3;
    }

    public String getInvalidAnswer2() {
        return invalidAnswer2;
    }

    public void setInvalidAnswer2(String invalidAnswer2) {
        this.invalidAnswer2 = invalidAnswer2;
    }

    public String getInvalidAnswer1() {
        return invalidAnswer1;
    }

    public void setInvalidAnswer1(String invalidAnswer1) {
        this.invalidAnswer1 = invalidAnswer1;
    }

    protected RobotInfoBody(Parcel in) {
        uniqueId = in.readString();
        name = in.readString();
        profile = in.readString();
        enable = in.readByte() != 0;
        hangye = in.readString();
        hangyedengji = in.readString();
        zidingyi = in.readString();
        shengri = in.readString();
        companyName = in.readString();
        sex = in.readInt();
        robotHeadImg = in.readString();
        invalidAnswer3 = in.readString();
        invalidAnswer2 = in.readString();
        invalidAnswer1 = in.readString();
    }

    public static final Creator<RobotInfoBody> CREATOR = new Creator<RobotInfoBody>() {
        @Override
        public RobotInfoBody createFromParcel(Parcel in) {
            return new RobotInfoBody(in);
        }

        @Override
        public RobotInfoBody[] newArray(int size) {
            return new RobotInfoBody[size];
        }
    };

    @Override
    public String toString() {
        return "RobotInfoBody{" +
                "name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", enable=" + enable +
                ", hangye='" + hangye + '\'' +
                ", hangyedengji='" + hangyedengji + '\'' +
                ", zidingyi='" + zidingyi + '\'' +
                ", shengri='" + shengri + '\'' +
                ", companyName='" + companyName + '\'' +
                ", sex=" + sex +
                ", robotHeadImg='" + robotHeadImg + '\'' +
                ", invalidAnswer3='" + invalidAnswer3 + '\'' +
                ", invalidAnswer2='" + invalidAnswer2 + '\'' +
                ", invalidAnswer1='" + invalidAnswer1 + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uniqueId);
        parcel.writeString(name);
        parcel.writeString(profile);
        parcel.writeByte((byte) (enable ? 1 : 0));
        parcel.writeString(hangye);
        parcel.writeString(hangyedengji);
        parcel.writeString(zidingyi);
        parcel.writeString(shengri);
        parcel.writeString(companyName);
        parcel.writeInt(sex);
        parcel.writeString(robotHeadImg);
        parcel.writeString(invalidAnswer3);
        parcel.writeString(invalidAnswer2);
        parcel.writeString(invalidAnswer1);
    }
}
