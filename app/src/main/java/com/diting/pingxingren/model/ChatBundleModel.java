package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ChatBundleModel  implements Parcelable{

    private String mFrom;//判断从哪里过去
    private String mUserName;//手机号  传过来
    private String headImg;
    private String name;
    private String welcomes;
    private String uniqueId;

    public ChatBundleModel() {
    }

    protected ChatBundleModel(Parcel in) {
        mFrom = in.readString();
        mUserName = in.readString();
        headImg = in.readString();
        name = in.readString();
        welcomes = in.readString();
        uniqueId = in.readString();
    }

    public static final Creator<ChatBundleModel> CREATOR = new Creator<ChatBundleModel>() {
        @Override
        public ChatBundleModel createFromParcel(Parcel in) {
            return new ChatBundleModel(in);
        }

        @Override
        public ChatBundleModel[] newArray(int size) {
            return new ChatBundleModel[size];
        }
    };

    public String getmFrom() {
        return mFrom;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWelcomes() {
        return welcomes;
    }

    public void setWelcomes(String welcomes) {
        this.welcomes = welcomes;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFrom);
        dest.writeString(mUserName);
        dest.writeString(headImg);
        dest.writeString(name);
        dest.writeString(welcomes);
        dest.writeString(uniqueId);
    }

    @Override
    public String toString() {
        return "ChatBundleModel{" +
                "mFrom='" + mFrom + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", name='" + name + '\'' +
                ", welcomes='" + welcomes + '\'' +
                ", uniqueId='" + uniqueId + '\'' +
                '}';
    }
}
