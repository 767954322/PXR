package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 系统消息状态
 * Created by Administrator on 2017/12/15.
 */

public class SystemIsReadBody implements Parcelable{
    private int ids;


    public SystemIsReadBody() {
    }

    protected SystemIsReadBody(Parcel in) {
        ids = in.readInt();
    }

    public static final Creator<SystemIsReadBody> CREATOR = new Creator<SystemIsReadBody>() {
        @Override
        public SystemIsReadBody createFromParcel(Parcel in) {
            return new SystemIsReadBody(in);
        }

        @Override
        public SystemIsReadBody[] newArray(int size) {
            return new SystemIsReadBody[size];
        }
    };

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ids);
    }
}
