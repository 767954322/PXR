package com.diting.voice.data.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/8/18.
 */

public class ContactBody implements Parcelable {
    private String accountList;

    protected ContactBody(Parcel in) {
        accountList = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accountList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactBody> CREATOR = new Creator<ContactBody>() {
        @Override
        public ContactBody createFromParcel(Parcel in) {
            return new ContactBody(in);
        }

        @Override
        public ContactBody[] newArray(int size) {
            return new ContactBody[size];
        }
    };

    public String getAccountList() {
        return accountList;
    }

    public void setAccountList(String accountList) {
        this.accountList = accountList;
    }

}
