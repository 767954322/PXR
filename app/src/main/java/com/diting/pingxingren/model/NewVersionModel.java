package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 09, 15:47.
 * Description: .
 */

public class NewVersionModel implements Parcelable {

    private String version;
    private int versionCode;
    private boolean enforce;
    private String description;
    private String apkUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public boolean isEnforce() {
        return enforce;
    }

    public void setEnforce(boolean enforce) {
        this.enforce = enforce;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.version);
        dest.writeInt(this.versionCode);
        dest.writeByte(this.enforce ? (byte) 1 : (byte) 0);
        dest.writeString(this.description);
        dest.writeString(this.apkUrl);
    }

    public NewVersionModel() {
    }

    protected NewVersionModel(Parcel in) {
        this.version = in.readString();
        this.versionCode = in.readInt();
        this.enforce = in.readByte() != 0;
        this.description = in.readString();
        this.apkUrl = in.readString();
    }

    public static final Parcelable.Creator<NewVersionModel> CREATOR = new Parcelable.Creator<NewVersionModel>() {
        @Override
        public NewVersionModel createFromParcel(Parcel source) {
            return new NewVersionModel(source);
        }

        @Override
        public NewVersionModel[] newArray(int size) {
            return new NewVersionModel[size];
        }
    };
}
