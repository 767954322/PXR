package com.diting.pingxingren.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 会问的列表
 * Created by Administrator on 2018/1/10.
 */

public class AskListModel implements Parcelable{

    private String question;//问答的问题
    private String id;//问答id
    private AskRobotModel askrobot;//为null时为没有添加过
    private boolean isAskChange;

    protected AskListModel(Parcel in) {
        question = in.readString();
        id = in.readString();
        askrobot = in.readParcelable(AskRobotModel.class.getClassLoader());
        isAskChange = in.readByte() != 0;
    }

    @Override
    public String toString() {
        return "AskListModel{" +
                "question='" + question + '\'' +
                ", id='" + id + '\'' +
                ", askrobot=" + askrobot +
                ", isAskChange=" + isAskChange +
                '}';
    }

    public static final Creator<AskListModel> CREATOR = new Creator<AskListModel>() {
        @Override
        public AskListModel createFromParcel(Parcel in) {
            return new AskListModel(in);
        }

        @Override
        public AskListModel[] newArray(int size) {
            return new AskListModel[size];
        }
    };

    public boolean isAskChange() {
        return isAskChange;
    }

    public void setAskChange(boolean askChange) {
        isAskChange = askChange;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AskRobotModel getAskrobot() {
        return askrobot;
    }

    public void setAskrobot(AskRobotModel askrobot) {
        this.askrobot = askrobot;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(id);
        parcel.writeParcelable(askrobot, i);
        parcel.writeByte((byte) (isAskChange ? 1 : 0));
    }
}
