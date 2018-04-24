package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Wang HaoLan.
 * Date: 2018 - 01 - 25, 17:32.
 * Description: .
 */

public class ThirdBindModel implements Parcelable{
    private int flag;
    private ThirdAccountModel account;

    @Override
    public String toString() {
        return "ThirdBindModel{" +
                "flag=" + flag +
                ", account=" + account +
                '}';
    }

    public ThirdBindModel() {
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public ThirdAccountModel getAccount() {
        return account;
    }

    public void setAccount(ThirdAccountModel account) {
        this.account = account;
    }

    protected ThirdBindModel(Parcel in) {
        flag = in.readInt();
        account = in.readParcelable(ThirdAccountModel.class.getClassLoader());
    }

    public static final Creator<ThirdBindModel> CREATOR = new Creator<ThirdBindModel>() {
        @Override
        public ThirdBindModel createFromParcel(Parcel in) {
            return new ThirdBindModel(in);
        }

        @Override
        public ThirdBindModel[] newArray(int size) {
            return new ThirdBindModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(flag);
        parcel.writeParcelable(account, i);
    }
}
