package com.qiantang.smartparty.widget;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhaoyong bai on 2018/5/27.
 * 暂时只用于表格设置间隔,后续如果有需要再进行扩展
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int top = 0;
    private int bottom = 0;
    private int left = 0;
    private int right = 0;


    /**
     * 每个方向的间隔数值
     *
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public SpaceItemDecoration(int top, int bottom, int left, int right) {
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.top = top;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager linearLayoutManager = (GridLayoutManager) parent.getLayoutManager();
        if (linearLayoutManager.getSpanCount() == 1) {
            //单列表 直接赋值即可
            outRect.bottom = bottom;
            outRect.top = top;
            outRect.left = left;
            outRect.right = right;
        } else {
            //表格
            if (left > 0 && right > 0) {
                //如果左右的间隔都不为0则直接赋值
                outRect.left = left;
                outRect.right = right;
            } else {
                if (left * right == 0) {
                    //这里左右一个为0,或者都为0,只考虑其中一个为0的情况,都为0没有影响
                    if (left > 0) {
                        //判断view 位置,如果为奇数列则设置左侧,偶数这是右侧
                        if (parent.getChildLayoutPosition(view) % 2 > 0) {
                            outRect.left = left / 2;
                        } else {
                            outRect.right = left / 2;
                        }
                    }

                    if (right > 0) {
                        //判断view 位置,如果为奇数列则设置左侧,偶数这是右侧
                        if (parent.getChildLayoutPosition(view) % 2 > 0) {
                            outRect.left = left / 2;
                        } else {
                            outRect.right = left / 2;
                        }
                    }
                }
            }
        }
    }
}
