package com.frf.harbinmetro.messageboard.model;

import java.io.Serializable;

public class Explain implements Serializable {
    private String explainid;
    private String content;
    private String answer;
    private String ofuserid;
    private String datetime;
    private String state;

    public String getExplainid() {
        return explainid;
    }
    public void setExplainid(String explainid) {
        this.explainid = explainid;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getAnswer() {
        return answer;
    }
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public String getOfuserid() {
        return ofuserid;
    }
    public void setOfuserid(String ofuserid) {
        this.ofuserid = ofuserid;
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
