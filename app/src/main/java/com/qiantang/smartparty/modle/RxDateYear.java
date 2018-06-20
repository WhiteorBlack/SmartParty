package com.qiantang.smartparty.modle;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/20.
 */
public class RxDateYear implements IPickerViewData {
    private int year;
    private List<RxDateMonth> month;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<RxDateMonth> getMonth() {
        return month;
    }

    public void setMonth(List<RxDateMonth> month) {
        this.month = month;
    }

    @Override
    public String getPickerViewText() {
        return year + "年";
    }
}
