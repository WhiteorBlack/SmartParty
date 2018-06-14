package com.qiantang.smartparty.modle;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class RxTestDetial {
    private String subject_id;
    private String questionnaire_id;
    private int grade;
    private int type;
    private String content;
    private List<RxQuestion> option;

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getQuestionnaire_id() {
        return questionnaire_id;
    }

    public void setQuestionnaire_id(String questionnaire_id) {
        this.questionnaire_id = questionnaire_id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<RxQuestion> getOption() {
        return option;
    }

    public void setOption(List<RxQuestion> option) {
        this.option = option;
    }
}
