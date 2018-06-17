package com.qiantang.smartparty.module.assistant.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxStructureLevelOne;
import com.qiantang.smartparty.modle.RxStructureLevelTwo;
import com.qiantang.smartparty.module.assistant.adapter.StructureAdapter;
import com.qiantang.smartparty.network.NetworkSubscriber;
import com.qiantang.smartparty.network.retrofit.ApiWrapper;
import com.qiantang.smartparty.utils.ToastUtil;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class StructureViewModel implements ViewModel {
    private StructureAdapter adapter;
    private BaseBindActivity activity;
    private List<MultiItemEntity> dataList = new ArrayList<>();
    private boolean isDealing=false;

    public StructureViewModel(StructureAdapter adapter, BaseBindActivity activity) {
        this.adapter = adapter;
        this.activity = activity;
        getOneData();
    }

    private void getOneData() {
        ApiWrapper.getInstance().dept1()
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new NetworkSubscriber<List<RxStructureLevelOne>>() {
                    @Override
                    public void onSuccess(List<RxStructureLevelOne> data) {
                        dataList.addAll(data);
                        adapter.setNewData(dataList);
                    }
                });
    }

    private void getTwoData(String id,int pos) {
        isDealing=true;
        ApiWrapper.getInstance().dept2(id)
                .compose(activity.bindUntilEvent(ActivityEvent.DESTROY))
                .doOnTerminate(()->isDealing=false)
                .subscribe(new NetworkSubscriber<List<RxStructureLevelTwo>>() {
                    @Override
                    public void onSuccess(List<RxStructureLevelTwo> data) {
                        ((RxStructureLevelOne)dataList.get(pos)).setSubItems(data);
                        adapter.notifyDataSetChanged();
                        adapter.expand(pos);
                    }
                });
    }

    private void getPerson(String id) {

    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                ToastUtil.toast("position"+position);
                switch (view.getId()) {
                    case R.id.iv_level_one:
                        RxStructureLevelOne rxStructureLevelOne = (RxStructureLevelOne) dataList.get(position);
                        if (rxStructureLevelOne.getSubItems() == null || rxStructureLevelOne.getSubItems().size() == 0) {
                            getTwoData(rxStructureLevelOne.getDept_id(),position);
                        }
                        break;
                    case R.id.iv_level_two:

                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {

    }
}
