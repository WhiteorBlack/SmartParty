package com.qiantang.smartparty.module.assistant.view;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qiantang.smartparty.BaseBindFragment;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.databinding.FragmentAssisantBinding;
import com.qiantang.smartparty.module.assistant.adapter.ActivityAdapter;
import com.qiantang.smartparty.module.assistant.adapter.MsgAdapter;
import com.qiantang.smartparty.module.assistant.viewmodel.AssisantViewModel;
import com.qiantang.smartparty.module.index.adapter.ClassAdapter;
import com.qiantang.smartparty.module.index.adapter.IndexCommonAdapter;

/**
 * Created by zhaoyong bai on 2018/5/21.
 */
public class AssisantFragment extends BaseBindFragment {
    private FragmentAssisantBinding binding;
    private AssisantViewModel viewModel;
    private MsgAdapter msgAdapter;
    private ActivityAdapter activityAdapter;
    private IndexCommonAdapter stateAdapter;

    @Override
    public View initBinding(LayoutInflater inflater, ViewGroup container) {
        viewModel = new AssisantViewModel(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_assisant, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void initView() {
        binding.toolbar.setTitle("党建助手");
        binding.toolbar.setIsHide(true);
        binding.toolbar.setIsTextHide(true);
        initClassRv(binding.rvClass);
        initMsgRv(binding.rvMsg);
        initActivityRv(binding.rvActivity);
        initStateRv(binding.rvState);
    }

    /**
     * 党建风采
     *
     * @param rvState
     */
    private void initStateRv(RecyclerView rvState) {
        stateAdapter=new IndexCommonAdapter(R.layout.item_study_state);
        rvState.addOnItemTouchListener(viewModel.stateToucnListener());
        rvState.setNestedScrollingEnabled(false);
        rvState.setLayoutManager(new LinearLayoutManager(getContext()));
        rvState.setAdapter(stateAdapter);
        viewModel.getStateData(stateAdapter);
    }

    /**
     * 党建活动
     *
     * @param rvActivity
     */
    private void initActivityRv(RecyclerView rvActivity) {
        activityAdapter=new ActivityAdapter(R.layout.item_activity);
        rvActivity.addOnItemTouchListener(viewModel.activityToucnListener());
        rvActivity.setNestedScrollingEnabled(false);
        rvActivity.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActivity.setAdapter(activityAdapter);
        viewModel.getActivityData(activityAdapter);
    }

    /**
     * 消息
     *
     * @param rvMsg
     */
    private void initMsgRv(RecyclerView rvMsg) {
        msgAdapter = new MsgAdapter(R.layout.item_msg);
        rvMsg.addOnItemTouchListener(viewModel.msgToucnListener());
        rvMsg.setNestedScrollingEnabled(false);
        rvMsg.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMsg.setAdapter(msgAdapter);
        viewModel.getMsgData(msgAdapter);
    }

    /**
     * 类别
     *
     * @param rvClass
     */
    private void initClassRv(RecyclerView rvClass) {
        rvClass.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvClass.setAdapter(new ClassAdapter(R.layout.item_index_class, viewModel.getClassData()));
        rvClass.setNestedScrollingEnabled(false);
        rvClass.addOnItemTouchListener(viewModel.classToucnListener());
    }

    @Override
    protected void viewModelDestroy() {
        viewModel.destroy();
    }
}
