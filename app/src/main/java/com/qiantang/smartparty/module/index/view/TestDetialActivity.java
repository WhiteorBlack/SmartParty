package com.qiantang.smartparty.module.index.view;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityTestDetialBinding;
import com.qiantang.smartparty.module.index.adapter.TestDetialAdapter;
import com.qiantang.smartparty.module.index.viewmodel.TestDetialViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;
import com.qiantang.smartparty.widget.NoScorllLinealayout;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestDetialActivity extends BaseBindActivity {
    private ActivityTestDetialBinding binding;
    private TestDetialAdapter adapter;
    private TestDetialViewModel viewModel;

    @Override
    protected void initBind() {
        adapter = new TestDetialAdapter(R.layout.item_test_detial);
        viewModel = new TestDetialViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_test_detial);
        binding.setViewModel(viewModel);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initView() {
        initRv(binding.rv);
       binding.progress.setEnabled(false);
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new NoScorllLinealayout(this,LinearLayoutManager.HORIZONTAL,false));
        rv.setAdapter(adapter);
        rv.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.btn_confirm:

                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }
}
