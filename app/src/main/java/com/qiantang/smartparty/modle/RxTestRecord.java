package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/15.
 */
public class RxTestRecord {
    private boolean granswer;
    private boolean answer;
    private String option_id;
    private String content;
    private int pos;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isGranswer() {
        return granswer;
    }

    public void setGranswer(boolean granswer) {
        this.granswer = granswer;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
