package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxFeeRecord;
import com.qiantang.smartparty.module.assistant.adapter.FeeRecordAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class FeeRecordViewModel implements ViewModel {
    private BaseBindActivity activity;
    private FeeRecordAdapter adapter;

    public FeeRecordViewModel(BaseBindActivity activity, FeeRecordAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        };
    }

    @Override
    public void destroy() {

    }

    public void testData() {
        List<RxFeeRecord> records = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            records.add(new RxFeeRecord());
        }
        adapter.setNewData(records);
    }
}
