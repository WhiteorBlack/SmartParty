package com.qiantang.smartparty.modle;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class RxStudyZan {
    private String username;
    private String id;
    private String user_id;

    public RxStudyZan(String username, String id, String user_id) {
        this.username = username;
        this.id = id;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
