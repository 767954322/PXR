package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 26, 14:33.
 * Description: .
 */

public class CheckBindThirdBody implements Parcelable {


    /**
     * userName : 13552240682
     */

    private String userName;

    public CheckBindThirdBody() {
    }

    protected CheckBindThirdBody(Parcel in) {
        userName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CheckBindThirdBody> CREATOR = new Creator<CheckBindThirdBody>() {
        @Override
        public CheckBindThirdBody createFromParcel(Parcel in) {
            return new CheckBindThirdBody(in);
        }

        @Override
        public CheckBindThirdBody[] newArray(int size) {
            return new CheckBindThirdBody[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
