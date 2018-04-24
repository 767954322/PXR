package com.diting.pingxingren.smarteditor.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 01, 23:57.
 * Description: .
 */

public class ArticleResultModel implements Parcelable {

    private String robotname;
    private String updatedtime;
    private String title;
    private List<ArticleContentModel> editortion;

    public String getRobotname() {
        return robotname;
    }

    public void setRobotname(String robotname) {
        this.robotname = robotname;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ArticleContentModel> getEditortion() {
        return editortion;
    }

    public void setEditortion(List<ArticleContentModel> editortion) {
        this.editortion = editortion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.robotname);
        dest.writeString(this.updatedtime);
        dest.writeString(this.title);
        dest.writeTypedList(this.editortion);
    }

    public ArticleResultModel() {
    }

    protected ArticleResultModel(Parcel in) {
        this.robotname = in.readString();
        this.updatedtime = in.readString();
        this.title = in.readString();
        this.editortion = in.createTypedArrayList(ArticleContentModel.CREATOR);
    }

    public static final Parcelable.Creator<ArticleResultModel> CREATOR = new Parcelable.Creator<ArticleResultModel>() {
        @Override
        public ArticleResultModel createFromParcel(Parcel source) {
            return new ArticleResultModel(source);
        }

        @Override
        public ArticleResultModel[] newArray(int size) {
            return new ArticleResultModel[size];
        }
    };
}
