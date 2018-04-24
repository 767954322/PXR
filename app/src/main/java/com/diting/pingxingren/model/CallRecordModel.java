package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 通话记录
 * Created by Administrator on 2017/12/15.
 */

public class CallRecordModel implements Parcelable{


    private String callType;
    private String updatedTime;
    private String toRobotName;
    private String fromUserName;
    private String startTime;
    private String toheadImgUrl;
    private boolean deleted;
    private int callTime;
    private String ownerType;
    private String toUserName;
    private String fromHeadImgUrl;
    private String createdTime;
    private String fromCompanyName;
    private String createdBy;
    private String updatedBy;
    private String owner;
    private String fromRobotName;
    private String toCompanyName;
    private int id;
    private String endType;

    public CallRecordModel() {
    }

    protected CallRecordModel(Parcel in) {
        callType = in.readString();
        updatedTime = in.readString();
        toRobotName = in.readString();
        fromUserName = in.readString();
        startTime = in.readString();
        toheadImgUrl = in.readString();
        deleted = in.readByte() != 0;
        callTime = in.readInt();
        ownerType = in.readString();
        toUserName = in.readString();
        fromHeadImgUrl = in.readString();
        createdTime = in.readString();
        fromCompanyName = in.readString();
        createdBy = in.readString();
        updatedBy = in.readString();
        owner = in.readString();
        fromRobotName = in.readString();
        toCompanyName = in.readString();
        id = in.readInt();
        endType = in.readString();
    }

    public static final Creator<CallRecordModel> CREATOR = new Creator<CallRecordModel>() {
        @Override
        public CallRecordModel createFromParcel(Parcel in) {
            return new CallRecordModel(in);
        }

        @Override
        public CallRecordModel[] newArray(int size) {
            return new CallRecordModel[size];
        }
    };

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getToRobotName() {
        return toRobotName;
    }

    public void setToRobotName(String toRobotName) {
        this.toRobotName = toRobotName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getToheadImgUrl() {
        return toheadImgUrl;
    }

    public void setToheadImgUrl(String toheadImgUrl) {
        this.toheadImgUrl = toheadImgUrl;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getCallTime() {
        return callTime;
    }

    public void setCallTime(int callTime) {
        this.callTime = callTime;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromHeadImgUrl() {
        return fromHeadImgUrl;
    }

    public void setFromHeadImgUrl(String fromHeadImgUrl) {
        this.fromHeadImgUrl = fromHeadImgUrl;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getFromCompanyName() {
        return fromCompanyName;
    }

    public void setFromCompanyName(String fromCompanyName) {
        this.fromCompanyName = fromCompanyName;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFromRobotName() {
        return fromRobotName;
    }

    public void setFromRobotName(String fromRobotName) {
        this.fromRobotName = fromRobotName;
    }

    public String getToCompanyName() {
        return toCompanyName;
    }

    public void setToCompanyName(String toCompanyName) {
        this.toCompanyName = toCompanyName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEndType() {
        return endType;
    }

    public void setEndType(String endType) {
        this.endType = endType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(callType);
        dest.writeString(updatedTime);
        dest.writeString(toRobotName);
        dest.writeString(fromUserName);
        dest.writeString(startTime);
        dest.writeString(toheadImgUrl);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeInt(callTime);
        dest.writeString(ownerType);
        dest.writeString(toUserName);
        dest.writeString(fromHeadImgUrl);
        dest.writeString(createdTime);
        dest.writeString(fromCompanyName);
        dest.writeString(createdBy);
        dest.writeString(updatedBy);
        dest.writeString(owner);
        dest.writeString(fromRobotName);
        dest.writeString(toCompanyName);
        dest.writeInt(id);
        dest.writeString(endType);
    }
}
