package com.qiantang.smartparty.modle;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by zhaoyong bai on 2018/6/20.
 */
public class RxDeptName implements IPickerViewData {
    private String dept_name;
    private String dept_id;

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

    @Override
    public String getPickerViewText() {
        return dept_name;
    }
}
