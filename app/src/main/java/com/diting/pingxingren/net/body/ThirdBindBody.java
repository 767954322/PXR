package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 25, 17:53.
 * Description: .
 */

public class ThirdBindBody implements Parcelable{
    private String userName;
    private String verifyCode;
    private String uid;
    private String accessToken;

    public ThirdBindBody() {
    }

    protected ThirdBindBody(Parcel in) {
        userName = in.readString();
        verifyCode = in.readString();
        uid = in.readString();
        accessToken = in.readString();
    }

    public static final Creator<ThirdBindBody> CREATOR = new Creator<ThirdBindBody>() {
        @Override
        public ThirdBindBody createFromParcel(Parcel in) {
            return new ThirdBindBody(in);
        }

        @Override
        public ThirdBindBody[] newArray(int size) {
            return new ThirdBindBody[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(verifyCode);
        parcel.writeString(uid);
        parcel.writeString(accessToken);
    }
}
