package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**修改备注
 * Created by Administrator on 2017/12/12.
 */

public class UpdateRemarkBody implements Parcelable{

    private String own_phone;// 关注列表点击进入时=登录用户uniqueid，粉丝列表点击进入时=机器人uniqueid
    private String  oth_phone ;//关注列表点击进入时=机器人uniqueid，粉丝列表点击进入时=登录用户uniqueid
    private String remarks ;//  备注


    public UpdateRemarkBody() {
    }

    protected UpdateRemarkBody(Parcel in) {
        own_phone = in.readString();
        oth_phone = in.readString();
        remarks = in.readString();
    }

    public static final Creator<UpdateRemarkBody> CREATOR = new Creator<UpdateRemarkBody>() {
        @Override
        public UpdateRemarkBody createFromParcel(Parcel in) {
            return new UpdateRemarkBody(in);
        }

        @Override
        public UpdateRemarkBody[] newArray(int size) {
            return new UpdateRemarkBody[size];
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(own_phone);
        dest.writeString(oth_phone);
        dest.writeString(remarks);
    }
}
