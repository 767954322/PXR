package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**会问列表
 * Created by Administrator on 2018/1/15.
 */

public class AskModel implements Parcelable{
        private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;
    private List<AskListModel> items;

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

    public List<AskListModel> getItems() {
        return items;
    }

    public void setItems(List<AskListModel> items) {
        this.items = items;
    }

    protected AskModel(Parcel in) {
        totalPage = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        totalRecord = in.readInt();
        total = in.readInt();
        items = in.createTypedArrayList(AskListModel.CREATOR);
    }

    public static final Creator<AskModel> CREATOR = new Creator<AskModel>() {
        @Override
        public AskModel createFromParcel(Parcel in) {
            return new AskModel(in);
        }

        @Override
        public AskModel[] newArray(int size) {
            return new AskModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(totalPage);
        parcel.writeInt(pageNo);
        parcel.writeInt(pageSize);
        parcel.writeInt(totalRecord);
        parcel.writeInt(total);
        parcel.writeTypedList(items);
    }
}
