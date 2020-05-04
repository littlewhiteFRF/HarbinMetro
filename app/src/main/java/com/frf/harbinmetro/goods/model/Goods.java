package com.frf.harbinmetro.goods.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.frf.harbinmetro.lostfind.model.LostFindInfo;
import com.frf.harbinmetro.searchline.model.Line;

import java.io.Serializable;

public class Goods extends Line implements Serializable {
    private String goodsid;//订单id
    private String ofuserid;//所属乘客id
    private String lineid;//线路id
    private String datetime;//日期时间
    private String state;//订单状态

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getOfuserid() {
        return ofuserid;
    }

    public void setOfuserid(String ofuserid) {
        this.ofuserid = ofuserid;
    }

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
