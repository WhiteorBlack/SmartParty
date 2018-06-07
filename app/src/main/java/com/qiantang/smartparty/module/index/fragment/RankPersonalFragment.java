package com.qiantang.smartparty.module.index.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentRankBinding;
import com.qiantang.smartparty.databinding.FragmentRankPersonalBinding;
import com.qiantang.smartparty.module.index.adapter.RankAdapter;
import com.qiantang.smartparty.module.index.adapter.RankPersonalAdapter;
import com.qiantang.smartparty.module.index.viewmodel.RankPersonalViewModel;
import com.qiantang.smartparty.module.index.viewmodel.RankViewModel;

/**
 * Created by zhaoyong bai on 2018/5/28.
 */
public class RankPersonalFragment extends BaseBindFragment {
    private RankPersonalAdapter adapter;
    private RankPersonalViewModel viewModel;
    private FragmentRankPersonalBinding binding;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        adapter = new RankPersonalAdapter(R.layout.item_rank_personal);
        viewModel = new RankPersonalViewModel(this, adapter);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rank_personal, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        initRv(binding.rv);
    }

    private void initRv(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
