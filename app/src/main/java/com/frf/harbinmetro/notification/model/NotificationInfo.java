package com.frf.harbinmetro.notification.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.frf.harbinmetro.lostfind.model.LostFindInfo;

public class NotificationInfo implements Parcelable {
    private String id;//通知编号
    private String title;//通知标题
    private String content;//通知内容
    private String dateTime;//通知发布日期
    private String publisher;//发布者

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public static final Parcelable.Creator<NotificationInfo> CREATOR=new Creator<NotificationInfo>() {

        @Override
        public NotificationInfo createFromParcel(Parcel source) {
            NotificationInfo bean = new NotificationInfo();

            bean.id = source.readString();
            bean.title = source.readString();
            bean.content = source.readString();
            bean.dateTime = source.readString();
            bean.publisher = source.readString();

            return bean;
        }

        @Override
        public NotificationInfo[] newArray(int size) {
            return new NotificationInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(dateTime);
        dest.writeString(publisher);
    }
}
