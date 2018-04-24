package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 25, 17:48.
 * Description: .
 */

public class ThirdLoginDateModel implements Parcelable {
    private String form;
    private int platform;
    private String AccessToken;
    private String Uid;


    public ThirdLoginDateModel() {
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    protected ThirdLoginDateModel(Parcel in) {
        form = in.readString();
        platform = in.readInt();
        AccessToken = in.readString();
        Uid = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(form);
        dest.writeInt(platform);
        dest.writeString(AccessToken);
        dest.writeString(Uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThirdLoginDateModel> CREATOR = new Creator<ThirdLoginDateModel>() {
        @Override
        public ThirdLoginDateModel createFromParcel(Parcel in) {
            return new ThirdLoginDateModel(in);
        }

        @Override
        public ThirdLoginDateModel[] newArray(int size) {
            return new ThirdLoginDateModel[size];
        }
    };

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
