package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityRecycleviewBinding;
import com.qiantang.smartparty.module.assistant.adapter.ActivityAdapter;
import com.qiantang.smartparty.module.mine.adapter.MyActivityAdapter;
import com.qiantang.smartparty.module.mine.viewmodel.MyActivityViewModel;

/**
 * Created by zhaoyong bai on 2018/5/30.
 * 我的活动
 */
public class MyActivity extends BaseBindActivity {
    private MyActivityViewModel viewModel;
    private MyActivityAdapter adapter;
    private ActivityRecycleviewBinding binding;

    @Override
    protected void initBind() {
        adapter=new MyActivityAdapter(R.layout.item_my_activity);
        viewModel = new MyActivityViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycleview);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("我的活动");
        binding.toolbar.setIsHide(true);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
