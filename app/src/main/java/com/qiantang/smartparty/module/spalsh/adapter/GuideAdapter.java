package com.qiantang.smartparty.module.spalsh.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoyong bai on 2018/6/22.
 */
public class GuideAdapter extends PagerAdapter {
    private List<ImageView> imageViews=new ArrayList<>();

    public GuideAdapter(List<ImageView> imageViews) {
        this.imageViews = imageViews;
    }

    public void setData(List<ImageView> imageViews) {
        this.imageViews = imageViews;
        notifyDataSetChanged();
    }

    /**
     * 获取当前要显示对象的数量
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageViews.size();
    }

    /**
     * 判断是否用对象生成界面
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    /**
     * 从ViewGroup中移除当前对象（图片）
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViews.get(position));
    }

    /**
     * 当前要显示的对象（图片）
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imageViews.get(position));
        return imageViews.get(position);
    }
}
