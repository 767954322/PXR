package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 私信item
 * Created by Administrator on 2017/12/15.
 */

public class PersonalMailItemModel implements Parcelable{

    private String forword_unique_id;
    private String receive_name;
    private String flag;
    private String createdBy;

    private String forword_name;
    private String message;
    private int id;

    private long createdTime;

    private boolean idRead;

    public boolean isIdRead() {
        return idRead;
    }

    public void setIdRead(boolean idRead) {
        this.idRead = idRead;
    }

    protected PersonalMailItemModel(Parcel in) {
        forword_unique_id = in.readString();
        receive_name = in.readString();
        flag = in.readString();
        createdBy = in.readString();
        forword_name = in.readString();
        message = in.readString();
        id = in.readInt();
        createdTime = in.readLong();
        idRead = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(forword_unique_id);
        dest.writeString(receive_name);
        dest.writeString(flag);
        dest.writeString(createdBy);
        dest.writeString(forword_name);
        dest.writeString(message);
        dest.writeInt(id);
        dest.writeLong(createdTime);
        dest.writeByte((byte) (idRead ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonalMailItemModel> CREATOR = new Creator<PersonalMailItemModel>() {
        @Override
        public PersonalMailItemModel createFromParcel(Parcel in) {
            return new PersonalMailItemModel(in);
        }

        @Override
        public PersonalMailItemModel[] newArray(int size) {
            return new PersonalMailItemModel[size];
        }
    };

    public String getForword_unique_id() {
        return forword_unique_id;
    }

    public void setForword_unique_id(String forword_unique_id) {
        this.forword_unique_id = forword_unique_id;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getForword_name() {
        return forword_name;
    }

    public void setForword_name(String forword_name) {
        this.forword_name = forword_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "PersonalMailItemModel{" +
                "forword_unique_id='" + forword_unique_id + '\'' +
                ", receive_name='" + receive_name + '\'' +
                ", flag='" + flag + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", forword_name='" + forword_name + '\'' +
                ", message='" + message + '\'' +
                ", id=" + id +
                ", createdTime=" + createdTime +
                ", idRead=" + idRead +
                '}';
    }
}
