package com.frf.harbinmetro.myinfo.model;

public class MyInfoSelect {
    private String myinfokey;
    private String myinfovalue;

    public String getMyinfokey() {
        return myinfokey;
    }

    public void setMyinfokey(String myinfokey) {
        this.myinfokey = myinfokey;
    }

    public String getMyinfovalue() {
        return myinfovalue;
    }

    public void setMyinfovalue(String myinfovalue) {
        this.myinfovalue = myinfovalue;
    }

    public MyInfoSelect(String myinfokey, String myinfovalue) {
        this.myinfokey = myinfokey;
        this.myinfovalue = myinfovalue;
    }
}
