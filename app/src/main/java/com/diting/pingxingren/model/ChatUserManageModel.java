package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 访客管理
 * Created by Administrator on 2017/12/14.
 */

public class ChatUserManageModel implements Parcelable{

    private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;
    private List<ChatUserManageItemModel> items;

    protected ChatUserManageModel(Parcel in) {
        totalPage = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        totalRecord = in.readInt();
        total = in.readInt();
    }

    public static final Creator<ChatUserManageModel> CREATOR = new Creator<ChatUserManageModel>() {
        @Override
        public ChatUserManageModel createFromParcel(Parcel in) {
            return new ChatUserManageModel(in);
        }

        @Override
        public ChatUserManageModel[] newArray(int size) {
            return new ChatUserManageModel[size];
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

    public List<ChatUserManageItemModel> getItems() {
        return items;
    }

    public void setItems(List<ChatUserManageItemModel> items) {
        this.items = items;
    }

    public ChatUserManageModel() {
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
