package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityFeeDetialBinding;
import com.qiantang.smartparty.databinding.ActivityFeeDetialSpecialBinding;
import com.qiantang.smartparty.module.assistant.viewmodel.FeeDetialSpecialViewModel;
import com.qiantang.smartparty.module.assistant.viewmodel.FeeDetialViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/29.
 */
public class FeeDetialSpecialActivity extends BaseBindActivity {
    private FeeDetialSpecialViewModel viewModel;
    private ActivityFeeDetialSpecialBinding binding;

    @Override
    protected void initBind() {
        viewModel = new FeeDetialSpecialViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fee_detial_special);
        binding.setViewModel(viewModel);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:

                break;
            case R.id.tv_right:
                ActivityUtil.startFeeRecordActivity(this);
                break;
        }
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("党费缴纳");
        binding.toolbar.setRight("缴费记录");
        binding.toolbar.setIsHide(false);
        initRv(binding.rvInfo);
    }

    private void initRv(RecyclerView rvInfo) {
        rvInfo.setLayoutManager(new GridLayoutManager(this, 4));
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
