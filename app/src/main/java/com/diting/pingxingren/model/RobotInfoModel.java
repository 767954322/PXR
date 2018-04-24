package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 09, 14:18.
 * Description: .
 */

public class RobotInfoModel implements Parcelable{
    private String mFrom;
    private String invalidAnswer3;
    private String industryIds;
    private String namePinyin;
    private String zidingyi;
    private int ownerType;
    private int sex;
    private String headImg;
    private String robotHeadImg;
    private int owner;
    private boolean star;
    private int id;
    private String welcomes;
    private String voicePassword;
    private int robotSkin;
    private String hangye;
    private String robotVal;
    private String avatarUrl;
    private String companyName;
    private String shortDomainName;
    private String uniqueId;
    private String fansNum;
    private int accountId;
    private String chargeMode;
    private String profile;
    private String invalidAnswer2;
    private String invalidAnswer1;
    private boolean enable;
    private String invalidAnswer7;
    private String invalidAnswer6;
    private String invalidAnswer5;
    private String invalidAnswer4;
    private boolean deleted;
    private String invalidAnswer9;
    private String invalidAnswer8;
    private String createdBy;
    private String updatedBy;
    private String invalidAnswer10;
    private String hangyedengji;
    private String shengri;
    private long updatedTime;
    private String name;
    private String invokeEnable;
    private String industry;
    private int companyId;
    private String guanfangjia;
    private String username;
    private String token;
    private int intel_calendar;
    private long createdTime;
    private boolean attentionState;

    protected RobotInfoModel(Parcel in) {
        mFrom = in.readString();
        invalidAnswer3 = in.readString();
        industryIds = in.readString();
        namePinyin = in.readString();
        zidingyi = in.readString();
        ownerType = in.readInt();
        sex = in.readInt();
        headImg = in.readString();
        robotHeadImg = in.readString();
        owner = in.readInt();
        star = in.readByte() != 0;
        id = in.readInt();
        welcomes = in.readString();
        voicePassword = in.readString();
        robotSkin = in.readInt();
        hangye = in.readString();
        robotVal = in.readString();
        avatarUrl = in.readString();
        companyName = in.readString();
        shortDomainName = in.readString();
        uniqueId = in.readString();
        fansNum = in.readString();
        accountId = in.readInt();
        chargeMode = in.readString();
        profile = in.readString();
        invalidAnswer2 = in.readString();
        invalidAnswer1 = in.readString();
        enable = in.readByte() != 0;
        invalidAnswer7 = in.readString();
        invalidAnswer6 = in.readString();
        invalidAnswer5 = in.readString();
        invalidAnswer4 = in.readString();
        deleted = in.readByte() != 0;
        invalidAnswer9 = in.readString();
        invalidAnswer8 = in.readString();
        createdBy = in.readString();
        updatedBy = in.readString();
        invalidAnswer10 = in.readString();
        hangyedengji = in.readString();
        shengri = in.readString();
        updatedTime = in.readLong();
        name = in.readString();
        invokeEnable = in.readString();
        industry = in.readString();
        companyId = in.readInt();
        guanfangjia = in.readString();
        username = in.readString();
        token = in.readString();
        intel_calendar = in.readInt();
        createdTime = in.readLong();
        attentionState = in.readByte() != 0;
    }

    public static final Creator<RobotInfoModel> CREATOR = new Creator<RobotInfoModel>() {
        @Override
        public RobotInfoModel createFromParcel(Parcel in) {
            return new RobotInfoModel(in);
        }

        @Override
        public RobotInfoModel[] newArray(int size) {
            return new RobotInfoModel[size];
        }
    };

