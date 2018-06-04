package com.qiantang.smartparty.module.assistant.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxAdviseRecord;
import com.qiantang.smartparty.module.assistant.adapter.AdviseRecordAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/1.
 */
public class AdviseRecordViewModel implements ViewModel {
    private BaseBindActivity activity;
    private AdviseRecordAdapter adviseRecordAdapter;

    public AdviseRecordViewModel(BaseBindActivity activity, AdviseRecordAdapter adviseRecordAdapter) {
        this.activity = activity;
        this.adviseRecordAdapter = adviseRecordAdapter;
    }

    public void onLoadMore(){

    }

    @Override
    public void destroy() {

    }

    public void testData() {
        List<RxAdviseRecord> records=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            records.add(new RxAdviseRecord());
        }
        adviseRecordAdapter.setNewData(records);
    }
}
