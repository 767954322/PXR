package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/8/29, 17:02.
 * Description: 注册请求体.
 */

public class ResetPassBody implements Parcelable {

    private String newPassword;
    private String mobile;
    private String verifyCode;

    public String getPassword() {
        return newPassword;
    }

    public void setPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.newPassword);
        dest.writeString(this.mobile);
        dest.writeString(this.verifyCode);
    }

    public ResetPassBody() {
    }

    protected ResetPassBody(Parcel in) {
        this.newPassword = in.readString();
        this.mobile = in.readString();
        this.verifyCode = in.readString();
    }

    public static final Creator<ResetPassBody> CREATOR = new Creator<ResetPassBody>() {
        @Override
        public ResetPassBody createFromParcel(Parcel source) {
            return new ResetPassBody(source);
        }

        @Override
        public ResetPassBody[] newArray(int size) {
            return new ResetPassBody[size];
        }
    };
}
