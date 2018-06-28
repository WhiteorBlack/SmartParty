package com.qiantang.smartparty.module.mine.view;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityNamePositionBinding;
import com.qiantang.smartparty.module.mine.viewmodel.NamePositionViewModel;

public class NamePositionActivity extends BaseBindActivity {

    private ActivityNamePositionBinding binding;
    private NamePositionViewModel viewModel;
    private int type = 0;

    @Override
    protected void initBind() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_name_position);
        viewModel = new NamePositionViewModel(this);
        binding.setViewModel(viewModel);
    }

    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        String name = getIntent().getStringExtra("info");
        viewModel.info.set(name);
        if (type == 0) {
            binding.toolbar.setTitle("修改名称");
        } else {
            binding.toolbar.setTitle("修改职位");
        }
        binding.toolbar.setRight("保存");
    }

    public void onClick(View view) {
        closeInput();
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_right:
                if (type == 0) {
                    viewModel.modifyName();
                } else {
                    viewModel.modifyNick();
                }
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {
        if (viewModel != null) viewModel.destroy();
    }
}
