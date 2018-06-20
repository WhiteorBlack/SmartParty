package com.qiantang.smartparty.modle;

import com.bigkoo.pickerview.model.IPickerViewData;

/**
 * Created by zhaoyong bai on 2018/6/20.
 */
public class RxDateDay implements IPickerViewData{
    private int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String getPickerViewText() {
        return day+"日";
    }
}
