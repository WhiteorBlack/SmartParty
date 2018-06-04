package com.qiantang.smartparty.module.mine.viewmodel;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.module.assistant.adapter.ActivityAdapter;
import com.qiantang.smartparty.module.mine.adapter.MyActivityAdapter;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/30.
 */
public class MyActivityViewModel implements ViewModel {
    private BaseBindActivity activity;
    private MyActivityAdapter adapter;

    public MyActivityViewModel(BaseBindActivity activity, MyActivityAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public RecyclerView.OnItemTouchListener onItemTouchListener() {
        return new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.tv_check:
                        ActivityUtil.startActivityDetialActivity(activity, "");
                        break;
                    case R.id.tv_del:

                        break;
                }
            }
        };
    }

    @Override
    public void destroy() {

    }
}
