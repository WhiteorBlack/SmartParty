package com.qiantang.smartparty.module.search.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.config.Event;
import com.qiantang.smartparty.databinding.FragmentRecycleviewBinding;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;
import com.qiantang.smartparty.module.search.viewmodel.FcNoticeViewModel;
import com.qiantang.smartparty.utils.RecycleViewUtils;

/**
 * Created by zhaoyong bai on 2018/7/10.
 * 使用fcNotice接口的搜索页面
 */
public class FcNoticeFragment extends BaseBindFragment {
    private FragmentRecycleviewBinding binding;
    private FcNoticeViewModel viewModel;
    private IndexCommonAdapter adapter;
    private int type;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycleview, container, false);
        type = getArguments().getInt("type");
        viewModel = new FcNoticeViewModel(this, type);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        int resId = 0;
        switch (type) {
            case Event.SEARCH_CHARACTER:
                resId = R.layout.item_character;
                break;
            case Event.SEARCH_MEETING:
                resId = R.layout.item_meeting_record;
                break;
            case Event.SEARCH_NEWS:
                resId = R.layout.item_news;
                break;
            case Event.SEARCH_MIEN:
                resId = R.layout.item_study_state;
                break;
            case Event.SEARCH_STUDY_STATE:
                resId = R.layout.item_news;
                break;
        }
        adapter = new IndexCommonAdapter(resId);
        viewModel.setAdapter(adapter);
        initRv(binding.rv);
    }


    private void initRv(RecyclerView rv) {
        adapter.setEnableLoadMore(true);
        adapter.setLoadMoreView(RecycleViewUtils.getLoadMoreView());
        rv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(viewModel.onItemTouchListener());
        adapter.setOnLoadMoreListener(() -> viewModel.onLoadMore(), rv);
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
