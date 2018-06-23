package com.qiantang.smartparty.modle;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qiantang.smartparty.module.assistant.adapter.StructureAdapter;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class RxStructurePerson implements MultiItemEntity {
    private String avatar;
    private String username;
    private String dept;

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int getItemType() {
        return StructureAdapter.TYPE_PERSON;
    }
}
