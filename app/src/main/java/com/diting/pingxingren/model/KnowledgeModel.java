package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Creator: Gu FanFan.
 * Date: 2017 - 12 - 13, 13:51.
 * Description: .
 */

public class KnowledgeModel implements Parcelable {

    private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;
    private List<KnowledgeItemModel> items;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<KnowledgeItemModel> getItems() {
        return items;
    }

    public void setItems(List<KnowledgeItemModel> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalPage);
        dest.writeInt(this.pageNo);
        dest.writeInt(this.pageSize);
        dest.writeInt(this.totalRecord);
        dest.writeInt(this.total);
        dest.writeTypedList(this.items);
    }

    public KnowledgeModel() {
    }

    protected KnowledgeModel(Parcel in) {
        this.totalPage = in.readInt();
        this.pageNo = in.readInt();
        this.pageSize = in.readInt();
        this.totalRecord = in.readInt();
        this.total = in.readInt();
        this.items = in.createTypedArrayList(KnowledgeItemModel.CREATOR);
    }

    public static final Parcelable.Creator<KnowledgeModel> CREATOR = new Parcelable.Creator<KnowledgeModel>() {
        @Override
        public KnowledgeModel createFromParcel(Parcel source) {
            return new KnowledgeModel(source);
        }

        @Override
        public KnowledgeModel[] newArray(int size) {
            return new KnowledgeModel[size];
        }
    };
}
