package com.qiantang.smartparty.modle;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/20.
 */
public class RxDateMonth implements IPickerViewData{
    private int month;
    private List<RxDateDay> dateDays;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public List<RxDateDay> getDateDays() {
        return dateDays;
    }

    public void setDateDays(List<RxDateDay> dateDays) {
        this.dateDays = dateDays;
    }

    @Override
    public String getPickerViewText() {
        return month+"月";
    }
}
