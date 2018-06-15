package com.qiantang.smartparty.module.index.adapter;

import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxQuestion;
import com.qiantang.smartparty.modle.RxTestRecord;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/15.
 */
public class TestRecordQuestionAdapter extends EasyBindQuickAdapter<RxTestRecord> {
    public TestRecordQuestionAdapter(int layoutResId) {
        super(layoutResId);
    }

    public TestRecordQuestionAdapter(int layoutResId, List<RxTestRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxTestRecord item) {
        holder.getBinding().setVariable(BR.item,item);
        holder.getBinding().executePendingBindings();
    }
}
