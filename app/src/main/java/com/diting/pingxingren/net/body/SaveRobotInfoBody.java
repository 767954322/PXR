package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017/9/11, 11:30.
 * Description: .
 */

public class SaveRobotInfoBody implements Parcelable {

    private String name;
    private String companyName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.companyName);
    }

    public SaveRobotInfoBody() {
    }

    protected SaveRobotInfoBody(Parcel in) {
        this.name = in.readString();
        this.companyName = in.readString();
    }

    public static final Creator<SaveRobotInfoBody> CREATOR = new Creator<SaveRobotInfoBody>() {
        @Override
        public SaveRobotInfoBody createFromParcel(Parcel source) {
            return new SaveRobotInfoBody(source);
        }

        @Override
        public SaveRobotInfoBody[] newArray(int size) {
            return new SaveRobotInfoBody[size];
        }
    };
}
