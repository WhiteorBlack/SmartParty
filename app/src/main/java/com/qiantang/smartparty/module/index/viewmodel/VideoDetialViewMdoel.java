package com.qiantang.smartparty.module.index.viewmodel;

import android.content.Intent;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.base.ViewModel;
import com.qiantang.smartparty.modle.RxComment;
import com.qiantang.smartparty.module.index.adapter.VideoDetialAdapter;
import com.qiantang.smartparty.module.index.view.VideoStudyDetialActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/5/25.
 */
public class VideoDetialViewMdoel implements ViewModel {
    private BaseBindActivity activity;
    private VideoDetialAdapter adapter;

    public VideoDetialViewMdoel(BaseBindActivity activity, VideoDetialAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }

    public void initData() {
        Intent intent = activity.getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        String id = intent.getStringExtra("id");
        ((VideoStudyDetialActivity) activity).startVideo(url, title);
    }

    public void testData(){
        List<RxComment> comments=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            comments.add(new RxComment());
        }
        adapter.setNewData(comments);
    }

    public void loadMore(){

    }

    @Override
    public void destroy() {

    }
}
