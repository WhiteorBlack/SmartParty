package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityInfoBinding;
import com.qiantang.smartparty.module.mine.viewmodel.InfoViewModel;
import com.qiantang.smartparty.utils.ActivityUtil;

/**
 * Created by zhaoyong bai on 2018/5/22.
 */
public class InfoActivity extends BaseBindActivity {
    private InfoViewModel viewModel;
    private ActivityInfoBinding binding;

    @Override
    protected void initBind() {
        viewModel = new InfoViewModel(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_info);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("个人档案");
        binding.toolbar.setIsHide(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_avatar:
                ActivityUtil.startChangeAvatarActivity(this, viewModel.getUserInfo().getAvatar());
                break;
            case R.id.ll_name:
                ActivityUtil.startNamePosActivity(this, 0, viewModel.getUserInfo().getUsername());
                break;
            case R.id.ll_position:
                ActivityUtil.startNamePosActivity(this, 1, viewModel.getUserInfo().getPosition());
                break;
            case R.id.ll_org:
                ActivityUtil.startDateDeptActivity(this, 0, viewModel.getUserInfo().getDept().getDeptName());
                break;
            case R.id.ll_date:
                ActivityUtil.startDateDeptActivity(this, 1, viewModel.getUserInfo().getJoinpatryTime());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.initData();
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