    @Override
    public String toString() {
        return "RobotInfoModel{" +
                "mFrom='" + mFrom + '\'' +
                ", invalidAnswer3='" + invalidAnswer3 + '\'' +
                ", industryIds='" + industryIds + '\'' +
                ", namePinyin='" + namePinyin + '\'' +
                ", zidingyi='" + zidingyi + '\'' +
                ", ownerType=" + ownerType +
                ", sex=" + sex +
                ", headImg='" + headImg + '\'' +
                ", robotHeadImg='" + robotHeadImg + '\'' +
                ", owner=" + owner +
                ", star=" + star +
                ", id=" + id +
                ", welcomes='" + welcomes + '\'' +
                ", voicePassword='" + voicePassword + '\'' +
                ", robotSkin=" + robotSkin +
                ", hangye='" + hangye + '\'' +
                ", robotVal='" + robotVal + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", companyName='" + companyName + '\'' +
                ", shortDomainName='" + shortDomainName + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                ", fansNum='" + fansNum + '\'' +
                ", accountId=" + accountId +
                ", chargeMode='" + chargeMode + '\'' +
                ", profile='" + profile + '\'' +
                ", invalidAnswer2='" + invalidAnswer2 + '\'' +
                ", invalidAnswer1='" + invalidAnswer1 + '\'' +
                ", enable=" + enable +
                ", invalidAnswer7='" + invalidAnswer7 + '\'' +
                ", invalidAnswer6='" + invalidAnswer6 + '\'' +
                ", invalidAnswer5='" + invalidAnswer5 + '\'' +
                ", invalidAnswer4='" + invalidAnswer4 + '\'' +
                ", deleted=" + deleted +
                ", invalidAnswer9='" + invalidAnswer9 + '\'' +
                ", invalidAnswer8='" + invalidAnswer8 + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", invalidAnswer10='" + invalidAnswer10 + '\'' +
                ", hangyedengji='" + hangyedengji + '\'' +
                ", shengri='" + shengri + '\'' +
                ", updatedTime=" + updatedTime +
                ", name='" + name + '\'' +
                ", invokeEnable='" + invokeEnable + '\'' +
                ", industry='" + industry + '\'' +
                ", companyId=" + companyId +
                ", guanfangjia='" + guanfangjia + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", intel_calendar=" + intel_calendar +
                ", createdTime=" + createdTime +
                ", attentionState=" + attentionState +
                '}';
    }

    public boolean isAttentionState() {
        return attentionState;
    }

    public void setAttentionState(boolean attentionState) {
        this.attentionState = attentionState;
    }

