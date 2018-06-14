package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class RxQuestion {
    private String content;
    private boolean answer;
    private String option_id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }
}
