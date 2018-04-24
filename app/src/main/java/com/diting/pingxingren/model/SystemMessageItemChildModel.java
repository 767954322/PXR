package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/12/14.
 */

public class SystemMessageItemChildModel implements Parcelable {

    private String updatedTime;
    private String phone;
    private String deleted;
    private String ownerType;
    private String ids;
    private String createdTime;
    private String createdBy;
    private String updatedBy;
    private String owner;
    private String id;
    private String flg;

    public String getFlg() {
        return flg;
    }

    public void setFlg(String flg) {
        this.flg = flg;
    }

    protected SystemMessageItemChildModel(Parcel in) {
        updatedTime = in.readString();
        phone = in.readString();
        deleted = in.readString();
        ownerType = in.readString();
        ids = in.readString();
        createdTime = in.readString();
        createdBy = in.readString();
        updatedBy = in.readString();
        owner = in.readString();
        id = in.readString();
        flg = in.readString();
    }

    public static final Creator<SystemMessageItemChildModel> CREATOR = new Creator<SystemMessageItemChildModel>() {
        @Override
        public SystemMessageItemChildModel createFromParcel(Parcel in) {
            return new SystemMessageItemChildModel(in);
        }

        @Override
        public SystemMessageItemChildModel[] newArray(int size) {
            return new SystemMessageItemChildModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedTime);
        dest.writeString(phone);
        dest.writeString(deleted);
        dest.writeString(ownerType);
        dest.writeString(ids);
        dest.writeString(createdTime);
        dest.writeString(createdBy);
        dest.writeString(updatedBy);
        dest.writeString(owner);
        dest.writeString(id);
        dest.writeString(flg);
    }
}
