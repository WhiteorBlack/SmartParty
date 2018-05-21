package com.qiantang.smartparty.module.study.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentStudyBinding;
import com.qiantang.smartparty.module.study.viewmodel.StudyViewModel;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class StudyFragment extends BaseBindFragment {
    private FragmentStudyBinding binding;
    private StudyViewModel viewModel;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study, container, false);
        viewModel = new StudyViewModel(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("学习感悟");
        binding.toolbar.setIsTextHide(false);
        binding.toolbar.setIsHide(true);
        binding.toolbar.tvPublish.setOnClickListener(this::onClick);
        initRecycleView(binding.rv);
        initRefresh(binding.cptr);
    }

    @Override
    public void refreshData() {
        super.refreshData();
    }

    private void initRecycleView(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_publish:

                break;
        }
    }


    @Override
    protected void viewModelDestroy() {

    }
}
