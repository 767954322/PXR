package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的活动分享
 * Created by Administrator on 2018/1/9.
 */

public class LuckyShareBody implements Parcelable{
    private  String mobile;
    private String account_id;
    private int id;



    public LuckyShareBody() {
    }

    protected LuckyShareBody(Parcel in) {
        mobile = in.readString();
        account_id = in.readString();
        id = in.readInt();
    }

    public static final Creator<LuckyShareBody> CREATOR = new Creator<LuckyShareBody>() {
        @Override
        public LuckyShareBody createFromParcel(Parcel in) {
            return new LuckyShareBody(in);
        }

        @Override
        public LuckyShareBody[] newArray(int size) {
            return new LuckyShareBody[size];
        }
    };

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mobile);
        parcel.writeString(account_id);
        parcel.writeInt(id);
    }
}
