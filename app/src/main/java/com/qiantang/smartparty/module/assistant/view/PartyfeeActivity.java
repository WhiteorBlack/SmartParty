package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityPartyFeeBinding;
import com.qiantang.smartparty.module.assistant.adapter.PartyFeeAdapter;
import com.qiantang.smartparty.module.assistant.viewmodel.PartyfeeViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;


/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class PartyfeeActivity extends BaseBindActivity {
    private PartyfeeViewModel viewModel;
    private PartyFeeAdapter adapter;
    private ActivityPartyFeeBinding binding;

    @Override
    protected void initBind() {
        adapter = new PartyFeeAdapter(R.layout.item_party_fee);
        viewModel = new PartyfeeViewModel(this, adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_party_fee);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("党费缴纳");
        binding.toolbar.setRight("缴费记录");
        binding.toolbar.setIsHide(false);
        initRv(binding.rv);
        viewModel.testData();
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                ActivityUtil.startFeeRecordActivity(this);
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
