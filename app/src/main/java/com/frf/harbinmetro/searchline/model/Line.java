package com.frf.harbinmetro.searchline.model;

import java.io.Serializable;

public class Line implements Serializable {
    private String lineid;
    private String startstation;
    private String finishstation;
    private String ticketprice;
    private String passstation;
    private String lineurl;

    public String getLineid() {
        return lineid;
    }

    public void setLineid(String lineid) {
        this.lineid = lineid;
    }

    public String getStartstation() {
        return startstation;
    }

    public void setStartstation(String startstation) {
        this.startstation = startstation;
    }

    public String getFinishstation() {
        return finishstation;
    }

    public void setFinishstation(String finishstation) {
        this.finishstation = finishstation;
    }

    public String getTicketprice() {
        return ticketprice;
    }

    public void setTicketprice(String ticketprice) {
        this.ticketprice = ticketprice;
    }

    public String getPassstation() {
        return passstation;
    }

    public void setPassstation(String passstation) {
        this.passstation = passstation;
    }

    public String getLineurl() {
        return lineurl;
    }

    public void setLineurl(String lineurl) {
        this.lineurl = lineurl;
    }
}
