package com.diting.pingxingren.smarteditor.net.body;

import android.os.Parcel;
import android.os.Parcelable;

import com.diting.pingxingren.smarteditor.model.ArticleContentModel;
import com.diting.pingxingren.smarteditor.model.ArticleIdModel;
import com.diting.pingxingren.smarteditor.model.ContentModel;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2018 - 02 - 01, 21:13.
 * Description: .
 */

public class SaveArticleContentBody implements Parcelable {

    private List<ContentModel> save;
    private List<ArticleIdModel> delete;
    private List<ArticleContentModel> update;

    public List<ContentModel> getSave() {
        return save;
    }

    public void setSave(List<ContentModel> save) {
        this.save = save;
    }

    public List<ArticleIdModel> getDelete() {
        return delete;
    }

    public void setDelete(List<ArticleIdModel> delete) {
        this.delete = delete;
    }

    public List<ArticleContentModel> getUpdate() {
        return update;
    }

    public void setUpdate(List<ArticleContentModel> update) {
        this.update = update;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.save);
        dest.writeTypedList(this.delete);
        dest.writeTypedList(this.update);
    }

    public SaveArticleContentBody() {
    }

    protected SaveArticleContentBody(Parcel in) {
        this.save = in.createTypedArrayList(ContentModel.CREATOR);
        this.delete = in.createTypedArrayList(ArticleIdModel.CREATOR);
        this.update = in.createTypedArrayList(ArticleContentModel.CREATOR);
    }

    public static final Parcelable.Creator<SaveArticleContentBody> CREATOR = new Parcelable.Creator<SaveArticleContentBody>() {
        @Override
        public SaveArticleContentBody createFromParcel(Parcel source) {
            return new SaveArticleContentBody(source);
        }

        @Override
        public SaveArticleContentBody[] newArray(int size) {
            return new SaveArticleContentBody[size];
        }
    };
}
