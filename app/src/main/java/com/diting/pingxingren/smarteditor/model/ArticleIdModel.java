package com.diting.pingxingren.smarteditor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 22, 16:20.
 * Description: .
 */

public class ArticleIdModel implements Parcelable {

    private String id;

    public ArticleIdModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
    }

    public ArticleIdModel() {
    }

    protected ArticleIdModel(Parcel in) {
        this.id = in.readString();
    }

    public static final Parcelable.Creator<ArticleIdModel> CREATOR = new Parcelable.Creator<ArticleIdModel>() {
        @Override
        public ArticleIdModel createFromParcel(Parcel source) {
            return new ArticleIdModel(source);
        }

        @Override
        public ArticleIdModel[] newArray(int size) {
            return new ArticleIdModel[size];
        }
    };
}
