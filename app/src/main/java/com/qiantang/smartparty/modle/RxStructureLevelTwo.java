package com.qiantang.smartparty.modle;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qiantang.smartparty.module.assistant.adapter.StructureAdapter;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class RxStructureLevelTwo extends AbstractExpandableItem<RxStructurePerson> implements MultiItemEntity {
    private String dept_name;
    private String dept_id;
    private int count;

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public String getDept_id() {
        return dept_id;
    }

    public void setDept_id(String dept_id) {
        this.dept_id = dept_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getLevel() {
        return StructureAdapter.TYPE_LEVEL_1;
    }

    @Override
    public int getItemType() {
        return StructureAdapter.TYPE_LEVEL_1;
    }
}
