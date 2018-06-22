package com.qiantang.smartparty.module.spalsh.view;

import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.qiantang.smartparty.BaseBindActivity;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.ActivityGuideBinding;
import com.qiantang.smartparty.module.spalsh.adapter.GuideAdapter;
import com.qiantang.smartparty.module.spalsh.viewmodel.GuideViewModel;

import java.util.ArrayList;

/**
 * Created by zhaoyong bai on 2018/6/22.
 */
public class GuideActivity extends BaseBindActivity implements ViewPager.OnPageChangeListener {
    private ActivityGuideBinding binding;
    private GuideViewModel viewModel;
    private GuideAdapter adapter;

    @Override
    protected void initBind() {
        adapter=new GuideAdapter(new ArrayList<>());
        viewModel = new GuideViewModel(this,adapter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guide);
        binding.setViewModel(viewModel);
    }

    @Override
    public void initView() {
        binding.viewpager.setOnPageChangeListener(this);
        binding.viewpager.setAdapter(adapter);
        viewModel.setData();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_get_in:
                viewModel.jumpNextPage();
                break;
        }
    }

    @Override
    protected void viewModelDestroy() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewModel.setPostion(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
