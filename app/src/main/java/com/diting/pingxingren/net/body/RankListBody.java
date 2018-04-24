package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/12/13.
 */

public class RankListBody implements Parcelable{
    private String own_phone;

    protected RankListBody(Parcel in) {
        own_phone = in.readString();
    }

    public static final Creator<RankListBody> CREATOR = new Creator<RankListBody>() {
        @Override
        public RankListBody createFromParcel(Parcel in) {
            return new RankListBody(in);
        }

        @Override
        public RankListBody[] newArray(int size) {
            return new RankListBody[size];
        }
    };

    public String getOwn_phone() {
        return own_phone;
    }

    public void setOwn_phone(String own_phone) {
        this.own_phone = own_phone;
    }

    public RankListBody() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(own_phone);
    }
}
