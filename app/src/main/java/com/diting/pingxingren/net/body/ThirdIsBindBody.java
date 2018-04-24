package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 25, 17:29.
 * Description: 判断第三方是否登录.
 */

public class ThirdIsBindBody implements Parcelable {
    private String accessToken;
    private String uid;

    protected ThirdIsBindBody(Parcel in) {
        accessToken = in.readString();
        uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeString(uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThirdIsBindBody> CREATOR = new Creator<ThirdIsBindBody>() {
        @Override
        public ThirdIsBindBody createFromParcel(Parcel in) {
            return new ThirdIsBindBody(in);
        }

        @Override
        public ThirdIsBindBody[] newArray(int size) {
            return new ThirdIsBindBody[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ThirdIsBindBody() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


}

