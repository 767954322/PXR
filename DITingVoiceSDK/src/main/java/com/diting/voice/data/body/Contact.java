package com.diting.voice.data.body;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by asus on 2017/3/13.
 */

public class Contact implements Parcelable{
    private String  name;
    private String phone;
    private String indexTag;
    private int isUploaded;

    public Contact(String name, String phone ,int isUploaded) {
        this.name = name;
        this.phone = phone;
        this.isUploaded = isUploaded;
    }

    protected Contact(Parcel in) {
        name = in.readString();
        phone = in.readString();
        indexTag = in.readString();
        isUploaded = in.readInt();
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    public int getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(int uploaded) {
        isUploaded = uploaded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(indexTag);
        dest.writeInt(isUploaded);
    }
}
