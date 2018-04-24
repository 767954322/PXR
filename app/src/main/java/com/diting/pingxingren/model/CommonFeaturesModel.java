package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 11, 17:24.
 * Description: .
 */

public class CommonFeaturesModel implements Parcelable {

    private String address;
    private int ownerType;
    private String scene;
    private String keywords;
    private String switchName;
    private String owner;
    private int id;
    private String serverName;
    private String serverEnName;
    private String version;
    private String email;
    private int enable;
    private String description;
    private boolean deleted;
    private String createdBy;
    private String updatedBy;
    private String enName;
    private long updatedTime;
    private String approvalName;
    private String name;
    private String providerName;
    private String url;
    private int approvalEnable;
    private String method;
    private String mobile;
    private long createdTime;
    private String providerEnName;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(int ownerType) {
        this.ownerType = ownerType;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {
        this.switchName = switchName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerEnName() {
        return serverEnName;
    }

    public void setServerEnName(String serverEnName) {
        this.serverEnName = serverEnName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getApprovalEnable() {
        return approvalEnable;
    }

    public void setApprovalEnable(int approvalEnable) {
        this.approvalEnable = approvalEnable;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getProviderEnName() {
        return providerEnName;
    }

    public void setProviderEnName(String providerEnName) {
        this.providerEnName = providerEnName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeInt(this.ownerType);
        dest.writeString(this.scene);
        dest.writeString(this.keywords);
        dest.writeString(this.switchName);
        dest.writeString(this.owner);
        dest.writeInt(this.id);
        dest.writeString(this.serverName);
        dest.writeString(this.serverEnName);
        dest.writeString(this.version);
        dest.writeString(this.email);
        dest.writeInt(this.enable);
        dest.writeString(this.description);
        dest.writeByte(this.deleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdBy);
        dest.writeString(this.updatedBy);
        dest.writeString(this.enName);
        dest.writeLong(this.updatedTime);
        dest.writeString(this.approvalName);
        dest.writeString(this.name);
        dest.writeString(this.providerName);
        dest.writeString(this.url);
        dest.writeInt(this.approvalEnable);
        dest.writeString(this.method);
        dest.writeString(this.mobile);
        dest.writeLong(this.createdTime);
        dest.writeString(this.providerEnName);
    }

    public CommonFeaturesModel() {
    }

    protected CommonFeaturesModel(Parcel in) {
        this.address = in.readString();
        this.ownerType = in.readInt();
        this.scene = in.readString();
        this.keywords = in.readString();
        this.switchName = in.readString();
        this.owner = in.readString();
        this.id = in.readInt();
        this.serverName = in.readString();
        this.serverEnName = in.readString();
        this.version = in.readString();
        this.email = in.readString();
        this.enable = in.readInt();
        this.description = in.readString();
        this.deleted = in.readByte() != 0;
        this.createdBy = in.readString();
        this.updatedBy = in.readString();
        this.enName = in.readString();
        this.updatedTime = in.readLong();
        this.approvalName = in.readString();
        this.name = in.readString();
        this.providerName = in.readString();
        this.url = in.readString();
        this.approvalEnable = in.readInt();
        this.method = in.readString();
        this.mobile = in.readString();
        this.createdTime = in.readLong();
        this.providerEnName = in.readString();
    }

    public static final Parcelable.Creator<CommonFeaturesModel> CREATOR = new Parcelable.Creator<CommonFeaturesModel>() {
        @Override
        public CommonFeaturesModel createFromParcel(Parcel source) {
            return new CommonFeaturesModel(source);
        }

        @Override
        public CommonFeaturesModel[] newArray(int size) {
            return new CommonFeaturesModel[size];
        }
    };
}
