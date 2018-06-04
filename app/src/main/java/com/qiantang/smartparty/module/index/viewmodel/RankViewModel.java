package com.qiantang.smartparty.module.index.viewmodel;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxRank;
import com.qiantang.smartparty.module.index.adapter.RankAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RankViewModel implements ViewModel {
    private BaseBindActivity activity;
    private BaseBindFragment fragment;
    private RankAdapter adapter;

    public RankViewModel(BaseBindFragment fragment, RankAdapter adapter) {
        this.fragment = fragment;
        this.adapter = adapter;
    }

    public RankViewModel(BaseBindActivity activity) {
        this.activity = activity;
    }

    public void testData() {
        List<RxRank> ranks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ranks.add(new RxRank());
        }
        adapter.setNewData(ranks);
    }

    @Override
    public void destroy() {

    }
}
