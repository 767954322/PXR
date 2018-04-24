package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/12/12.
 */

public class FollowListBody implements Parcelable{
    private String own_phone;// 登录用户uniqueid

    public FollowListBody() {
    }

    public String getOwn_phone() {
        return own_phone;
    }

    public void setOwn_phone(String own_phone) {
        this.own_phone = own_phone;
    }

    public FollowListBody(Parcel in) {
        own_phone = in.readString();
    }

    public static final Creator<FollowListBody> CREATOR = new Creator<FollowListBody>() {
        @Override
        public FollowListBody createFromParcel(Parcel in) {
            return new FollowListBody(in);
        }

        @Override
        public FollowListBody[] newArray(int size) {
            return new FollowListBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(own_phone);
    }
}
