package com.qiantang.smartparty.module.assistant.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.adapter.CommentAdapter;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class ActivityDetialViewModel implements ViewModel {
    private BaseBindActivity activity;
    private CommentAdapter adapter;
    public ObservableBoolean isInput=new ObservableBoolean(false);

    public ActivityDetialViewModel(BaseBindActivity activity,CommentAdapter adapter) {
        this.activity = activity;
        this.adapter=adapter;
    }

    public void testData(){
        List<RxComment> comments=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            comments.add(new RxComment());
        }
        adapter.setNewData(comments);
    }
    public RecyclerView.OnItemTouchListener onItemTouchListener(){
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
