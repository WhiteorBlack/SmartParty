package com.qiantang.smartparty.module.index.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.modle.RxQuestion;
import com.qiantang.smartparty.modle.RxTestDetial;
import com.qiantang.smartparty.BR;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/14.
 */
public class TestDetialAdapter extends EasyBindQuickAdapter<RxTestDetial> {
    public TestDetialAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxTestDetial item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        List<RxQuestion> questions = item.getOption();
        for (int i = 0; i < questions.size(); i++) {
            questions.get(i).setPos(i);
        }
        TestQuestionAdapter adapter = new TestQuestionAdapter(R.layout.item_test_question, questions);
        RecyclerView recyclerView = holder.getBinding().getRoot().findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                switch (view.getId()) {
                    case R.id.chb:
                        if (item.getType() == 1) {
                            //单选
                            for (int i = 0; i < questions.size(); i++) {
                                if (i == position) {
                                    questions.get(i).setSelect(!questions.get(i).isSelect());
                                } else {
                                    questions.get(i).setSelect(false);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        });
    }
}
