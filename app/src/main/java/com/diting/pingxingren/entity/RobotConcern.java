package com.diting.pingxingren.entity;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by asus on 2016/11/23.
 */
public class RobotConcern extends BaseObservable implements Parcelable{
    @SerializedName("name")
    private String robotName = "";
    @SerializedName("userName")
    private String username = "";
    private String companyName = "";
    @SerializedName("welcomes")
    private String welcome = "";
    private String profile = "";
    private String phone = "";
    @SerializedName(value = "headImg",alternate = {"headImgUrl"})
    private String headPortrait = "";
    private Double robotValue = 0.0;
    @SerializedName("fansNum")
    private Integer fansCount = 0;
    private boolean isConcerned = false;
    private String uniqueId = "";
    private String remark = "";
    private int industry = 1;
    @SerializedName("juli")
    private Double distance = 0.0;

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Double getRobotValue() {
        return robotValue;
    }

    public void setRobotValue(Double robotValue) {
        this.robotValue = robotValue;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public boolean isConcerned() {
        return isConcerned;
    }

    public void setConcerned(boolean concerned) {
        isConcerned = concerned;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getIndustry() {
        return industry;
    }

    public void setIndustry(int industry) {
        this.industry = industry;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(robotName);
        dest.writeString(username);
        dest.writeString(companyName);
        dest.writeString(welcome);
        dest.writeString(profile);
        dest.writeString(phone);
        dest.writeString(headPortrait);
        dest.writeDouble(robotValue);
        dest.writeInt(fansCount);
        dest.writeByte((byte) (isConcerned?1:0));
        dest.writeString(uniqueId);
        dest.writeString(remark);
        dest.writeInt(industry);
        dest.writeDouble(distance);
    }

    public static final Parcelable.Creator<RobotConcern> CREATOR = new Creator<RobotConcern>() {
        public RobotConcern createFromParcel(Parcel source) {
            RobotConcern robotConcern = new RobotConcern();
            robotConcern.robotName = source.readString();
            robotConcern.username = source.readString();
            robotConcern.companyName = source.readString();
            robotConcern.welcome = source.readString();
            robotConcern.profile = source.readString();
            robotConcern.phone = source.readString();
            robotConcern.headPortrait = source.readString();
            robotConcern.robotValue = source.readDouble();
            robotConcern.fansCount = source.readInt();
            robotConcern.isConcerned = source.readByte()!= 0;
            robotConcern.uniqueId = source.readString();
            robotConcern.remark = source.readString();
            robotConcern.industry = source.readInt();
            robotConcern.distance = source.readDouble();
            return robotConcern;
        }

        public RobotConcern[] newArray(int size) {
            return new RobotConcern[size];
        }
    };


    @Override
    public String toString() {
        return "RobotConcern{" +
                "robotName='" + robotName + '\'' +
                ", username='" + username + '\'' +
                ", companyName='" + companyName + '\'' +
                ", welcome='" + welcome + '\'' +
                ", profile='" + profile + '\'' +
                ", phone='" + phone + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                ", robotValue=" + robotValue +
                ", fansCount=" + fansCount +
                ", isConcerned=" + isConcerned +
                ", uniqueId='" + uniqueId + '\'' +
                ", remark='" + remark + '\'' +
                ", industry=" + industry +
                ", distance=" + distance +
                '}';
    }
}