    public String getmFrom() {
        return mFrom;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getInvalidAnswer3() {
        return invalidAnswer3;
    }

    public void setInvalidAnswer3(String invalidAnswer3) {
        this.invalidAnswer3 = invalidAnswer3;
    }

    public String getIndustryIds() {
        return industryIds;
    }

    public void setIndustryIds(String industryIds) {
        this.industryIds = industryIds;
    }

    public String getNamePinyin() {
        return namePinyin;
    }

    public void setNamePinyin(String namePinyin) {
        this.namePinyin = namePinyin;
    }

    public String getZidingyi() {
        return zidingyi;
    }

    public void setZidingyi(String zidingyi) {
        this.zidingyi = zidingyi;
    }

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getRobotHeadImg() {
        return robotHeadImg;
    }

    public void setRobotHeadImg(String robotHeadImg) {
        this.robotHeadImg = robotHeadImg;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWelcomes() {
        return welcomes;
    }

    public void setWelcomes(String welcomes) {
        this.welcomes = welcomes;
    }

    public String getVoicePassword() {
        return voicePassword;
    }

    public void setVoicePassword(String voicePassword) {
        this.voicePassword = voicePassword;
    }

    public int getRobotSkin() {
        return robotSkin;
    }

    public void setRobotSkin(int robotSkin) {
        this.robotSkin = robotSkin;
    }

    public String getHangye() {
        return hangye;
    }

    public void setHangye(String hangye) {
        this.hangye = hangye;
    }

    public String getRobotVal() {
        return robotVal;
    }

    public void setRobotVal(String robotVal) {
        this.robotVal = robotVal;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getShortDomainName() {
        return shortDomainName;
    }

    public void setShortDomainName(String shortDomainName) {
        this.shortDomainName = shortDomainName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getInvalidAnswer7() {
        return invalidAnswer7;
    }

    public void setInvalidAnswer7(String invalidAnswer7) {
        this.invalidAnswer7 = invalidAnswer7;
    }

    public String getInvalidAnswer6() {
        return invalidAnswer6;
    }

    public void setInvalidAnswer6(String invalidAnswer6) {
        this.invalidAnswer6 = invalidAnswer6;
    }

    public String getInvalidAnswer5() {
        return invalidAnswer5;
    }

    public void setInvalidAnswer5(String invalidAnswer5) {
        this.invalidAnswer5 = invalidAnswer5;
    }

    public String getInvalidAnswer4() {
        return invalidAnswer4;
    }

    public void setInvalidAnswer4(String invalidAnswer4) {
        this.invalidAnswer4 = invalidAnswer4;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getInvalidAnswer9() {
        return invalidAnswer9;
    }

    public void setInvalidAnswer9(String invalidAnswer9) {
        this.invalidAnswer9 = invalidAnswer9;
    }

    public String getInvalidAnswer8() {
        return invalidAnswer8;
    }

    public void setInvalidAnswer8(String invalidAnswer8) {
        this.invalidAnswer8 = invalidAnswer8;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getInvalidAnswer10() {
        return invalidAnswer10;
    }

    public void setInvalidAnswer10(String invalidAnswer10) {
        this.invalidAnswer10 = invalidAnswer10;
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

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvokeEnable() {
        return invokeEnable;
    }

    public void setInvokeEnable(String invokeEnable) {
        this.invokeEnable = invokeEnable;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getGuanfangjia() {
        return guanfangjia;
    }

    public void setGuanfangjia(String guanfangjia) {
        this.guanfangjia = guanfangjia;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIntel_calendar() {
        return intel_calendar;
    }

    public void setIntel_calendar(int intel_calendar) {
        this.intel_calendar = intel_calendar;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFrom);
        dest.writeString(invalidAnswer3);
        dest.writeString(industryIds);
        dest.writeString(namePinyin);
        dest.writeString(zidingyi);
        dest.writeInt(ownerType);
        dest.writeInt(sex);
        dest.writeString(headImg);
        dest.writeString(robotHeadImg);
        dest.writeInt(owner);
        dest.writeByte((byte) (star ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(welcomes);
        dest.writeString(voicePassword);
        dest.writeInt(robotSkin);
        dest.writeString(hangye);
        dest.writeString(robotVal);
        dest.writeString(avatarUrl);
        dest.writeString(companyName);
        dest.writeString(shortDomainName);
        dest.writeString(uniqueId);
        dest.writeString(fansNum);
        dest.writeInt(accountId);
        dest.writeString(chargeMode);
        dest.writeString(profile);
        dest.writeString(invalidAnswer2);
        dest.writeString(invalidAnswer1);
        dest.writeByte((byte) (enable ? 1 : 0));
        dest.writeString(invalidAnswer7);
        dest.writeString(invalidAnswer6);
        dest.writeString(invalidAnswer5);
        dest.writeString(invalidAnswer4);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(invalidAnswer9);
        dest.writeString(invalidAnswer8);
        dest.writeString(createdBy);
        dest.writeString(updatedBy);
        dest.writeString(invalidAnswer10);
        dest.writeString(hangyedengji);
        dest.writeString(shengri);
        dest.writeLong(updatedTime);
        dest.writeString(name);
        dest.writeString(invokeEnable);
        dest.writeString(industry);
        dest.writeInt(companyId);
        dest.writeString(guanfangjia);
        dest.writeString(username);
        dest.writeString(token);
        dest.writeInt(intel_calendar);
        dest.writeLong(createdTime);
        dest.writeByte((byte) (attentionState ? 1 : 0));
    }
}
