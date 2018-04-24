package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 11, 13:53.
 * Description: 公司信息.
 */

public class CompanyInfoBody implements Parcelable {

    private String name;
    private String headPortrait;

    @Override
    public String toString() {
        return "CompanyInfoBody{" +
                "name='" + name + '\'' +
                ", headPortrait='" + headPortrait + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.headPortrait);
    }

    public CompanyInfoBody() {
    }

    protected CompanyInfoBody(Parcel in) {
        this.name = in.readString();
        this.headPortrait = in.readString();
    }

    public static final Parcelable.Creator<CompanyInfoBody> CREATOR = new Parcelable.Creator<CompanyInfoBody>() {
        @Override
        public CompanyInfoBody createFromParcel(Parcel source) {
            return new CompanyInfoBody(source);
        }

        @Override
        public CompanyInfoBody[] newArray(int size) {
            return new CompanyInfoBody[size];
        }
    };
}
