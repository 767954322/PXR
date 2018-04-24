package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 27, 11:40.
 * Description: .
 */

public class ThirdCheckBindModel  implements Parcelable{

    /**
     * qq : true
     * weixin : true
     * weibo : false
     */

    private boolean qq;
    private boolean weixin;
    private boolean weibo;

    protected ThirdCheckBindModel(Parcel in) {
        qq = in.readByte() != 0;
        weixin = in.readByte() != 0;
        weibo = in.readByte() != 0;
    }

    public static final Creator<ThirdCheckBindModel> CREATOR = new Creator<ThirdCheckBindModel>() {
        @Override
        public ThirdCheckBindModel createFromParcel(Parcel in) {
            return new ThirdCheckBindModel(in);
        }

        @Override
        public ThirdCheckBindModel[] newArray(int size) {
            return new ThirdCheckBindModel[size];
        }
    };

    public boolean isQq() {
        return qq;
    }

    public void setQq(boolean qq) {
        this.qq = qq;
    }

    public boolean isWeixin() {
        return weixin;
    }

    public void setWeixin(boolean weixin) {
        this.weixin = weixin;
    }

    public boolean isWeibo() {
        return weibo;
    }

    public void setWeibo(boolean weibo) {
        this.weibo = weibo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (qq ? 1 : 0));
        parcel.writeByte((byte) (weixin ? 1 : 0));
        parcel.writeByte((byte) (weibo ? 1 : 0));
    }
}
