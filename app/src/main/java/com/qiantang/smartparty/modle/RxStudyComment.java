package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class RxStudyComment {
    private String username;
    private String content;
    private String user_id;
    private String id;

    public RxStudyComment(String username, String content, String user_id) {
        this.username = username;
        this.content = content;
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
