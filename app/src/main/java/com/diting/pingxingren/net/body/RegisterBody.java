package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/8/29, 17:02.
 * Description: 注册请求体.
 */

public class RegisterBody implements Parcelable {

    private String userName;
    private String password;
    private String mobile;
    private String verifyCode;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.mobile);
        dest.writeString(this.verifyCode);
    }

    public RegisterBody() {
    }

    protected RegisterBody(Parcel in) {
        this.userName = in.readString();
        this.password = in.readString();
        this.mobile = in.readString();
        this.verifyCode = in.readString();
    }

    public static final Creator<RegisterBody> CREATOR = new Creator<RegisterBody>() {
        @Override
        public RegisterBody createFromParcel(Parcel source) {
            return new RegisterBody(source);
        }

        @Override
        public RegisterBody[] newArray(int size) {
            return new RegisterBody[size];
        }
    };
}
