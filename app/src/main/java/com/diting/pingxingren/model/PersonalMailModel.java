package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 私信
 * Created by Administrator on 2017/12/15.
 */

public class PersonalMailModel implements Parcelable{

    private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;
    private List<PersonalMailItemModel> items;






    public PersonalMailModel() {
    }

    protected PersonalMailModel(Parcel in) {
        totalPage = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        totalRecord = in.readInt();
        total = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalPage);
        dest.writeInt(pageNo);
        dest.writeInt(pageSize);
        dest.writeInt(totalRecord);
        dest.writeInt(total);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonalMailModel> CREATOR = new Creator<PersonalMailModel>() {
        @Override
        public PersonalMailModel createFromParcel(Parcel in) {
            return new PersonalMailModel(in);
        }

        @Override
        public PersonalMailModel[] newArray(int size) {
            return new PersonalMailModel[size];
        }
    };

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

    public List<PersonalMailItemModel> getItems() {
        return items;
    }

    public void setItems(List<PersonalMailItemModel> items) {
        this.items = items;
    }
}
