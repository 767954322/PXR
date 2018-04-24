package com.diting.pingxingren.smarteditor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 01 - 29, 15:31.
 * Description: .
 */

public class ContentModel implements Parcelable {

    private String content;
    private String contentindex;
    private String contenttype;
    private String editor_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentindex() {
        return Integer.valueOf(contentindex);
    }

    public void setContentindex(int contentindex) {
        this.contentindex = String.valueOf(contentindex);
    }

    public int getContenttype() {
        return Integer.valueOf(contenttype);
    }

    public void setContenttype(int contenttype) {
        this.contenttype = String.valueOf(contenttype);
    }

    public int getEditor_id() {
        return Integer.valueOf(editor_id);
    }

    public void setEditor_id(int editor_id) {
        this.editor_id = String.valueOf(editor_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.contentindex);
        dest.writeString(this.contenttype);
        dest.writeString(this.editor_id);
    }

    public ContentModel() {
    }

    @Override
    public String toString() {
        return "ContentModel{" +
                "content='" + content + '\'' +
                ", contentindex='" + contentindex + '\'' +
                ", contenttype='" + contenttype + '\'' +
                ", editor_id='" + editor_id + '\'' +
                '}';
    }

    protected ContentModel(Parcel in) {
        this.content = in.readString();
        this.contentindex = in.readString();
        this.contenttype = in.readString();
        this.editor_id = in.readString();
    }

    public static final Parcelable.Creator<ContentModel> CREATOR = new Parcelable.Creator<ContentModel>() {
        @Override
        public ContentModel createFromParcel(Parcel source) {
            return new ContentModel(source);
        }

        @Override
        public ContentModel[] newArray(int size) {
            return new ContentModel[size];
        }
    };
}
