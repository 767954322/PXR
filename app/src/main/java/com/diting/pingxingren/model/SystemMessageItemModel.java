package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class SystemMessageItemModel implements Parcelable {
    private String updatedTime;
    private String zhengwen;
    private boolean deleted;
    private String ownerType;
    private String biaoti;
    private long createdTime;
    private String createdBy;
    private String updatedBy;
    private String owner;
    private int id;
    private List<SystemMessageItemChildModel> mail_phone;
    private boolean idRead;

    protected SystemMessageItemModel(Parcel in) {
        updatedTime = in.readString();
        zhengwen = in.readString();
        deleted = in.readByte() != 0;
        ownerType = in.readString();
        biaoti = in.readString();
        createdTime = in.readLong();
        createdBy = in.readString();
        updatedBy = in.readString();
        owner = in.readString();
        id = in.readInt();
        mail_phone = in.createTypedArrayList(SystemMessageItemChildModel.CREATOR);
        idRead = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(updatedTime);
        dest.writeString(zhengwen);
        dest.writeByte((byte) (deleted ? 1 : 0));
        dest.writeString(ownerType);
        dest.writeString(biaoti);
        dest.writeLong(createdTime);
        dest.writeString(createdBy);
        dest.writeString(updatedBy);
        dest.writeString(owner);
        dest.writeInt(id);
        dest.writeTypedList(mail_phone);
        dest.writeByte((byte) (idRead ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SystemMessageItemModel> CREATOR = new Creator<SystemMessageItemModel>() {
        @Override
        public SystemMessageItemModel createFromParcel(Parcel in) {
            return new SystemMessageItemModel(in);
        }

        @Override
        public SystemMessageItemModel[] newArray(int size) {
            return new SystemMessageItemModel[size];
        }
    };

    public boolean isIdRead() {
        return idRead;
    }

    public void setIdRead(boolean idRead) {
        this.idRead = idRead;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getZhengwen() {
        return zhengwen;
    }

    public void setZhengwen(String zhengwen) {
        this.zhengwen = zhengwen;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getBiaoti() {
        return biaoti;
    }

    public void setBiaoti(String biaoti) {
        this.biaoti = biaoti;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<SystemMessageItemChildModel> getMail_phone() {
        return mail_phone;
    }

    public void setMail_phone(List<SystemMessageItemChildModel> mail_phone) {
        this.mail_phone = mail_phone;
    }
}
