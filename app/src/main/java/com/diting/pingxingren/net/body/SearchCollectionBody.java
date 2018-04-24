package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 收藏列表
 * Created by Administrator on 2018/1/9.
 */

public class SearchCollectionBody implements Parcelable {
    private String onlineuniqueid;
    private String sort;

    public SearchCollectionBody() {
    }

    protected SearchCollectionBody(Parcel in) {
        onlineuniqueid = in.readString();
        sort = in.readString();
    }

    public static final Creator<SearchCollectionBody> CREATOR = new Creator<SearchCollectionBody>() {
        @Override
        public SearchCollectionBody createFromParcel(Parcel in) {
            return new SearchCollectionBody(in);
        }

        @Override
        public SearchCollectionBody[] newArray(int size) {
            return new SearchCollectionBody[size];
        }
    };

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOnlineuniqueid() {
        return onlineuniqueid;
    }

    public void setOnlineuniqueid(String onlineuniqueid) {
        this.onlineuniqueid = onlineuniqueid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(onlineuniqueid);
        dest.writeString(sort);
    }
}
