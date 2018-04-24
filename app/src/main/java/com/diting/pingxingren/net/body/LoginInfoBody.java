package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gu FanFan.
 * Date: 2017/4/11, 14:16.
 * 登录请求体.
 */

public class LoginInfoBody implements Parcelable {

    private String userName;
    private String password;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.password);
    }

    public LoginInfoBody() {
    }

    public LoginInfoBody(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    protected LoginInfoBody(Parcel in) {
        this.userName = in.readString();
        this.password = in.readString();
    }

    public static final Creator<LoginInfoBody> CREATOR = new Creator<LoginInfoBody>() {
        @Override
        public LoginInfoBody createFromParcel(Parcel source) {
            return new LoginInfoBody(source);
        }

        @Override
        public LoginInfoBody[] newArray(int size) {
            return new LoginInfoBody[size];
        }
    };
}
