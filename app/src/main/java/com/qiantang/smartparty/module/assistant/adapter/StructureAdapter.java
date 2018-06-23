package com.qiantang.smartparty.module.assistant.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxIndexNews;
import com.qiantang.smartparty.modle.RxStructureLevelOne;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.modle.RxStructureLevelTwo;
import com.qiantang.smartparty.modle.RxStructurePerson;

import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/17.
 */
public class StructureAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */

    public static final int TYPE_LEVEL_0 = 0;
    public static final int TYPE_LEVEL_1 = 1;
    public static final int TYPE_PERSON = 2;

    public StructureAdapter(List<MultiItemEntity> data) {
        super(data);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int resId = 0;
        boolean isAuto = true;
        switch (viewType) {
            case TYPE_LEVEL_0:
                resId = R.layout.item_structure_one;
                break;
            case TYPE_LEVEL_1:
                resId = R.layout.item_structure_two;
                break;
            case TYPE_PERSON:
                resId=R.layout.item_structure_person;
                break;
        }
        ViewDataBinding binding = DataBindingUtil.inflate(mLayoutInflater, resId, parent, false);
        return new BindingViewHolder<>(binding, isAuto);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        ViewDataBinding binding = ((BindingViewHolder) helper).getBinding();
        switch (helper.getItemViewType()) {
            case TYPE_LEVEL_0:
                RxStructureLevelOne rxStructureLevelOne = (RxStructureLevelOne) item;
                binding.setVariable(BR.item, rxStructureLevelOne);
                helper.addOnClickListener(R.id.iv_level_one).addOnClickListener(R.id.ll_level_one);
                helper.setImageResource(R.id.iv_level_one, rxStructureLevelOne.isExpanded() ? R.mipmap.icon_arrow_black_bottom : R.mipmap.icon_arrow_black_right);

                break;
            case TYPE_LEVEL_1:
                RxStructureLevelTwo rxStructureLevelTwo = (RxStructureLevelTwo) item;
                binding.setVariable(BR.item, rxStructureLevelTwo);
                helper.addOnClickListener(R.id.iv_level_two).addOnClickListener(R.id.ll_level_two);
                helper.setImageResource(R.id.iv_level_two, rxStructureLevelTwo.isExpanded() ? R.mipmap.icon_arrow_black_bottom : R.mipmap.icon_arrow_black_right);
                break;
            case TYPE_PERSON:
                RxStructurePerson rxStructurePerson = (RxStructurePerson) item;
                binding.setVariable(BR.item, rxStructurePerson);
                ((SimpleDraweeView) binding.getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST + rxStructurePerson.getAvatar());
                break;
        }
        binding.executePendingBindings();
    }
}
