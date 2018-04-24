package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 15, 16:41.
 * Description: .
 */

public class QueryCommonLanguageBody implements Parcelable {

    private String user_id;
    private String shiro;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShiro() {
        return shiro;
    }

    public void setShiro(String shiro) {
        this.shiro = shiro;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.shiro);
    }

    public QueryCommonLanguageBody() {
    }

    protected QueryCommonLanguageBody(Parcel in) {
        this.user_id = in.readString();
        this.shiro = in.readString();
    }

    public static final Creator<QueryCommonLanguageBody> CREATOR = new Creator<QueryCommonLanguageBody>() {
        @Override
        public QueryCommonLanguageBody createFromParcel(Parcel source) {
            return new QueryCommonLanguageBody(source);
        }

        @Override
        public QueryCommonLanguageBody[] newArray(int size) {
            return new QueryCommonLanguageBody[size];
        }
    };
}
