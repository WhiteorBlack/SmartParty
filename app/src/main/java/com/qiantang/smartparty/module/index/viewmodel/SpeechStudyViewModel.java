package com.qiantang.smartparty.module.index.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxVideoStudy;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.index.adapter.VideoStudyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class SpeechStudyViewModel implements ViewModel {
    private BaseBindActivity activity;
    private IndexCommonAdapter adapter;


    public SpeechStudyViewModel(BaseBindActivity activity, IndexCommonAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void onLoadMore() {

    }

    public void testData() {
        List<RxVideoStudy> monthScores = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            RxVideoStudy rxVideoStudy = new RxVideoStudy();
            rxVideoStudy.setPicUrl("http://pic.qiantucdn.com/58pic/14/78/41/77358PICZaq_1024.jpg");
            rxVideoStudy.setVideoUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
            monthScores.add(rxVideoStudy);
        }
        adapter.setNewData(monthScores);
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
}
