package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 添加取消关注
 * Created by Administrator on 2017/12/12.
 */

public class AddOrCancelFollowBody implements Parcelable{

   private String  own_phone;// 登录用户uniqueid
  private String   oth_phone ;//关注用户uniqueid

    public AddOrCancelFollowBody() {
    }

    protected AddOrCancelFollowBody(Parcel in) {
        own_phone = in.readString();
        oth_phone = in.readString();
    }

    public static final Creator<AddOrCancelFollowBody> CREATOR = new Creator<AddOrCancelFollowBody>() {
        @Override
        public AddOrCancelFollowBody createFromParcel(Parcel in) {
            return new AddOrCancelFollowBody(in);
        }

        @Override
        public AddOrCancelFollowBody[] newArray(int size) {
            return new AddOrCancelFollowBody[size];
        }
    };

    public String getOwn_phone() {
        return own_phone;
    }

    public void setOwn_phone(String own_phone) {
        this.own_phone = own_phone;
    }

    public String getOth_phone() {
        return oth_phone;
    }

    public void setOth_phone(String oth_phone) {
        this.oth_phone = oth_phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(own_phone);
        dest.writeString(oth_phone);
    }
}
