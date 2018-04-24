package com.diting.pingxingren.entity;

/**
 * Created by Administrator on 2017/12/11.
 */

public class CommunicateBean {

    public String name;//		机器人名称
    public String uniqueId;//机器人uniqueId
    public String robotHeadImg;//	机器人头像
    public String profile;//	机器人个性
    public String companyName;//	主人名称
    public String fansNum;//	粉丝数
    public int industryids;// 0已关注/1未关注
    public String ShortDomainName;// 备注

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

    public boolean getIndustryids() {
        if (1 == industryids)
            return false;
        else
            return true;

    }

    public void setIndustryids(int industryids) {
        this.industryids = industryids;
    }

    public String getShortDomainName() {
        return ShortDomainName;
    }

    public void setShortDomainName(String shortDomainName) {
        ShortDomainName = shortDomainName;
    }
}
