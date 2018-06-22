package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityTestRecordBinding;
import com.qiantang.smartparty.module.index.adapter.TestRecordAdapter;
import com.qiantang.smartparty.module.index.viewmodel.TestRecordViewModel;
import com.qiantang.smartparty.widget.NoScorllLinealayout;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestRecordActivity extends BaseBindActivity {
    private ActivityTestRecordBinding binding;
    private TestRecordAdapter adapter;
    private TestRecordViewModel viewModel;

    @Override
    protected void initBind() {
        adapter = new TestRecordAdapter(R.layout.item_test_record);
        viewModel = new TestRecordViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_record);
        binding.setViewModel(viewModel);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initView() {
        initRv(binding.rv);
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new NoScorllLinealayout(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_confirm:
                viewModel.next(binding.rv);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
