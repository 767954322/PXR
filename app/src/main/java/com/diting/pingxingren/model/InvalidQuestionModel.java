package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 无效问题
 * Created by Administrator on 2017/12/16.
 */

public class InvalidQuestionModel implements Parcelable{


    private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;
    private List<InvalidQuestionItemModel> items;

    public InvalidQuestionModel() {
    }

    protected InvalidQuestionModel(Parcel in) {
        totalPage = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        totalRecord = in.readInt();
        total = in.readInt();
    }

    public static final Creator<InvalidQuestionModel> CREATOR = new Creator<InvalidQuestionModel>() {
        @Override
        public InvalidQuestionModel createFromParcel(Parcel in) {
            return new InvalidQuestionModel(in);
        }

        @Override
        public InvalidQuestionModel[] newArray(int size) {
            return new InvalidQuestionModel[size];
        }
    };

    public List<InvalidQuestionItemModel> getItems() {
        return items;
    }

    public void setItems(List<InvalidQuestionItemModel> items) {
        this.items = items;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalPage);
        dest.writeInt(pageNo);
        dest.writeInt(pageSize);
        dest.writeInt(totalRecord);
        dest.writeInt(total);
    }
}
