package com.diting.pingxingren.smarteditor.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 编辑页面数据
 * Created by 2018 on 2018/1/11.
 */

public class EditListModel implements Parcelable {

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_PICTURE = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_SOUND = 1;

    private int type;//类型
    private String obj;//内容
    private String addTime;//添加时间
    private String path;//路劲
    private Object thumbnail;//缩略内容(除文字外)

    private String editor_id; // 文章id
    private String id; // 内容id

    public Object getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Object thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEditor_id() {
        return editor_id;
    }

    public void setEditor_id(String editor_id) {
        this.editor_id = editor_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EditListModel(int type, String obj) {
        this.type = type;
        this.obj = obj;
    }

    public EditListModel(int type, String obj, String addTime) {
        this.type = type;
        this.obj = obj;
        this.addTime = addTime;
        if (this.type == 2 || this.type == 3) {
            this.path = this.obj;
        }
    }

    @Override
    public String toString() {
        return "EditListModel{" +
                "type=" + type +
                ", obj=" + obj +
                ", addTime='" + addTime + '\'' +
                ", path='" + path + '\'' +
                ", thumbnail=" + thumbnail +
                ", editor_id='" + editor_id + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public EditListModel(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.obj);
        dest.writeString(this.addTime);
        dest.writeString(this.path);
        dest.writeParcelable((Parcelable) this.thumbnail, flags);
        dest.writeString(this.editor_id);
        dest.writeString(this.id);
    }

    protected EditListModel(Parcel in) {
        this.type = in.readInt();
        this.obj = in.readString();
        this.addTime = in.readString();
        this.path = in.readString();
        this.thumbnail = in.readParcelable(Object.class.getClassLoader());
        this.editor_id = in.readString();
        this.id = in.readString();
    }

    public static final Parcelable.Creator<EditListModel> CREATOR = new Parcelable.Creator<EditListModel>() {
        @Override
        public EditListModel createFromParcel(Parcel source) {
            return new EditListModel(source);
        }

        @Override
        public EditListModel[] newArray(int size) {
            return new EditListModel[size];
        }
    };
}
