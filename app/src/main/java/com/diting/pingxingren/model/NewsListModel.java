package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 2018 on 2018/1/9.
 */

public class NewsListModel implements Parcelable {

    /**
     * updatedTime : null
     * title : 国家最高科技奖获得者王泽山:业内称其为“火药王”
     * deleted : false
     * imageUrl : http://05.imgmini.eastday.com/mobile/20180108/20180108110051_3227608c986c23d38c9275ab8f710aa5_3_mwpm_03200403.jpg
     * ownerType : null
     * createdTime : null
     * url : http://mini.eastday.com/mobile/180108110051042.html
     * createdBy : null
     * updatedBy : null
     * owner : null
     * type : top
     * id : null
     */

    private Object updatedTime;
    private String title;
    private boolean deleted;
    private String imageUrl;
    private Object ownerType;
    private Object createdTime;
    private String url;
    private Object createdBy;
    private Object updatedBy;
    private Object owner;
    private String type;
    private Object id;
    private String authorName;

    @Override
    public String toString() {
        return "NewsListModel{" +
                "title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", url='" + url + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    protected NewsListModel(Parcel in) {
    }

    public static final Creator<NewsListModel> CREATOR = new Creator<NewsListModel>() {
        @Override
        public NewsListModel createFromParcel(Parcel in) {
            return new NewsListModel(in);
        }

        @Override
        public NewsListModel[] newArray(int size) {
            return new NewsListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Object getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Object updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Object ownerType) {
        this.ownerType = ownerType;
    }

    public Object getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Object createdTime) {
        this.createdTime = createdTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }


}
