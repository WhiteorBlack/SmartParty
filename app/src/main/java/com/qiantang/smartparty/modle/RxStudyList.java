package com.qiantang.smartparty.modle;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class RxStudyList implements MultiItemEntity{
    private String image;
    private String user_id;
    private List<RxStudyZan> zanAppMap;
    private String avatar;
    private String creationtime;
    private String username;
    private String comment_id;
    private String content;
    private List<RxStudyComment> commentAppMap;
    private int type;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<RxStudyZan> getZanAppMap() {
        return zanAppMap;
    }

    public void setZanAppMap(List<RxStudyZan> zanAppMap) {
        this.zanAppMap = zanAppMap;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreationtime() {
        return creationtime;
    }

    public void setCreationtime(String creationtime) {
        this.creationtime = creationtime;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<RxStudyComment> getCommentAppMap() {
        return commentAppMap;
    }

    public void setCommentAppMap(List<RxStudyComment> commentAppMap) {
        this.commentAppMap = commentAppMap;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
