package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 11, 10:40.
 * Description: .
 */

public class UnreadMailModel implements Parcelable {

    private int unreadLetterNum;
    private int unreadMessageNum;
    private int flg_count;
    private int flg;

    public int getUnreadLetterNum() {
        return unreadLetterNum;
    }

    public void setUnreadLetterNum(int unreadLetterNum) {
        this.unreadLetterNum = unreadLetterNum;
    }

    public int getUnreadMessageNum() {
        return unreadMessageNum;
    }

    public void setUnreadMessageNum(int unreadMessageNum) {
        this.unreadMessageNum = unreadMessageNum;
    }

    public int getFlg_count() {
        return flg_count;
    }

    public void setFlg_count(int flg_count) {
        this.flg_count = flg_count;
    }

    public int getFlg() {
        return flg;
    }

    public void setFlg(int flg) {
        this.flg = flg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.unreadLetterNum);
        dest.writeInt(this.unreadMessageNum);
        dest.writeInt(this.flg_count);
        dest.writeInt(this.flg);
    }

    public UnreadMailModel() {
    }

    protected UnreadMailModel(Parcel in) {
        this.unreadLetterNum = in.readInt();
        this.unreadMessageNum = in.readInt();
        this.flg_count = in.readInt();
        this.flg = in.readInt();
    }

    public static final Parcelable.Creator<UnreadMailModel> CREATOR = new Parcelable.Creator<UnreadMailModel>() {
        @Override
        public UnreadMailModel createFromParcel(Parcel source) {
            return new UnreadMailModel(source);
        }

        @Override
        public UnreadMailModel[] newArray(int size) {
            return new UnreadMailModel[size];
        }
    };

    @Override
    public String toString() {
        return "UnreadMailModel{" +
                "unreadLetterNum=" + unreadLetterNum +
                ", unreadMessageNum=" + unreadMessageNum +
                ", flg_count=" + flg_count +
                ", flg=" + flg +
                '}';
    }
}
