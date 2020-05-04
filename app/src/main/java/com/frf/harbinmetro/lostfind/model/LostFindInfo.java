package com.frf.harbinmetro.lostfind.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LostFindInfo implements Parcelable {
    private String id;//失物招领编号
    private String title;//失物招领标题
    private String feature;//失物招领特征
    private String address;//失物招领领取地
    private String dateTime;//失物招领发布日期
    private String publisher;//失物招领发布者
    private String state;//失物招领状态
    private String imageurl;//图片url

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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public static final Parcelable.Creator<LostFindInfo> CREATOR=new Creator<LostFindInfo>() {

        @Override
        public LostFindInfo createFromParcel(Parcel source) {
            LostFindInfo bean=new LostFindInfo();

            bean.id=source.readString();
            bean.title=source.readString();
            bean.feature=source.readString();
            bean.address=source.readString();
            bean.dateTime=source.readString();
            bean.publisher=source.readString();
            bean.state=source.readString();
            bean.imageurl=source.readString();

            return bean;
        }

        @Override
        public LostFindInfo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new LostFindInfo[size];
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
        dest.writeString(feature);
        dest.writeString(address);
        dest.writeString(dateTime);
        dest.writeString(publisher);
        dest.writeString(state);
        dest.writeString(imageurl);
    }
}
