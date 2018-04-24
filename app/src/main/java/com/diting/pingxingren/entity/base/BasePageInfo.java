package com.diting.pingxingren.entity.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2017/9/21.
 */

public class BasePageInfo implements Parcelable{
    private int totalPage;
    private int pageNo;
    private int pageSize;
    private int totalRecord;
    private int total;

    protected BasePageInfo(Parcel in) {
        totalPage = in.readInt();
        pageNo = in.readInt();
        pageSize = in.readInt();
        totalRecord = in.readInt();
        total = in.readInt();;
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


    public static final Creator<BasePageInfo> CREATOR = new Creator<BasePageInfo>() {
        @Override
        public BasePageInfo createFromParcel(Parcel in) {
            return new BasePageInfo(in);
        }

        @Override
        public BasePageInfo[] newArray(int size) {
            return new BasePageInfo[size];
        }
    };

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
