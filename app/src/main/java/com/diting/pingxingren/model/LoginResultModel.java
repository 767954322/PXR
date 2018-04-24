package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 08, 15:46.
 * Description: 登录结果.
 */

public class LoginResultModel implements Parcelable {

    private String headImgUrl;
    private String message;
    private String uniqueId;

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        dest.writeString(this.headImgUrl);
        dest.writeString(this.message);
        dest.writeString(this.uniqueId);
    }

    public LoginResultModel() {
    }

    protected LoginResultModel(Parcel in) {
        this.headImgUrl = in.readString();
        this.message = in.readString();
        this.uniqueId = in.readString();
    }

    public static final Parcelable.Creator<LoginResultModel> CREATOR = new Parcelable.Creator<LoginResultModel>() {
        @Override
        public LoginResultModel createFromParcel(Parcel source) {
            return new LoginResultModel(source);
        }

        @Override
        public LoginResultModel[] newArray(int size) {
            return new LoginResultModel[size];
        }
    };
}
