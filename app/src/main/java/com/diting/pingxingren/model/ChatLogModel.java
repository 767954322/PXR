package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.diting.pingxingren.entity.ChatMessageItem;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16.
 */

public class ChatLogModel implements Parcelable{


    private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;
    private List<ChatLogItemModel> items;

    public ChatLogModel() {
    }

    protected ChatLogModel(Parcel in) {
        totalPage = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        totalRecord = in.readInt();
        total = in.readInt();
        items = in.createTypedArrayList(ChatLogItemModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalPage);
        dest.writeInt(pageNo);
        dest.writeInt(pageSize);
        dest.writeInt(totalRecord);
        dest.writeInt(total);
        dest.writeTypedList(items);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatLogModel> CREATOR = new Creator<ChatLogModel>() {
        @Override
        public ChatLogModel createFromParcel(Parcel in) {
            return new ChatLogModel(in);
        }

        @Override
        public ChatLogModel[] newArray(int size) {
            return new ChatLogModel[size];
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

    public List<ChatLogItemModel> getItems() {
        return items;
    }

    public void setItems(List<ChatLogItemModel> items) {
        this.items = items;
    }
}
