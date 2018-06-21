package com.qiantang.smartparty.module.login.view;

import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.WindowManager;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityCompeteInfoBinding;
import com.qiantang.smartparty.module.login.viewmodel.CompeteViewModel;

/**
 * Created by zhaoyong bai on 2018/6/4.
 */
public class CompeteInfoActivity extends BaseBindActivity {
    private CompeteViewModel viewModel;
    private ActivityCompeteInfoBinding binding;

    @Override
    protected void initBind() {
        viewModel = new CompeteViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compete_info);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
//        mImmersionBar.keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).init();
        binding.toolbar.setTitle("完善信息");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_arrow:
            case R.id.tv_nation:
                viewModel.nation();
                break;
            case R.id.iv_arrow_org:
            case R.id.tv_org:
                viewModel.orgPop();
                break;
            case R.id.iv_arrow_id:
            case R.id.tv_id:
                viewModel.idPop();
                break;
            case R.id.iv_arrow_date:
            case R.id.tv_date:
                viewModel.datePop();
                break;
            case R.id.btn_confirm:
                viewModel.commit();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
