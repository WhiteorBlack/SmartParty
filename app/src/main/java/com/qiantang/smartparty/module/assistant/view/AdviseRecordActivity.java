package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityAdviseRecordBinding;
import com.qiantang.smartparty.module.assistant.adapter.AdviseRecordAdapter;
import com.qiantang.smartparty.module.assistant.viewmodel.AdviseRecordViewModel;

/**
 * Created by zhaoyong bai on 2018/6/1.
 */
public class AdviseRecordActivity extends BaseBindActivity {
    private ActivityAdviseRecordBinding binding;
    private AdviseRecordAdapter adviseRecordAdapter;
    private AdviseRecordViewModel viewModel;

    @Override
    protected void initBind() {
        adviseRecordAdapter = new AdviseRecordAdapter(R.layout.item_advise_record);
        viewModel = new AdviseRecordViewModel(this, adviseRecordAdapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_advise_record);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("反馈记录");
        initRv(binding.rv);
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adviseRecordAdapter);
        viewModel.getData();
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
