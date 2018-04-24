package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * 沟通模块的实体  包括粉丝  关注 和价值排行
 * Created by Administrator on 2017/12/12.
 */

public class CommunicateModel implements Parcelable {
    private String name;//		机器人名称
    private String uniqueId;//机器人uniqueId
    private String robotHeadImg;//	机器人头像
    private String profile;//	机器人个性
    private String companyName;//	主人名称
    private String fansNum;//	粉丝数
    private int industryIds;// 0已关注/1未关注
    private String shortDomainName;// 备注
    private String robotVal;//价值
    private String namePinyin;//手机号
    private String  invalidAnswer3;
    private String  invalidAnswer2;
    private String  invalidAnswer1;


    @Override
    public String toString() {
        return "CommunicateModel{" +
                "name='" + name + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", robotHeadImg='" + robotHeadImg + '\'' +
                ", profile='" + profile + '\'' +
                ", companyName='" + companyName + '\'' +
                ", fansNum='" + fansNum + '\'' +
                ", industryIds=" + industryIds +
                ", shortDomainName='" + shortDomainName + '\'' +
                ", robotVal='" + robotVal + '\'' +
                ", namePinyin='" + namePinyin + '\'' +
                ", invalidAnswer3='" + invalidAnswer3 + '\'' +
                ", invalidAnswer2='" + invalidAnswer2 + '\'' +
                ", invalidAnswer1='" + invalidAnswer1 + '\'' +
                '}';
    }

    protected CommunicateModel(Parcel in) {
        name = in.readString();
        uniqueId = in.readString();
        robotHeadImg = in.readString();
        profile = in.readString();
        companyName = in.readString();
        fansNum = in.readString();
        industryIds = in.readInt();
        shortDomainName = in.readString();
        robotVal = in.readString();
        namePinyin = in.readString();
        invalidAnswer3 = in.readString();
        invalidAnswer2 = in.readString();
        invalidAnswer1 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(uniqueId);
        dest.writeString(robotHeadImg);
        dest.writeString(profile);
        dest.writeString(companyName);
        dest.writeString(fansNum);
        dest.writeInt(industryIds);
        dest.writeString(shortDomainName);
        dest.writeString(robotVal);
        dest.writeString(namePinyin);
        dest.writeString(invalidAnswer3);
        dest.writeString(invalidAnswer2);
        dest.writeString(invalidAnswer1);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommunicateModel> CREATOR = new Creator<CommunicateModel>() {
        @Override
        public CommunicateModel createFromParcel(Parcel in) {
            return new CommunicateModel(in);
        }

        @Override
        public CommunicateModel[] newArray(int size) {
            return new CommunicateModel[size];
        }
    };

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

    public CommunicateModel() {
    }


    public String getShortDomainName() {
        return shortDomainName;
    }

    public void setShortDomainName(String shortDomainName) {
        this.shortDomainName = shortDomainName;
    }

    public boolean getIndustryIds() {
        if (1 == industryIds)
            return false;
        else
            return true;
    }

    public void setIndustryIds(int industryIds) {
        this.industryIds = industryIds;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }





    public String getRobotVal() {
        if ("null".equals(robotVal) || TextUtils.isEmpty(robotVal)) {
            return "50.0";
        } else {
            return robotVal;
        }
    }

    public void setRobotVal(String robotVal) {
        this.robotVal = robotVal;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRobotHeadImg() {
        return robotHeadImg;
    }

    public void setRobotHeadImg(String robotHeadImg) {
        this.robotHeadImg = robotHeadImg;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }



}
