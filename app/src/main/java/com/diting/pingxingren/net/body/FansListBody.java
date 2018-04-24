package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 沟通粉丝列表
 * Created by Administrator on 2017/12/12.
 */

public class FansListBody implements Parcelable{
    private String oth_phone;// 登录用户uniqueid


    protected FansListBody(Parcel in) {
        oth_phone = in.readString();
    }

    public FansListBody() {
    }

    public static final Creator<FansListBody> CREATOR = new Creator<FansListBody>() {
        @Override
        public FansListBody createFromParcel(Parcel in) {
            return new FansListBody(in);
        }

        @Override
        public FansListBody[] newArray(int size) {
            return new FansListBody[size];
        }
    };

    public String getOth_phone() {
        return oth_phone;
    }

    public void setOth_phone(String oth_phone) {
        this.oth_phone = oth_phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(oth_phone);
    }
}
