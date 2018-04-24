package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 访客管理
 * Created by Administrator on 2017/12/14.
 */

public class ChatUserManageItemModel implements Parcelable{


    @Override
    public String toString() {
        return "ChatUserManageItemModel{" +
                "count=" + count +
                ", createdTime=" + createdTime +
                ", extra4=" + extra4 +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", robotName='" + robotName + '\'' +
                ", welcome='" + welcome + '\'' +
                ", uuid='" + uuid + '\'' +
                ", app_key='" + app_key + '\'' +
                ", loginUsername='" + loginUsername + '\'' +
                '}';
    }

    private int count;
    private long createdTime;
    private int extra4;
    private String headImgUrl;
    private String robotName;
    private String welcome;
    private String uuid;
    private String app_key;
    private String loginUsername;

    public ChatUserManageItemModel() {
    }

    protected ChatUserManageItemModel(Parcel in) {
        count = in.readInt();
        createdTime = in.readLong();
        extra4 = in.readInt();
        headImgUrl = in.readString();
        robotName = in.readString();
        welcome = in.readString();
        uuid = in.readString();
        app_key = in.readString();
        loginUsername = in.readString();
    }

    public static final Creator<ChatUserManageItemModel> CREATOR = new Creator<ChatUserManageItemModel>() {
        @Override
        public ChatUserManageItemModel createFromParcel(Parcel in) {
            return new ChatUserManageItemModel(in);
        }

        @Override
        public ChatUserManageItemModel[] newArray(int size) {
            return new ChatUserManageItemModel[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getExtra4() {
        return extra4;
    }

    public void setExtra4(int extra4) {
        this.extra4 = extra4;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getRobotName() {
        return robotName;
    }

    public void setRobotName(String robotName) {
        this.robotName = robotName;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(count);
        dest.writeLong(createdTime);
        dest.writeInt(extra4);
        dest.writeString(headImgUrl);
        dest.writeString(robotName);
        dest.writeString(welcome);
        dest.writeString(uuid);
        dest.writeString(app_key);
        dest.writeString(loginUsername);
    }
}
