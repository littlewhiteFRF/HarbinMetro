package com.frf.harbinmetro.contact.model;

import java.io.Serializable;

public class QuestionInfo implements Serializable {
    private String id;//问题编号
    private String questionTitle;//问题标题
    private String answer;//问题答案
    private int resId;//图片ID

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
