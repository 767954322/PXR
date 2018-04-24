package com.diting.pingxingren.net.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 编辑未知问题
 * Created by Administrator on 2017/12/16.
 */

public class InvalidQuestionEditBody implements Parcelable{
  private   String question;
    private String answer;

    public InvalidQuestionEditBody() {
    }

    protected InvalidQuestionEditBody(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    public static final Creator<InvalidQuestionEditBody> CREATOR = new Creator<InvalidQuestionEditBody>() {
        @Override
        public InvalidQuestionEditBody createFromParcel(Parcel in) {
            return new InvalidQuestionEditBody(in);
        }

        @Override
        public InvalidQuestionEditBody[] newArray(int size) {
            return new InvalidQuestionEditBody[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }
}
