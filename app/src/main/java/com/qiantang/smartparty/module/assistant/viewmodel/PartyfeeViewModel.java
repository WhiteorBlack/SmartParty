package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxPartyFee;
import com.qiantang.smartparty.module.assistant.adapter.PartyFeeAdapter;
import com.qiantang.smartparty.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class PartyfeeViewModel implements ViewModel {
    private BaseBindActivity activity;
    private PartyFeeAdapter adapter;

    public PartyfeeViewModel(BaseBindActivity activity, PartyFeeAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void testData() {
        List<RxPartyFee> partyFees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            partyFees.add(new RxPartyFee());
        }
        adapter.setNewData(partyFees);
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActivityUtil.startFeeDetialActivity(activity, "");
            }
        };
    }

    @Override
    public void destroy() {

    }
}
