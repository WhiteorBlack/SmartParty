package com.qiantang.smartparty.modle;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by zhaoyong bai on 2018/6/20.
 */
public class RxPartyPos implements IPickerViewData {
    public RxPartyPos(String name, int code) {
        this.name = name;
        this.code = code;
    }

    private String name;
    private int code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getPickerViewText() {
        return name;
    }
}
