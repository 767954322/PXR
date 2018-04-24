package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 11, 17:14.
 * Description: .
 */

public class CommonFeaturesBody implements Parcelable {

    private List<Integer> openIds;
    private List<Integer> closedIds;

    public List<Integer> getOpenIds() {
        return openIds;
    }

    public void setOpenIds(List<Integer> openIds) {
        this.openIds = openIds;
    }

    public List<Integer> getClosedIds() {
        return closedIds;
    }

    public void setClosedIds(List<Integer> closedIds) {
        this.closedIds = closedIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.openIds);
        dest.writeList(this.closedIds);
    }

    public CommonFeaturesBody() {
    }

    protected CommonFeaturesBody(Parcel in) {
        this.openIds = new ArrayList<Integer>();
        in.readList(this.openIds, Integer.class.getClassLoader());
        this.closedIds = new ArrayList<Integer>();
        in.readList(this.closedIds, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<CommonFeaturesBody> CREATOR = new Parcelable.Creator<CommonFeaturesBody>() {
        @Override
        public CommonFeaturesBody createFromParcel(Parcel source) {
            return new CommonFeaturesBody(source);
        }

        @Override
        public CommonFeaturesBody[] newArray(int size) {
            return new CommonFeaturesBody[size];
        }
    };
}
