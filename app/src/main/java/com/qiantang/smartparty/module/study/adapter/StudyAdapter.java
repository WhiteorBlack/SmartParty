package com.qiantang.smartparty.module.study.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiantang.smartparty.BR;
import com.qiantang.smartparty.R;
import com.qiantang.smartparty.adapter.BindingViewHolder;
import com.qiantang.smartparty.adapter.EasyBindQuickAdapter;
import com.qiantang.smartparty.config.Config;
import com.qiantang.smartparty.modle.RxStudy;
import com.qiantang.smartparty.modle.RxStudyList;
import com.qiantang.smartparty.utils.AutoUtils;
import com.qiantang.smartparty.utils.ToastUtil;
import com.qiantang.smartparty.widget.AutoLinearLayout;
import com.qiantang.smartparty.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/6.
 */
public class StudyAdapter extends EasyBindQuickAdapter<RxStudyList> {

    public StudyAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void easyConvert(BindingViewHolder holder, RxStudyList item) {
        holder.getBinding().setVariable(BR.item, item);
        holder.getBinding().executePendingBindings();
        ((SimpleDraweeView) holder.getBinding().getRoot().findViewById(R.id.sdv)).setImageURI(Config.IMAGE_HOST + item.getAvatar());
        holder.addOnClickListener(R.id.iv_comment)
                .addOnClickListener(R.id.tv_del);
        if (item.getZanAppMap().size() > 0) {  //填充赞的人 数据
            AutoLinearLayout llLike = holder.getBinding().getRoot().findViewById(R.id.ll_like);
            llLike.removeAllViews();
            for (int i = 0; i < item.getZanAppMap().size(); i++) {
                String name = item.getZanAppMap().get(i).getUsername();
                TextView textView = new TextView(mContext);
                textView.setTextSize(12);
                textView.setTextColor(mContext.getResources().getColor(R.color.paleRed));
                if (i == item.getZanAppMap().size() - 1) {

                } else {
                    name += "、";
                }
                textView.setText(name);
                llLike.addView(textView);
            }
        }

        if (item.getCommentAppMap().size() > 0) {
            //填充评论数据
            StudyCommentAdapter commentAdapter = new StudyCommentAdapter(R.layout.item_study_comment, item.getCommentAppMap());
            RecyclerView rvComment = holder.getBinding().getRoot().findViewById(R.id.rv_comment);
            rvComment.setLayoutManager(new LinearLayoutManager(mContext));
            rvComment.setAdapter(commentAdapter);
            rvComment.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

                }
            });
        }

        if (!TextUtils.isEmpty(item.getImage())) {
            //填充照片信息
            RecyclerView rvImage = holder.getBinding().getRoot().findViewById(R.id.rv);
            rvImage.setLayoutManager(new GridLayoutManager(mContext, 3));
            List<String> imgList = new ArrayList<>();
            if (item.getImage().contains(",")) {
                String[] images = item.getImage().split(",");
                for (int i = 0; i < images.length; i++) {
                    imgList.add(images[i]);
                }
            } else {
                imgList.add(item.getImage());
            }
            StudyImageAdapter imageAdapter = new StudyImageAdapter(R.layout.item_study_image, imgList);
            rvImage.setAdapter(imageAdapter);
            rvImage.addItemDecoration(new SpaceItemDecoration(0, 0, 20, 20));
            rvImage.addOnItemTouchListener(new com.chad.library.adapter.base.listener.OnItemClickListener() {
                @Override
                public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ToastUtil.toast(imgList.get(position));
                }
            });
        }
    }
}
