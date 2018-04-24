package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 16:30.
 * Description: 个人用户信息.
 */

public class UserInfoModel implements Parcelable {
    @Override
    public String toString() {
        return "UserInfoModel{" +
                "userName='" + userName + '\'' +
                ", robotName='" + robotName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", fansCount=" + fansCount +
                ", welcome='" + welcome + '\'' +
                ", short_domain_name='" + short_domain_name + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", profile='" + profile + '\'' +
                ", telephoneSwitch=" + telephoneSwitch +
                ", unique_id='" + unique_id + '\'' +
                '}';
    }

    private String userName;
    private String robotName;
    private String companyName;
    private int fansCount;
    private String welcome;
    private String short_domain_name;
    private String headPortrait;
    private String profile;
    private int telephoneSwitch;
    private String unique_id;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getShort_domain_name() {
        return short_domain_name;
    }

    public void setShort_domain_name(String short_domain_name) {
        this.short_domain_name = short_domain_name;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getTelephoneSwitch() {
        return telephoneSwitch;
    }

    public void setTelephoneSwitch(int telephoneSwitch) {
        this.telephoneSwitch = telephoneSwitch;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.robotName);
        dest.writeString(this.companyName);
        dest.writeInt(this.fansCount);
        dest.writeString(this.welcome);
        dest.writeString(this.short_domain_name);
        dest.writeString(this.headPortrait);
        dest.writeString(this.profile);
        dest.writeInt(this.telephoneSwitch);
        dest.writeString(this.unique_id);
    }

    public UserInfoModel() {
    }

    protected UserInfoModel(Parcel in) {
        this.userName = in.readString();
        this.robotName = in.readString();
        this.companyName = in.readString();
        this.fansCount = in.readInt();
        this.welcome = in.readString();
        this.short_domain_name = in.readString();
        this.headPortrait = in.readString();
        this.profile = in.readString();
        this.telephoneSwitch = in.readInt();
        this.unique_id = in.readString();
    }

    public static final Parcelable.Creator<UserInfoModel> CREATOR = new Parcelable.Creator<UserInfoModel>() {
        @Override
        public UserInfoModel createFromParcel(Parcel source) {
            return new UserInfoModel(source);
        }

        @Override
        public UserInfoModel[] newArray(int size) {
            return new UserInfoModel[size];
        }
    };
}
